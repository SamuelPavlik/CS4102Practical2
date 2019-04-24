import processing.core.PApplet;
import processing.core.PVector;

public class Triangle implements Comparable<Triangle> {
    private static final float NORMALIZER = 0.05f;

    public PVector p1;
    public PVector p2;
    public PVector p3;
    public Weightable c1;
    public Weightable c2;
    public Weightable c3;
    public PVector centrePoint;

    public Triangle(PVector p1, PVector p2, PVector p3, Weightable c1, Weightable c2, Weightable c3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.centrePoint = getCentre();
    }

    public Triangle(PVector p1, PVector p2, PVector p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Triangle addVals(PVector p1, PVector p2, PVector p3, Color c1, Color c2, Color c3) {
        Triangle copy = this.copy();
        copy.p1.add(p1);
        copy.p2.add(p2);
        copy.p3.add(p3);
        copy.c1.add(c1);
        copy.c2.add(c2);
        copy.c3.add(c3);

        return copy;
    }

    public Triangle copy() {
        return new Triangle(p1.copy(), p2.copy(), p3.copy(), c1.copy(), c2.copy(), c3.copy());
    }

    public PVector getCentre() {
        return (p1.copy().add(p2).add(p3)).mult(0.3333f);
    }

    public Triangle scale(float val) {
        p1.mult(val);
        p2.mult(val);
        p3.mult(val);

        return this;
    }

    public Triangle moveBy(PVector vector) {
        p1.add(vector);
        p2.add(vector);
        p3.add(vector);

        return this;
    }

    public Triangle reverse() {
        p1.y = -p1.y;
        p2.y = -p2.y;
        p3.y = -p3.y;

        return this;
    }

    public Weightable getAverageColor() {
        return c1.copy().add(c2).add(c3).mult(0.3333);
    }

    public void draw(PApplet pApplet) {
        pApplet.stroke(100);
//        pApplet.noFill();
        pApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    @Override
    public int compareTo(Triangle triangle) {
        float diff = centrePoint.z - triangle.centrePoint.z;
        if (diff > 0)
            return 1;
        else if (diff < 0)
            return -1;
        else
            return 0;
    }

    private double getPointWeight(PVector point, double w1, double w2, double w3) {
        double[] bWeights = getBaryWeights(point);
        double resultWeight = (bWeights[0] * w1 + bWeights[1] * w2 + bWeights[2] * w3) /
                            (bWeights[0] + bWeights[1] + bWeights[2]);

        return resultWeight;
    }

    private double[] getBaryWeights(PVector point) {
        double w1 = ((p2.y - p3.y)*(point.x - p3.x) + (p3.x - p2.x)*(point.y - p3.y)) /
                ((p2.y - p3.y)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.y - p3.y));

        double w2 = ((p3.y - p1.y)*(point.x - p3.x) + (p1.x - p3.x)*(point.y - p3.y)) /
                ((p2.y - p3.y)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.y - p3.y));

        double w3 = (1 - w1) - w2;

        return new double[]{w1, w2, w3};
    }

    public static Triangle triangleInCentre(int screenWidth, int screenHeight, float scaleVal) {
        PVector p1 = new PVector(-1, 1);
        PVector p2 = new PVector(1, 1);
        PVector p3 = new PVector(0, -1);
        Triangle triangle = new Triangle(p1, p2, p3);
        triangle.scale(scaleVal);
        triangle.moveBy(new PVector(screenWidth / 2.0f, screenHeight / 2.0f));

        return triangle;
    }
}
