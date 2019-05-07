package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public abstract class RectangleGraphicElement extends GraphicElement{
    public RectangleGraphicElement(SimulationElement se) {
        super(se);
    }

    public Shape setShapeStyle(Shape shape) {
        if(isFilled())
            shape.setFill(Paint.valueOf(getColor()));
        else{
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(Paint.valueOf(getColor()));
        }

        return shape;
    }

    public Rectangle getShape() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(getX());
        rectangle.setY(getY());
        rectangle.setHeight(getHeight());
        rectangle.setWidth(getWidth());

        setShapeStyle(rectangle);

        return rectangle;
    }
}