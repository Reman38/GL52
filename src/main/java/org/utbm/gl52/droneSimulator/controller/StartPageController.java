package org.utbm.gl52.droneSimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.utbm.gl52.droneSimulator.view.ParameterWindowView;
import org.utbm.gl52.droneSimulator.view.SimulationWindowView;
import java.io.IOException;

public class StartPageController extends ControllerHelper {

    @FXML
    public void launchSimulationWithDefaultParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("click default");
            try {
                SimulationWindowView simulationWindowView = new SimulationWindowView();
                createWindow(event, simulationWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }

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
            try {
                ParameterWindowView parameterWindowView = new ParameterWindowView();
                createWindow(event, parameterWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
