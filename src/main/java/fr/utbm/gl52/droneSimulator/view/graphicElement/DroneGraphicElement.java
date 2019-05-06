package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class DroneGraphicElement extends CenteredAndErgonomicGraphicElement {
    public DroneGraphicElement(SimulationElement se) {
        super(se);

        setColor("red");
        isFilled(true);
    }

    public static Rectangle getShape(SimulationElement se) {
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(se);
        return droneGraphicElement.getShape();
    }
}