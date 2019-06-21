package fr.utbm.gl52.droneSimulator.model;

public interface SimulationElementInterface {

    /**
     * Get the abscissa of the element
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
     * Get the width of the element
     * .
     * @return width of the element
     */
    Float getWidth();

    /**
     * Get the height of the element
     * .
     * @return height of the element
     */
    Float getHeight();
}
