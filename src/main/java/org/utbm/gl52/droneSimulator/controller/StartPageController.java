package org.utbm.gl52.droneSimulator.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class StartPageController {

    @FXML
    public void launchSimulationWithDefaultParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("click default");
        }
    }

    @FXML
    public void launchSimulationWithRandomParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("click random");
        }
    }

    @FXML
    public void launchParametersWindows(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("click custom");
        }
    }
}
