package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class ParcelGraphicElement extends CenteredAndErgonomicGraphicElement {
    public ParcelGraphicElement(SimulationElement se) {
        super(se);

        setColor("green");
        isFilled(false);
    }
}