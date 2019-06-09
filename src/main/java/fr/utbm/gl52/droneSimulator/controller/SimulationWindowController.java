package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SimulationWindowController implements Initializable {

    public SimulationWindowController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Increase the simulation speed until the limit
     */
    @FXML
    public void onSpeedPlusAction(){
        Float currentSpeed = Simulation.getSimulationSpeed();
        Float newSpeed = currentSpeed + 1;
        if(newSpeed <= Simulation.getMaxSimulationSpeedAcceleration()) {
            Simulation.setSimulationSpeed(newSpeed);
            SimulationWindowView.refreshSimulationSpeed();
        }
    }

    /**
     * Decrease the simulation speed until the limit
     */
    @FXML
    public void onSpeedMinusAction(){
        Float currentSpeed = Simulation.getSimulationSpeed();
        Float newSpeed = currentSpeed - 1;
        if(newSpeed >= Simulation.getMinSimulationSpeedAcceleration()) {
            Simulation.setSimulationSpeed(newSpeed);
            SimulationWindowView.refreshSimulationSpeed();
        }
    }
}
