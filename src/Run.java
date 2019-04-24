import processing.core.PApplet;
import processing.core.PVector;

import java.io.IOException;

public class Run extends PApplet {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;
    private static Run instance = null;
    FaceFactory faceFactory;
    Face face;
    Triangle triangle;

    public void setup() {
        try {
            faceFactory = new FaceFactory("CS4102 2019 P2 data/mesh.csv",
                    "CS4102 2019 P2 data/sh_000.csv",
                    "CS4102 2019 P2 data/tx_000.csv",
                    "CS4102 2019 P2 data/sh_ev.csv",
                    "CS4102 2019 P2 data/tx_ev.csv");
//            face = faceFactory.createFace("CS4102 2019 P2 data/sh_015.csv",
//                    "CS4102 2019 P2 data/tx_001.csv");

//            face.reverse();
//            face.scale(0.003f);
//            face.moveBy(new PVector(WIDTH / 2.0f, HEIGHT / 2.0f));

//            triangle = Triangle.triangleInCentre(WIDTH, HEIGHT, 200);
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

        try {
            face = faceFactory.createSyntheticFace("CS4102 2019 P2 data/sh_015.csv",
                    "CS4102 2019 P2 data/tx_015.csv", "CS4102 2019 P2 data/sh_010.csv",
                    "CS4102 2019 P2 data/tx_010.csv", "CS4102 2019 P2 data/sh_020.csv",
                    "CS4102 2019 P2 data/tx_020.csv", new Weightable(1, 0, 0), new Weightable(1, 0, 0));

            face.reverse();
            face.scale(0.003f);
            face.moveBy(new PVector(WIDTH / 2.0f, HEIGHT / 2.0f));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        face.draw(this);
//        triangle.draw(this);
//        triangle.drawWeights(this);
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
