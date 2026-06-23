import 'dart:convert';

import 'package:http/http.dart' as http;

import '../config.dart';

/// Structured result of asking Claude to interpret a spoken request.
class ParsedMath {
  /// Whether the utterance was actually a math/calculation request.
  final bool isMath;

  /// A clean arithmetic expression evaluable by [MathEngine], e.g. "80 * 0.85".
  final String expression;

  /// A short, natural sentence to read back to the user.
  final String spokenSummary;

  const ParsedMath({
    required this.isMath,
    required this.expression,
    required this.spokenSummary,
  });
}

class ClaudeException implements Exception {
  final String message;
  const ClaudeException(this.message);
  @override
  String toString() => 'ClaudeException: $message';
}

/// Turns natural-language speech into a structured math expression using the
/// Claude Messages API.
///
/// Claude does NOT compute the answer — it only normalizes phrasing like
/// "what's 15% off 80" or "split 120 between 4 of us" into an expression that
/// the deterministic [MathEngine] then evaluates.
class ClaudeParser {
  final http.Client _http;

  ClaudeParser({http.Client? client}) : _http = client ?? http.Client();

  static const String _system = '''
You convert a person's spoken words into a single arithmetic expression that a
standard math parser can evaluate. The parser understands + - * / ^ parentheses
and decimal numbers only — no functions, variables, or units.

Rules:
- Resolve real-world phrasing into arithmetic. Examples:
  "fifteen percent off eighty" -> "80 * (1 - 0.15)"
  "split one hundred twenty between four" -> "120 / 4"
  "what is twenty percent of sixty" -> "60 * 0.20"
  "six times eight plus two" -> "6 * 8 + 2"
- If the utterance is not a calculation, set is_math to false and leave
  expression empty.
- spoken_summary is one short sentence a phone can read aloud, e.g.
  "Fifteen percent off eighty is sixty-eight." Do not include the result if you
  are unsure — keep it to restating the calculation.
- Never include commentary, units, or currency symbols in expression.
''';

  static const Map<String, dynamic> _schema = {
    'type': 'object',
    'additionalProperties': false,
    'properties': {
      'is_math': {
        'type': 'boolean',
        'description': 'True if the utterance is a calculation request.',
      },
      'expression': {
        'type': 'string',
        'description':
            'Arithmetic expression using + - * / ^ ( ) and decimals; empty if not math.',
      },
      'spoken_summary': {
        'type': 'string',
        'description': 'One short sentence to read aloud.',
      },
    },
    'required': ['is_math', 'expression', 'spoken_summary'],
  };

  /// Returns the parsed interpretation, or null if AI parsing is unavailable.
  /// Throws [ClaudeException] on a transport/API error so the caller can fall
  /// back gracefully.
  Future<ParsedMath?> parse(String transcript) async {
    if (!AppConfig.hasClaude) return null;

    final body = jsonEncode({
      'model': AppConfig.claudeModel,
      'max_tokens': 512,
      'system': _system,
      'messages': [
        {'role': 'user', 'content': transcript},
      ],
      'output_config': {
        'format': {'type': 'json_schema', 'schema': _schema},
      },
    });

    late final http.Response resp;
    try {
      resp = await _http
          .post(
            Uri.parse(AppConfig.anthropicEndpoint),
            headers: {
              'x-api-key': AppConfig.anthropicApiKey,
              'anthropic-version': AppConfig.anthropicVersion,
              'content-type': 'application/json',
            },
            body: body,
          )
          .timeout(const Duration(seconds: 20));
    } catch (e) {
      throw ClaudeException('network error: $e');
    }

    if (resp.statusCode != 200) {
      throw ClaudeException('HTTP ${resp.statusCode}: ${resp.body}');
    }

    final data = jsonDecode(resp.body) as Map<String, dynamic>;
    final content = (data['content'] as List<dynamic>? ?? const []);
    final textBlock = content.cast<Map<String, dynamic>>().firstWhere(
          (b) => b['type'] == 'text',
          orElse: () => <String, dynamic>{},
        );
    final text = textBlock['text'] as String?;
    if (text == null || text.isEmpty) return null;

    final parsed = jsonDecode(text) as Map<String, dynamic>;
    return ParsedMath(
      isMath: parsed['is_math'] as bool? ?? false,
      expression: (parsed['expression'] as String? ?? '').trim(),
      spokenSummary: (parsed['spoken_summary'] as String? ?? '').trim(),
    );
  }
}
