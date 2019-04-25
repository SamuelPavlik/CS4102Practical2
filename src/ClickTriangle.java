import processing.core.PApplet;
import processing.core.PVector;

public class ClickTriangle {
    private final float WEIGHT = 1;

    private Triangle triangle;
    private Weightable lastPosWeight;

    public ClickTriangle(int screenWidth, int screenHeight, float scaleVal) {
        this.triangle = Triangle.triangleInCentre(screenWidth, screenHeight, scaleVal);
        this.lastPosWeight = new Weightable(0.4, 0.3, 0.3);
    }

    public void draw(PApplet pApplet) {
        triangle.draw(pApplet);
    }

    public Weightable onMouseClick(PApplet pApplet) {
        PVector mousePos = new PVector(pApplet.mouseX, pApplet.mouseY);
        if (triangle.PointInTriangle(mousePos)) {
            lastPosWeight = triangle.getPointWeight(mousePos, WEIGHT, WEIGHT, WEIGHT);
        }
        return lastPosWeight;
    }
}
