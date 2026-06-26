import 'package:flutter/material.dart';

import 'ui/home_page.dart';

void main() => runApp(const VocalcApp());

class VocalcApp extends StatelessWidget {
  const VocalcApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'vocalc',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF4C5BD4),
          brightness: Brightness.dark,
        ),
        useMaterial3: true,
      ),
      home: const HomePage(),
    );
  }
}
