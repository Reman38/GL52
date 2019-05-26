package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.*;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.util.*;

public class SimulationWindowView {

    private static Parent root;

    private static List<ParcelGraphicElement> parcelGraphicElements = new ArrayList<>();

    private static Map<Drone, TextArea> consoleMap = new HashMap<>();

    public static void init(String simulationMode) throws IOException{

        startModel(simulationMode);

        FXMLLoader loader = new FXMLLoader(
                SimulationWindowView.class.getResource("/fxml/SimulationWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
        Pane pane = (Pane) root.lookup("#simulationPane");

        startView(pane);
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
        int i = 1;
        for (Drone drone : Simulation.getDrones()) {
            displayDrone(pane, drone);
            displayDroneLogTab(i, drone);
            i++;
        }
    }

    private static void displayDrone(Pane pane, Drone drone) {
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
        Shape shape = droneGraphicElement.getShape();
        pane.getChildren().add(shape);
    }

    private static void displayDroneLogTab(int i, Drone drone) {
        TabPane tabPaneLog = (TabPane) root.lookup("#tabPaneLogs");;
        TextArea text;
        Tab tab;
        text = new TextArea();
        text.setEditable(false);
        consoleMap.put(drone, text);
        tab = new Tab();
        tab.setClosable(false);
        tab.setContent(text);
        tab.setText("Drone " + i);
        tabPaneLog.getTabs().add(tab);
    }

    public static void displayParcels(Pane pane) {
        for (Parcel parcel : Simulation.getParcels()) {
            ParcelGraphicElement parcelGraphicElement = new ParcelGraphicElement(parcel);
            parcelGraphicElements.add(parcelGraphicElement);
            Shape shape = parcelGraphicElement.getShape();
            pane.getChildren().add(shape);
        }
    }

    public static void displayChargingStation(Pane pane) {
        for (ChargingStation chargingStation : Simulation.getChargingStations()) {
            ChargingStationGraphicElement chargingStationGraphicElement = new ChargingStationGraphicElement(chargingStation);
            Shape shape = chargingStationGraphicElement.getShape();
            pane.getChildren().add(shape);
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
            String content = text.getText();

            content = (content == null) ? "" : content + event + "\n";

            text.setText(content);
        }
    }

    public static void removeParcelGraphicIfExists(ParcelGraphicElement parcelToRemove) {
        if(parcelToRemove != null) {
            Pane pane = (Pane) root.lookup("#simulationPane");
            parcelGraphicElements.remove(parcelToRemove);
            pane.getChildren().remove(parcelToRemove.getShape());
        }
    }

    public static javafx.scene.Parent getParent() {
        return root;
    }

    public static List<ParcelGraphicElement> getParcelGraphicElements() {
        return parcelGraphicElements;
    }
}
