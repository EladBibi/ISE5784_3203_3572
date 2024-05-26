package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static primitives.Util.alignZero;

/**
 * Tests for point-class methods
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
class PointTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link primitives.Point#subtract(Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);

        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);

        // TC01: Standard vector & point subtraction
        assertEquals(new Vector(3, 6, 9), v1.subtract(v2),
                "Vector subtraction not working properly");
        assertEquals(v1, p2.subtract(p1),
                "Point subtraction not working properly");

        // =============== Boundary Values Tests ==================

        // TC02: Vector & Point subtraction with itself
        //vector - itself:
        assertThrows(IllegalArgumentException.class, () -> v1.subtract(v1),
                "No exception was thrown");
        //point - itself:
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "point - itself does not throw an exception");

    }

    /**
     * Test method for {@link primitives.Point#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, 4, 6);

        Vector v1 = new Vector(1, 2, 3);
        Vector v1Opposite = new Vector(-1, -2, -3);

        // TC01: Standard point addition with vector
        assertEquals(p2, p1.add(v1),
                "point + vector operation does not return the correct point");
        assertEquals(Point.ZERO, p1.add(v1Opposite),
                "point + vector operation should have returned zero-vector(center of coordinates)");
    }

    /**
     * Test method for {@link primitives.Point#distance(Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 2, 3);
        Point p3 = new Point(2, 4, 5);

        // TC01: Standard distance computation
        assertEquals(0d, alignZero(p1.distance(p3) - 3),
                "distance computation not working properly");
        assertEquals(0d, alignZero(p3.distance(p1) - 3),
                "distance computation not working properly");

        // =============== Boundary Values Tests ==================

        // TC02: Distance between identical points
        assertEquals(0d, alignZero(p1.distance(p1)),
                "point distance to itself is not zero");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(Point)}.
     */
    @Test
    void testDistanceSquared() {
        // ============ Equivalence Partitions Tests ==============

        Point p1 = new Point(1, 2, 3);
        Point p3 = new Point(2, 4, 5);

        // TC01: Standard distance computation
        assertEquals(0d, alignZero(p1.distanceSquared(p3) - 9),
                "squared distance computation not working properly");
        assertEquals(0d, alignZero(p3.distanceSquared(p1) - 9),
                "squared distance computation not working properly");

        // =============== Boundary Values Tests ==================
        // TC02: Distance between identical points
        assertEquals(0d, alignZero(p1.distanceSquared(p1)),
                "point squared distance to itself is not zero");
    }
}