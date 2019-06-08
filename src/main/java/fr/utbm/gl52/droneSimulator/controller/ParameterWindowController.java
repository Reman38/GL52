package fr.utbm.gl52.droneSimulator.controller;


import fr.utbm.gl52.droneSimulator.model.ChargingStation;
import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import fr.utbm.gl52.droneSimulator.view.ErrorPopupView;
import fr.utbm.gl52.droneSimulator.view.graphicElement.ChargingStationGraphicElement;
import fr.utbm.gl52.droneSimulator.view.graphicElement.DroneGraphicElement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.calculateModelCoordinate;
import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.isLeftClick;
import static fr.utbm.gl52.droneSimulator.model.Simulation.setCompetitionDifficulty;
import static fr.utbm.gl52.droneSimulator.view.ParameterWindowView.getChargingStationGraphicElements;
import static fr.utbm.gl52.droneSimulator.view.ParameterWindowView.getDroneGraphicElements;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.*;

public class ParameterWindowController{
    
    private Boolean isDronePlacement = false;
    private Boolean isDroneRemoving = false;
    private Boolean isChargingStationPlacement = false;
    private Boolean isChargingStationRemoving = false;
    private static final Float selectRadius = 10f;
    private Integer droneIdIncrement = 0;
    private Integer chargingStationIdIncrement = 0;

