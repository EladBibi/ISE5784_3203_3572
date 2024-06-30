package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * Represents a spot-light source. has position, direction and a powerful directional
 * light spread (can be viewed as a projector)
 *
 * @author Pini Goldfraind &amp; Elad Bibi
 */
public class SpotLight extends PointLight {

    /**
     * The direction of the light-source
     */
    protected final Vector direction;

    /**
     * Constructor that initializes the spot-light source with the given color, position and direction
     *
     * @param intensity the color intensity of the light source
     * @param position  the position of the light source
     * @param direction the direction of the light source
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * Constructor that initializes the spot-light source with the given color, position and focus point
     *
     * @param intensity   the color intensity of the light source
     * @param position    the position of the light source
     * @param lookAtPoint a point in the scene for the spot-light to be focused on
     */
    public SpotLight(Color intensity, Point position, Point lookAtPoint) {
        super(intensity, position);
        direction = lookAtPoint.subtract(position).normalize();
    }

    @Override
    public SpotLight setKc(double kC) {
        return (SpotLight) super.setKc(kC);
    }

    @Override
    public SpotLight setKl(double kL) {
        return (SpotLight) super.setKl(kL);
    }

    @Override
    public SpotLight setKq(double kQ) {
        return (SpotLight) super.setKq(kQ);
    }

    @Override
    public Color getIntensity(Point p) {
        //extracting the angle from the spot-light forward direction
        //and the source-to-point direction
        double angle = alignZero(getL(p).dotProduct(direction));
        if (angle <= 0) return Color.BLACK;
        return super.getIntensity(p).scale(angle);
    }

}
