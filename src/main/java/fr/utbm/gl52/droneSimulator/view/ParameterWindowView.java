package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicElement;
import fr.utbm.gl52.droneSimulator.view.graphicElement.MainAreaGraphicElement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.addElementTo;

public class ParameterWindowView {
    private Parent root;

    public ParameterWindowView() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
            getClass().getResource("/fxml/ParameterWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();

        Pane pane = (Pane) root.lookup("#visualSettingPane");


        Simulation simulation = new Simulation();

        createDroneWeightSlider();

        createDroneBatterySlider();

        simulation.start();

        GraphicElement.setCoefficient(0.065f);

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

    public javafx.scene.Parent getParent(){
        return root;
    }
}
