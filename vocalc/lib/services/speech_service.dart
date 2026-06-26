import 'package:speech_to_text/speech_to_text.dart';

/// Thin wrapper around speech_to_text that exposes just what the UI needs.
class SpeechService {
  final SpeechToText _stt = SpeechToText();
  bool _available = false;

  bool get isListening => _stt.isListening;

  /// Initializes the plugin and requests permission. Returns true if speech
  /// recognition is usable on this device.
  Future<bool> init({
    void Function(String status)? onStatus,
    void Function(String error)? onError,
  }) async {
    _available = await _stt.initialize(
      onStatus: (s) => onStatus?.call(s),
      onError: (e) => onError?.call(e.errorMsg),
    );
    return _available;
  }

  /// Starts listening. [onResult] fires repeatedly with the live transcript;
  /// [onFinal] fires once with the final transcript when the user stops.
  Future<void> listen({
    required void Function(String partial) onResult,
    required void Function(String finalText) onFinal,
  }) async {
    if (!_available) return;
    await _stt.listen(
      listenOptions: SpeechListenOptions(
        partialResults: true,
        cancelOnError: true,
      ),
      onResult: (r) {
        onResult(r.recognizedWords);
        if (r.finalResult) onFinal(r.recognizedWords);
      },
    );
  }

  Future<void> stop() => _stt.stop();
}
