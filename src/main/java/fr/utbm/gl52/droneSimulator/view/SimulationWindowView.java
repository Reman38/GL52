package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.*;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class SimulationWindowView {

    private static Parent root;

    private static List<ParcelGraphicElement> parcelGraphicElements = new ArrayList<>();

    private static Map<Drone, TextArea> consoleMap = new HashMap<>();

    private static Boolean isViewFullyLoaded = false;

    public static void init(String simulationMode) throws IOException{

        startModel(simulationMode);

        FXMLLoader loader = new FXMLLoader(
                SimulationWindowView.class.getResource("/fxml/SimulationWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
        Pane pane = (Pane) root.lookup("#simulationPane");

        startView(pane);

        isViewFullyLoaded = true;
    }

    public static void startView(Pane pane) {
        GraphicElement.setModelViewCoefficient(0.65f);
        CenteredAndErgonomicGraphicElement.setZoomCoefficient(20f);

        displayMainArea(pane);
        displayAreas(pane);
        displayParcels(pane);
        displayChargingStation(pane);
        displayDronesAndAssociatedTabs(pane);

        // for (Drone drone : Simulation.getDrones()) {
        /*Drone drone = Simulation.getDrones().get(0);
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
        Rectangle rectangle = droneGraphicElement.getShape();
        pane.getChildren().add(rectangle);*/
        // }
    }

    public static void displayDronesAndAssociatedTabs(Pane pane) {
        for (Drone drone : Simulation.getDrones()) {
            displayDrone(pane, drone);
            displayDroneLogTab(drone);
        }
    }

    private static void displayDrone(Pane pane, Drone drone) {
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
        Shape shape = droneGraphicElement.getShape();
        Text id = droneGraphicElement.getGraphicalId();
        pane.getChildren().add(shape);
        pane.getChildren().add(id);
    }

    private static void displayDroneLogTab(Drone drone) {
        TabPane tabPaneLog = (TabPane) root.lookup("#tabPaneLogs");;
        TextArea text;
        Tab tab;
        text = new TextArea();
        text.setEditable(false);
        text.textProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue,
                                Object newValue) {
                text.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
                //use Double.MIN_VALUE to scroll to the top
            }
        });
        consoleMap.put(drone, text);
        tab = new Tab();
        tab.setClosable(false);
        tab.setContent(text);
        tab.setText("Drone " + drone.getId());
        tabPaneLog.getTabs().add(tab);
    }

    public static void displayParcels(Pane pane) {
        for (Parcel parcel : Simulation.getParcels()) {
            ParcelGraphicElement parcelGraphicElement = new ParcelGraphicElement(parcel);
            parcelGraphicElements.add(parcelGraphicElement);
            Text id = parcelGraphicElement.getGraphicalId();
            Shape shape = parcelGraphicElement.getShape();
            pane.getChildren().add(shape);
            pane.getChildren().add(id);
        }
    }

    public static void displayChargingStation(Pane pane) {
        for (ChargingStation chargingStation : Simulation.getChargingStations()) {
            ChargingStationGraphicElement chargingStationGraphicElement = new ChargingStationGraphicElement(chargingStation);
            Shape shape = chargingStationGraphicElement.getShape();
            Text id = chargingStationGraphicElement.getGraphicalId();
            pane.getChildren().add(shape);
            pane.getChildren().add(id);
        }
    }

    public static void displayAreas(Pane pane) {
        for (Area area : Simulation.getAreas()) {
            Shape shape = AreaGraphicElement.getShape(area);
            pane.getChildren().add(shape);
        }
    }
    public static void displayMainArea(Pane pane) {
        Shape mainAreaGraphicElementShape = MainAreaGraphicElement.getShape(Simulation.getMainArea());
        pane.getChildren().add(mainAreaGraphicElementShape);
    }
    public static void startModel(String simulationMode) {
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

    public static void logDroneEventInTab(Drone drone, String event){
        TextArea text = consoleMap.get(drone);

        if(drone == null){
            throw new IllegalArgumentException("This drone does not exist");
        } else {
            /*String content = text.getText();

            content = (content == null) ? "" : content + event + "\n";

            text.setText(content);*/
            text.appendText(event + "\n");
        }
    }

    public static void removeParcelGraphicIfExists(ParcelGraphicElement parcelToRemove) {
        if(parcelToRemove != null) {
            Pane pane = (Pane) root.lookup("#simulationPane");
            parcelGraphicElements.remove(parcelToRemove);
            pane.getChildren().remove(parcelToRemove.getShape());
            pane.getChildren().remove(parcelToRemove.getGraphicalId());
        }
    }

    public static javafx.scene.Parent getParent() {
        return root;
    }

    public static List<ParcelGraphicElement> getParcelGraphicElements() {
        return parcelGraphicElements;
    }

    public static Boolean isViewFullyLoaded() {
        return isViewFullyLoaded;
    }
}
