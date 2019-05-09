package fr.utbm.gl52.droneSimulator.controller;


import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import fr.utbm.gl52.droneSimulator.view.graphicElement.DroneGraphicElement;
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
import static fr.utbm.gl52.droneSimulator.view.ParameterWindowView.getDroneGraphicElements;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.*;

public class ParameterWindowController{

    //TODO: Penser à reset to les autres flags en cas de nouveau click sur un bouton
    private Boolean isDronePlacement = false;
    private Boolean isDroneRemoving = false;
    private static final Float selectRadius = 10f;

   @FXML
   public void addDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       isDronePlacement = true;
   }

   @FXML
   public void removeDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       isDroneRemoving = true;
   }

   @FXML
    public void onVisualPaneClicked(MouseEvent event){
       if(ControllerHelper.isLeftClick(event)) {
           Parent root = ControllerHelper.getRootWith(event);
           if(isDronePlacement){
               createDroneElement(event, root);
           } else if(isDroneRemoving){
                removeDroneElement(event, root);
           }
       }
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

    private DroneGraphicElement getDroneInRadius(float x, float y) {
       for (DroneGraphicElement drone: getDroneGraphicElements()){
            if(isInRadius(drone.getX(), x) && isInRadius(drone.getY(), y)){
                return drone;
            }
       }
       return null;
    }

    private Boolean isInRadius(Float x, float x1) {
       return (x >= x1 - selectRadius && x <= x1 + selectRadius);
    }

    @FXML
    public void launchSimulationWithCustomParameters(MouseEvent event){
        if(ControllerHelper.isLeftClick(event)) {
            System.out.println("run simulation");
            try {
                SimulationWindowView simulationWindowView = new SimulationWindowView();
                createWindow(event, simulationWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
