import processing.core.PApplet;
import processing.core.PVector;

/**
 * Object representing triangle to draw into screen
 */
public class ClickTriangle {
    private static final float CIRCLE_RADIUS = 10;
    private static final float WEIGHT = 1;

    private Triangle triangle;
    private Weightable lastPosWeight;
    private FaceFactory faceFactory;
    private PVector clickedPos;
    private boolean displayFaces;

    public ClickTriangle(int screenWidth, int screenHeight, float scaleVal, FaceFactory faceFactory, boolean displayFaces) {
        this.triangle = Triangle.triangleOnScreen(screenWidth, screenHeight, scaleVal);
        this.lastPosWeight = new Weightable(0.4, 0.3, 0.3);
        this.faceFactory = faceFactory;
        this.clickedPos = triangle.getCentre();
        this.displayFaces = displayFaces;
    }

    /**
     * Draw the clickable triangle
     * @param pApplet PApplet to draw into
     */
    public void draw(PApplet pApplet) {
        triangle.draw(pApplet);

        if (displayFaces) {
            Face face1 = faceFactory.createSyntheticFace(new Weightable(1, 0, 0), new Weightable(1, 0, 0));
            face1.reverse();
            face1.scale(0.001f);
            face1.moveBy(triangle.p1);
            face1.recalc();
            face1.draw(pApplet, Run.LIGHT_VECTOR, Run.LIGHT_INT);

            Face face2 = faceFactory.createSyntheticFace(new Weightable(0, 1, 0), new Weightable(0, 1, 0));
            face2.reverse();
            face2.scale(0.001f);
            face2.moveBy(triangle.p2);
            face2.recalc();
            face2.draw(pApplet, Run.LIGHT_VECTOR, Run.LIGHT_INT);

            Face face3 = faceFactory.createSyntheticFace(new Weightable(0, 0, 1), new Weightable(0, 0, 1));
            face3.reverse();
            face3.scale(0.001f);
            face3.moveBy(triangle.p3);
            face3.recalc();
            face3.draw(pApplet, Run.LIGHT_VECTOR, Run.LIGHT_INT);
        }

        pApplet.fill(0);
        pApplet.stroke(0);
        pApplet.ellipse(clickedPos.x, clickedPos.y, 2*CIRCLE_RADIUS, 2*CIRCLE_RADIUS);
    }

    /**
     * Calculate barycentric weights of point where mouse was pressed if in the triangle
     * @param pApplet PApplet to draw into
     * @return barycentric weights of point where mouse was pressed
     */
    public Weightable onMouseClick(PApplet pApplet) {
        PVector mousePos = new PVector(pApplet.mouseX, pApplet.mouseY);
        if (triangle.isInTriangle(mousePos)) {
            clickedPos = mousePos;
            lastPosWeight = triangle.getPointWeight(mousePos, WEIGHT, WEIGHT, WEIGHT);
        }
        return lastPosWeight;
    }
}
