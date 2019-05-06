package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.exception.NotSupportedValueException;
import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public abstract class GraphicElement implements GraphicElementInterface{
    private Float[] coord;
    private Float width;
    private Float height;
    private String color;
    private Boolean isFilled;

    private static Float coefficient;

    public GraphicElement(SimulationElement se) {
        setCoord(se.getCoord());
        setWidth(se.getWidth());
        setHeight(se.getHeight());
    }

    public static void setCoefficient(Float _coefficient) throws NotSupportedValueException{
        if (_coefficient <= 0)
            throw new NotSupportedValueException("Coefficient can't be <= 0");
        else
            coefficient = _coefficient;
    }

    public void setCoord(Float[] _coord) {
        coord = _coord;
    }
    public void setWidth(Float _width) {
        width = _width;
    }
    public void setHeight(Float _height) {
        height = _height;
    }

    public void setColor(String s) {
        color = s;
    }
    public void isFilled(boolean b) {
        isFilled = b;
    }

    public Boolean isFilled() {
        return isFilled;
    }
    public static Float getCoefficient() {
        return coefficient;
    }
    public Float getHeight() {
        return height * getCoefficient();
    }
    public Float getWidth() {
        return width * getCoefficient();
    }
    public Float getX() {
        return coord[0] * getCoefficient();
    }
    public Float getY() {
        return coord[1] * getCoefficient();
    }

    public Float[] getCoord() {
        return coord;
    }
    public String getColor() {
        return color;
    }
}