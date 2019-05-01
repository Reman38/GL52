package org.utbm.gl52.droneSimulator.controller;

import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.utbm.gl52.droneSimulator.view.SimulationWindowView;
import java.io.IOException;

public class ParameterWindowController extends ControllerHelper{

    @FXML
    public void launchSimulationWithCustomParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("run simulation");
            try {
                SimulationWindowView simulationWindowView = new SimulationWindowView();
                createWindow(event, simulationWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
