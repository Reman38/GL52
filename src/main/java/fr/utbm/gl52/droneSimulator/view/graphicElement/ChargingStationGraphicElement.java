package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;

public class ChargingStationGraphicElement extends CenteredAndErgonomicGraphicElement {

    public ChargingStationGraphicElement(SimulationElement se){
        super(se);

        setColor("green");
        isFilled(true);
    }
}
