public class Color implements Weightable {
    public double r;
    public double g;
    public double b;

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public Color add(Weightable weightable) {
        Color color = (Color) weightable;
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;

        return this;
    }

    @Override
    public Color mult(double scalar) {
        this.r *= scalar;
        this.g *= scalar;
        this.b *= scalar;

        return this;
    }

    @Override
    public Color copy() {
        return new Color(this.r, this.g, this.b);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
