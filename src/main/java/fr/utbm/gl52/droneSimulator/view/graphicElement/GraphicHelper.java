package fr.utbm.gl52.droneSimulator.view.graphicElement;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GraphicHelper {

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
