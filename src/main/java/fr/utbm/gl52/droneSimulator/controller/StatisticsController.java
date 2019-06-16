package fr.utbm.gl52.droneSimulator.controller;

import fr.utbm.gl52.droneSimulator.view.graphicElement.GraphicHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;

import java.io.IOException;

import static fr.utbm.gl52.droneSimulator.view.StatisticsWindowView.*;

public class StatisticsController {

    @FXML
    public void refreshData(ActionEvent ae){
        Parent root = ControllerHelper.getRootWith(ae);

        ComboBox comboBox;
        comboBox = (ComboBox) root.lookup("#comboBox1");
        Integer simu1 = (Integer) comboBox.getValue();

        comboBox = (ComboBox) root.lookup("#comboBox2");
        Integer simu2 = (Integer) comboBox.getValue();

        if (simu1 == null || simu2 == null){
            try {
                GraphicHelper.throwErrorPopup("It's necessary to select 2 simulation!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (simu1 == simu2){
            try {
                GraphicHelper.throwErrorPopup("It's necessary to select 2 different simulation!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            updateCharts();
        }
    }
}
