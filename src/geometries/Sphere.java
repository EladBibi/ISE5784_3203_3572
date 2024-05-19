package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * A sphere in a three-dimensional space, represented with a center point and radius
 *
 * @author Pini Goldfraind
 */
public class Sphere extends RadialGeometry {

    private final Point center;

    /**
     * Constructor that initializes the sphere from the given center-point and radius
     *
     * @param center point that represents the sphere's center
     * @param radius sphere's radius
     */
    public Sphere(Point center, double radius) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
