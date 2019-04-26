import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Triangle implements Comparable<Triangle> {
    private static final float NORMALIZER = 0.05f;

    public PVector p1;
    public PVector p2;
    public PVector p3;
    public Weightable c1;
    public Weightable c2;
    public Weightable c3;
    public PVector centrePoint;
    public Weightable avgColor;

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

    public Triangle addVals(PVector p1, PVector p2, PVector p3, Weightable c1, Weightable c2, Weightable c3) {
        Triangle copy = this.copy();
        copy.p1.add(p1);
        copy.p2.add(p2);
        copy.p3.add(p3);
        copy.c1.add(c1);
        copy.c2.add(c2);
        copy.c3.add(c3);

        return copy;
    }

    public Triangle add(Triangle other, double shWeightNorm, double txWeightNorm) {
        p1.add(other.p1.copy().mult((float) shWeightNorm));
        p2.add(other.p2.copy().mult((float) shWeightNorm));
        p3.add(other.p3.copy().mult((float) shWeightNorm));
        c1.add(other.c1.copy().mult((float) txWeightNorm));
        c2.add(other.c2.copy().mult((float) txWeightNorm));
        c3.add(other.c3.copy().mult((float) txWeightNorm));

        return this;
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

    public PVector getNormal() {
        PVector v1 = p2.copy().add(p1.copy().mult(-1));
        PVector v2 = p3.copy().add(p1.copy().mult(-1));
        PVector normal = v1.cross(v2);

        return normal.normalize();
    }

    public Weightable getAverageColor() {
        return c1.copy().add(c2).add(c3).mult(0.3333);
    }

    public Weightable getLambertianColor(PVector lightVector, double lightInt, double diffCoef) {
        Weightable avgColor = getAverageColor();
        PVector normal = getNormal();
        double toMult = lightVector.normalize().dot(normal) * lightInt * diffCoef;

        return avgColor.mult(toMult);
    }

    public Weightable[] getLambertianColorGrad(PVector lightVector, double lightInt, double diffCoef,
                                               HashMap<PVector, PVector> normMap) {
        Weightable color1 = c1.copy();
        Weightable color2 = c2.copy();
        Weightable color3 = c3.copy();
        double toMult1 = lightVector.normalize().dot(normMap.get(p1)) * lightInt * diffCoef;
        double toMult2 = lightVector.normalize().dot(normMap.get(p2)) * lightInt * diffCoef;
        double toMult3 = lightVector.normalize().dot(normMap.get(p3)) * lightInt * diffCoef;

        return new Weightable[]{color1.mult(toMult1), color2.mult(toMult2), color3.mult(toMult3)};
    }

    public void draw(PApplet pApplet) {
        pApplet.stroke(100);
        pApplet.noFill();
        pApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    public void drawFlat(PApplet pApplet, PVector lightVector, double lightInt, double diffCoef) {
        Weightable color = getLambertianColor(lightVector, lightInt, diffCoef);
        pApplet.fill(((float) color.w1), ((float) color.w2), ((float) color.w3));
        pApplet.stroke(((float) color.w1), ((float) color.w2), ((float) color.w3));
        pApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    public void drawGrad(PApplet pApplet, PVector lightVector, double lightInt, double diffCoef, HashMap<PVector, PVector> normMap) {
        Weightable[] cs = getLambertianColorGrad(lightVector, lightInt, diffCoef, normMap);

        pApplet.beginShape();
        pApplet.stroke(((float) cs[0].w1), ((float) cs[0].w2), ((float) cs[0].w3));
        pApplet.fill(((float) cs[0].w1), ((float) cs[0].w2), ((float) cs[0].w3));
        pApplet.vertex(p1.x, p1.y);
        pApplet.stroke(((float) cs[1].w1), ((float) cs[1].w2), ((float) cs[1].w3));
        pApplet.fill(((float) cs[1].w1), ((float) cs[1].w2), ((float) cs[1].w3));
        pApplet.vertex(p2.x, p2.y);
        pApplet.stroke(((float) cs[2].w1), ((float) cs[2].w2), ((float) cs[2].w3));
        pApplet.fill(((float) cs[2].w1), ((float) cs[2].w2), ((float) cs[2].w3));
        pApplet.vertex(p3.x, p3.y);
        pApplet.endShape();
    }

    public void drawWeights(PApplet pApplet) {
        System.out.println(c1);
        pApplet.fill(0);
        pApplet.text(c1.toString() + "", p1.x, p1.y);
        pApplet.text(c2.toString() + "", p2.x, p2.y);
        pApplet.text(c3.toString() + "", p3.x, p3.y);

        if (pApplet.mousePressed) {
            System.out.println("Pressed");
            PVector mousePos = new PVector(pApplet.mouseX, pApplet.mouseY);
            pApplet.text(getPointWeight(mousePos, c1.w1, c2.w2, c3.w3) + "", pApplet.mouseX, pApplet.mouseY);
        }
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

    private float sign (PVector p1, PVector p2, PVector p3)
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    public boolean PointInTriangle (PVector pt)
    {
        float d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(pt, p1, p2);
        d2 = sign(pt, p2, p3);
        d3 = sign(pt, p3, p1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }

    public Weightable getPointWeight(PVector point, Weightable w1, Weightable w2, Weightable w3) {
        return getPointWeight(point, w1.w1, w2.w2, w3.w3);
    }

    public Weightable getPointWeight(PVector point, double w1, double w2, double w3) {
        double[] bWeights = getBaryWeights(point);
        Weightable result = new Weightable(bWeights[0] * w1, bWeights[1] * w2, bWeights[2] * w3);

        return result.div(bWeights[0] + bWeights[1] + bWeights[2]);
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
        Triangle triangle = new Triangle(p1, p2, p3, new Weightable(10,0, 0), new Weightable(0, 10, 0),
                new Weightable(0, 0, 10));
        triangle.scale(scaleVal);
        triangle.moveBy(new PVector(screenWidth / 2.0f, screenHeight / 2.0f));

        return triangle;
    }

    public static PVector getTotalNormal(Set<Triangle> triangles) {
        PVector total = new PVector(0,0,0);
        for (Triangle t : triangles) {
            total.add(t.getNormal());
        }

        return total.normalize();
    }
}
