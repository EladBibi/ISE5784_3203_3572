package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    final double DELTA = 0.000001;

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

        assertEquals(n, plane.getNormal(), "Plane normal computation not working properly");
        assertEquals(plane.getNormal(), plane.getNormal(new Point(-4.5, -1.5, 15)),
                "Plane normal computation not working properly");
        //verifying the length is 1
        assertEquals(1d, plane.getNormal().length(), DELTA,
                "Normal is not a unit-vector");
    }

    /**
     * Test method for {@link Plane#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Point point = new Point(2, 1, -2);
        Vector vector = new Vector(1, 1, 0);
        Plane plane = new Plane(new Point(0, 2, 1), new Point(0, 2, 3), new Point(10, 2, 0));


        // ============ Equivalence Partitions Tests ==============

        // TC01: Standard intersection (1 point)
        List<Point> expected = List.of(new Point(3, 2, -2));
        assertEquals(1, plane.findIntersections(new Ray(point, vector)).size(),
                "Intersection with the plane gives the wrong intersection-count");
        assertEquals(expected, plane.findIntersections(new Ray(point, vector)),
                "Intersection with the plane gives the wrong point");
        // =============== Boundary Values Tests ==================

        // TC02: Ray is parallel to the plane(0 points)
        point = new Point(2, 1, -2);
        vector = new Vector(1, 0, 0);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "Ray is parallel to the plane");

        // TC03: Ray is included in the plane(0 points)
        point = new Point(2, 2, -2);
        vector = new Vector(1, 0, 0);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "Ray is included in the plane");

        // TC04: Ray's head is NOT on the plane(0 points)
        point = new Point(1.4, 3, -2);
        vector = new Vector(1, 1, 1);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "No intersection with the plane");

        // TC05: Ray is orthogonal to the plane, head point is ON the plane(0 points)
        point = new Point(2, 2, -2);
        vector = new Vector(0, -1, 0);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "Ray is orthogonal to the plane, starts ON the plane");

        // TC06: Ray is orthogonal to the plane, head point is ABOVE the plane(0 points)
        point = new Point(2, 3, -2);
        vector = new Vector(0, 1, 0);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "Ray is orthogonal to the plane, starts ABOVE the plane");

        // TC07: Ray is orthogonal to the plane, head point is UNDER the plane (0 points)
        point = new Point(2, 1, -2);
        vector = new Vector(0, -1, 0);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "Ray is orthogonal to the plane, starts UNDER the plane");

        // TC08: The ray begins on the plane(0 points)
        point = new Point(2, 2, -2);
        vector = new Vector(1, 1, 0);
        assertNull(plane.findIntersections(new Ray(point, vector)),
                "Ray is orthogonal to the plane, starts UNDER the plane");

        // TC09: The ray begins at the point saved as reference by the plane aka 'q point'(0 points)
        point = new Point(2, 2, -2);
        vector = new Vector(1, 1, 0);
        Plane planeAltCtor = new Plane(new Point(2, 2, -2), new Vector(0, 1, 0));
        assertNull(planeAltCtor.findIntersections(new Ray(point, vector)),
                "The ray begins at the point saved by the plane(the point provided to the constructor - q)");
    }

    /**
     * Test method for {@link Plane#findGeoIntersections(Ray, double)}.
     */
    @Test
    void testFindGeoIntersections() {
        String msg = "Distance-based intersections calculation does not work properly";
        Point point = new Point(2, 1, 0);
        Vector n = Vector.FORWARDS;
        Plane plane = new Plane(point, n);
        double maxDistance = 4d;
        // ============ Equivalence Partitions Tests ==============

        // TC01: Intersection is within the distance(one intersection)
        Ray ray = new Ray(new Point(0.5, 0, -3), Vector.FORWARDS);
        assertEquals(1, plane.findGeoIntersections(ray, maxDistance).size(), msg);

        // TC02: Intersection is outside the distance(no intersection)
        ray = new Ray(new Point(0.5, 0, -5), Vector.FORWARDS);
        assertNull(plane.findGeoIntersections(ray, maxDistance), msg);

        // =============== Boundary Values Tests ==================

        // TC03: Intersection is precisely in the given distance range(no intersection)
        ray = new Ray(new Point(0.5, 0, -4), Vector.FORWARDS);
        assertNull(plane.findGeoIntersections(ray, maxDistance), msg);
    }
}