package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectangleElement {
    private Float[] coord;
    private Float width;
    private Float height;
    private String color;
    private Boolean isFilled;

    public RectangleElement(SimulationElement se) {
        setCoord(se.getCoord());
        setWidth(se.getWidth());
        setHeight(se.getHeight());

        setProperColor();
    }

    public Float getX() {
        return coord[0];
    }

    public Float getY() {
        return coord[1];
    }

    public Boolean isFilled() {
        return isFilled;
    }

    public Rectangle getShape() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(getX());
        rectangle.setY(getY());
        rectangle.setHeight(getHeight());
        rectangle.setWidth(getWidth());

        if(isFilled())
            rectangle.setFill(Paint.valueOf(getColor()));
        else
            rectangle.setStroke(Paint.valueOf(getColor()));

        return rectangle;
    }

    public void setProperColor() {
        switch (getClass().getName()){
            case "Drone":
                setColor("red");
                break;
            case "Parcel":
                setColor("green");
                break;
            case "Areal":
                setColor("blue");
                break;
            case "MainArea":
                setColor("black");
                break;
            default:

                break;
        }
    }

    private void setColor(String s) {
        color = s;
    }

    public Float[] getCoord() {
        return coord;
    }
    public void setCoord(Float[] coord) {
        this.coord = coord;
    }
    public Float getWidth() {
        return width;
    }
    public void setWidth(Float width) {
        this.width = width;
    }
    public Float getHeight() {
        return height;
    }
    public void setHeight(Float height) {
        this.height = height;
    }
    public String getColor() {
        return color;
    }
}