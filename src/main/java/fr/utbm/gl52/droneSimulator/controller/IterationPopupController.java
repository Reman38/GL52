package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.IterationPopupView;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Slider;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.getRootWith;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.createSimulationWindow;

public class IterationPopupController {

    @FXML
    public void validate(ActionEvent event) {
        setIterationParameter(event);
        launchSimulation(event);
    }

    private void launchSimulation(ActionEvent event) {
        try {
            SimulationWindowView.init(IterationPopupView.getSimulationMode());
            createSimulationWindow(event, SimulationWindowView.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setIterationParameter(ActionEvent event) {
        Parent root = getRootWith(event);
        Slider IterationSlider = (Slider)  root.lookup("#iterationSlider");
        double iterationNumber =  IterationSlider.getValue();
        Simulation.setTimeSimulationParameters((int) iterationNumber);
    }
}
