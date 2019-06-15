package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.service.DbDroneService;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.*;

import java.io.IOException;
import java.util.Random;

public class StatisticsWindowView {
    private Parent root;

    public StatisticsWindowView() throws IOException {
        var simulationId = 1;

        var dbDroneService = new DbDroneService();

        var dbDrones = dbDroneService.getAllFromSimulationId(simulationId);

        for (DbDrone dbDrone : dbDrones) {
            System.out.println(dbDrone.getIdDrone());
        }

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

    public ObservableList<XYChart.Series<String, Double>> getDummyChartData() {
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();

        XYChart.Series<String, Double> simulation1 = new XYChart.Series<>();
        XYChart.Series<String, Double> simulation2 = new XYChart.Series<>();

        simulation1.setName("Simulation 1");
        simulation2.setName("Simulation 2");

        Random r = new Random();
        int nbIteration = 5;

        for (int iteration = 1; iteration <= nbIteration; iteration++) {
            simulation1.getData().add(new XYChart.Data<>(Integer.toString(iteration), r.nextDouble()));
            simulation2.getData().add(new XYChart.Data<>(Integer.toString(iteration), r.nextDouble()));
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
