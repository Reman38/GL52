package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.IterationPopupView;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.getRootWith;
import static fr.utbm.gl52.droneSimulator.model.Simulation.setCompetitionDifficulty;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.createSimulationWindow;

public class IterationPopupController {

    @FXML
    public void validate(ActionEvent event) {
        Parent root = ControllerHelper.getRootWith(event);
        ConfigureCompetitionDifficultyParameter(root);
        setIterationParameter(event);
        launchSimulation(event);
        Stage parentWindow = IterationPopupView.getParentWindow();
        parentWindow.hide();
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

    /**
     * Configure completion difficulty according to view combobox selection
     *
     * @param root The root node of the window
     *
     * @throws IllegalArgumentException No choice was made
     */
    private void ConfigureCompetitionDifficultyParameter(Parent root) throws IllegalArgumentException {
        ComboBox<String> competitionLevels = (ComboBox<String>) root.lookup("#difficultyComboBox");
        setCompetitionDifficulty(competitionLevels.getValue());
    }
}
