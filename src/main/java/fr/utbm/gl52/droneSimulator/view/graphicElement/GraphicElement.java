package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.exception.NotSupportedValueException;
import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public abstract class GraphicElement implements GraphicElementInterface{
    private Float[] coord = new Float[2];
//    private Float[] coord = {0f,0f}; / TODO remove
    public Float width;
    public Float height;
    private String color;
    private Boolean isFilled;

    private static Float coefficient;

    public GraphicElement(SimulationElement se) {
    }

    public void setX(Float x) {
        coord[0] = x;
    }

    public void setY(Float y) {
        coord[1] = y;
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

    public String getColor() {
        return color;
    }
}