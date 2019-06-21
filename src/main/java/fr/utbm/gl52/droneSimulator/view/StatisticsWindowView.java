package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.repository.HibernateHelper;
import fr.utbm.gl52.droneSimulator.service.DbDroneService;
import fr.utbm.gl52.droneSimulator.service.DbParameterService;
import fr.utbm.gl52.droneSimulator.service.DbParcelService;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import fr.utbm.gl52.droneSimulator.service.entity.DbParameter;
import fr.utbm.gl52.droneSimulator.service.entity.DbParcel;
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
    private static LineChart<Number, Number> lineChart;
    private static LineChart<Number, Number> lineChart2;
    private static LineChart<Number, Number> lineChart3;
    private static LineChart<Number, Number> lineChart4;

    /**
     * Default constructor
     *
     * @throws IOException The associated FXML file was not found
     */
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

    /**
     * Calculation of the average distance travelled per drones
     *
     * @param dbDrones drones of the iteration
     * @return Average distance in kilometers
     */
    private static Hashtable<Integer, Double> averageDistancesDronesTravelled(List<DbDrone> dbDrones) {
        Double oldValue;
        Hashtable<Integer, Double> total = new Hashtable<>();

        for (DbDrone dbDrone : dbDrones) {
            if (total.containsKey(dbDrone.getIdIteration())){
                oldValue = total.get(dbDrone.getIdIteration());
                total.put(dbDrone.getIdIteration(), oldValue + dbDrone.getKilometers());
            } else {
                total.put(dbDrone.getIdIteration(), dbDrone.getKilometers());
            }
        }

        return getAverageDrone(total, dbDrones.size());
    }

    /**
     *  Calculation of the average charging number per drones
     *
     * @param dbDrones drones of the iteration
     * @return Average of battery reload per drone
     */
    private static Hashtable<Integer, Double> averageChargingNumberDrones(List<DbDrone> dbDrones) {
        Double oldValue;
        Hashtable<Integer, Double> total = new Hashtable<>();

        for (DbDrone dbDrone : dbDrones) {
            if (total.containsKey(dbDrone.getIdIteration())){
                oldValue = total.get(dbDrone.getIdIteration());
                total.put(dbDrone.getIdIteration(), oldValue + dbDrone.getChargingTime());
            } else {
                total.put(dbDrone.getIdIteration(), dbDrone.getChargingTime());
            }
        }

        return getAverageDrone(total, dbDrones.size());
    }

    /**
     * Insert data to charts
     *
     * @param simu1 Id of simulation 1
     * @param simu2 Id of simulation 2
     * @param dataSimu1 Data associated with simulation 1
     * @param dataSimu2 Data associated with simulation 2
     * @return Reworked data
     */
    private static ObservableList<XYChart.Series<Number, Number>> getDummyChartData(Integer simu1, Integer simu2, Hashtable<Integer, Double> dataSimu1, Hashtable<Integer, Double> dataSimu2) {
        ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();

        XYChart.Series<Number, Number> simulation1 = new XYChart.Series<>();
        XYChart.Series<Number, Number> simulation2 = new XYChart.Series<>();

        simulation1.setName("Simulation " + simu1);
        simulation2.setName("Simulation " + simu2);

        var dbParameterService = new DbParameterService();
        var parameterSimu1 = dbParameterService.getSimulationParameter(simu1);
        var parameterSimu2 = dbParameterService.getSimulationParameter(simu2);

        for (Integer iteration = 1; iteration <= parameterSimu1.getNbIteration(); iteration++) {
            simulation1.getData().add(new XYChart.Data<>(iteration-1, dataSimu1.get(iteration)));
        }
        for (Integer iteration = 1; iteration <= parameterSimu2.getNbIteration(); iteration++) {
            simulation2.getData().add(new XYChart.Data<>(iteration-1, dataSimu2.get(iteration)));
        }

        data.addAll(simulation1, simulation2);
        return data;
    }

    /**
     * Get the value of a comboBox
     *
     * @param i get the combobox number
     * @return Value of the combo box
     */
    private static Integer getComboBox(Integer i){
        ComboBox comboBox;
        comboBox = (ComboBox) staticRoot.lookup("#comboBox" + i);
        return (Integer) comboBox.getValue();
    }

    /**
     * Initializes the graphs the first time
     */
    private static void createCharts(){
        lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart.setTitle("Average Distance Travelled Per Drone");
        lineChart.getXAxis().setLabel("Iteration");
        lineChart.getYAxis().setLabel("Km");

        lineChart2 = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart2.setTitle("Average Charging Number Per Drone");
        lineChart2.getXAxis().setLabel("Iteration");
        lineChart2.getYAxis().setLabel("Charging Number");

        lineChart3 = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart3.setTitle("Average Delivery Time Per Parcel");
        lineChart3.getXAxis().setLabel("Iteration");
        lineChart3.getYAxis().setLabel("Minute");

        lineChart4 = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart4.setTitle("Average Time Delivery Constraints Delta Per Parcel");
        lineChart4.getXAxis().setLabel("Iteration");
        lineChart4.getYAxis().setLabel("Minute");

        lineChart.setPrefWidth((1000-50)/2);
        lineChart.setPrefHeight((700-50)/2);
        lineChart2.setPrefWidth((1000)/2);
        lineChart2.setPrefHeight((700-50)/2);
        lineChart3.setPrefHeight((1000)/2);
        lineChart3.setPrefHeight((700-50)/2);
        lineChart4.setPrefHeight((1000)/2);
        lineChart4.setPrefHeight((700-50)/2);

        HBox hBox;
        hBox = (HBox) staticRoot.lookup("#hbox1");
        hBox.getChildren().addAll(lineChart, lineChart2);

        hBox = (HBox) staticRoot.lookup("#hbox2");
        hBox.getChildren().addAll(lineChart3, lineChart4);
    }

    /**
     *  Resize charts according to the size of windows
     */
    private static void resizeCharts(){
        Pane statisticsPane = (Pane) staticRoot.lookup("#statisticsPane");

        lineChart.setPrefWidth((statisticsPane.getWidth()-50)/2);
        lineChart.setPrefHeight((statisticsPane.getHeight()-50)/2);
        lineChart2.setPrefWidth((statisticsPane.getWidth()-50)/2);
        lineChart2.setPrefHeight((statisticsPane.getHeight()-50)/2);
        lineChart3.setPrefHeight((statisticsPane.getWidth()-50)/2);
        lineChart3.setPrefHeight((statisticsPane.getHeight()-50)/2);
        lineChart4.setPrefHeight((statisticsPane.getWidth()-50)/2);
        lineChart4.setPrefHeight((statisticsPane.getHeight()-50)/2);
    }

    /**
     * Update charts after press the button 'compare'
     */
    public static void updateCharts(){
        var dbDroneService = new DbDroneService();
        var dbParcelService = new DbParcelService();

        var simu1 = getComboBox(1);
        var simu2 = getComboBox(2);
        var dbDronesSimu1 = dbDroneService.getAllFromSimulationId(simu1);
        var dbDronesSimu2 = dbDroneService.getAllFromSimulationId(simu2);
        var dbDeliveryDataSimu1 = dbParcelService.getAllFromSimulationId(simu1, HibernateHelper.Event.DELIVERY.toString());
        var dbDeliveryDataSimu2 = dbParcelService.getAllFromSimulationId(simu2, HibernateHelper.Event.DELIVERY.toString());
        var dbDeliveryConstraintsDataSimu1 = dbParcelService.getAllFromSimulationId(simu1, HibernateHelper.Event.DELIVERYCONSTRAINTS.toString());
        var dbDeliveryConstraintsDataSimu2 = dbParcelService.getAllFromSimulationId(simu2, HibernateHelper.Event.DELIVERYCONSTRAINTS.toString());

        resizeCharts();

        lineChart.setData(getDummyChartData(simu1, simu2, averageDistancesDronesTravelled(dbDronesSimu1), averageDistancesDronesTravelled(dbDronesSimu2)));
        lineChart2.setData(getDummyChartData(simu1, simu2, averageChargingNumberDrones(dbDronesSimu1), averageChargingNumberDrones(dbDronesSimu2)));
        lineChart3.setData(getDummyChartData(simu1, simu2, averageEventParcel(dbDeliveryDataSimu1, simu1, "delivery"), averageEventParcel(dbDeliveryDataSimu2, simu1, "delivery")));
        lineChart4.setData(getDummyChartData(simu1, simu2, averageEventParcel(dbDeliveryConstraintsDataSimu1, simu2, "deliveryConstraints"), averageEventParcel(dbDeliveryConstraintsDataSimu2, simu2, "deliveryConstraints")));

        HBox hBox;
        hBox = (HBox) staticRoot.lookup("#hbox1");
        hBox.getChildren().removeAll(lineChart, lineChart2);
        hBox.getChildren().addAll(lineChart, lineChart2);

        hBox = (HBox) staticRoot.lookup("#hbox2");
        hBox.getChildren().removeAll(lineChart3, lineChart4);
        hBox.getChildren().addAll(lineChart3, lineChart4);
    }

    /**
     *  Calculation of the average of an event per parcel
     *
     * @param dbDatas Data from database associated with parcels
     * @param simu Id of the simulation
     * @param event Event to filter
     * @return the average time delta associated with the event
     */
    private static Hashtable<Integer, Double> averageEventParcel(List<DbParcel> dbDatas, int simu, String event) {
        Double oldValue;
        Hashtable<Integer, Double> total = new Hashtable<>();

        for (DbParcel dbData : dbDatas) {
            if (total.containsKey(dbData.getIdIteration())){
                oldValue = total.get(dbData.getIdIteration());
                total.put(dbData.getIdIteration(), oldValue + dbData.getDelta());
            } else {
                total.put(dbData.getIdIteration(), (double) dbData.getDelta());
            }
        }

        return getAverageEvent(total, simu, event);
    }

    /**
     * Divides the total by the number of drone
     *
     * @param average data per drone
     * @param size Number of drones
     * @return Computed average
     */
    private static Hashtable<Integer, Double> getAverageDrone(Hashtable<Integer, Double> average, int size) {
        Set<Integer> keys = average.keySet();
        Iterator<Integer> itr = keys.iterator();
        Integer key;

        while(itr.hasNext()){
            key = itr.next();

            average.replace(key, average.get(key) / size);
        }

        return average;
    }

    /**
     * Divides the total by the number of event
     *
     * @param average Data per parcel
     * @param simu Id of the simulation
     * @param event Associated event
     * @return Average associated with the event.
     */
    private static Hashtable<Integer, Double> getAverageEvent(Hashtable<Integer, Double> average, int simu, String event) {
        Set<Integer> keys = average.keySet();
        Iterator<Integer> itr = keys.iterator();
        Integer key;

        var dbParcelService = new DbParcelService();

        while(itr.hasNext()){
            key = itr.next();

            average.replace(key, average.get(key) / dbParcelService.getNbEventIterationSimu(simu, key, event));
        }

        return average;
    }

    public static javafx.scene.Parent getParent(){ return staticRoot; }
}
