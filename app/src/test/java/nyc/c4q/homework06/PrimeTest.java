package nyc.c4q.homework06;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Prime}.
 *
 * The public entry point is {@link Prime#primeNumber(String)}, which receives the
 * raw equation string from the calculator screen (the typed number followed by the
 * "p" prime trigger, e.g. "7p") and returns a human readable verdict string.
 *
 * Note the exact spacing of the result strings produced by the production code:
 *   - prime     -> "<n>  Is  prime"   (two spaces around "Is")
 *   - not prime -> "<n> Is not prime" (single leading space)
 */
public class PrimeTest {

    private String primeOf(String input) {
        return new Prime().primeNumber(input);
    }

    @Test
    public void smallPrimesAreReportedAsPrime() {
        assertEquals("2  Is  prime", primeOf("2p"));
        assertEquals("3  Is  prime", primeOf("3p"));
        assertEquals("5  Is  prime", primeOf("5p"));
        assertEquals("7  Is  prime", primeOf("7p"));
    }

    @Test
    public void largerPrimesAreReportedAsPrime() {
        assertEquals("11  Is  prime", primeOf("11p"));
        assertEquals("13  Is  prime", primeOf("13p"));
        assertEquals("97  Is  prime", primeOf("97p"));
    }

    @Test
    public void compositeOddNumbersAreReportedAsNotPrime() {
        assertEquals("9 Is not prime", primeOf("9p"));
        assertEquals("15 Is not prime", primeOf("15p"));
        assertEquals("25 Is not prime", primeOf("25p"));
        // 121 == 11 * 11 exercises the loop's largest divisor (sqrt(121) == 11).
        assertEquals("121 Is not prime", primeOf("121p"));
    }

    @Test
    public void evenCompositesAreReportedAsNotPrime() {
        assertEquals("4 Is not prime", primeOf("4p"));
        assertEquals("6 Is not prime", primeOf("6p"));
        assertEquals("100 Is not prime", primeOf("100p"));
    }

    @Test
    public void oneIsReportedAsNotPrime() {
        assertEquals("1 Is not prime", primeOf("1p"));
    }

    @Test
    public void missingNumberReturnsZeroGuard() {
        // No digits before the "p" trigger -> guard returns "0".
        assertEquals("0", primeOf("p"));
    }

    @Test
    public void expressionWithOperatorsReturnsZeroGuard() {
        // The presence of an operator splits the equation into >1 parts and the
        // guard short circuits to "0" instead of attempting a primality test.
        assertEquals("0", primeOf("7+3p"));
    }

    /**
     * Documents a KNOWN BUG: non-numeric input is passed straight to
     * Integer.parseInt and throws instead of being handled gracefully. In the app
     * this surfaces as an uncaught crash. Captured here so a future fix turns this
     * into a behavioural change we notice.
     */
    @Test(expected = NumberFormatException.class)
    public void nonNumericInputThrows_knownBug() {
        primeOf("abcp");
    }
}
