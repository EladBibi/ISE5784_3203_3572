package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * Represents a Sharp spot-light source in the scene. has position, direction and small,
 * powerful diameter of illumination (can be viewed as a flashlight with a condensed, focused light beam)
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class FlashLight extends SpotLight {

    /**
     * The focus strength of the flash-light source. the higher it is, the more focused
     * and narrow the light projection will be
     */
    private double beamFocusStrength = 15;

    /**
     * Constructor that initializes the flash-light source from teh given color, position, direction
     * and beam focus strength
     *
     * @param intensity         the color intensity for the light source
     * @param position          the position for the light source
     * @param direction         the direction for the light source
     * @param beamFocusStrength the beam diameter of the flash-light source. the higher it is, the more focused
     *                          and narrow the light projection will be
     */
    public FlashLight(Color intensity, Point position, Vector direction, double beamFocusStrength) {
        super(intensity, position, direction);
        this.beamFocusStrength = beamFocusStrength;
    }

    /**
     * Getter method for the flash-light's beam strength
     *
     * @return the beam strength of the flash-light source. the diameter size/strength of the flash-light
     */
    public double getBeamFocusStrength() {
        return beamFocusStrength;
    }

    /**
     * Setter method for the flash-light's beam strength
     * @param beamFocusStrength the beam diameter of the flash-light source. the higher it is, the more focused
     *      *                          and narrow the light projection will be
     * @return the flash-light source object itself
     */
    public FlashLight setBeamFocusStrength(double beamFocusStrength) {
        this.beamFocusStrength = beamFocusStrength;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        //vector from the light-source position to the point
        Vector l = getL(p);
        double angle = alignZero(l.dotProduct(direction));
        double beamFactor = angle > 0 ? Math.pow(angle, beamFocusStrength) : 0;
        //Scaling the intensity of the spot-light by the beam factor
        return super.getIntensity(p).scale(beamFactor);
    }
}
