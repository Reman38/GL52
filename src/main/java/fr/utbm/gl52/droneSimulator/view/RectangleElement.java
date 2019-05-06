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

    private static Float[] coefficient;

    public RectangleElement(SimulationElement se) {
        setCoord(se.getCoord());
        setWidth(se.getWidth());
        setHeight(se.getHeight());

        setProperColor();
        setProperFill();

        setCoefficient(10f);
    }

    public static void setCoefficient(Float coefficient) {
        setXCoefficient(coefficient);
        setYCoefficient(coefficient);
    }

    public static void setXCoefficient(Float xc) {
        if (xc > 0)
            coefficient[0] = xc;
    }

    public static void setYCoefficient(Float yc) {
        if (yc > 0)
            coefficient[1] = yc;
    }

    public Float getHeight() {
        return height * getYCoefficient();
    }
    public Float getWidth() {
        return width * getXCoefficient();
    }

    public static Float getYCoefficient() {
        return coefficient[1];
    }

    public static Float getXCoefficient() {
        return coefficient[0];
    }

    private void setProperFill() {
        setFilled(true);
//        setFilled(false);
    }

    public Float getX() {
        return coord[0] * getXCoefficient();
    }

    public Float getY() {
        return coord[1] * getYCoefficient();
    }

    public Boolean isFilled() {
        return isFilled;
    }
    private void setFilled(boolean b) {
        isFilled = b;
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
        setColor("red");
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
    public void setWidth(Float width) {
        this.width = width;
    }
    public void setHeight(Float height) {
        this.height = height;
    }
    public String getColor() {
        return color;
    }
}