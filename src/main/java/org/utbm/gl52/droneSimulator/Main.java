package org.utbm.gl52.droneSimulator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.utbm.gl52.droneSimulator.View.StartPage;

import java.io.IOException;

public class Main extends Application {

    public void start(Stage primaryStage) throws IOException {

        StartPage startPage = new StartPage();

        Scene scene = new Scene(startPage.getParent());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hello World");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
