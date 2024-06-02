package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        //TODO
        assertTrue(n.equals(triangle.getNormal(new Point(4, -2, 8))) ||
                        n.scale(-1d).equals(triangle.getNormal(new Point(4, -2, 8))),
                "Normal computation for triangle not working properly");
        //verifying the length is 1
        assertEquals(1d, triangle.getNormal((new Point(4, -2, 8))).length(), DELTA,
                "Normal is not a unit-vector");
    }

    /**
     * Test method for {@link geometries.Triangle#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Point point = new Point(1, 1, -2);
        Vector vector = new Vector(0, 1, 0);
        Triangle triangle = new Triangle(
                new Point(2.25, 2, -2.83),
                new Point(1.4, 2, -0.5),
                new Point(0.18, 2, -2.81));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Standard intersection (1 point)
        List<Point> expected = List.of(new Point(1, 2, -2));
        assertEquals(1, triangle.findIntersections(new Ray(point, vector)).size(),
                "Intersection with the triangle gives the wrong intersection-count");
        assertEquals(expected, triangle.findIntersections(new Ray(point, vector)),
                "Intersection with the triangle gives the wrong point");

        // TC02: Ray passes the side of the triangle (0 point)
        point = new Point(3, 1, -2);
        vector = new Vector(0, 1, 0);
        assertNull(triangle.findIntersections(new Ray(point, vector)),
                "Wrong intersection count");

        // TC03: Ray passes at the junction of two of the triangle's sides (0 point)
        point = new Point(3, 1, -3.5);
        vector = new Vector(0, 1, 0);
        assertNull(triangle.findIntersections(new Ray(point, vector)),
                "Wrong intersection count");

        // =============== Boundary Values Tests ==================

        // TC04: Intersection with one of the side's direction (0 point)
        point = new Point(3.06, 1, -2.84);
        vector = new Vector(0, 1, 0);
        assertNull(triangle.findIntersections(new Ray(point, vector)),
                "Wrong intersection count");

        // TC05: Intersection at a corner (0 point)
        point = new Point(2.25, 1, -2.83);
        vector = new Vector(0, 1, 0);
        assertNull(triangle.findIntersections(new Ray(point, vector)),
                "Wrong intersection count");

        // TC06: Intersection with the triangle's edge (1 point)
        expected = List.of(new Point(1.68, 2, -1.27));
        point = new Point(1.68, 1, -1.27);
        vector = new Vector(0, 1, 0);
        assertEquals(1, triangle.findIntersections(new Ray(point, vector)).size(),
                "Wrong intersection count");
        assertEquals(expected, triangle.findIntersections(new Ray(point, vector)),
                "Wrong intersections");
    }
}