import processing.core.PApplet;
import processing.core.PVector;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Object to store triangle vertices and weights or colors assigned to those vertices
 */
public class Triangle implements Comparable<Triangle> {
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

    /**
     * Add points and colors of another triangle to this triangle
     * @param other other triangle
     * @param shWeightNorm weight of shading normalised between 0 and 1
     * @param txWeightNorm weight of texture normalised between 0 and 1
     * @return this triangle with after the addition
     */
    public Triangle add(Triangle other, double shWeightNorm, double txWeightNorm) {
        p1.add(other.p1.copy().mult((float) shWeightNorm));
        p2.add(other.p2.copy().mult((float) shWeightNorm));
        p3.add(other.p3.copy().mult((float) shWeightNorm));
        c1.add(other.c1.copy().mult((float) txWeightNorm));
        c2.add(other.c2.copy().mult((float) txWeightNorm));
        c3.add(other.c3.copy().mult((float) txWeightNorm));

        return this;
    }

    /**
     * @return generated copy of this triangle, with all its field copied as well
     */
    public Triangle copy() {
        return new Triangle(p1.copy(), p2.copy(), p3.copy(), c1.copy(), c2.copy(), c3.copy());
    }

    /**
     * @return centre point of this triangle
     */
    public PVector getCentre() {
        return (p1.copy().add(p2).add(p3)).mult(0.3333f);
    }

    /**
     * Multiply each point of this triangle by given value
     * @param val value to multiply by
     * @return scaled triangle
     */
    public Triangle scale(float val) {
        p1.mult(val);
        p2.mult(val);
        p3.mult(val);

        return this;
    }

    /**
     * Move this triangle by given vector
     * @param vector vector to the triangle by
     * @return moved triangle
     */
    public Triangle moveBy(PVector vector) {
        p1.add(vector);
        p2.add(vector);
        p3.add(vector);

        return this;
    }

    /**
     * @return this triangle with reversed y coordinate of each point
     */
    public Triangle reverse() {
        p1.y = -p1.y;
        p2.y = -p2.y;
        p3.y = -p3.y;

        return this;
    }

    /**
     * @return normal vector of this triangle
     */
    public PVector getNormal() {
        PVector v1 = p2.copy().add(p1.copy().mult(-1));
        PVector v2 = p3.copy().add(p1.copy().mult(-1));
        PVector normal = v1.cross(v2);

        return normal.normalize();
    }

    /**
     * @return average color of this triangle based on its vertices
     */
    public Weightable getAverageColor() {
        return c1.copy().add(c2).add(c3).mult(0.3333);
    }

    /**
     * @param lightVector vector of incoming light
     * @param lightInt light intensity
     * @param diffCoef diffuse coefficient
     * @return average color of polygon using Lambert's illumination model
     */
    public Weightable getLambertianColor(PVector lightVector, double lightInt, double diffCoef) {
        Weightable avgColor = getAverageColor();
//        PVector normal = getNormal();
//        double toMult = lightVector.normalize().dot(normal) * lightInt * diffCoef;

        return getLambertianColor(lightVector, lightInt, diffCoef, avgColor);
    }

    /**
     * @param lightVector vector of incoming light
     * @param lightInt light intensity
     * @param diffCoef diffuse coefficient
     * @return color of point using Lambert's illumination model
     */
    public Weightable getLambertianColor(PVector lightVector, double lightInt, double diffCoef, Weightable color) {
        PVector normal = getNormal();
        double toMult = lightVector.normalize().dot(normal) * lightInt * diffCoef;

        return color.mult(toMult);
    }


    /**
     * Calculates colors for all three vertices of triangle using Lambert's illumination model. The normals for
     * the points are calculated by adding normals of each triangle the point is part of.
     * @param lightVector vector of incoming light
     * @param lightInt light intensity
     * @param diffCoef diffuse coefficient
     * @param normMap map of points to triangles it is part of
     * @return colors for all three vertices of triangle using Lambert's illumination model
     */
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

