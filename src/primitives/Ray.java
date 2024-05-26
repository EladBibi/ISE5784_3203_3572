package primitives;

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
    public final Point head;

    /**
     * The ray's direction vector normalized
     */
    public final Vector direction;

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
}
