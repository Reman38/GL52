package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public abstract class CenteredAndErgonomicGraphicElement extends RectangleGraphicElement {
    private static Float zoomCoefficient = 200f;

    public CenteredAndErgonomicGraphicElement(SimulationElement se) {
        super(se);
    }

    public static Float getZoomCoefficient() {
        return zoomCoefficient;
    }

    public static void setZoomCoefficient(Float zoomCoefficient) {
        CenteredAndErgonomicGraphicElement.zoomCoefficient = zoomCoefficient;
    }

    public Float getHeight() {
        return super.getHeight() * getZoomCoefficient();
    }
    public Float getWidth() {
        return super.getWidth() * getZoomCoefficient();
    }

    public Float getX() {
        return super.getX()-getWidth()/2;
    }
    public Float getY() {
        return super.getY()-getHeight()/2;
    }
}