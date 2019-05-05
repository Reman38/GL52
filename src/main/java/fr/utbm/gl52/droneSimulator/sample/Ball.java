package fr.utbm.gl52.droneSimulator.sample;

import javafx.scene.shape.Circle;

public class Ball extends Circle{

    private int dx = 1;
    private int dy = 1;

    public Ball(int x, int y, int z){
        super(x,y,z);
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
}
