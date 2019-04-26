import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PGraphics2D;

import java.io.IOException;
import java.util.HashMap;

public class Run extends PApplet {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;

    private static final String SOURCE = "CS4102 2019 P2 data/";

    private static final String MESH_FILE = SOURCE + "mesh.csv";
    private static final String AVG_FACE_SH_FILE = SOURCE + "sh_000.csv";
    private static final String AVG_FACE_TX_FILE = SOURCE + "tx_000.csv";
    private static final String SH_EV_FILE = SOURCE + "sh_ev.csv";
    private static final String TX_EV_FILE = SOURCE + "tx_ev.csv";

    private static final String FACE1_SH_FILE = SOURCE + "sh_001.csv";
    private static final String FACE1_TX_FILE = SOURCE + "tx_001.csv";
    private static final String FACE2_SH_FILE = SOURCE + "sh_002.csv";
    private static final String FACE2_TX_FILE = SOURCE + "tx_002.csv";
    private static final String FACE3_SH_FILE = SOURCE + "sh_003.csv";
    private static final String FACE3_TX_FILE = SOURCE + "tx_003.csv";

    private static final Weightable START_POS_WEIGHTS = new Weightable(0.333, 0.333, 0.333);
    private static final float FACE_SCALE = 0.003f;
    private static final float WIDTH_TO_TRIAN_RATE = 3.0f;
    private static final PVector TO_CENTRE_VECTOR = new PVector(WIDTH / 2.0f, HEIGHT / 2.0f);

    private FaceFactory faceFactory;
    private Face face;
    private ClickTriangle triangle;

    public void setup() {
        try {
            triangle = new ClickTriangle(WIDTH, HEIGHT, WIDTH / WIDTH_TO_TRIAN_RATE);

            faceFactory = new FaceFactory(MESH_FILE, AVG_FACE_SH_FILE, AVG_FACE_TX_FILE, SH_EV_FILE, TX_EV_FILE);
            faceFactory.addFace(FACE1_SH_FILE, FACE1_TX_FILE);
            faceFactory.addFace(FACE2_SH_FILE, FACE2_TX_FILE);
            faceFactory.addFace(FACE3_SH_FILE, FACE3_TX_FILE);

            face = faceFactory.createSyntheticFace(START_POS_WEIGHTS, START_POS_WEIGHTS);
            face.reverse();
            face.scale(FACE_SCALE);
            face.moveBy(TO_CENTRE_VECTOR);
            face.recalc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        noLoop();
    }

    public void settings(){
        size(WIDTH, HEIGHT, P2D);
    }

    public void draw(){
        //Draw scene
        background(255) ;

        //Draw the clickable triangle
        triangle.draw(this);

        //Draw the face
        face.draw(this);

        System.out.println("Drawn");
    }

    @Override
    public void mousePressed() {
        Weightable weightable = triangle.onMouseClick(this);
        face = faceFactory.createSyntheticFace(weightable, weightable);
        face.reverse();
        face.scale(FACE_SCALE);
        face.moveBy(TO_CENTRE_VECTOR);
        face.recalc();
        redraw();
    }

    public static void main(String[] args){
        String[] processingArgs = {"Run"};
        Run runClass = new Run();
        PApplet.runSketch(processingArgs, runClass);
    }
}
