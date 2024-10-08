package lighting;

import primitives.Color;
import primitives.Double3;

/**
 * A class representing the scene's ambient light. a light that has no source and
 * does not fade throughout the scene
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class AmbientLight extends Light {

    /**
     * Constant for the representing NO ambient light (black color intensed to zero)
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, 0d);

    /**
     * A constructor which initializes the ambient light with the given color and intensity
     *
     * @param color the color of the ambient light
     * @param kA    double3 object for scaling the given color with. the color's rgb
     *              coords will be scaled with the respective xyz coords of the given double3 object
     */
    public AmbientLight(Color color, Double3 kA) {
        super(color.scale(kA));
    }

    /**
     * A constructor which initializes the ambient light with the given color and intensity
     *
     * @param color the color of the ambient light
     * @param kA    scalar for scaling the given color with
     */
    public AmbientLight(Color color, Double kA) {
        super(color.scale(kA));
    }

}
