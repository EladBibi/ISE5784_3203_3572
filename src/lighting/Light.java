package lighting;

import primitives.Color;

/**
 * Abstract class representing a light with its intensity
 *
 * @author Elad Bibi &amp; Pini Goldfraind
 */
abstract class Light {
    /**
     * The color intensity of the light
     */
    protected final Color intensity;

    /**
     * Constructor that initializes the light with the given color intensity
     *
     * @param intensity the color intensity for the light
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * Getter for the light's color intensity
     *
     * @return the intensity of the light color
     */
    public Color getIntensity() {
        return intensity;
    }
}
