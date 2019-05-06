package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class DroneGraphicElement extends RectangleElement{
    private static Float fixCoefficient = 200f;

    public DroneGraphicElement(SimulationElement se) {
        super(se);

        setColor("red");
        isFilled(true);
    }

    public static Rectangle getShape(SimulationElement se) {
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(se);
        return droneGraphicElement.getShape();
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