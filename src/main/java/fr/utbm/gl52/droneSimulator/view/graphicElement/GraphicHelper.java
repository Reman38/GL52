package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.view.ErrorPopupView;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class GraphicHelper {

    public static String ERROR_TITLE = "Error";
    public static String ITERATION_TITLE = "Choose iteration number";

    /**
     * Create a window
     *
     * @param event Trigger event
     * @param parent Parent window
     */
    public static void createWindow(Event event, Parent parent) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Drone Simulator");
        stage.setMaximized(true);
        stage.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    /**
     * Create a simple window
     *
     * @param parent Parent window
     */
    public static void createSimpleWindow(Parent parent) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Drone Simulator");
        stage.show();
    }

    /**
     * Create a simulation window
     *
     * @param event Trigger event
     * @param parent Parent window
     */
    public static void createSimulationWindow(Event event, Parent parent) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Drone Simulator");
        stage.setMaximized(true);
        ((Node)(event.getSource())).getScene().getWindow().hide();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("closing");
                Simulation.setAppClosed(true);
                Simulation.stop();
                Platform.exit();
            }
        });
        stage.show();
    }

    /**
     * Create a popup
     *
     * @param parent Parent window
     * @param title Title of the popup
     */
    public static void createPopup(Parent parent, String title) {
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setMaximized(false);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Throw an error popup
     *
     * @param errorMsg The string message of the error
     *
     * @throws IOException The associated fxml template was not found
     */
    public static void throwErrorPopup(String errorMsg) throws IOException {
        ErrorPopupView errorPopupView = new ErrorPopupView(errorMsg);
        createPopup(errorPopupView.getParent(), ERROR_TITLE);
    }

    /**
     * Throw an error popup that signals that at choosing a difficulty is mandatory
     */
    public static void throwNoDifficultyChosenErrorPopup(){
        try {
            throwErrorPopup("You must choose a valid competition difficulty");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Change cursor to default on selected pane
     *
     * @param pane Selected pane
     */
    public static void useDefaultCursorOn(Pane pane){
        pane.setCursor(Cursor.DEFAULT);
    }

    /**
     * Change cursor to crossHair on selected pane
     *
     * @param pane Selected pane
     */
    public static void useCrossHairCursorOn(Pane pane){
        pane.setCursor(Cursor.CROSSHAIR);
    }

    /**
     * Add the element to the pane
     *
     * @param pane Pane that could contains element
     * @param element Element to add on pane
     */
    public static void addElementTo(Pane pane, Shape element){
        pane.getChildren().add(element);
    }
}
