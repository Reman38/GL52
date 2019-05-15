package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.*;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.IOException;

public class SimulationWindowView {
    private final Parent root;

    public SimulationWindowView(String simulationMode) throws IOException {
        startModel(simulationMode);

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/fxml/SimulationWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
        Pane pane = (Pane) root.lookup("#simulationPane");

        startView(pane);
    }
    public void startView(Pane pane) {
        GraphicElement.setModelViewCoefficient(0.65f);
        CenteredAndErgonomicGraphicElement.setZoomCoefficient(20f);

        displayMainArea(pane);
        displayAreas(pane);
        displayParcels(pane);
        displayChargingStation(pane);
        displayDrones(pane);

        // for (Drone drone : Simulation.getDrones()) {
        /*Drone drone = Simulation.getDrones().get(0);
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
        Rectangle rectangle = droneGraphicElement.getShape();
        pane.getChildren().add(rectangle);*/
        // }
    }

    public void displayDrones(Pane pane) {
        for (Drone drone : Simulation.getDrones()) {
            DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
            Shape shape = droneGraphicElement.getShape();
            pane.getChildren().add(shape);
        }
    }

    public void displayParcels(Pane pane) {
        for (Parcel parcel : Simulation.getParcels()) {
            Shape shape = ParcelGraphicElement.getShape(parcel);
            pane.getChildren().add(shape);
        }
    }

    public void displayChargingStation(Pane pane) {
        for (ChargingStation chargingStation : Simulation.getChargingStations()) {
            ChargingStationGraphicElement chargingStationGraphicElement = new ChargingStationGraphicElement(chargingStation);
            Shape shape = chargingStationGraphicElement.getShape();
            pane.getChildren().add(shape);
        }
    }

    public void displayAreas(Pane pane) {
        for (Area area : Simulation.getAreas()) {
            Shape shape = AreaGraphicElement.getShape(area);
            pane.getChildren().add(shape);
        }
    }
    public void displayMainArea(Pane pane) {
        Shape mainAreaGraphicElementShape = MainAreaGraphicElement.getShape(Simulation.getMainArea());
        pane.getChildren().add(mainAreaGraphicElementShape);
    }
    public void startModel(String simulationMode) {
        new Simulation();
        if(simulationMode.equals(Simulation.DEFAULT)){
            Simulation.startDefault();
        } else if(simulationMode.equals(Simulation.RANDOM)){
            Simulation.startRandom();

        } else if(simulationMode.equals(Simulation.CUSTOM)){
            Simulation.startCustom();
        } else {
            throw new IllegalArgumentException("the mode '" + simulationMode + "' doesn't exist");
        }
    }

    public javafx.scene.Parent getParent() {
        return root;
    }
}
