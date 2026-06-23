# vocalc — voice-first calculator

Speak a calculation in plain language; vocalc transcribes it, understands it,
computes it exactly, and reads the answer back. Built with Flutter so one
codebase targets **Android and iOS first**, with **web, Windows, macOS, and
Linux** available from the same source later.

This is the v2 spin-off of the classic ScientificCalculator. v1 stays as-is.

## Why it's different

Most calculator apps people complain about share the same gaps. vocalc targets
them directly:

- **Voice-first.** Talk to it — no tiny buttons. "What's fifteen percent off
  eighty," "split one hundred twenty between four."
- **Natural-language understanding.** A language model translates phrasing into a
  clean expression; a deterministic engine does the actual arithmetic, so
  results are exact (no LLM math errors on big numbers).
- **Persistent history.** The most common complaint about other calculators is
  history that's missing or lost on restart. vocalc keeps the last 100 results
  locally.
- **Spoken answers.** Results are read back via text-to-speech.

## Architecture

```
speech  ─►  SpeechService (speech_to_text)        on-device transcription
transcript ─► ClaudeParser (Claude Messages API)  natural language → expression
expression ─► MathEngine (math_expressions)       exact, deterministic compute
result   ─►  TtsService (flutter_tts)             read answer aloud
         └►  HistoryStore (shared_preferences)     persisted history
```

`lib/` layout:

| File | Responsibility |
|---|---|
| `config.dart` | Claude model id + API key injection (one place to change) |
| `models.dart` | `CalcResult`, `HistoryEntry` |
| `services/speech_service.dart` | Speech-to-text wrapper |
| `services/claude_parser.dart` | NL → structured expression via Claude (raw HTTPS) |
| `services/math_engine.dart` | Deterministic evaluator + offline word normalizer |
| `services/calculator_service.dart` | Orchestrates the pipeline + offline fallback |
| `services/tts_service.dart` | Text-to-speech wrapper |
| `services/history_store.dart` | Local persistence |
| `ui/home_page.dart` | Mic, live transcript, result, history |

## Getting started

This repo ships the Dart source (`lib/`, `test/`, `pubspec.yaml`). The native
platform folders are generated on your machine so they match your Flutter
version.

```bash
# 1. Generate the android/ios/web/... project scaffolding in place
flutter create .

# 2. Fetch dependencies
flutter pub get

# 3. Run the unit tests
flutter test

# 4. Run on a device/emulator, passing your Anthropic API key
flutter run --dart-define=ANTHROPIC_API_KEY=sk-ant-...
```

Without a key, the app still runs in **offline mode**: a local word-to-symbol
normalizer handles basic spoken math ("six times eight", "twelve plus seven").

### Required platform permissions

After `flutter create .`, add microphone + speech permissions:

**Android** — `android/app/src/main/AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.INTERNET"/>
```

**iOS** — `ios/Runner/Info.plist`:
```xml
<key>NSMicrophoneUsageDescription</key>
<string>vocalc uses the microphone to hear your calculations.</string>
<key>NSSpeechRecognitionUsageDescription</key>
<string>vocalc converts your speech into calculations.</string>
```

## ⚠️ API key security

`config.dart` reads the key from a `--dart-define` for local development only.
**Never ship a real Anthropic key inside a published app** — it can be extracted
from the bundle. For production, stand up a tiny backend proxy that holds the key
and forwards requests, then point `anthropicEndpoint` at your proxy and leave
`anthropicApiKey` empty.

## Roadmap

v1 (now): voice-first capture, NL parsing, exact basic math, spoken answers,
persistent history.

Next: scientific/complex math, editable history, unit conversion, web + desktop
targets, and a hosted API proxy.
