package primitives;

/**
 * Represents a material of an object
 */
public class Material {

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
     * The blurriness spread of objects that are obscured by the object with this material.
     * (blurry or dirty mirror effect)
     */
    public double transparencyBlur = 0;

    /**
     * The distance of the transparency blur effect. the lower it is, the closer objects
     * need to be in order for them to be visible through the transparent material.
     * (set to low number for an extremely blurry mirror effect. set to higher number to
     * achieve a mirror that blurs object that are far)
     */
    public double transparencyBlurRange = 0;

    /**
     * How blurred objects will be reflected on this material
     */
    public double reflectionBlur = 0;

    /**
     * The distance of the reflection blur effect. the lower it is, the stronger the blur effect of
     * close object on this material. (set to low number for close object's reflection to be blurry.
     * set to higher number to achieve blurrier reflection of far objects)
     */
    public double reflectionBlurRange = 0;

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
     * @param blur      how blur the material is
     * @param blurRange how strong the blurriness effect is at range
     * @param rayCasts the minimum amount of ray casts for the multisampling of this effect
     * @return the material object itself
     */
    public Material setTransparencyBlur(double blur, double blurRange, int rayCasts) {
        transparencyBlur = blur;
        transparencyBlurRange = blurRange;
        transparencyBlurCasts = rayCasts;
        return this;
    }

    /**
     * Setter for the reflection blur effect
     *
     * @param blur      how blurred objects will be reflected on this material
     * @param blurRange how strong the blurriness effect is for objects at range
     * @param rayCasts the minimum amount of ray casts for the multisampling of this effect
     * @return the material object itself
     */
    public Material setReflectionBlur(double blur, double blurRange, int rayCasts) {
        reflectionBlur = blur;
        reflectionBlurRange = blurRange;
        reflectionBlurCasts = rayCasts;
        return this;
    }

    /**
     * Setter for the minimum amount of ray casts per reflection blurriness multisampling computation.
     *
     * @param rayCasts the minimum amount of ray casts
     * @return the material object itself
     */
    public Material setReflectionBlurCasts(int rayCasts) {
        reflectionBlurCasts = rayCasts;
        return this;
    }

    /**
     * Setter for the minimum amount of ray casts per transparency blurriness multisampling computation.
     *
     * @param rayCasts the minimum amount of ray casts
     * @return the material object itself
     */
    public Material setTransparencyBlurCasts(int rayCasts) {
        transparencyBlurCasts = rayCasts;
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
