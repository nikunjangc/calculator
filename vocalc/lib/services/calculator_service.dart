import '../models.dart';
import 'claude_parser.dart';
import 'math_engine.dart';

/// Orchestrates the full pipeline: transcript -> (AI parse) -> deterministic
/// evaluation -> result.
class CalculatorService {
  final ClaudeParser _claude;
  final MathEngine _engine;

  CalculatorService({ClaudeParser? claude, MathEngine? engine})
      : _claude = claude ?? ClaudeParser(),
        _engine = engine ?? MathEngine();

  Future<CalcResult> compute(String transcript) async {
    final trimmed = transcript.trim();
    if (trimmed.isEmpty) {
      return CalcResult.failure(transcript, "I didn't catch that.");
    }

    String expression;
    String spoken;

    ParsedMath? parsed;
    try {
      parsed = await _claude.parse(trimmed);
    } on ClaudeException {
      parsed = null; // fall through to the offline path
    }

    if (parsed != null) {
      if (!parsed.isMath || parsed.expression.isEmpty) {
        return CalcResult.failure(
          trimmed,
          "That didn't sound like a calculation.",
        );
      }
      expression = parsed.expression;
      spoken = parsed.spokenSummary;
    } else {
      // Offline / no-API fallback: light local normalization.
      expression = MathEngine.normalizeWords(trimmed);
      spoken = '';
    }

    try {
      final value = _engine.evaluate(expression);
      final formatted = MathEngine.format(value);
      final summary =
          spoken.isNotEmpty ? spoken : '$expression equals $formatted';
      return CalcResult.success(trimmed, expression, formatted, summary);
    } catch (_) {
      return CalcResult.failure(trimmed, "I couldn't compute that.");
    }
  }
}