    /**
     * Draw this triangle
     * @param pApplet PApplet to draw into
     */
    public void draw(PApplet pApplet) {
        pApplet.stroke(100);
        pApplet.noFill();
        pApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    /**
     * Draw this triangle using flat shading and Lambert's illumination model
     * @param pApplet PApplet to draw into
     * @param lightVector vector of incoming light
     * @param lightInt light intensity
     * @param diffCoef diffuse coefficient
     */
    public void drawFlat(PApplet pApplet, PVector lightVector, double lightInt, double diffCoef) {
        Weightable color = getLambertianColor(lightVector, lightInt, diffCoef);
        pApplet.fill(((float) color.w1), ((float) color.w2), ((float) color.w3));
        pApplet.stroke(((float) color.w1), ((float) color.w2), ((float) color.w3));
        pApplet.triangle(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    /**
     * Draw this triangle using gradient shading and Lambert's illumination model. Each vertex has normal identical to
     * the triangle's normal and colors are interpolated between the triangle's vertices.
     * @param pApplet PApplet to draw into
     * @param lightVector vector of incoming light
     * @param lightInt light intensity
     * @param diffCoef diffuse coefficient
     */
    public void drawGradPart(PApplet pApplet, PVector lightVector, double lightInt, double diffCoef) {
        Weightable[] cs = {getLambertianColor(lightVector, lightInt, diffCoef, c1),
                getLambertianColor(lightVector, lightInt, diffCoef, c2),
                getLambertianColor(lightVector, lightInt, diffCoef, c3)};

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

    /**
     * Draw this triangle using gradient shading and Lambert's illumination model. Each vertex has normal calculated
     * by adding normals of all triangles the vertex is part of and colors are interpolated between the triangle's
     * vertices.
     * @param pApplet PApplet to draw into
     * @param lightVector vector of incoming light
     * @param lightInt light intensity
     * @param diffCoef diffuse coefficient
     * @param normMap maps a vertex to normal calculated by adding normals of all triangles the vertex is part of
     */
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

    /**
     * Draw the clickable triangle's weights
     * @param pApplet PApplet to draw into
     */
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

    /**
     * source: https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
     * Calculate sign of a triangle
     * @param p1 vertex 1
     * @param p2 vertex 2
     * @param p3 vertex 2
     * @return sign of triangle
     */
    private float sign (PVector p1, PVector p2, PVector p3)
    {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    /**
     * source: https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
     * @param pt PVector of position to determine
     * @return true if point inside triangle, false otherwise
     */
    public boolean isInTriangle(PVector pt)
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

    /**
     * Calculate weights of a point in triangle using Barycentric coordinates
     * @param point calculate weights of this PVector point
     * @param w1 weight at vertex 1
     * @param w2 weight at vertex 2
     * @param w3 weight at vertex 3
     * @return barycentric weights of given point based on weights of the triangle's vertices
     */
    public Weightable getPointWeight(PVector point, double w1, double w2, double w3) {
        double[] bWeights = getBaryWeights(point);
        Weightable result = new Weightable(bWeights[0] * w1, bWeights[1] * w2, bWeights[2] * w3);

        return result.div(bWeights[0] + bWeights[1] + bWeights[2]);
    }

    /**
     * Calculate normalized weights of a point in triangle using Barycentric coordinates
     * @param point calculate weights of this PVector point
     * @return normalized barycentric weights of given point based on weights of the triangle's vertices
     */
    private double[] getBaryWeights(PVector point) {
        double w1 = ((p2.y - p3.y)*(point.x - p3.x) + (p3.x - p2.x)*(point.y - p3.y)) /
                ((p2.y - p3.y)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.y - p3.y));

        double w2 = ((p3.y - p1.y)*(point.x - p3.x) + (p1.x - p3.x)*(point.y - p3.y)) /
                ((p2.y - p3.y)*(p1.x - p3.x) + (p3.x - p2.x)*(p1.y - p3.y));

        double w3 = (1 - w1) - w2;

        return new double[]{w1, w2, w3};
    }

    /**
     * Generate triangle in centre of screen
     * @param screenWidth screen width
     * @param screenHeight screen height
     * @param scaleVal size of triangle
     * @return generated triangle
     */
    public static Triangle triangleOnScreen(int screenWidth, int screenHeight, float scaleVal) {
        PVector p1 = new PVector(-1, 1);
        PVector p2 = new PVector(1, 1);
        PVector p3 = new PVector(0, -1);
        Triangle triangle = new Triangle(p1, p2, p3, new Weightable(10,0, 0), new Weightable(0, 10, 0),
                new Weightable(0, 0, 10));
        triangle.scale(scaleVal);
        triangle.moveBy(new PVector(screenWidth / 2.0f - scaleVal + 100, screenHeight / 2.0f));

        return triangle;
    }

    /**
     * Calculate normalized normal of a vertex by adding normals of each triangle passed in
     * @param triangles triangles to calculate the normal from
     * @return normalized normal of the vertex
     */
    public static PVector getTotalNormal(Set<Triangle> triangles) {
        PVector total = new PVector(0,0,0);
        for (Triangle t : triangles) {
            total.add(t.getNormal());
        }

        return total.normalize();
    }
}
