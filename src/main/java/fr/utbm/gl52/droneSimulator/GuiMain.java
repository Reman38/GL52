package fr.utbm.gl52.droneSimulator;

import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import fr.utbm.gl52.droneSimulator.view.StartPageView;
import fr.utbm.gl52.droneSimulator.view.StatisticsWindowView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GuiMain extends Application {

    public void start(Stage primaryStage) throws IOException {
//       StartPageView startPageView = new StartPageView();
       StatisticsWindowView startPageView = new StatisticsWindowView();

        Scene scene = new Scene(startPageView.getParent());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Drone Simulator");
//        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
