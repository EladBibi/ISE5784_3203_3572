package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static primitives.Util.isZero;


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
        assertThrows(Exception.class, () -> v1.add(v1Opposite),
                "ERROR: Vector + -itself does not throw an exception");
        assertThrowsExactly(IllegalArgumentException.class, () -> v1.add(v1Opposite),
                "ERROR: Vector + -itself throws the wrong exception");
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
        assertTrue(isZero(v1.dotProduct(v2) + 28),
                "DotProduct operation not working properly");

        // =============== Boundary Values Tests ==================

        // TC02: Dot product operation on orthogonal vectors
        assertTrue(isZero(v1.dotProduct(v3)),
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
        assertTrue(isZero(vr.dotProduct(v1)) && isZero(vr.dotProduct(v3)),
                "crossProduct() result is not orthogonal to its operands");

        // TC02: Different length vectors
        assertTrue(isZero(vr.length() - v1.length() * v3.length()),
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
        assertTrue(isZero(v4.lengthSquared() - 9),
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
        assertTrue(isZero(v4.length() - 3),
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
        assertTrue(isZero(u.length() - 1),
                "the normalized vector is not a unit vector");
        assertFalse(v.dotProduct(u) < 0,
                "the normalized vector is opposite to the original one");
        assertThrows(Exception.class, () -> v.crossProduct(u),
                "the normalized vector is not parallel to the original one. exception throw is expected");
    }
}