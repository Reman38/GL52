package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.shape.Rectangle;

public class AreaGraphicElement extends RectangleElement{
    public AreaGraphicElement(SimulationElement se) {
        super(se);

        setColor("blue");
        isFilled(false);
    }

    public static Rectangle getShape(SimulationElement se) {
        AreaGraphicElement areaGraphicElement = new AreaGraphicElement(se);
        return areaGraphicElement.getShape();
    }
}