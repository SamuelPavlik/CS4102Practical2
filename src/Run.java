import processing.core.PApplet;

public class Run extends PApplet {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;
    private static Run instance = null;

    public void setup() {
        instance = this;
    }

    public void settings(){
        size(WIDTH, HEIGHT);
    }

    public void draw(){
        //Draw scene
        background(41, 184, 255) ;
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
