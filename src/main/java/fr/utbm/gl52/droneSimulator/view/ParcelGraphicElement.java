package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class ParcelGraphicElement extends RectangleElement{
    private static Float fixCoefficient = 200f;

    public ParcelGraphicElement(SimulationElement se) {
        super(se);

        setColor("green");
        isFilled(false);
    }

    public static Rectangle getShape(SimulationElement se) {
        ParcelGraphicElement parcelGraphicElement = new ParcelGraphicElement(se);
        return parcelGraphicElement.getShape();
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
}