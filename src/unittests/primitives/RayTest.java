package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

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

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        Point a = new Point(1, 1, 1);
        Point b = new Point(2, 3, 4);
        Point c = new Point(2, 1, 2);
        Point d = new Point(2, 3, 3);

        Vector rayDir = new Vector(0, 1, 0);
        Ray ray = new Ray(b, rayDir);

        List<Point> list;

        // ============ Equivalence Partitions Tests ==============
        //TC01 Closest point is in the middle of the list
        list = List.of(a, d, c);
        assertEquals(d, ray.findClosestPoint(list), "Point is in the middle of the list");

        // =============== Boundary Values Tests ==================
        //TC02 The list is empty, null is expected
        list = List.of();
        assertEquals(null, ray.findClosestPoint(list), "Point list is empty");

        //TC03 Closest point is at the beginning of the list
        list = List.of(d, c, a);
        assertEquals(d, ray.findClosestPoint(list), "Point is at the front of the list");

        //TC04 Closest point is at the end of the list
        list = List.of(a, c, d);
        assertEquals(d, ray.findClosestPoint(list), "Point is at the end of the list");
    }
}