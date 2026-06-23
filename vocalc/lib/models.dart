/// The outcome of evaluating one spoken request.
class CalcResult {
  /// Raw transcript the user spoke.
  final String input;

  /// The normalized arithmetic expression that was evaluated (may be empty on
  /// error).
  final String expression;

  /// Formatted result value, or a human-readable error message when [ok] is
  /// false.
  final String value;

  /// Short sentence suitable for reading back aloud.
  final String spokenSummary;

  /// Whether evaluation succeeded.
  final bool ok;

  const CalcResult({
    required this.input,
    required this.expression,
    required this.value,
    required this.spokenSummary,
    required this.ok,
  });

  factory CalcResult.success(
    String input,
    String expression,
    String value,
    String spokenSummary,
  ) =>
      CalcResult(
        input: input,
        expression: expression,
        value: value,
        spokenSummary: spokenSummary,
        ok: true,
      );

  factory CalcResult.failure(String input, String message) => CalcResult(
        input: input,
        expression: '',
        value: message,
        spokenSummary: message,
        ok: false,
      );
}

/// One persisted history row.
class HistoryEntry {
  final String input;
  final String expression;
  final String value;
  final DateTime time;

  const HistoryEntry({
    required this.input,
    required this.expression,
    required this.value,
    required this.time,
  });

  Map<String, dynamic> toJson() => {
        'input': input,
        'expression': expression,
        'value': value,
        'time': time.toIso8601String(),
      };

  factory HistoryEntry.fromJson(Map<String, dynamic> json) => HistoryEntry(
        input: json['input'] as String? ?? '',
        expression: json['expression'] as String? ?? '',
        value: json['value'] as String? ?? '',
        time: DateTime.tryParse(json['time'] as String? ?? '') ?? DateTime.now(),
      );

  factory HistoryEntry.fromResult(CalcResult r) => HistoryEntry(
        input: r.input,
        expression: r.expression,
        value: r.value,
        time: DateTime.now(),
      );
}
