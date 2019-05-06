package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public abstract class CenteredAndErgonomicGraphicElement extends RectangleGraphicElement {
    private static Float fixCoefficient = 200f;

    public CenteredAndErgonomicGraphicElement(SimulationElement se) {
        super(se);
    }

    public static Float getFixCoefficient() {
        return fixCoefficient;
    }

    public Float getHeight() {
        return super.getHeight() * getFixCoefficient();
    }
    public Float getWidth() {
        return super.getWidth() * getFixCoefficient();
    }

    public Float getX() {
        return super.getX()-getWidth()/2;
    }
    public Float getY() {
        return super.getY()-getHeight()/2;
    }
}