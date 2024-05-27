package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for sphere geometry
 *
 * @author Elad Bibi
 */
class SphereTest {

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
}