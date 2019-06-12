package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static fr.utbm.gl52.droneSimulator.model.Simulation.getCompetitionDifficultyLevels;
import static fr.utbm.gl52.droneSimulator.model.Simulation.initMainArea;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.addElementTo;

public class ParameterWindowView {
    private Parent root;
    private static List<DroneGraphicElement> droneGraphicElements = new ArrayList<>();
    private static List<ChargingStationGraphicElement> chargingStationGraphicElements = new ArrayList<>();

    private static Simulation simulation = new Simulation();

    public ParameterWindowView() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
            getClass().getResource("/fxml/ParameterWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();

        Pane pane = (Pane) root.lookup("#visualSettingPane");

        createDroneWeightSlider();

        createDroneBatterySlider();

        createSimulationDurationSlider();

        createIterationNumberslider();

        createCompetitionDropDown();

        initMainArea();

        GraphicElement.setModelViewCoefficient(0.65f);
        CenteredAndErgonomicGraphicElement.setZoomCoefficient(20f);

        Shape mainAreaGraphicElementShape = MainAreaGraphicElement.getShape(Simulation.getMainArea());
        addElementTo(pane, mainAreaGraphicElementShape);
    }

    private void createDroneBatterySlider() {
        Slider droneBatteryCapacity = (Slider) root.lookup("#droneBatteryCapacity");
        Text batteryCapacity = (Text) root.lookup("#batteryCapacity");
        droneBatteryCapacity.setMin(Simulation.getDroneBatteryCapacity()[0]);
        droneBatteryCapacity.setMax(Simulation.getDroneBatteryCapacity()[1]);
        batteryCapacity.setText(Simulation.getDroneBatteryCapacity()[0].toString() + " min");
        droneBatteryCapacity.setShowTickLabels(true);

        refreshDroneBattery(droneBatteryCapacity, batteryCapacity);
    }

    private void refreshDroneBattery(Slider droneBatteryCapacity, Text batteryCapacity) {
        droneBatteryCapacity.valueProperty().addListener((observable, oldValue, newValue) -> {
            batteryCapacity.setText(String.format("%.2f", newValue) + " min");
        });
    }

    private void createDroneWeightSlider() {
        Slider droneWeightCapacity = (Slider)  root.lookup("#droneWeightCapacity");
        Text weightCapacity = (Text) root.lookup("#weightCapacity");
        weightCapacity.setText(Simulation.getDroneWeightCapacity()[0].toString() + "kg");
        droneWeightCapacity.setMin(Simulation.getDroneWeightCapacity()[0]);
        droneWeightCapacity.setMax(Simulation.getDroneWeightCapacity()[1]);
        droneWeightCapacity.setShowTickLabels(true);

        refreshDroneWeight(droneWeightCapacity, weightCapacity);
    }

    private void refreshDroneWeight(Slider droneWeightCapacity, Text weightCapacity) {
        droneWeightCapacity.valueProperty().addListener((observable, oldValue, newValue) -> {
            weightCapacity.setText(String.format("%.2f", newValue) + "kg");
        });
    }

    private void createSimulationDurationSlider() {
        Slider simulationDurationSlider = (Slider)  root.lookup("#simulationDurationSlider");
        Text simulationDuration = (Text) root.lookup("#SimulationDurationText");
        simulationDuration.setText(Simulation.getSimulationDurationRange()[0].toString() + " min");
        simulationDurationSlider.setMin(Simulation.getSimulationDurationRange()[0]);
        simulationDurationSlider.setMax(Simulation.getSimulationDurationRange()[1]);
        simulationDurationSlider.setShowTickLabels(true);

        refreshSimulationDuration(simulationDurationSlider, simulationDuration);
    }

    private void refreshSimulationDuration(Slider simulationDurationSlider, Text simulationDuration) {
        simulationDurationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            simulationDuration.setText(String.format("%d", newValue.intValue()) + " min");
        });
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

    private void createCompetitionDropDown(){
        ComboBox competitionLevels = (ComboBox) root.lookup("#competitionLevelComboBox");

        for(Map.Entry<String, Integer[]> difficulty: getCompetitionDifficultyLevels().entrySet())
        competitionLevels.getItems().add(difficulty.getKey());
    }

    public javafx.scene.Parent getParent(){
        return root;
    }

    public static List<DroneGraphicElement> getDroneGraphicElements() {
        return droneGraphicElements;
    }

    public static List<ChargingStationGraphicElement> getChargingStationGraphicElements() {
        return chargingStationGraphicElements;
    }

    public static Simulation getSimulation(){
        return simulation;
    }
}
