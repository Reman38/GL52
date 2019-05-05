package fr.utbm.gl52.droneSimulator.model;

public class MainArea extends Area {

    public MainArea(float x, float y, float caseWidth, float caseHeight) {
        super(x, y, caseWidth, caseHeight);
    }

    public void setX(float x){
        coord[0] = x;
    }

    public void setY(float y){
        coord[1] = y;
    }

    public void setWidth(float w){
        width = w;
    }

    public void setHeight(float h){
        height = h;
    }
}
