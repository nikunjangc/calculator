import 'package:flutter_test/flutter_test.dart';
import 'package:vocalc/services/math_engine.dart';

void main() {
  final engine = MathEngine();

  group('MathEngine.evaluate', () {
    test('basic arithmetic with precedence', () {
      expect(engine.evaluate('6 * 8 + 2'), 50);
      expect(engine.evaluate('12 + 7 * (3 - 1)'), 26);
    });

    test('percentage-style expressions', () {
      expect(engine.evaluate('80 * (1 - 0.15)'), closeTo(68, 1e-9));
      expect(engine.evaluate('60 * 0.20'), closeTo(12, 1e-9));
    });

    test('division and powers', () {
      expect(engine.evaluate('120 / 4'), 30);
      expect(engine.evaluate('2 ^ 10'), 1024);
    });

    test('throws on malformed input', () {
      expect(() => engine.evaluate('12 +'), throwsA(anything));
      expect(() => engine.evaluate(''), throwsA(anything));
    });
  });

  group('MathEngine.format', () {
    test('whole numbers drop the decimal', () {
      expect(MathEngine.format(50.0), '50');
      expect(MathEngine.format(1024.0), '1024');
    });

    test('decimals trim trailing zeros', () {
      expect(MathEngine.format(68.0), '68');
      expect(MathEngine.format(3.14), '3.14');
    });
  });

  group('MathEngine.normalizeWords (offline fallback)', () {
    test('converts spoken operators to symbols', () {
      expect(engine.evaluate(MathEngine.normalizeWords('six times eight')), 48);
      expect(
        engine.evaluate(MathEngine.normalizeWords('twelve plus seven')),
        19,
      );
    });
  });
}
