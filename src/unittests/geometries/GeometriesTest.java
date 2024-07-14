package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for the geometries container class
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
class GeometriesTest {

    /**
     * Test method for {@link geometries.Geometries#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Geometries geometries = sceneBuilder();
        Point point = new Point(1.68, 2.93, 0);
        Vector vector = new Vector(0, -1, 0);

        // ============ Equivalence Partitions Tests ==============

        // TC01: Multiple intersections
        assertEquals(6, geometries.findIntersections(new Ray(point, vector)).size(),
                "Wrong intersection count");
        point = new Point(1.5, 3, -2);
        vector = new Vector(0, -1, 0.3);
        assertEquals(5, geometries.findIntersections(new Ray(point, vector)).size(),
                "Wrong intersection count");

        // =============== Boundary Values Tests ==================

        // TC02: Ray intersects with all the geometries in the container
        point = new Point(1.4, 3, -1.4);
        vector = new Vector(0.08, -1, 0.3);
        assertEquals(7, geometries.findIntersections(new Ray(point, vector)).size(),
                "Wrong intersection count");

        // TC03: No intersections
        point = new Point(1.68, 2.93, 0);
        vector = new Vector(0, 1, 0);
        assertNull(geometries.findIntersections(new Ray(point, vector)),
                "Wrong intersection count");

        // TC04: One intersection only
        point = new Point(1, 1, 1);
        vector = new Vector(1, 0, 0);
        assertEquals(1, geometries.findIntersections(new Ray(point, vector)).size(),
                "Wrong intersection count");

        // TC05: Empty geometries container
        Geometries emptyGeos = new Geometries();
        assertNull(emptyGeos.findIntersections(new Ray(point, vector)),
                "Wrong intersection count");
    }

    /**
     * Builds the scene for the tests
     *
     * @return geometries object containing the object for the tests
     */
    private Geometries sceneBuilder() {
        return new Geometries(
                new Plane(new Point(1, 2, 6), new Point(0, 2, 7), new Point(10, 5, 3)),
                new Plane(new Point(0, 2, 1), new Point(0, 2, 3), new Point(10, 2, 0)),
                new Sphere(new Point(2, -3, 0), 0.7),
                new Sphere(new Point(1, 0, 0), 1),
                new Triangle(new Point(2.25, 2, -2.83), new Point(1.4, 2, -0.5), new Point(0.18, 2, -2.81))
        );
    }

}