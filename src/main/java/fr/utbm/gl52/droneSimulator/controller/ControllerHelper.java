package fr.utbm.gl52.droneSimulator.controller;
;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicElement.getModelViewCoefficient;

public class ControllerHelper {

    protected static void createWindow(MouseEvent event, Parent parent) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Drone Simulator");
        stage.setMaximized(true);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    protected static Parent getRootWith(Event e){
        Node source = (Node) e.getSource();
        return source.getScene().getRoot();
    }

    protected static Boolean isLeftClick(MouseEvent event){
        return event.getButton().equals(MouseButton.PRIMARY);
    }

    protected static Float calculateModelCoordinate(Float x){
        return x / getModelViewCoefficient();
    }

    public static boolean isSameCoord(Float[] coord, Float[] coord1) {
        return (coord[0].equals(coord1[0])) && (coord[1].equals(coord1[1]));
    }
}
