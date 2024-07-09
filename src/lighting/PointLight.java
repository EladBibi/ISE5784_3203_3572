package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * Represents a point-light source. light that has a position and spreads light
 * equally in its 360 degrees diameter (a bulb for instance)
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class PointLight extends Light implements LightSource {

    /**
     * The position of the light source in the scene
     */
    protected final Point position;
    /**
     * Constant factor for light range from the source. with the weakest effect on spread.
     * default is one
     */
    private double kC = 1;
    /**
     * The second factor for light spread range from the source. middle effect on light range
     */
    private double kL = 0;
    /**
     * The third factor for light spread range from the source. has the strongest effect
     */
    private double kQ = 0;

    /**
     * Constructor that initiates a point light source from the given color intensity and source positioning
     *
     * @param intensity the color intensity of the point light source
     * @param position  the position of the light source
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * Setter for the first light spread factor
     *
     * @param kC kC factor used when computing the color intensity at range from the source
     * @return the point light source object itself
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * Setter for the second light spread factor
     *
     * @param kL kL factor used when computing the color intensity at range from the source.
     *           the higher it is, the shorter the range the light-source will have (light fades faster)
     * @return the point light source object itself
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * Setter for the third, strongest light spread factor
     *
     * @param kQ kQ factor used when computing the color intensity at range from the source.
     *           the higher it is, the shorter the range the light-source will have (light fades faster)
     * @return the point light source object itself
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

    @Override
    public Color getIntensity(Point p) {
        double disFromSource = p.distance(position);
        double scalingFactor = 1d / (kC + (kL * disFromSource) + (kQ * (disFromSource * disFromSource)));
        return intensity.scale(scalingFactor);
    }

    @Override
    public double getDistance(Point point) {
        return position.distance(point);
    }

    @Override
    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }
}
