package primitives;

import geometries.Plane;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.alignZero;


/**
 * Tests for vector class methods
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
class VectorTests {

    /**
     * Delta value for accuracy when comparing the numbers of type 'double' in
     * assertEquals
     */
    private final double DELTA = 0.001;

    /**
     * Test method for {@link primitives.Vector#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============

        Vector v1 = new Vector(1, 2, 3);
        Vector v1Opposite = new Vector(-1, -2, -3);
        Vector v2 = new Vector(-2, -4, -6);

        // TC01: Standard vector addition
        assertEquals(v1Opposite, v1.add(v2),
                "Vector addition not working properly");

        // =============== Boundary Values Tests ==================

        // TC02: Vector addition with the opposite
        assertThrows(IllegalArgumentException.class, () -> v1.add(v1Opposite),
                "ERROR: Vector + -itself does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============

        Vector v1 = new Vector(1, -2, 3);

        // TC01: Standard vector scaling
        assertEquals(new Vector(-5f, 10f, -15f), v1.scale(-5f),
                "Vector scaling operation not working properly with negative numbers");

        // =============== Boundary Values Tests ==================
        // TC02: Scaling vector by zero
        assertThrows(Exception.class, () -> v1.scale(0f),
                "Vector scaling operation did not throw exception for zero-vector");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);

        // TC01: Standard DotProduct operation
        assertEquals(0d, alignZero(v1.dotProduct(v2) + 28),
                "DotProduct operation not working properly");

        // =============== Boundary Values Tests ==================

        // TC02: Dot product operation on orthogonal vectors
        assertEquals(0d, alignZero(v1.dotProduct(v3)),
                "DotProduct operation on orthogonal vectors is not zero");

        // TC03: One operand is a unit-vector
        assertEquals(-3.741d, v1.dotProduct(v2.normalize()), DELTA);

    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(-2, -4, -6);
        Vector v3 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v3);

        // TC01: Verifying orthogonality to the operands
        assertEquals(0d, alignZero(vr.dotProduct(v1)),
                "crossProduct() result is not orthogonal to its operands");
        assertEquals(0d, alignZero(vr.dotProduct(v3)),
                "crossProduct() result is not orthogonal to its operands");

        // TC02: Different length vectors
        assertEquals(0d, alignZero(vr.length() - v1.length() * v3.length()),
                "crossProduct() wrong result length");

        // =============== Boundary Values Tests ==================

        // TC03: Vectors are parallel to each other
        assertThrows(Exception.class, () -> v1.crossProduct(v2),
                "crossProduct() on parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        Vector v4 = new Vector(1, 2, 2);

        // TC01: Standard squared-length computation
        assertEquals(0d, alignZero(v4.lengthSquared() - 9),
                "vector lengthSquared gave the wrong value");
    }

    /**
     * Test method for {@link Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        Vector v4 = new Vector(1, 2, 2);

        // TC01: Standard Vector length computation
        assertEquals(0d, alignZero(v4.length() - 3),
                "vector length gave the wrong value");
    }

    /**
     * Test method for {@link Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        Vector u = v.normalize();

        // TC01: Standard Vector normalization
        assertEquals(0d, alignZero(u.length() - 1),
                "the normalized vector is not a unit vector");
        assertFalse(v.dotProduct(u) < 0,
                "the normalized vector is opposite to the original one");
        assertThrows(Exception.class, () -> v.crossProduct(u),
                "the normalized vector is not parallel to the original one. exception throw is expected");
    }

    /**
     * Test method for {@link Vector#rotate(Vector, double)}.
     */
    @Test
    void testRotate() {
        Vector n = new Vector(0, 0, 1);
        Vector u = Vector.UP;

        // TC01: Standard rotation on the xy plane (90 degrees)
        Vector result1 = new Vector(1, 0, 0);
        assertEquals(result1, u.rotate(n, 90), "wrong rotation vector");

        // TC02: Standard rotation on the xy plane (180 degrees)
        Vector result2 = new Vector(0, -1, 0);
        assertEquals(result2, u.rotate(n, 180), "wrong rotation vector");

        // TC03: Standard rotation on the xy plane (270 degrees)
        Vector result3 = new Vector(-1, 0, 0);
        assertEquals(result3, u.rotate(n, 270), "wrong rotation vector");

        // TC04: Full rotation on the xy plane (360 degrees)
        Vector result4 = Vector.UP;
        assertEquals(result4, u.rotate(n, 360), "wrong rotation vector");

        // =============== Boundary Values Tests ==================

        // TC05: Zero degree rotation (no rotation)
        Vector result5 = Vector.UP;
        assertEquals(result5, u.rotate(n, 0), "wrong rotation vector");

        // TC06: Rotate vector that is aligned with the normal (should raise an exception)
        Vector alignedWithNormal = new Vector(0, 0, 1);
        assertThrows(IllegalArgumentException.class, () -> alignedWithNormal.rotate(n, 90),
                "Expected an IllegalArgumentException to be thrown");

        // TC07: Rotation on a plane parallel to xy but with an arbitrary point
        Plane arbitraryPlane = new Plane(new Point(3, 4, 5), n);
        Vector result6 = new Vector(1, 0, 0);
        assertEquals(result6, u.rotate(n, 90), "wrong rotation vector");

        // TC08: Rotation with negative angle (should handle negative degrees correctly)
        Vector result7 = new Vector(-1, 0, 0);
        assertEquals(result7, u.rotate(n, -90), "wrong rotation vector");

        // TC09: Rotation on the yz plane (-90 degrees)
        Plane yzPlane = new Plane(new Point(0, 1, 0), new Vector(1, 0, 0));
        Vector v = new Vector(0, 1, 0);
        Vector result8 = new Vector(0, 0, 1);
        assertEquals(result8, v.rotate(new Vector(1, 0, 0), -90), "wrong rotation vector");

        // TC10: Rotation on the xz plane (-90 degrees)
        Plane xzPlane = new Plane(new Point(1, 0, 0), new Vector(0, 1, 0));
        Vector w = new Vector(1, 0, 0);
        Vector result9 = new Vector(0, 0, -1);
        assertEquals(result9, w.rotate(new Vector(0, 1, 0), -90), "wrong rotation vector");

        // TC11: Large degree rotation (450 degrees, equivalent to 90 degrees)
        Vector result10 = new Vector(1, 0, 0);
        assertEquals(result10, u.rotate(n, 450), "wrong rotation vector");
    }
}
