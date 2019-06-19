package fr.utbm.gl52.droneSimulator.controller;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicElement.getModelViewCoefficient;

public class ControllerHelper {

    /**
     * Get the root element of the window using the trigger event.
     *
     * @param e Trigger event
     * @return The root node of the window
     */
    protected static Parent getRootWith(Event e){
        Node source = (Node) e.getSource();
        return source.getScene().getRoot();
    }

    /**
     * Check if the event is triggered by a left click
     *
     * @param event The trigger event
     * @return true if its a right click
     */
    static Boolean isLeftClick(MouseEvent event){
        return event.getButton().equals(MouseButton.PRIMARY);
    }

    /**
     * Calculate the coordinate in the model with the coordinate in the view.
     *
     * @param x The view abscissa or ordinate to convert
     * @return Model coordinate in meter
     */
    static Float calculateModelCoordinate(Float x){
        return x / getModelViewCoefficient();
    }

    /**
     * Check if coords are equals
     *
     * @param coord Coords 1
     * @param coord1 Coords 2
     * @return true if they are equal
     */
    public static boolean isSameCoord(Float[] coord, Float[] coord1) {
        return (coord[0].equals(coord1[0])) && (coord[1].equals(coord1[1]));
    }
}
