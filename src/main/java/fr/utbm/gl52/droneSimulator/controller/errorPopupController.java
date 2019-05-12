package fr.utbm.gl52.droneSimulator.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.Window;


import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.getRootWith;

public class errorPopupController {

    @FXML
    public void closeErrorPopupAction(ActionEvent event){
        Parent root = getRootWith(event);
        Window stage = root.getScene().getWindow();
        stage.hide();
    }
}
