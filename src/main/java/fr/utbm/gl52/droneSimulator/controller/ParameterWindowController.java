package fr.utbm.gl52.droneSimulator.controller;


import fr.utbm.gl52.droneSimulator.model.Drone;
import fr.utbm.gl52.droneSimulator.model.Simulation;
import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import fr.utbm.gl52.droneSimulator.view.graphicElement.DroneGraphicElement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.calculateModelCoordinate;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper.*;

public class ParameterWindowController{

    private Boolean isDronePlacement = false;

    /*@FXML private AnchorPane ap;
    Parent root = ap.getParent();

    Slider droneWeightCapacity = (Slider)  root.lookup("#droneWeightCapacity");
    Slider droneBatteryCapacity = (Slider) root.lookup("#droneBatteryCapacity");*/

   /* @FXML
    public void onWeightCapacityDraged(ActionEvent ae){
        Node source = (Node) ae.getSource();
        Parent root = source.getParent();
        Slider droneWeightCapacity = (Slider)  root.lookup("#droneWeightCapacity");
        Text maxWeightCapacity = (Text) root.lookup("#maxWeightCapacity");
        DecimalFormat df = new DecimalFormat("###.#");
        df.setRoundingMode(RoundingMode.DOWN);
        maxWeightCapacity.setText(df.format(droneWeightCapacity.getValue()) + "kg");
    }

    @FXML
    public void onWeightDragDone(){
        //droneWeightCapacity.set
    }*/

   @FXML
   public void addDroneAction(ActionEvent ae){
       Node source = (Node) ae.getSource();
       Parent root = source.getScene().getRoot();
       Pane pane = (Pane) root.lookup("#visualSettingPane");
       useCrossHairCursorOn(pane);
       isDronePlacement = true;
   }

   @FXML
    public void onVisualPaneClicked(MouseEvent event){
       if(ControllerHelper.isLeftClick(event)) {
           if(isDronePlacement){
               Parent root = ControllerHelper.getRootWith(event);
               createDroneElement(event, root);
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
            addElementTo(pane, DroneGraphicElement.getShape(drone));
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void launchSimulationWithCustomParameters(MouseEvent event){
        if(ControllerHelper.isLeftClick(event)) {
            System.out.println("run simulation");
            try {
                SimulationWindowView simulationWindowView = new SimulationWindowView();
                ControllerHelper.createWindow(event, simulationWindowView.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
