package primitives;

/**
 * Represents a material of an object
 */
public class Material {

    /**
     * The distance of the ray's origin from the blackboard used for the super-sampling
     * algorithms for creating diffusive and reflective surfaces. the strength of the effect(aka
     * the blackboard's size) will be determined by the given angle (the angle from the origin ray)
     */
    public final double SUPER_SAMPLING_BLACKBOARD_DISTANCE = 1;

    /**
     * The transparency factor of the material. determines how much light
     * passes through (1 = fully transparent)
     */
    public Double3 kT = Double3.ZERO;

    /**
     * The refraction factor of the material. determines how much light will
     * bounce off the material (1 = reflective mirror)
     */
    public Double3 kR = Double3.ZERO;

    /**
     * The Diffusion factor of the material
     */
    public Double3 kD = Double3.ZERO;
    /**
     * The Shininess factor of the material
     */
    public Double3 kS = Double3.ZERO;
    /**
     * The shininess factor of the material. the higher it is, the more specular
     * light will be generated
     */
    public int nShininess = 0;

    /**
     * The diameter of the blackboard used for the diffusive glass effect.
     * The blurriness spread of objects that are obscured by the object with this material.
     * (blurry or dirty glass effect)
     */
    public double transparencyBlackboardDiameter = 0;


    /**
     * The diameter of the blackboard used for the glossy surfaces effect.
     * How blurred objects will be reflected on this material
     */
    public double reflectionBlackboardDiameter = 0;

    /**
     * The minimum amount of ray casts per reflection blurriness multisampling computation.
     * improves image-quality but heavily impacts performance
     */
    public int reflectionBlurCasts = 1;

    /**
     * The minimum amount of ray casts per transparency blurriness multisampling computation.
     * improves image-quality but heavily impacts performance
     */
    public int transparencyBlurCasts = 1;

    /**
     * Setter for the transparency blur effect
     *
     * @param blurAngle the angle between the casting ray and the multisampling blackboard.
     *                  the higher the angle, the more blurry objects that are behind the
     *                  material will appear (blurred glass effect)
     * @param rayCasts  the minimum amount of ray casts for the multisampling of this effect
     * @return the material object itself
     */
    public Material setTransparencyBlur(double blurAngle, int rayCasts) {
        if (blurAngle >= 90 || blurAngle < 0)
            throw new IllegalArgumentException("The angle must be between 0 and 90");
        double blurAngleInRadians = Math.toRadians(blurAngle);
        transparencyBlackboardDiameter = 2 * Math.tan(blurAngleInRadians);
        transparencyBlurCasts = rayCasts;
        return this;
    }

    /**
     * Setter for the reflection blur effect
     *
     * @param blurAngle the angle between the casting ray and the multisampling blackboard.
     *                  the higher the angle, the more blurry object's reflections will appear on the
     *                  material (blurred mirror effect)
     * @param rayCasts  the minimum amount of ray casts for the multisampling of this effect
     * @return the material object itself
     */
    public Material setReflectionBlur(double blurAngle, int rayCasts) {
        if (blurAngle >= 90 || blurAngle < 0)
            throw new IllegalArgumentException("The angle must be between 0 and 90");
        double blurAngleInRadians = Math.toRadians(blurAngle);
        reflectionBlackboardDiameter = 2 * Math.tan(blurAngleInRadians);
        reflectionBlurCasts = rayCasts;
        return this;
    }

    /**
     * Setter for the Diffusion factor of the material
     *
     * @param kD kD factor for the material.
     *           the higher it is the larger the diffusion light-spread will be
     * @return the material object itself
     */
    public Material setKd(Double3 kD) {
        this.kD = kD;
        return this;
    }

    /**
     * Setter for the Diffusion factor of the material
     *
     * @param kD kD factor for the material
     * @return the material object itself
     */
    public Material setKd(Double kD) {
        this.kD = new Double3(kD);
        return this;
    }

    /**
     * Setter for the Specular factor of the material
     *
     * @param kS kD factor for the material
     * @return the material object itself
     */
    public Material setKs(Double3 kS) {
        this.kS = kS;
        return this;
    }

    /**
     * Setter for the Specular factor of the material
     *
     * @param kS kD factor for the material.
     *           the higher it is the stronger the specular light reflection will be
     * @return the material object itself
     */
    public Material setKs(Double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for the transparency factor
     *
     * @param kT the transparency factor. determines how mush light can pass through the material surface
     * @return the material object itself
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /**
     * Setter for the transparency factor
     *
     * @param kT the transparency factor. determines how mush light can pass through the material surface
     * @return the material object itself
     */
    public Material setKt(Double kT) {
        this.kT = new Double3(kT);
        return this;
    }

    /**
     * Setter for the reflectiveness factor
     *
     * @param kR the reflectiveness factor. determines how mush light gets
     *           *           reflected off the material surface
     * @return the material object itself
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /**
     * Setter for the reflectiveness factor
     *
     * @param kR the reflectiveness factor. determines how mush light gets
     *           reflected off the material surface
     * @return the material object itself
     */
    public Material setKr(Double kR) {
        this.kR = new Double3(kR);
        return this;
    }

    /**
     * Setter for the shininess factor of the material
     *
     * @param nShininess the shininess factor for the material.
     *                   the higher it is, the smaller the specular-light-reflection point will be
     * @return the material object itself
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }


}
