package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the ray class
 */
class RayTest extends Object {

    /**
     * Test method for {@link primitives.Ray#getPoint(double)}.
     */
    @Test
    void testGetPoint() {
        // ============ Equivalence Partitions Tests ==============
        Point head = new Point(1, 0, 0);
        Vector dir = new Vector(0, 1, 0);
        Ray ray = new Ray(head, dir);

        //TC01 Negative scalar
        assertEquals(new Point(1, -1, 0), ray.getPoint(-1d), "Attempt with a negative-scalar");
        //TC02 Positive scalar
        assertEquals(new Point(1, 1.5, 0), ray.getPoint(1.5d), "Attempt with a positive-scalar");
        // =============== Boundary Values Tests ==================

        //TC03 Scaling by zero, the head point is expected
        assertEquals(head, ray.getPoint(0d), "Attempt with a zero-scalar");
    }
}