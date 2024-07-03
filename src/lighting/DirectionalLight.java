package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents directional light in a scene. has direction but no position
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
public class DirectionalLight extends Light implements LightSource {

    /**
     * The direction of the directional light-source
     */
    private final Vector direction;

    /**
     * Constructor that initializes a directional light from the given color intensity and direction vector
     *
     * @param intensity the color intensity for the light source
     * @param direction the direction vector for the light source
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    @Override
    public Color getIntensity(Point p) {
        return intensity;
    }

    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public Vector getL(Point p) {
        return direction;
    }
}
