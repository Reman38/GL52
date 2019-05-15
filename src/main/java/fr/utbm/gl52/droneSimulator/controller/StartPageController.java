package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.ParameterWindowView;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.createWindow;

public class StartPageController extends ControllerHelper {

    @FXML
    public void launchSimulationWithDefaultParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("click default");
            try {
                SimulationWindowView simulationWindowView = new SimulationWindowView(Simulation.DEFAULT);
                createWindow(event, simulationWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void launchSimulationWithRandomParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            try {
                SimulationWindowView simulationWindowView = new SimulationWindowView(Simulation.RANDOM);
                createWindow(event, simulationWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
