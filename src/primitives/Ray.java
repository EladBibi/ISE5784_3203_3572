package primitives;

import java.util.Comparator;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Represents a Ray in a 3-Dimensional space.
 * contains head-point for the ray's starting position and a normalized direction vector
 *
 * @author Pini Goldfraind
 */
public class Ray {

    /**
     * The ray's starting position in the 3D space
     */
    private final Point head;

    /**
     * The ray's direction vector normalized
     */
    private final Vector direction;

    /**
     * Constructor that accepts a head point and direction vector.
     * will only initialize the ray with a normalized direction vector.
     * will normalize the given direction vector if it's not already
     *
     * @param head      starting position for the ray
     * @param direction direction of the ray. does not have to be normalized
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        if (direction.length() != 1f)
            direction = direction.normalize();
        this.direction = direction;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public final String toString() {
        return "h" + this.head + " " + this.direction;
    }

    /**
     * Getter for the ray's head point
     *
     * @return the head point of the ray
     */
    public Point getHead() {
        return this.head;
    }

    /**
     * Getter for the ray's direction vector
     *
     * @return the direction vector of the ray
     */
    public Vector getDirection() {
        return this.direction;
    }

    /**
     * Gives a point on the ray
     *
     * @param t scalar for scaling the direction vector
     * @return head point + t * direction vector
     */
    public Point getPoint(double t) {
        return isZero(t) ? head : head.add(direction.scale(t));
    }

    /**
     * Returns the point from the given list that is the closest to the ray's starting point
     *
     * @param points container of points in the space
     * @return the closest point the ray's starting position (head point)
     */
    public Point findClosestPoint(List<Point> points) {
        return points.stream().min(Comparator.comparingDouble(p -> this.head.distance(p))).orElse(null);
    }
}
