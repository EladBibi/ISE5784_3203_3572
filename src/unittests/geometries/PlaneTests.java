package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for a plane geometry in a 3D space
 *
 * @author Elad Bibi
 */
class PlaneTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.000001;

    /**
     * Test method for {@link geometries.Plane#Plane(Point p1, Point p2, Point p3)}.
     */
    @Test
    void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: The given points for the plane's constructor are not unique
        Point p1 = new Point(2, 5, -3);
        Point p2 = new Point(1, 5, 0);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p1),
                "Plane construction with 3 identical points should throw an exception");
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p1, p2),
                "Plane construction with 2 identical points should throw an exception");

        // TC02: The given points for the plane's constructor are linearly dependent(on a line)
        Point p3 = new Point(0, 5, 3);
        assertThrows(IllegalArgumentException.class, () -> new Plane(p1, p2, p3),
                "Plane construction with linearly dependent points should throw an exception");

    }

    /**
     * Test method for {@link Plane#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Normal computation for standard plane input
        Point p1 = new Point(2, 2, -2);
        Point p2 = new Point(0.5, 1.5, 3);
        Point p3 = new Point(4, 1, 2);
        Vector n = new Vector(3, 16, 2.5).normalize();
        Plane plane = new Plane(p1, p2, p3);

        assertTrue(plane.getNormal().equals(n) || plane.getNormal().equals(n.scale(-1d)),
                "Plane normal computation not working properly");
        assertTrue(plane.getNormal().equals(plane.getNormal(new Point(-4.5, -1.5, 15))),
                "Plane normal computation not working properly");
        //verifying the length is 1
        assertEquals(1d, plane.getNormal().length(), DELTA,
                "Normal is not a unit-vector");
    }
}