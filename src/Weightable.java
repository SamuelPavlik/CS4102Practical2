/**
 * Object representing RGB color or weights of triangle's vertex
 */
public class Weightable {
    public double w1;
    public double w2;
    public double w3;

    public Weightable(double w1, double w2, double w3) {
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    /**
     * Add fields of other Weightable object to this Weightable
     * @param weightable other Weightable object
     * @return this Weightable object with added fields
     */
    public Weightable add(Weightable weightable) {
        this.w1 += weightable.w1;
        this.w2 += weightable.w2;
        this.w3 += weightable.w3;

        return this;
    }

    /**
     * Multiply fields by scalar
     * @param scalar scalar to multiply by
     * @return this Weightable object with multiplied fields
     */
    public Weightable mult(double scalar) {
        this.w1 *= scalar;
        this.w2 *= scalar;
        this.w3 *= scalar;

        return this;
    }

    /**
     * @param scalar
     * @return this Weightable with fields divided by scalar
     */
    public Weightable div(double scalar) {
        this.w1 /= scalar;
        this.w2 /= scalar;
        this.w3 /= scalar;

        return this;
    }

    /**
     * @return copy of this Weightable
     */
    public Weightable copy() {
        return new Weightable(this.w1, this.w2, this.w3);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        builder.append(Math.round(w1 *100)/100.0);
        builder.append(",");
        builder.append(Math.round(w2 *100)/100.0);
        builder.append(",");
        builder.append(Math.round(w3 *100)/100.0);
        builder.append("}");
        return builder.toString();
    }
}
