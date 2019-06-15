package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.service.DbDroneService;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;

import java.io.IOException;
import java.util.*;

public class StatisticsWindowView {
    private Parent root;
    public Hashtable<Integer, Double> averageDroneKmIterationSimu1 = new Hashtable<>();
    public Hashtable<Integer, Double> averageDroneKmIterationSimu2 = new Hashtable<>();
    int nbIterationSimu1 = 3;
    int nbIterationSimu2 = 3;

    public StatisticsWindowView() throws IOException {
        int simulation1Id = 1;
        int simulation2Id = 2;

        var dbDroneService = new DbDroneService();
        var dbDronesSimu1 = dbDroneService.getAllFromSimulationId(simulation1Id);
        var dbDronesSimu2 = dbDroneService.getAllFromSimulationId(simulation2Id);

        averageDroneKmIterationSimu1 = averageDroneKmSimu(dbDronesSimu1);
        averageDroneKmIterationSimu2 = averageDroneKmSimu(dbDronesSimu2);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/StatisticsWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
        LineChart lineChart;
        int nbChart = 4;

        for(int i = 1; i <= nbChart; i++){
            lineChart = (LineChart) root.lookup("#lineChart" + i);
            createLineChart(lineChart);
        }
    }

    private Hashtable<Integer, Double> averageDroneKmSimu(List<DbDrone> dbDronesSimu) {
        Double oldValue;
        Hashtable<Integer, Double> averageDroneKmIterationSimu = new Hashtable<>();

        for (DbDrone dbDrone : dbDronesSimu) {
            if (averageDroneKmIterationSimu.containsKey(dbDrone.getIdIteration())){
                oldValue = averageDroneKmIterationSimu.get(dbDrone.getIdIteration());
                averageDroneKmIterationSimu.put(dbDrone.getIdIteration(), oldValue + dbDrone.getKilometers());
            } else {
                averageDroneKmIterationSimu.put(dbDrone.getIdIteration(), dbDrone.getKilometers());
            }
        }

        return averageDroneKmIterationSimu;
    }

    public ObservableList<XYChart.Series<String, Double>> getDummyChartData() {
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();

        XYChart.Series<String, Double> simulation1 = new XYChart.Series<>();
        XYChart.Series<String, Double> simulation2 = new XYChart.Series<>();

        simulation1.setName("Simulation 1");
        simulation2.setName("Simulation 2");

        for (int iteration = 1; iteration <= nbIterationSimu1; iteration++) {
            simulation1.getData().add(new XYChart.Data<>(Integer.toString(iteration), averageDroneKmIterationSimu1.get(iteration)));
        }

        for (int iteration = 1; iteration <= nbIterationSimu2; iteration++) {
            simulation2.getData().add(new XYChart.Data<>(Integer.toString(iteration), averageDroneKmIterationSimu2.get(iteration)));
        }

        data.addAll(simulation1, simulation2);
        return data;
    }

    public void createLineChart(LineChart line) {
        line.setData(getDummyChartData());
        line.setTitle("Event");
    }
    public javafx.scene.Parent getParent(){ return root; }
}
