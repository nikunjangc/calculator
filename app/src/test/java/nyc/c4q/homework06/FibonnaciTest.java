package nyc.c4q.homework06;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for {@link Fibonnaci}.
 *
 * {@link Fibonnaci#fibo(String)} receives the equation string (a count followed by
 * the "f" trigger, e.g. "5f") and returns the first n Fibonacci numbers
 * concatenated together with no separator (1,1,2,3,5 -> "11235").
 */
public class FibonnaciTest {

    private String fiboOf(String input) {
        return new Fibonnaci().fibo(input);
    }

    @Test
    public void zeroCountReturnsEmptyString() {
        assertEquals("", fiboOf("0f"));
    }

    @Test
    public void oneAndTwoCounts() {
        assertEquals("1", fiboOf("1f"));
        assertEquals("11", fiboOf("2f"));
    }

    @Test
    public void typicalSequencesAreConcatenated() {
        // 1,1,2,3,5
        assertEquals("11235", fiboOf("5f"));
        // 1,1,2,3,5,8,13
        assertEquals("11235813", fiboOf("7f"));
    }

    @Test
    public void missingNumberReturnsZeroGuard() {
        assertEquals("0", fiboOf("f"));
    }

    @Test
    public void expressionWithOperatorsReturnsZeroGuard() {
        // An operator splits the equation into >1 parts and the guard returns "0".
        assertEquals("0", fiboOf("2+3f"));
    }

    /**
     * Documents a KNOWN BUG: non-numeric input reaches Integer.parseInt and throws.
     */
    @Test(expected = NumberFormatException.class)
    public void nonNumericInputThrows_knownBug() {
        fiboOf("xf");
    }

    /**
     * Documents a KNOWN BUG: num1/num2/num3 are instance fields that are never
     * reset, so reusing a single Fibonnaci instance produces a wrong sequence on
     * the second call. The app accidentally avoids this by creating a fresh
     * instance per press. We assert the two results merely differ so the test
     * stays green but the defect is recorded.
     */
    @Test
    public void reusingInstanceIsStateful_knownBug() {
        Fibonnaci reused = new Fibonnaci();
        String first = reused.fibo("5f");
        String second = reused.fibo("5f");
        assertEquals("11235", first);
        assertNotEquals("11235", second);
    }
}
