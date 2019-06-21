package fr.utbm.gl52.droneSimulator.view.graphicElement;

import javafx.scene.shape.Shape;

interface GraphicElementInterface {

    /**
     * If true, fill the element
     *
     * @param b true to fill the element, false to just have a border
     */
    void isFilled(boolean b);

    /**
     * Set the color of the graphic element
     *
     * @param s name of the color
     */
    void setColor(String s);

    /**
     * Check if the element is filled or not
     *
     * @return true if the element is filled
     */
    Boolean isFilled();

    /**
     * Get the height of the graphic element
     * .
     * @return height of the graphic element
     */
    Float getHeight();

    /**
     * Get the width of the element
     * .
     * @return width of the element
     */
    Float getWidth();

    /**
     * Get the abscissa of the graphic element.
     *
     * @return abscissa of the element
     */
    Float getX();

    /**
     * Get the ordinate of the element
     *
     * @return ordinate of the element
     */
    Float getY();

    /**
     * Get the shape instance of the graphic element
     *
     * @return a shape instance
     */
    Shape getShape();

    /**
     * Get the color of the graphic element
     *
     * @return the name of the color
     */
    String getColor();
}