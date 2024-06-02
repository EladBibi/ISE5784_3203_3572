package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for sphere geometry
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
class SphereTest {
    private final Point p100 = new Point(1, 0, 0);

    /**
     * Test method for {@link geometries.Sphere#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Standard sphere normal computation
        Point center = new Point(1, 1, 1);
        Vector normal = new Vector(1.5, 0, 0).normalize();
        double radius = 1.5d;

        assertEquals(normal, new Sphere(center, radius).getNormal(new Point(2.5, 1, 1)),
                "Sphere normal computation not working properly");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(p100, 1d);
        final Point gp1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        final Point gp2 = new Point(1.53484692283495, 0.844948974278318, 0);

        var expected = List.of(gp1, gp2);
        final Vector v310 = new Vector(3, 1, 0);
        final Vector v110 = new Vector(1, 1, 0);
        final Point p01 = new Point(-1, 0, 0);

        Point point;
        Vector vector;
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(p01, v110)), "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        final var result1 = sphere.findIntersections(new Ray(p01, v310));
        assertEquals(2, result1.size(), "Wrong number of points");
        assertEquals(expected, result1, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        final Point p1 = new Point(1.3, 0.5, -0.145);
        final var result2 = sphere.findIntersections(new Ray(p1, v310));
        expected = List.of(new Point(1.746878019100727, 0.6489593397002422, -0.145));
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(expected, result2, "Wrong intersection points");

        // TC04: Ray starts after the sphere (0 points)
        point = new Point(-0.5, 0, 0);
        vector = new Vector(-1, -0.1, 0.1);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Wrong number of points");

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC05: Ray starts at sphere and goes inside (1 point)
        expected = List.of(new Point(0.04950495049504933, 0.28712871287128694, 0.11881188118811883));
        point = new Point(1, 1, 0);
        vector = new Vector(-4, -3, 0.5);
        assertEquals(1, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray starts on the sphere edge and goes inside. 1 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray starts on the sphere edge and goes inside. 1 points. wrong intersection point");

        // TC06: Ray starts at sphere and goes outside (0 points)
        point = new Point(1, 1, 0);
        vector = new Vector(-4, 1, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray starts on the sphere edge and goes outside. 0 points");

        // **** Group: Ray's line goes through the center
        // TC07: Ray starts before the sphere (2 points)
        point = new Point(1, 2, 0);
        vector = new Vector(0, -1, 0);
        final Ray ray1 = new Ray(point, vector);
        expected = Stream.of(new Point(1, -1, 0), new Point(1, 1, 0))
                .sorted(Comparator.comparingDouble(p -> p.distance(ray1.getHead()))).toList();
        assertEquals(2, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray starts outside, goes through center. 2 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray starts outside, goes through center. 2 points. wrong intersection points");

        // TC08: Ray starts at sphere and goes inside (1 points)
        point = new Point(1, 1, 0);
        vector = new Vector(0, -1, 0);
        expected = List.of(new Point(1, -1, 0));
        assertEquals(1, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray starts on edge, goes through center. 1 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray starts on edge, goes through center. 1 points");

        // TC09: Ray starts inside (1 points)
        point = new Point(1, 0.6, 0);
        vector = new Vector(0, 1, 0);
        expected = List.of(new Point(1, 1, 0));
        assertEquals(1, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray starts inside. 1 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray starts inside. 1 points");

        // TC10: Ray starts inside and passes through center (1 points)
        point = new Point(1, -0.6, 0);
        vector = new Vector(0, 1, 0);
        expected = List.of(new Point(1, 1, 0));
        assertEquals(1, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray starts inside, passes through center. 1 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray starts inside, passes through center. 1 points");

        // TC11: Ray starts at the center (1 points)
        point = new Point(1, 0, 0);
        vector = new Vector(0, 1, 0);
        expected = List.of(new Point(1, 1, 0));
        assertEquals(1, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray starts at center. 1 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray starts at center. 1 points");

        // TC12: Ray starts at sphere and goes outside (0 points)
        point = new Point(1, 1, 0);
        vector = new Vector(0, 1, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray starts on edge, goes outside. 0 points");

        // TC13: Ray starts after sphere (0 points)
        point = new Point(1, 1.5, 0);
        vector = new Vector(0, 1, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray starts outside, goes outside. 0 points");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC14: Ray starts before the tangent point(0 points)
        point = new Point(1.6, 1, 0);
        vector = new Vector(-1, 0, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray is tangent to the sphere(kisses the edge once). 0 points");

        // TC15: Ray starts at the tangent point(0 points)
        point = new Point(1, 1, 0);
        vector = new Vector(-1, 0, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray starts on edge, goes outside. 0 points");

        // TC16: Ray starts after the tangent point(0 points)
        point = new Point(0.5, 1, 0);
        vector = new Vector(-1, 0, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray is on tangent line to the sphere, starts after the intersection point. 0 points");

        // **** Group: Special cases
        // TC17: Ray's line is inside, ray is orthogonal to ray start to sphere's center line(1 points)
        point = new Point(1.6, 0, 0);
        vector = new Vector(0, 1, 0);
        expected = List.of(new Point(1.6, 0.8, 0));
        assertEquals(1, sphere.findIntersections(new Ray(point, vector)).size(),
                "Ray is tangent to the center point, starts inside. 1 points");
        assertEquals(expected, sphere.findIntersections(new Ray(point, vector)),
                "Ray is tangent to the center point, starts inside. 1 points");

        // TC18: Ray's line is outside, ray is orthogonal to ray start to sphere's center line(0 points)
        point = new Point(2.6, 0, 0);
        vector = new Vector(0, 1, 0);
        assertNull(sphere.findIntersections(new Ray(point, vector)),
                "Ray is tangent to the center point, starts outside. 0 points");
    }
}