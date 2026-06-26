import 'package:flutter_tts/flutter_tts.dart';

/// Reads results back to the user.
class TtsService {
  final FlutterTts _tts = FlutterTts();
  bool _ready = false;

  Future<void> _ensureReady() async {
    if (_ready) return;
    await _tts.setSpeechRate(0.5);
    await _tts.setPitch(1.0);
    await _tts.awaitSpeakCompletion(true);
    _ready = true;
  }

  Future<void> speak(String text) async {
    if (text.trim().isEmpty) return;
    await _ensureReady();
    await _tts.stop();
    await _tts.speak(text);
  }

  Future<void> stop() => _tts.stop();
}
