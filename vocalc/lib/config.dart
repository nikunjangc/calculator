/// Central configuration for vocalc.
///
/// The Claude model and API key live here so the rest of the app never hard-codes
/// them. The key is injected at build/run time via --dart-define and is NEVER
/// committed to source.
class AppConfig {
  AppConfig._();

  /// Claude model used to turn natural-language speech into a clean math
  /// expression.
  ///
  /// Defaults to the most capable model. This is a real-time, parse-heavy
  /// workload, so for lower latency and cost you can switch to a faster model
  /// (e.g. 'claude-haiku-4-5') by changing this one constant.
  static const String claudeModel = 'claude-opus-4-8';

  /// Anthropic API key, injected at build/run time:
  ///   flutter run --dart-define=ANTHROPIC_API_KEY=sk-ant-...
  ///
  /// SECURITY: never ship a real API key inside a published app — it can be
  /// extracted from the bundle. In production, route requests through your own
  /// backend proxy and leave this empty so the app talks to your server instead
  /// of Anthropic directly.
  static const String anthropicApiKey =
      String.fromEnvironment('ANTHROPIC_API_KEY', defaultValue: '');

  static const String anthropicEndpoint = 'https://api.anthropic.com/v1/messages';
  static const String anthropicVersion = '2023-06-01';

  /// Whether AI-assisted natural-language parsing is available. When false, the
  /// app falls back to a local word-to-symbol normalizer so basic spoken math
  /// still works offline.
  static bool get hasClaude => anthropicApiKey.isNotEmpty;
}
