package renderer;

import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * Abstract class representing a 2D blackboard in a 3D space, through which we can randomly generate
 * points. the blackboard's shape and pattern of point-generation, is up to the implementing class
 */
public abstract class BlackboardBase {

    /**
     * The diameter of the 2D blackboard (the size of each side)
     */
    protected final double diameter;

    /**
     * The center point of the blackboard
     */
    protected final Point center;

    /**
     * The vector representing the up direction of the blackboard
     */
    protected final Vector up;

    /**
     * The vector representing the right direction of the blackboard.
     * together with the up vector, we can represent a 2D blackboard
     */
    protected final Vector right;

    /**
     * Constructor that initializes a blackboard with diameter, center point and 2D direction vectors
     *
     * @param diameter the diameter of the blackboard
     * @param center   the center point of the blackboard
     * @param normal   normal vector for the black board(can be either direction)
     */
    protected BlackboardBase(double diameter, Point center, Vector normal) {
        this.diameter = diameter;
        this.center = center;
        Vector v = normal.equals(Vector.RIGHT) ? Vector.FORWARDS : Vector.RIGHT;
        up = normal.crossProduct(v).normalize();
        right = up.crossProduct(normal).normalize();
    }

    /**
     * Generate points on the blackboard's grid in a semi-random technique (jitter method)
     *
     * @param totalPoints the minimum total amount of points to generate.
     *                    if the total cell count of the grid cannot be divided perfectly with this number,
     *                    this number will be scaled up (so that there are even amount of points in each cell)
     * @return a list of generated points on the blackboard
     */
    public abstract List<Point> randomizePoints(int totalPoints);
}
