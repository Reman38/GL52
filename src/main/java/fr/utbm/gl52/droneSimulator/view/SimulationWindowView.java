package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.*;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

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
        GraphicElement.setCoefficient(0.065f);

        displayMainArea(pane);
        displayAreas(pane);
        displayParcels(pane);
        displayChargingStation(pane);

        // for (Drone drone : Simulation.getDrones()) {
        Drone drone = Simulation.getDrones().get(0);
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
        Rectangle rectangle = droneGraphicElement.getShape();
        pane.getChildren().add(rectangle);
        // }

        // TODO make it work
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame moveDrone = new KeyFrame(Duration.seconds(1f/30f), new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                rectangle.setX(droneGraphicElement.getX());
                rectangle.setY(droneGraphicElement.getY());
            }
        });

        timeline.getKeyFrames().add(moveDrone);
        timeline.play();
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
