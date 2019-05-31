package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

import java.io.IOException;

public class IterationPopupView {
    private Parent root;
    private static String simulationMode;

    private static Simulation simulation = new Simulation();

    public IterationPopupView(String simulationMode) throws IOException{
        loadFXML();
        createIterationNumberslider();
        IterationPopupView.simulationMode = simulationMode;
    }

    private void createIterationNumberslider() {
        Slider IterationSlider = (Slider)  root.lookup("#iterationSlider");
        Text IterationNumber = (Text) root.lookup("#iterationNumber");
        IterationNumber.setText(Simulation.getNumberOfSimulationIterationRange()[0].toString());
        IterationSlider.setMin(Simulation.getNumberOfSimulationIterationRange()[0]);
        IterationSlider.setMax(Simulation.getNumberOfSimulationIterationRange()[1]);
        IterationSlider.setShowTickLabels(true);

        refreshIterationNumber(IterationSlider, IterationNumber);
    }

    private void refreshIterationNumber(Slider IterationSlider, Text iterationNumber) {
        IterationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            iterationNumber.setText(String.format("%d", newValue.intValue()));
        });
    }

    private void loadFXML() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
                getClass().getResource("/fxml/iterationPopup.fxml")
        );
        loader.load();

        root = loader.getRoot();
    }

    public Parent getParent(){
        return root;
    }

    public static String getSimulationMode() {
        return simulationMode;
    }

    public static Simulation getSimulation() {
        return simulation;
    }
}
