package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

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

        Simulation simulation = new Simulation();
        simulation.start();

        for (Drone drone: Simulation.getDrones()) {
            RectangleElement rectangleElement = new RectangleElement(drone);
            Rectangle rectangle = rectangleElement.getShape();
            test.getChildren().add(rectangle);
        }
    }

    public javafx.scene.Parent getParent() {
        return root;
    }
}
