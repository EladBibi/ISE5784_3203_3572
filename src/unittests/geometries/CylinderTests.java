package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for Cylinder geometry(finite) in a 3D space
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
class CylinderTests {

    /**
     * Test method for {@link geometries.Cylinder#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
    }

    /**
     * Test method for {@link geometries.Cylinder#getNormal(Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: standard cylinder normal computation
        Point rayHead = new Point(2, 1, 1);
        Vector rayDir = new Vector(0, 1, 0);
        Ray center = new Ray(rayHead, rayDir);
        Cylinder cylinder = new Cylinder(center, 1.5d, 5d);
        Vector normal = new Vector(2, 0, 1).normalize();

        assertEquals(normal, cylinder.getNormal(new Point(4, 2, 2)),
                "Cylinder normal computation not working properly");

        // TC02: The Point is on the cylinder's bottom base (an inverted center-vector is expected as normal)
        assertEquals(rayDir.inverted(), cylinder.getNormal(new Point(2.4, 1, -0.3)),
                "Issue when computing normal for a point on the cylinder's base");

        // TC03: The Point is on the cylinder's ceiling (the center-vector is expected as normal)
        assertEquals(rayDir, cylinder.getNormal(new Point(2.4, 6, -0.3)),
                "Issue when computing normal for a point on the cylinder's ceiling");

        // =============== Boundary Values Tests ==================

        // TC04: The Point is on the edge of the cylinder's base
        Point edgePoint = new Point(0.5, 1, 1);
        assertEquals(rayDir.inverted(), cylinder.getNormal(edgePoint),
                "Issue when computing normal for a point that's on the base's edge");

        // TC05: The Point is on the edge of the cylinder's ceiling
        Point edgePointTop = new Point(0.5, 6, 1);
        assertEquals(rayDir, cylinder.getNormal(edgePointTop),
                "Issue when computing normal for a point that's on the ceiling edge of the cylinder");

        // TC06: The Point is the cylinder's head-point(middle of the base)
        assertEquals(rayDir.inverted(), cylinder.getNormal(rayHead),
                "Issue when computing normal for the cylinder's head point");

        // TC07: The Point is the cylinder's head-point projected on the ceiling(middle of the ceiling)
        Point headTop = new Point(2, 6, 1);
        assertEquals(rayDir, cylinder.getNormal(headTop),
                "Issue when computing normal for the center point on the cylinder's top");
    }

}