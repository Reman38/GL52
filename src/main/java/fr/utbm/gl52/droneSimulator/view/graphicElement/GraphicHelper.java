package fr.utbm.gl52.droneSimulator.view.graphicElement;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class GraphicHelper {

    public static void createWindow(MouseEvent event, Parent parent) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Drone Simulator");
        stage.setMaximized(true);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public static void useDefaultCursorOn(Pane pane){
        pane.setCursor(Cursor.DEFAULT);
    }

    public static void useCrossHairCursorOn(Pane pane){
        pane.setCursor(Cursor.CROSSHAIR);
    }

    public static void addElementTo(Pane pane, Shape element){
        pane.getChildren().add(element);
    }
}
