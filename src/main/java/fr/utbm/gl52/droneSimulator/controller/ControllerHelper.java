package fr.utbm.gl52.droneSimulator.controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ControllerHelper {

    public void createWindow(MouseEvent event, Parent parent) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Drone Simulator");
        stage.setMaximized(true);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
