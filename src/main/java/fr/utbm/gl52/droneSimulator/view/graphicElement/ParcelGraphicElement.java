package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class ParcelGraphicElement extends CenteredAndErgonomicGraphicElement {
    public ParcelGraphicElement(SimulationElement se) {
        super(se);

        setColor("green");
        isFilled(false);
    }

    public static Rectangle getShape(SimulationElement se) {
        ParcelGraphicElement parcelGraphicElement = new ParcelGraphicElement(se);
        return parcelGraphicElement.getShape();
    }

    public Float getHeight() {
        return super.getHeight() * getFixCoefficient();
    }
    public Float getWidth() {
        return super.getWidth() * getFixCoefficient();
    }
}