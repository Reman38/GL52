package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.*;
import fr.utbm.gl52.droneSimulator.view.graphicElement.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
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

    private static List<DroneGraphicElement> droneGraphicElements = new ArrayList<>();

    private static Map<Drone, TextArea> consoleMap = new HashMap<>();

    private static Boolean isViewFullyLoaded = false;

    private static Pane simulationPane;

    /**
     * Initialize the simulation
     *
     * @param simulationMode Mode of simulation defined in Simulation
     *
     * @throws IOException The associated FXML file is not found
     */
    public static void init(String simulationMode) throws IOException{

        startModel(simulationMode);

        FXMLLoader loader = new FXMLLoader(
                SimulationWindowView.class.getResource("/fxml/SimulationWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
        simulationPane = (Pane) root.lookup("#simulationPane");

        startView(simulationPane);

        refreshSimulationSpeed();

        isViewFullyLoaded = true;
    }

    /**
     * Place all elements on the simulation pane
     *
     * @param pane The simulation pane
     */
    private static void startView(Pane pane) {
        GraphicElement.setModelViewCoefficient(0.65f);
        CenteredAndErgonomicGraphicElement.setZoomCoefficient(20f);

        displayMainArea(pane);
        displayAreas(pane);
        displayParcels();
        displayChargingStation(pane);
        displayDronesAndAssociatedTabs();
    }

    /**
     * Add drones on simulation pane and add one tab per drone
     */
    public static void displayDronesAndAssociatedTabs() {
        for (Drone drone : Simulation.getDrones()) {
            displayDrone(simulationPane, drone);
            displayDroneLogTab(drone);
        }
    }

    /**
     * Add a drone to the simulation pane
     *
     * @param pane Simulation Pane
     * @param drone drone to display
     */
    private static void displayDrone(Pane pane, Drone drone) {
        DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
        droneGraphicElements.add(droneGraphicElement);
        Shape shape = droneGraphicElement.getShape();
        Text id = droneGraphicElement.getGraphicalId();
        pane.getChildren().add(shape);
        pane.getChildren().add(id);
    }

    /**
     * Add a drone tab
     *
     * @param drone drone to display
     */
    private static void displayDroneLogTab(Drone drone) {
        TabPane tabPaneLog = (TabPane) root.lookup("#tabPaneLogs");
        TextArea text;
        Tab tab;
        text = new TextArea();
        text.setEditable(false);
        text.textProperty().addListener((ChangeListener<Object>) (observable, oldValue, newValue) -> {
            text.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
            //use Double.MIN_VALUE to scroll to the top
        });
        consoleMap.put(drone, text);
        tab = new Tab();
        tab.setClosable(false);
        tab.setContent(text);
        tab.setText("Drone " + drone.getId());
        tabPaneLog.getTabs().add(tab);
    }

    /**
     * Add parcels to the simulation pane
     */
    public static void displayParcels() {
        for (Parcel parcel : Simulation.getParcels()) {
            addParcelToView(parcel);
        }
    }

    /**
     * Add charging stations to the simulation pane
     *
     * @param pane Simulation Pane
     */
    private static void displayChargingStation(Pane pane) {
        for (ChargingStation chargingStation : Simulation.getChargingStations()) {
            ChargingStationGraphicElement chargingStationGraphicElement = new ChargingStationGraphicElement(chargingStation);
            Shape shape = chargingStationGraphicElement.getShape();
            Text id = chargingStationGraphicElement.getGraphicalId();
            pane.getChildren().add(shape);
            pane.getChildren().add(id);
        }
    }

    /**
     * Add areas to the simulation pane
     *
     * @param pane Simulation Pane
     */
    private static void displayAreas(Pane pane) {
        for (Area area : Simulation.getAreas()) {
            Shape shape = AreaGraphicElement.getShape(area);
            pane.getChildren().add(shape);
        }
    }

    /**
     * Add the main area to the simulation pane
     *
     * @param pane Simulation Pane
     */
    private static void displayMainArea(Pane pane) {
        Shape mainAreaGraphicElementShape = MainAreaGraphicElement.getShape(Simulation.getMainArea());
        pane.getChildren().add(mainAreaGraphicElementShape);
    }

    /**
     * Refresh the simulation speed on the screen
     */
    public static void refreshSimulationSpeed() {
        Text speedText = (Text) root.lookup("#speedText");
        String text = String.format("Speed: x%.1f", Simulation.getSimulationSpeed());
        speedText.setText(text);
    }

    /**
     * Start the model according to the chosen simulation mode
     *
     * @param simulationMode The mode of simulation
     */
    private static void startModel(String simulationMode) {
        new Simulation();
        switch (simulationMode) {
            case Simulation.DEFAULT:
                Simulation.startDefault();
                break;
            case Simulation.RANDOM:
                Simulation.startRandom();
                break;
            case Simulation.CUSTOM:
                Simulation.startCustom();
                break;
            default:
                throw new IllegalArgumentException("the mode '" + simulationMode + "' doesn't exist");
        }
        Simulation.flushParameters();
    }

    /**
     * Add an event to a drone tab
     *
     * @param drone  Concerned drone
     * @param event Event to log
     */
    public static void logDroneEventInTab(Drone drone, String event){
        TextArea text = consoleMap.get(drone);

        if(drone == null){
            throw new IllegalArgumentException("This drone does not exist");
        } else {
            synchronized (Drone.class) {
                text.appendText(event + "\n");
            }
        }
    }

    /**
     * remove drone tabs
     */
    private static void removeEventTabs(){
        TabPane tabPaneLog = (TabPane) root.lookup("#tabPaneLogs");
        tabPaneLog.getTabs().removeAll(tabPaneLog.getTabs());
    }

    /**
     * Remove the parcel from the simulation if it exists
     *
     * @param parcelToRemove Parcel graphic element to remove
     */
    public static void removeParcelGraphicIfExists(ParcelGraphicElement parcelToRemove) {
        if(parcelToRemove != null) {

            removeParcelFromPane(parcelToRemove);
            parcelGraphicElements.remove(parcelToRemove);
        }
    }

    /**
     * Remove a list of parcel graphic elements
     *
     * @param parcels Parcels to remove
     */
    public static void removeParcelsGraphic(List<ParcelGraphicElement> parcels){
        for(ParcelGraphicElement parcel: parcels){
            removeParcelFromPane(parcel);
        }
        parcelGraphicElements.removeAll(parcels);
    }

    /**
     * Remove a parcel from the view
     *
     * @param parcel Parcel to remove
     */
    public static void removeParcelFromPane(ParcelGraphicElement parcel){
        Pane pane = (Pane) root.lookup("#simulationPane");
        pane.getChildren().remove(parcel.getShape());
        pane.getChildren().remove(parcel.getGraphicalId());
    }

    /**
     * add the parcel to the view
     *
     * @param parcel parcel to add
     */
    public static void addParcelToView(Parcel parcel) {
        ParcelGraphicElement parcelGraphicElement = new ParcelGraphicElement(parcel);
        parcelGraphicElements.add(parcelGraphicElement);
        Text id = parcelGraphicElement.getGraphicalId();
        Shape shape = parcelGraphicElement.getShape();
        simulationPane.getChildren().add(shape);
        simulationPane.getChildren().add(id);
    }

    public static javafx.scene.Parent getParent() {
        return root;
    }

    public static List<ParcelGraphicElement> getParcelGraphicElements() {
        return parcelGraphicElements;
    }

    public static Boolean isViewFullyLoaded() {
        try{
                TabPane tabPaneLog = (TabPane) root.lookup("#tabPaneLogs");
            return isViewFullyLoaded && tabPaneLog.getTabs().size() == droneGraphicElements.size() && tabPaneLog.getTabs().get(0).getText() != null;
        } catch (NullPointerException | IndexOutOfBoundsException e){
            return false;
        }
    }

    public static void cleanView() {

        isViewFullyLoaded = false;

        for(DroneGraphicElement droneGraphic: droneGraphicElements){
            Platform.runLater(() ->simulationPane.getChildren().remove(droneGraphic.getShape()));
            Platform.runLater(() ->simulationPane.getChildren().remove(droneGraphic.getGraphicalId()));
        }

        droneGraphicElements = new ArrayList<>();

        for(ParcelGraphicElement parcelGraphic: parcelGraphicElements){
            Platform.runLater(() ->simulationPane.getChildren().remove(parcelGraphic.getShape()));
            Platform.runLater(() ->simulationPane.getChildren().remove(parcelGraphic.getGraphicalId()));
        }

        parcelGraphicElements = new ArrayList<>();

        Platform.runLater(SimulationWindowView::removeEventTabs);
    }

    public static void setViewFullyLoaded(boolean b) {
        isViewFullyLoaded = b;
    }
}
