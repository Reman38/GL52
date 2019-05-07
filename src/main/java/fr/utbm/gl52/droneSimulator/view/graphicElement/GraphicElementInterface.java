package fr.utbm.gl52.droneSimulator.view.graphicElement;

import javafx.scene.shape.Shape;

interface GraphicElementInterface {
    void isFilled(boolean b);
    void setColor(String s);

    Boolean isFilled();
    Float getHeight();
    Float getWidth();
    Float getX();
    Float getY();
    Shape getShape();
    String getColor();
}