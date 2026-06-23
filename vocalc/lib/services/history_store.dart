import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

import '../models.dart';

/// Persists calculation history locally. History is the #1 thing users complain
/// is missing or lost in other calculators, so it survives app restarts here.
class HistoryStore {
  static const String _key = 'vocalc_history_v1';
  static const int _max = 100;

  Future<List<HistoryEntry>> load() async {
    final prefs = await SharedPreferences.getInstance();
    final raw = prefs.getStringList(_key) ?? const [];
    return raw
        .map((s) => HistoryEntry.fromJson(jsonDecode(s) as Map<String, dynamic>))
        .toList();
  }

  Future<List<HistoryEntry>> add(HistoryEntry entry) async {
    final prefs = await SharedPreferences.getInstance();
    final list = await load();
    list.insert(0, entry);
    if (list.length > _max) list.removeRange(_max, list.length);
    await prefs.setStringList(
      _key,
      list.map((e) => jsonEncode(e.toJson())).toList(),
    );
    return list;
  }

  Future<void> clear() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_key);
  }
}
