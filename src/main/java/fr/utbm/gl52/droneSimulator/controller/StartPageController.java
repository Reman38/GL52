package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.IterationPopupView;
import fr.utbm.gl52.droneSimulator.view.ParameterWindowView;
import fr.utbm.gl52.droneSimulator.view.StatisticsWindowView;
import fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.*;

public class StartPageController extends ControllerHelper {

    /**
     * Launch the default simulation
     *
     * @param event The trigger event
     */
    @FXML
    public void launchSimulationWithDefaultParameters(ActionEvent event){
        try {
            IterationPopupView iterationPopupView = new IterationPopupView(getStage(event), Simulation.DEFAULT);
           createPopup(iterationPopupView.getParent(), GraphicHelper.ITERATION_TITLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch a random simulation
     *
     * @param event The trigger event
     */
    @FXML
    public void launchSimulationWithRandomParameters(ActionEvent event){
        try {
            IterationPopupView iterationPopupView = new IterationPopupView(getStage(event), Simulation.RANDOM);
            createPopup(iterationPopupView.getParent(), GraphicHelper.ITERATION_TITLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch the parameter window to configure a custom simulation
     *
     * @param event The trigger event
     */
    @FXML
    public void launchParametersWindow(ActionEvent event){
        try {
            ParameterWindowView parameterWindowView = new ParameterWindowView();
            createWindow(event, parameterWindowView.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Launch the statistic window.
     *
     * @param event The trigger event
     */
    @FXML
    public void launchStatisticWindow(ActionEvent event){
        try {
            new StatisticsWindowView();
            createWindow(event, StatisticsWindowView.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the stage linked with the event
     *
     * @param event Trigger event
     *
     * @return The linked stage
     */
    private Stage getStage(ActionEvent event) {
        return (Stage)getRootWith(event).getScene().getWindow();
    }
}
