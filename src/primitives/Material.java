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

    public double transparencyBlur = 0;

    //the distance
    public double transparencyBlurRange = 0;

    public double reflectionBlur = 0;

    public double reflectionBlurRange = 0;

    public int blurLod = 30;

    public Material setTransparencyBlur(double blur, double blurRange) {
        transparencyBlur = blur;
        transparencyBlurRange = blurRange;
        return this;
    }

    public Material setReflectionBlur(double blur, double blurRange) {
        reflectionBlur = blur;
        reflectionBlurRange = blurRange;
        return this;
    }

    public Material setBlurLod(int rayCasts) {
        blurLod = rayCasts;
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
