import 'package:math_expressions/math_expressions.dart';

/// Deterministic arithmetic engine.
///
/// Claude is only ever asked to translate natural language into a clean
/// expression string — the actual numbers are crunched here, so results are
/// exact and large numbers are never approximated by a language model.
class MathEngine {
  final ExpressionParser _parser = GrammarParser();

  /// Evaluates a plain arithmetic expression such as "12 + 7 * (3 - 1)".
  /// Throws if the expression is malformed.
  double evaluate(String expression) {
    final cleaned = _clean(expression);
    if (cleaned.isEmpty) {
      throw const FormatException('empty expression');
    }
    final Expression exp = _parser.parse(cleaned);
    final ContextModel cm = ContextModel();
    final double result = exp.evaluate(EvaluationType.REAL, cm) as double;
    if (result.isNaN || result.isInfinite) {
      throw const FormatException('non-finite result');
    }
    return result;
  }

  /// Normalizes raw, lightly-formatted input into something the parser accepts.
  String _clean(String expression) {
    return expression
        .replaceAll('×', '*')
        .replaceAll('x', '*')
        .replaceAll('÷', '/')
        .replaceAll('−', '-')
        .replaceAll(',', '')
        .trim();
  }

  /// Formats a numeric result: drops the trailing ".0" on whole numbers and
  /// trims excessive decimal noise.
  static String format(double value) {
    if (value == value.roundToDouble() && value.abs() < 1e15) {
      return value.toInt().toString();
    }
    // Limit to 10 significant-ish decimals, then strip trailing zeros.
    String s = value.toStringAsFixed(10);
    s = s.replaceAll(RegExp(r'0+$'), '');
    s = s.replaceAll(RegExp(r'\.$'), '');
    return s;
  }

  /// Offline fallback: a light word-to-symbol normalizer so basic spoken math
  /// ("twelve plus seven", "6 times 8") still works without the AI parser.
  /// This is intentionally simple — the AI path handles richer phrasing.
  static String normalizeWords(String input) {
    var s = ' ${input.toLowerCase()} ';
    const replacements = <String, String>{
      ' plus ': ' + ',
      ' add ': ' + ',
      ' minus ': ' - ',
      ' subtract ': ' - ',
      ' times ': ' * ',
      ' multiplied by ': ' * ',
      ' divided by ': ' / ',
      ' over ': ' / ',
      ' to the power of ': ' ^ ',
      ' squared ': ' ^ 2 ',
      ' percent of ': ' / 100 * ',
      ' point ': ' . ',
    };
    replacements.forEach((word, symbol) => s = s.replaceAll(word, symbol));

    const words = <String, String>{
      ' zero ': ' 0 ',
      ' one ': ' 1 ',
      ' two ': ' 2 ',
      ' three ': ' 3 ',
      ' four ': ' 4 ',
      ' five ': ' 5 ',
      ' six ': ' 6 ',
      ' seven ': ' 7 ',
      ' eight ': ' 8 ',
      ' nine ': ' 9 ',
      ' ten ': ' 10 ',
    };
    words.forEach((word, digit) => s = s.replaceAll(word, digit));

    return s.trim();
  }
}
