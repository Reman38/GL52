package fr.utbm.gl52.droneSimulator.controller;
;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicElement.getCoefficient;

public class ControllerHelper {

    protected static Parent getRootWith(Event e){
        Node source = (Node) e.getSource();
        return source.getScene().getRoot();
    }

    protected static Boolean isLeftClick(MouseEvent event){
        return event.getButton().equals(MouseButton.PRIMARY);
    }

    protected static Float calculateModelCoordinate(Float x){
        return x / getCoefficient();
    }
}
