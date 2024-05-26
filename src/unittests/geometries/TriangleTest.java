package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import geometries.*;
import primitives.*;

/**
 * Tests for triangle geometry
 *
 * @author Elad Bibi
 */
class TriangleTest {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Triangle#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Standard triangle normal computation
        Point p1 = new Point(2, 2, -2);
        Point p2 = new Point(0.5, 1.5, 3);
        Point p3 = new Point(4, 1, 2);
        Triangle triangle = new Triangle(p1, p2, p3);
        Vector n = new Vector(3, 16, 2.5).normalize();

        assertTrue(n.equals(triangle.getNormal(new Point(4, -2, 8))) ||
                        n.scale(-1d).equals(triangle.getNormal(new Point(4, -2, 8))),
                "Normal computation for triangle not working properly");
        //verifying the length is 1
        assertEquals(1d, triangle.getNormal((new Point(4, -2, 8))).length(), DELTA,
                "Normal is not a unit-vector");
    }
}