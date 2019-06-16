package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.service.DbDroneService;
import fr.utbm.gl52.droneSimulator.service.DbParameterService;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import fr.utbm.gl52.droneSimulator.service.entity.DbParameter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.*;

public class StatisticsWindowView {
    private static Parent staticRoot;
    public static LineChart<Number, Number> lineChart;
    public static LineChart<Number, Number> lineChart2;

    public StatisticsWindowView() throws IOException {
        var dbParameterService = new DbParameterService();
        var allSimu = dbParameterService.getAllSimulation();

        ObservableList<Integer> listSimu = FXCollections.observableArrayList();
        for (DbParameter dbParameter : allSimu) {
            listSimu.add(dbParameter.getIdSimu());
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/StatisticsWindow.fxml")
        );
        loader.load();

        staticRoot = loader.getRoot();

        ComboBox comboBox;
        for(int i = 1; i <= 2; i++){
            comboBox = (ComboBox) staticRoot.lookup("#comboBox" + i);
            comboBox.setItems(listSimu);
        }

        createCharts();
    }

    public static Hashtable<Integer, Double> averageDistancesDronesTravelled(List<DbDrone> dbDrones) {
        Double oldValue;
        Hashtable<Integer, Double> average = new Hashtable<>();

        for (DbDrone dbDrone : dbDrones) {
            if (average.containsKey(dbDrone.getIdIteration())){
                oldValue = average.get(dbDrone.getIdIteration());
                average.put(dbDrone.getIdIteration(), oldValue + dbDrone.getKilometers());
            } else {
                average.put(dbDrone.getIdIteration(), dbDrone.getKilometers());
            }
        }
        return average;
    }

    public static Hashtable<Integer, Double> averageChargingNumberDrones(List<DbDrone> dbDrones) {
        Double oldValue;
        Hashtable<Integer, Double> average = new Hashtable<>();

        for (DbDrone dbDrone : dbDrones) {
            if (average.containsKey(dbDrone.getIdIteration())){
                oldValue = average.get(dbDrone.getIdIteration());
                average.put(dbDrone.getIdIteration(), oldValue + dbDrone.getChargingTime());
            } else {
                average.put(dbDrone.getIdIteration(), dbDrone.getChargingTime());
            }
        }
        return average;
    }

    public static ObservableList<XYChart.Series<Number, Number>> getDummyChartData(Integer simu1, Integer simu2, Hashtable<Integer, Double> dataSimu1, Hashtable<Integer, Double> dataSimu2) {
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();

        XYChart.Series<Number, Number> simulation1 = new XYChart.Series<>();
        XYChart.Series<Number, Number> simulation2 = new XYChart.Series<>();

        simulation1.setName("Simulation " + simu1);
        simulation2.setName("Simulation " + simu2);

        for (Integer iteration = 1; iteration <= 3; iteration++) {
            simulation1.getData().add(new XYChart.Data<>(iteration, dataSimu1.get(iteration)));
        }
        for (Integer iteration = 1; iteration <= 3; iteration++) {
            simulation2.getData().add(new XYChart.Data<>(iteration, dataSimu2.get(iteration)));
        }

        data.addAll(simulation1, simulation2);
        return data;
    }

    public static Integer getComboBox(Integer i){
        ComboBox comboBox;
        comboBox = (ComboBox) staticRoot.lookup("#comboBox" + i);
        return (Integer) comboBox.getValue();
    }

    public static void createCharts(){
        lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart.setTitle("Average Distance Travelled");
        lineChart.getXAxis().setLabel("Iteration");
        lineChart.getYAxis().setLabel("Km");

        lineChart2 = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart2.setTitle("Average Charging Number");
        lineChart2.getXAxis().setLabel("Iteration");
        lineChart2.getYAxis().setLabel("Charging Number");

        HBox hBox;
        hBox = (HBox) staticRoot.lookup("#hbox1");
        hBox.getChildren().addAll(lineChart, lineChart2);
    }

    public static void resizeCharts(){
        Pane statisticsPane = (Pane) staticRoot.lookup("#statisticsPane");

        lineChart.setPrefWidth(statisticsPane.getWidth()/2);
        lineChart.setPrefHeight(statisticsPane.getHeight());
        lineChart2.setPrefWidth(statisticsPane.getWidth()/2);
        lineChart2.setPrefHeight(statisticsPane.getHeight());
    }

    public static void updateCharts(){
        var dbDroneService = new DbDroneService();
        var simu1 = getComboBox(1);
        var simu2 = getComboBox(2);
        var dbDronesSimu1 = dbDroneService.getAllFromSimulationId(simu1);
        var dbDronesSimu2 = dbDroneService.getAllFromSimulationId(simu2);

        resizeCharts();

        lineChart.setData(getDummyChartData(simu1, simu2, averageDistancesDronesTravelled(dbDronesSimu1), averageDistancesDronesTravelled(dbDronesSimu2)));
        lineChart2.setData(getDummyChartData(simu1, simu2, averageChargingNumberDrones(dbDronesSimu1), averageChargingNumberDrones(dbDronesSimu2)));

        HBox hBox;
        hBox = (HBox) staticRoot.lookup("#hbox1");
        hBox.getChildren().removeAll(lineChart, lineChart2);
        hBox.getChildren().addAll(lineChart, lineChart2);
    }
    public static javafx.scene.Parent getParent(){ return staticRoot; }
}