    /**
     * Set the next action as a drone placement.
     *
     * @param ae The event that has triggered this action.
     */
   @FXML
   public void addDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isDronePlacement = true;
   }

    /**
     * Set the next action as a drone removal.
     *
     * @param ae The event that has triggered this action.
     */
   @FXML
   public void removeDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isDroneRemoving = true;
   }

    /**
     * Set the next action as a charging station placement.
     *
     * @param ae The event that has triggered this action.
     */
   @FXML
   public void addChargingStationAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isChargingStationPlacement = true;
   }

    /**
     * Set the next action as a charging station removal.
     *
     * @param ae The event that has triggered this action.
     */
   @FXML
   public void removeChargingStationAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isChargingStationRemoving = true;
   }

    /**
     * Start simulation element placement or removing according to previous action
     *
     * @param event The trigger event
     */
   @FXML
    public void onVisualPaneClicked(MouseEvent event){
       if(isLeftClick(event)) {
           Parent root = ControllerHelper.getRootWith(event);
           if(isDronePlacement){
               createDroneElement(event, root);
           } else if(isDroneRemoving){
                removeDroneElement(event, root);
           } else if(isChargingStationPlacement){
               createChargingStationElement(event, root);
           } else if (isChargingStationRemoving){
               removeChargingStationElement(event, root);
           }
       }
   }

    /**
     * Launch the simulation
     *
     * @param event The trigger event
     *
     * @throws IOException The simulation window was not found
     */
    @FXML
    public void launchSimulationWithCustomParameters(MouseEvent event) throws IOException {
        if(isLeftClick(event)) {
            System.out.println("run simulation");
            if(isAtLeastOneDroneInstanciated()){
                if(isAtLeastOneChargingStationInstanciated()) {
                    TryToConfigureSimulationParameters(event);
                } else {
                    throwZeroChargingStationErrorPopup();
                }
            } else {
                throwZeroDroneErrorPopup();
            }
        }
    }

    /**
     * Configure simulation parameters before starting it
     *
     * @param event The trigger event
     */
    private void TryToConfigureSimulationParameters(MouseEvent event) {
        try {
            ConfigureSimulationParameters(event);
            LaunchSimulationWindow(event);
        } catch(IllegalArgumentException e){
            throwNoDifficultyChosenErrorPopup();
        }
    }

    /**
     * Throw an error popup that signals that at choosing a difficulty is mandatory
     */
    private void throwNoDifficultyChosenErrorPopup(){
        try {
            throwErrorPopup("You must choose a valid competition difficulty");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Throw an error popup that signals that at least one drone is needed
     */
    private void throwZeroDroneErrorPopup() {
        try {
            throwErrorPopup("You must add at least one drone!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Throw an error popup that signals that at least one drone is needed
     */
    private void throwZeroChargingStationErrorPopup() {
        try {
            throwErrorPopup("You must add at least one charging station!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Throw an error popup
     *
     * @param errorMsg The string message of the error
     *
     * @throws IOException The associated fxml template was not found
     */
    private void throwErrorPopup(String errorMsg) throws IOException {
        ErrorPopupView errorPopupView = new ErrorPopupView(errorMsg);
        createPopup(errorPopupView.getParent(), ERROR_TITLE);
    }

    /**
     * Launch the simulation window
     *
     * @param event The trigger event.
     */
    private void LaunchSimulationWindow(MouseEvent event) {
        try {
            SimulationWindowView.init(Simulation.CUSTOM);
            createSimulationWindow(event, SimulationWindowView.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the simulation parameters according to the view inputs
     *
     * @param event The trigger event
     *
     * @throws IllegalArgumentException The difficulty was not set
     */
    private void ConfigureSimulationParameters(MouseEvent event) throws IllegalArgumentException {
        Parent root = ControllerHelper.getRootWith(event);
        ConfigureTimeSimulationParameters(root);
        ConfigureCompetitionDifficultyParameter(root);
    }

    /**
     * Confiure competion difficulty according to view combobox selection
     *
     * @param root The root node of the window
     *
     * @throws IllegalArgumentException No choice was made
     */
    private void ConfigureCompetitionDifficultyParameter(Parent root) throws IllegalArgumentException {
        ComboBox<String> competitionLevels = (ComboBox<String>) root.lookup("#competitionLevelComboBox");
        setCompetitionDifficulty(competitionLevels.getValue());
    }

    /**
     * Configure simulation duration and iterations according to view sliders
     *
     * @param root Root node of the window
     */
    private void ConfigureTimeSimulationParameters(Parent root) {
        Slider IterationSlider = (Slider)  root.lookup("#iterationSlider");
        Slider simulationDurationSlider = (Slider)  root.lookup("#simulationDurationSlider");
        double simulationDuration = simulationDurationSlider.getValue();
        double iterationNumber =  IterationSlider.getValue();
        Simulation.setTimeSimulationParameters((int) simulationDuration, (int) iterationNumber);
    }

    /**
     * Checks if at least one drone is instanciated
     *
     * @return true if yes
     */
    private boolean isAtLeastOneDroneInstanciated() {
        return Simulation.getDrones().size() > 0;
    }

    /**
     * Checks if at least one charging station is instanciated
     *
     * @return true if yes
     */
    private boolean isAtLeastOneChargingStationInstanciated() {
        return Simulation.getChargingStations().size() > 0;
    }

    /**
     * Create a drone element and add it to the view
     *
     * @param event The trigger event
     * @param root Root node of the window
     */
    private void createDroneElement(MouseEvent event, Parent root) {
        Slider droneWeightCapacity = (Slider)  root.lookup("#droneWeightCapacity");
        Slider droneBatteryCapacity = (Slider) root.lookup("#droneBatteryCapacity");
        Pane pane = (Pane) root.lookup("#visualSettingPane");
        Drone drone = new Drone(droneIdIncrement++);
        drone.setWeightCapacity((float) droneWeightCapacity.getValue());
        drone.setBatteryFullCapacity((float)droneBatteryCapacity.getValue());
        addDroneElement(event, pane, drone);
    }

    /**
     * Add a drone graphic element to the window
     *
     * @param event Trigger event
     * @param pane The simulationPane
     * @param drone The drone to add
     */
    private void addDroneElement(MouseEvent event, Pane pane, Drone drone) {
        try {
            drone.setCoord(calculateModelCoordinate((float) event.getX()) , calculateModelCoordinate((float) event.getY()));
            Simulation.getDrones().add(drone);
            //System.out.println(drone.toString());
            useDefaultCursorOn(pane);
            isDronePlacement = false;
            DroneGraphicElement droneGraphicElement = new DroneGraphicElement(drone);
            getDroneGraphicElements().add(droneGraphicElement);
            addElementTo(pane, droneGraphicElement.getShape());
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the targeted drone
     *
     * @param event The trigger event
     * @param root The root node of the window
     */
    private void removeDroneElement(MouseEvent event, Parent root){
        DroneGraphicElement droneGraphic = getDroneInRadius((float) event.getX(), (float) event.getY());
        if(droneGraphic != null) {
            Drone drone = (Drone) droneGraphic.getSimulationElement();
            Simulation.getDrones().remove(drone);
            Pane pane = (Pane) root.lookup("#visualSettingPane");
            pane.getChildren().remove(droneGraphic.getShape());
            getDroneGraphicElements().remove(droneGraphic);
            isDroneRemoving = false;
            useDefaultCursorOn(pane);
        }
    }

    /**
     * Get one drone in a radius around the given coords
     *
     * @param x abscissa in meters
     * @param y ordinate in meters
     *
     * @return drone in radius if it exists
     */
    private DroneGraphicElement getDroneInRadius(Float x, Float y) {
       for (DroneGraphicElement drone: getDroneGraphicElements()){
            if(isInRadius(drone.getX(), x) && isInRadius(drone.getY(), y)){
                return drone;
            }
       }
       return null;
    }

    /**
     * Create a chargin station element and add it to the view
     *
     * @param event The trigger event
     * @param root Root node of the window
     */
    private void createChargingStationElement(MouseEvent event, Parent root) {
        Pane pane = (Pane) root.lookup("#visualSettingPane");
        ChargingStation chargingStation = new ChargingStation(chargingStationIdIncrement++);
        addChargingStationElement(event, pane, chargingStation);
    }

    /**
     * Add a charging station graphic element to the window
     *
     * @param event Trigger event
     * @param pane The simulationPane
     * @param chargingStation The charging station to add
     */
    private void addChargingStationElement(MouseEvent event, Pane pane, ChargingStation chargingStation) {
        try {
            chargingStation.setCoord(calculateModelCoordinate((float) event.getX()) , calculateModelCoordinate((float) event.getY()));
            Simulation.getChargingStations().add(chargingStation);
            useDefaultCursorOn(pane);
            isChargingStationPlacement = false;
            ChargingStationGraphicElement chargingStationGraphicElement = new ChargingStationGraphicElement(chargingStation);
            getChargingStationGraphicElements().add(chargingStationGraphicElement);
            addElementTo(pane, chargingStationGraphicElement.getShape());
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the targeted charging station
     *
     * @param event The trigger event
     * @param root The root node of the window
     */
    private void removeChargingStationElement(MouseEvent event, Parent root) {
        ChargingStationGraphicElement chargingStationGraphic = getChargingStationInRadius((float) event.getX(), (float) event.getY());
        if(chargingStationGraphic != null) {
            ChargingStation chargingStation = (ChargingStation) chargingStationGraphic.getSimulationElement();
            Simulation.getChargingStations().remove(chargingStation);
            Pane pane = (Pane) root.lookup("#visualSettingPane");
            pane.getChildren().remove(chargingStationGraphic.getShape());
            getChargingStationGraphicElements().remove(chargingStationGraphic);
            isChargingStationRemoving = false;
            useDefaultCursorOn(pane);
        }
    }

    /**
     * Get one charging station in a radius around the given coords
     *
     * @param x abscissa in meters
     * @param y ordinate in meters
     *
     * @return charging station in radius if it exists
     */
    private ChargingStationGraphicElement getChargingStationInRadius(Float x, Float y) {
        for (ChargingStationGraphicElement chargingStation: getChargingStationGraphicElements()){
            if(isInRadius(chargingStation.getX(), x) && isInRadius(chargingStation.getY(), y)){
                return chargingStation;
            }
        }
        return null;
    }

    /**
     * Chec if the point is in the radius
     *
     * @param x reference point
     * @param x1 tested point
     *
     * @return true f yes
     */
    private Boolean isInRadius(Float x, Float x1) {
       return (x >= x1 - selectRadius && x <= x1 + selectRadius);
    }

    /**
     * Reset all placement flags
     */
    private void resetFlags(){
        isDronePlacement = false;
        isDroneRemoving = false;
        isChargingStationPlacement = false;
        isChargingStationRemoving = false;
    }
}
