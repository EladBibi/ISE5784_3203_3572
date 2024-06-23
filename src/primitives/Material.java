package primitives;

/**
 * Represents a material of an object
 */
public class Material {
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
     * Setter for the Diffusion factor of the material
     *
     * @param kD kD factor for the material
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
     * @param kS kD factor for the material
     * @return the material object itself
     */
    public Material setKs(Double kS) {
        this.kS = new Double3(kS);
        return this;
    }

    /**
     * Setter for the shininess factor of the material
     *
     * @param nShininess the shininess factor for the material
     * @return the material object itself
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }
}
