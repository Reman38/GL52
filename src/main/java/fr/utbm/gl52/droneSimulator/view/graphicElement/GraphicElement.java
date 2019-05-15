package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.exception.NotSupportedValueException;
import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public abstract class GraphicElement implements GraphicElementInterface{
    private String color;
    private Boolean isFilled;
    private SimulationElement simulationElement;

    private static Float modelViewCoefficient;

    public GraphicElement(SimulationElement se) {
        simulationElement = se;
    }

    public static void setModelViewCoefficient(Float _coefficient){
        try {
            if (_coefficient < 0)
                throw new NotSupportedValueException("Coefficient can't be < 0");
            else
                modelViewCoefficient = _coefficient;
        } catch (NotSupportedValueException e) {
            e.printStackTrace();
        }
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
    public static Float getModelViewCoefficient() {
        return modelViewCoefficient;
    }
    public Float getHeight() {
        return simulationElement.getHeight() * getModelViewCoefficient();
    }
    public Float getWidth() {
        return simulationElement.getWidth() * getModelViewCoefficient();
    }
    public Float getX() {
        return simulationElement.getX() * getModelViewCoefficient();
    }
    public Float getY() {
        return simulationElement.getY() * getModelViewCoefficient();
    }

    public SimulationElement getSimulationElement() {
        return simulationElement;
    }

    public String getColor() {
        return color;
    }
}