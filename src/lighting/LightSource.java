package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Interface containing elementary methods for acquiring illumination-colors
 * at different points in our scene
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
public interface LightSource {

    /**
     * Gives the color intensity of the given point in the scene, illuminated by this light-source
     *
     * @param p a point in the scene
     * @return the illumination color of the given point
     */
    public Color getIntensity(Point p);

    /**
     * Gives the squared distance from the light source origin-point to the given point
     *
     * @return the squared distance from the light source origin-point to the given point
     */
    double getDistanceSquared(Point point);

    /**
     * Gives the normalized direction vector from this light-source to the given point
     *
     * @param p a point in the scene
     * @return the normalized direction vector to the given scene
     */
    public Vector getL(Point p);
}
