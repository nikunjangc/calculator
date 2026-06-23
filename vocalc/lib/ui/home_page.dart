import 'package:flutter/material.dart';

import '../config.dart';
import '../models.dart';
import '../services/calculator_service.dart';
import '../services/history_store.dart';
import '../services/speech_service.dart';
import '../services/tts_service.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  final SpeechService _speech = SpeechService();
  final TtsService _tts = TtsService();
  final CalculatorService _calc = CalculatorService();
  final HistoryStore _history = HistoryStore();

  bool _speechReady = false;
  bool _listening = false;
  bool _working = false;
  String _transcript = '';
  CalcResult? _result;
  List<HistoryEntry> _entries = const [];

  @override
  void initState() {
    super.initState();
    _bootstrap();
  }

  Future<void> _bootstrap() async {
    final ready = await _speech.init(
      onStatus: (s) {
        if (s == 'done' || s == 'notListening') {
          if (mounted) setState(() => _listening = false);
        }
      },
      onError: (_) {
        if (mounted) setState(() => _listening = false);
      },
    );
    final entries = await _history.load();
    if (!mounted) return;
    setState(() {
      _speechReady = ready;
      _entries = entries;
    });
  }

  Future<void> _toggleMic() async {
    if (_listening) {
      await _speech.stop();
      setState(() => _listening = false);
      return;
    }
    setState(() {
      _listening = true;
      _transcript = '';
      _result = null;
    });
    await _speech.listen(
      onResult: (partial) => setState(() => _transcript = partial),
      onFinal: _onFinalTranscript,
    );
  }

  Future<void> _onFinalTranscript(String text) async {
    setState(() {
      _listening = false;
      _working = true;
      _transcript = text;
    });
    final result = await _calc.compute(text);
    final entries =
        result.ok ? await _history.add(HistoryEntry.fromResult(result)) : _entries;
    if (!mounted) return;
    setState(() {
      _result = result;
      _entries = entries;
      _working = false;
    });
    await _tts.speak(result.spokenSummary);
  }

  Future<void> _clearHistory() async {
    await _history.clear();
    setState(() => _entries = const []);
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('vocalc'),
        actions: [
          if (_entries.isNotEmpty)
            IconButton(
              tooltip: 'Clear history',
              onPressed: _clearHistory,
              icon: const Icon(Icons.delete_outline),
            ),
        ],
      ),
      body: Column(
        children: [
          if (!AppConfig.hasClaude) _banner(theme),
          Expanded(child: _resultArea(theme)),
          _historyArea(theme),
          const SizedBox(height: 16),
          _mic(theme),
          const SizedBox(height: 32),
        ],
      ),
    );
  }

  Widget _banner(ThemeData theme) => Container(
        width: double.infinity,
        color: theme.colorScheme.surfaceContainerHighest,
        padding: const EdgeInsets.all(8),
        child: Text(
          'AI parsing off — running in offline mode (basic spoken math only). '
          'Pass --dart-define=ANTHROPIC_API_KEY=... to enable.',
          textAlign: TextAlign.center,
          style: theme.textTheme.bodySmall,
        ),
      );

  Widget _resultArea(ThemeData theme) {
    final result = _result;
    return Center(
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              _transcript.isEmpty
                  ? (_listening ? 'Listening…' : 'Tap the mic and speak a calculation')
                  : '“$_transcript”',
              textAlign: TextAlign.center,
              style: theme.textTheme.titleMedium?.copyWith(
                color: theme.colorScheme.onSurfaceVariant,
              ),
            ),
            const SizedBox(height: 24),
            if (_working)
              const CircularProgressIndicator()
            else if (result != null)
              Column(
                children: [
                  if (result.ok && result.expression.isNotEmpty)
                    Text(result.expression, style: theme.textTheme.titleLarge),
                  const SizedBox(height: 8),
                  Text(
                    result.value,
                    textAlign: TextAlign.center,
                    style: theme.textTheme.displaySmall?.copyWith(
                      fontWeight: FontWeight.bold,
                      color: result.ok
                          ? theme.colorScheme.primary
                          : theme.colorScheme.error,
                    ),
                  ),
                ],
              ),
          ],
        ),
      ),
    );
  }

  Widget _historyArea(ThemeData theme) {
    if (_entries.isEmpty) return const SizedBox.shrink();
    return SizedBox(
      height: 160,
      child: ListView.builder(
        padding: const EdgeInsets.symmetric(horizontal: 16),
        itemCount: _entries.length,
        itemBuilder: (context, i) {
          final e = _entries[i];
          return ListTile(
            dense: true,
            title: Text(e.expression.isEmpty ? e.input : e.expression),
            subtitle: Text(e.input),
            trailing: Text(
              e.value,
              style: theme.textTheme.titleMedium
                  ?.copyWith(fontWeight: FontWeight.bold),
            ),
          );
        },
      ),
    );
  }

  Widget _mic(ThemeData theme) {
    final enabled = _speechReady && !_working;
    return GestureDetector(
      onTap: enabled ? _toggleMic : null,
      child: AnimatedContainer(
        duration: const Duration(milliseconds: 200),
        width: 84,
        height: 84,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: _listening
              ? theme.colorScheme.error
              : (enabled
                  ? theme.colorScheme.primary
                  : theme.colorScheme.surfaceContainerHighest),
        ),
        child: Icon(
          _listening ? Icons.stop : Icons.mic,
          size: 40,
          color: theme.colorScheme.onPrimary,
        ),
      ),
    );
  }
}
