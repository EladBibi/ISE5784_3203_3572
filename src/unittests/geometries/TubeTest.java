package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Tests for tube geometry(infinite cylinder)
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
class TubeTest {

    /**
     * Test method for {@link geometries.Tube#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Standard normal computation for tube
        Ray center = new Ray(new Point(2, 1, 1), new Vector(0, 1, 0));
        double radius = 1.5d;
        Vector normal = new Vector(1.5, 0, 0).normalize();

        assertEquals(normal, new Tube(center, radius).getNormal(new Point(3.5, 2, 1)),
                "Tube normal computation not working properly");

        // =============== Boundary Values Tests ==================

        // TC02: the center-to-point vector is orthogonal to the center vector
        normal = new Vector(-1.5, 0, 0).normalize();
        assertEquals(normal, new Tube(center, radius).getNormal(new Point(0.5, 1, 1)),
                "Normal computation issue when the point-to-center vector is orthogonal(perpendicular) " +
                        "to the center-ray's direction vector");
    }

    /**
     * Test method for {@link geometries.Tube#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
    }
}