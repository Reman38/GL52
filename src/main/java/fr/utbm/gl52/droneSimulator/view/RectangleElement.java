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

        setProperColor(se);
    }

    public Float getX() {
        return coord[0];
    }

    public Float getY() {
        return coord[1];
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

    public void setProperColor(SimulationElement se) {
        switch (se.getClass().getSimpleName()){
            case "Drone":
                setColor("red");
                setFilled(true);
                break;
            case "Parcel":
                setColor("green");
                setFilled(true);
                break;
            case "Areal":
                setColor("blue");
                setFilled(false);
                break;
            case "MainArea":
                setColor("black");
                setFilled(false);
                break;
            default:
                setColor("black");
                setFilled(true);
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
    public Boolean isFilled() {
        return isFilled;
    }
    public void setFilled(Boolean filled) {
        isFilled = filled;
    }
}