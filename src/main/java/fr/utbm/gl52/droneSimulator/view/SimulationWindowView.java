package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Area;
import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Parcel;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

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

        for (Area area: Simulation.getAreas()) {
            Shape shape = AreaGraphicElement.getShape(area);
            test.getChildren().add(shape);
        }

        for (Parcel parcel: Simulation.getParcels()) {
            Shape shape = ParcelGraphicElement.getShape(parcel);
            test.getChildren().add(shape);
        }

        for (Drone drone: Simulation.getDrones()) {
            Shape shape = DroneGraphicElement.getShape(drone);
            test.getChildren().add(shape);
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
