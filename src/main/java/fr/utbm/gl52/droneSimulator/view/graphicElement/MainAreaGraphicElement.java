package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class MainAreaGraphicElement extends AreaGraphicElement {
    public MainAreaGraphicElement(SimulationElement se) {
        super(se);

        setColor("black");
    }

    public static Rectangle getShape(SimulationElement se) {
        MainAreaGraphicElement mainAreaGraphicElement = new MainAreaGraphicElement(se);
        return mainAreaGraphicElement.getShape();
    }
}