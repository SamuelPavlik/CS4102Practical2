import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;

public class Run extends PApplet {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;
    private static Run instance = null;
    FaceFactory faceFactory;
    Face face;
    ClickTriangle triangle;
    Weightable posWeigths;

    public void setup() {
        try {
            triangle = new ClickTriangle(WIDTH, HEIGHT, 200);

            faceFactory = new FaceFactory("CS4102 2019 P2 data/mesh.csv",
                    "CS4102 2019 P2 data/sh_000.csv",
                    "CS4102 2019 P2 data/tx_000.csv",
                    "CS4102 2019 P2 data/sh_ev.csv",
                    "CS4102 2019 P2 data/tx_ev.csv");
            faceFactory.addFace("CS4102 2019 P2 data/sh_001.csv",
                    "CS4102 2019 P2 data/tx_001.csv");
            faceFactory.addFace("CS4102 2019 P2 data/sh_002.csv",
                    "CS4102 2019 P2 data/tx_002.csv");
            faceFactory.addFace("CS4102 2019 P2 data/sh_003.csv",
                    "CS4102 2019 P2 data/tx_003.csv");

            posWeigths = new Weightable(0.4, 0.3, 0.3);
            try {
//                face = faceFactory.createSyntheticFace("CS4102 2019 P2 data/sh_001.csv",
//                        "CS4102 2019 P2 data/tx_001.csv", "CS4102 2019 P2 data/sh_002.csv",
//                        "CS4102 2019 P2 data/tx_002.csv", "CS4102 2019 P2 data/sh_003.csv",
//                        "CS4102 2019 P2 data/tx_003.csv", posWeigths, posWeigths);
                face = faceFactory.createSyntheticFace(new Weightable(0.4, 0.3, 0.3), new Weightable(0.4, 0.3, 0.3));
                face.reverse();
                face.scale(0.001f);
                face.moveBy(new PVector(WIDTH / 2.0f, HEIGHT / 2.0f));
            } catch (Exception e) {
                e.printStackTrace();
            }
//            face = faceFactory.createFace("CS4102 2019 P2 data/sh_015.csv",
//                    "CS4102 2019 P2 data/tx_001.csv");

//            face.reverse();
//            face.scale(0.003f);
//            face.moveBy(new PVector(WIDTH / 2.0f, HEIGHT / 2.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
        instance = this;
    }

    public void settings(){
        size(WIDTH, HEIGHT);
    }

    public void draw(){
        //Draw scene
        background(255) ;

        //Get weights
        triangle.draw(this);
        face.draw(this);

    }

    @Override
    public void mousePressed() {
        Weightable weightable = triangle.onMouseClick(this);
        if (weightable != posWeigths) {
            posWeigths = weightable;
            try {
                face = faceFactory.createSyntheticFace(posWeigths, posWeigths);
                face.reverse();
                face.scale(0.001f);
                face.moveBy(new PVector(WIDTH / 2.0f, HEIGHT / 2.0f));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        String[] processingArgs = {"Run"};
        Run runClass = new Run();
        PApplet.runSketch(processingArgs, runClass);
    }

    public static Run getInstance() {
        if (instance == null)
            instance = new Run();
        return instance;
    }
}
