package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public class DroneGraphicElement extends CenteredAndErgonomicGraphicElement {
    public DroneGraphicElement(SimulationElement se) {
        super(se);

        setColor("red");
        isFilled(true);
    }
}