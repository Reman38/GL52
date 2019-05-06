package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.exception.NotSupportedValueException;
import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectangleElement extends GraphicElement{
    private Float[] coord;
    private Float width;
    private Float height;
    private String color;
    private Boolean isFilled;

    private static Float coefficient;

    public RectangleElement(SimulationElement se) {
        super(se);

        // drone
        setColor("blue");
        isFilled(true);
    }

    public Rectangle getShape() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(getX());
        rectangle.setY(getY());
        rectangle.setHeight(getHeight());
        rectangle.setWidth(getWidth());

        if(isFilled())
            rectangle.setFill(Paint.valueOf(getColor()));
        else
            rectangle.setStroke(Paint.valueOf(getColor()));

        return rectangle;
    }
}