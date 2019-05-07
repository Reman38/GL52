package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Area;
import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Parcel;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.io.IOException;

public class SimulationWindowView {
    private Parent root;

    public SimulationWindowView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/SimulationWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
        Pane test = (Pane) root.lookup("#simulationPane");

        startModel();

        GraphicElement.setCoefficient(0.065f);

        Shape mainAreaGraphicElementShape = MainAreaGraphicElement.getShape(Simulation.getMainArea());
        test.getChildren().add(mainAreaGraphicElementShape);

        for (Area area : Simulation.getAreas()) {
            Shape shape = AreaGraphicElement.getShape(area);
            test.getChildren().add(shape);
        }

        for (Parcel parcel : Simulation.getParcels()) {
            Shape shape = ParcelGraphicElement.getShape(parcel);
            test.getChildren().add(shape);
        }

        for (Drone drone : Simulation.getDrones()) {
            Shape shape = DroneGraphicElement.getShape(drone);
            test.getChildren().add(shape);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Animation.INDEFINITE);

            KeyFrame moveBall = new KeyFrame(Duration.seconds(.5), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    System.out.println(drone.getX());
                    System.out.println(drone.getY());

                    shape.setTranslateX(shape.getTranslateX() + drone.getSpeed());
                    shape.setTranslateY(shape.getTranslateY() + drone.getSpeed());
                }
            });

            timeline.getKeyFrames().add(moveBall);
            timeline.play();
        }

    }
    public void startModel() {
        Simulation simulation = new Simulation();
        simulation.start();
    }

    public javafx.scene.Parent getParent() {
        return root;
    }
}
