package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.IterationPopupView;
import fr.utbm.gl52.droneSimulator.view.ParameterWindowView;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.*;

public class StartPageController extends ControllerHelper {

    @FXML
    public void launchSimulationWithDefaultParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("click default");
            try {
                IterationPopupView iterationPopupView = new IterationPopupView(Simulation.DEFAULT);
               createPopup(iterationPopupView.getParent(), GraphicHelper.ITERATION_TITLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @FXML
    public void launchSimulationWithRandomParameters(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            try {
                IterationPopupView iterationPopupView = new IterationPopupView(Simulation.RANDOM);
                createPopup(iterationPopupView.getParent(), GraphicHelper.ITERATION_TITLE);
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
