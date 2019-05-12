package fr.utbm.gl52.droneSimulator.controller;


import fr.utbm.gl52.droneSimulator.model.ChargingStation;
import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import fr.utbm.gl52.droneSimulator.view.ErrorPopupView;
import fr.utbm.gl52.droneSimulator.view.graphicElement.ChargingStationGraphicElement;
import fr.utbm.gl52.droneSimulator.view.graphicElement.DroneGraphicElement;
import fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.calculateModelCoordinate;
import static fr.utbm.gl52.droneSimulator.view.ParameterWindowView.getChargingStationGraphicElements;
import static fr.utbm.gl52.droneSimulator.view.ParameterWindowView.getDroneGraphicElements;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.*;

public class ParameterWindowController{
    
    private Boolean isDronePlacement = false;
    private Boolean isDroneRemoving = false;
    private Boolean isChargingStationPlacement = false;
    private Boolean isChargingStationRemoving = false;
    private static final Float selectRadius = 10f;

   @FXML
   public void addDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isDronePlacement = true;
   }

   @FXML
   public void removeDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isDroneRemoving = true;
   }

   @FXML
   public void addChargingStationAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isChargingStationPlacement = true;
   }

   @FXML
   public void removeChargingStationAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       resetFlags();
       isChargingStationRemoving = true;
   }

   @FXML
    public void onVisualPaneClicked(MouseEvent event){
       if(ControllerHelper.isLeftClick(event)) {
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

    @FXML
    public void launchSimulationWithCustomParameters(MouseEvent event){
        if(ControllerHelper.isLeftClick(event)) {
            System.out.println("run simulation");
            if(isAtLeastOneDroneInstanciated()){
                ConfigureTimeSimulationParameters(event);
                LaunchSimulationWindow(event);
            } else {
                throwZeroDroneErrorPopup();
            }
        }
    }

    private void throwZeroDroneErrorPopup() {
        try {
            ErrorPopupView errorPopupView = new ErrorPopupView("You must add at least one drone!");
            createErrorPopup(errorPopupView.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LaunchSimulationWindow(MouseEvent event) {
        try {
            SimulationWindowView simulationWindowView = new SimulationWindowView();
            createWindow(event, simulationWindowView.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ConfigureTimeSimulationParameters(MouseEvent event) {
        Parent root = ControllerHelper.getRootWith(event);
        Slider IterationSlider = (Slider)  root.lookup("#iterationSlider");
        Slider simulationDurationSlider = (Slider)  root.lookup("#simulationDurationSlider");
        Double simulationDuration = simulationDurationSlider.getValue();
        Double iterationNumber =  IterationSlider.getValue();
        Simulation.setTimeSimulationParameters(simulationDuration.intValue(), iterationNumber.intValue());
    }

    private boolean isAtLeastOneDroneInstanciated() {
        return Simulation.getDrones().size() > 0;
    }

    private void createDroneElement(MouseEvent event, Parent root) {
        Slider droneWeightCapacity = (Slider)  root.lookup("#droneWeightCapacity");
        Slider droneBatteryCapacity = (Slider) root.lookup("#droneBatteryCapacity");
        Pane pane = (Pane) root.lookup("#visualSettingPane");
        Drone drone = new Drone();
        drone.setWeightCapacity((float) droneWeightCapacity.getValue());
        drone.setBatteryCapacity((int) droneBatteryCapacity.getValue());
        addDroneElement(event, pane, drone);
    }

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

    private DroneGraphicElement getDroneInRadius(Float x, Float y) {
       for (DroneGraphicElement drone: getDroneGraphicElements()){
            if(isInRadius(drone.getX(), x) && isInRadius(drone.getY(), y)){
                return drone;
            }
       }
       return null;
    }

    private void createChargingStationElement(MouseEvent event, Parent root) {
        Pane pane = (Pane) root.lookup("#visualSettingPane");
        ChargingStation chargingStation = new ChargingStation();
        addChargingStationElement(event, pane, chargingStation);
    }

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

    private ChargingStationGraphicElement getChargingStationInRadius(Float x, Float y) {
        for (ChargingStationGraphicElement chargingStation: getChargingStationGraphicElements()){
            if(isInRadius(chargingStation.getX(), x) && isInRadius(chargingStation.getY(), y)){
                return chargingStation;
            }
        }
        return null;
    }

    private Boolean isInRadius(Float x, Float x1) {
       return (x >= x1 - selectRadius && x <= x1 + selectRadius);
    }

    private void resetFlags(){
        isDronePlacement = false;
        isDroneRemoving = false;
        isChargingStationPlacement = false;
        isChargingStationRemoving = false;
    }
}
