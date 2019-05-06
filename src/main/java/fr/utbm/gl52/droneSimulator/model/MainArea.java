package fr.utbm.gl52.droneSimulator.model;

public class MainArea extends Area {

    public MainArea(Float x, Float y, Float caseWidth, Float caseHeight) {
        super(x, y, caseWidth, caseHeight);
    }

    public void setX(Float x){
        coord[0] = x;
    }

    public void setY(Float y){
        coord[1] = y;
    }

    public void setWidth(Float w){
        width = w;
    }

    public void setHeight(Float h){
        height = h;
    }
}
