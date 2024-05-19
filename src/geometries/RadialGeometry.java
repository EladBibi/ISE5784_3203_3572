package geometries;

/**
 * Abstract class that represents a radial geometry object in a 3D space
 *
 * @author Pini &amp; Elad Bibi
 */
public abstract class RadialGeometry implements Geometry {

    /**
     * Geometry's radius
     */
    protected final double radius;

    protected final double radiusSquared;

    /**
     * Constructor that and initializes the geometry with the given radius
     *
     * @param radius geometry's radius as double
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }
}
