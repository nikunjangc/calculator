package nyc.c4q.homework06;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link FactorialAndPercentage}.
 *
 * Covers the two pure helpers ({@link FactorialAndPercentage#factorial(int)} and
 * {@link FactorialAndPercentage#percentage(Double)}) plus the public
 * {@link FactorialAndPercentage#simplify(String)} string rewriter that the
 * calculator uses to expand "!" and "%" tokens before evaluation.
 */
public class FactorialAndPercentageTest {

    private String simplify(String input) {
        return new FactorialAndPercentage().simplify(input);
    }

    // ---- factorial(int) -----------------------------------------------------

    @Test
    public void factorialOfZeroAndOne() {
        assertEquals(1, FactorialAndPercentage.factorial(0));
        assertEquals(1, FactorialAndPercentage.factorial(1));
    }

    @Test
    public void factorialOfSmallNumbers() {
        assertEquals(120, FactorialAndPercentage.factorial(5));
        assertEquals(479001600, FactorialAndPercentage.factorial(12));
    }

    /**
     * Documents a KNOWN BUG: factorial uses int arithmetic, so 13! silently
     * overflows. The mathematically correct value is 6227020800, but int wraps it
     * to 1932053504. Recorded so a future widening to long is a visible change.
     */
    @Test
    public void factorialOverflowsAboveTwelve_knownBug() {
        assertEquals(1932053504, FactorialAndPercentage.factorial(13));
    }

    /**
     * Documents a KNOWN BUG: a negative argument recurses without a base case and
     * blows the stack rather than rejecting the input.
     */
    @Test(expected = StackOverflowError.class)
    public void factorialOfNegativeOverflowsStack_knownBug() {
        FactorialAndPercentage.factorial(-1);
    }

    // ---- percentage(Double) -------------------------------------------------

    @Test
    public void percentageScalesByOneHundredth() {
        assertEquals(0.0, FactorialAndPercentage.percentage(0.0), 1e-9);
        assertEquals(0.5, FactorialAndPercentage.percentage(50.0), 1e-9);
        assertEquals(1.0, FactorialAndPercentage.percentage(100.0), 1e-9);
    }

    // ---- simplify(String) ---------------------------------------------------

    @Test
    public void simplifyExpandsSingleFactorial() {
        assertEquals("6", simplify("3!"));
        assertEquals("120", simplify("5!"));
    }

    @Test
    public void simplifyExpandsPercentage() {
        assertEquals("0.5", simplify("50%"));
    }

    /**
     * Documents current behaviour: a decimal factorial is unsupported and the
     * original string is returned unchanged.
     */
    @Test
    public void simplifyLeavesDecimalFactorialUnchanged() {
        assertEquals("3.5!", simplify("3.5!"));
    }
}
