package fr.utbm.gl52.droneSimulator.view;

import fr.utbm.gl52.droneSimulator.model.Simulation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Map;

import static fr.utbm.gl52.droneSimulator.model.Simulation.getCompetitionDifficultyLevels;

public class IterationPopupView {
    private Parent root;
    private static String simulationMode;

    private static Simulation simulation = new Simulation();
    private static Stage parentWindow;

    /**
     * Constructor
     *
     * @param simulationMode Mode of simulation
     *
     * @throws IOException The associated FXML file was not found
     */
    public IterationPopupView(String simulationMode) throws IOException{
        initView(simulationMode);
    }

    /**
     *
     * @param parentWindow Parent window of the popup
     * @param simulationMode Mode of simulation
     *
     * @throws IOException The associated FXML file was not found
     */
    public IterationPopupView(Stage parentWindow, String simulationMode) throws IOException{
        initView(simulationMode);
        IterationPopupView.parentWindow = parentWindow;
    }

    /**
     * Load view data
     *
     * @param simulationMode Mode of simulation
     * @throws IOException The associated FXML file was not found
     */
    private void initView(String simulationMode) throws IOException {
        loadFXML();
        createIterationNumberslider();
        createCompetitionDropDown();
        IterationPopupView.simulationMode = simulationMode;
    }

    /**
     * Parametrize the iteration slider
     */
    private void createIterationNumberslider() {
        Slider IterationSlider = (Slider)  root.lookup("#iterationSlider");
        Text IterationNumber = (Text) root.lookup("#iterationNumber");
        IterationNumber.setText(Simulation.getNumberOfSimulationIterationRange()[0].toString());
        IterationSlider.setMin(Simulation.getNumberOfSimulationIterationRange()[0]);
        IterationSlider.setMax(Simulation.getNumberOfSimulationIterationRange()[1]);
        IterationSlider.setShowTickLabels(true);

        refreshIterationNumber(IterationSlider, IterationNumber);
    }

    /**
     * Refresh the iteration text number on slider change
     *
     * @param IterationSlider The observed slider
     * @param iterationNumber The text to refresh
     */
    private void refreshIterationNumber(Slider IterationSlider, Text iterationNumber) {
        IterationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            iterationNumber.setText(String.format("%d", newValue.intValue()));
        });
    }

    /**
     * create the list of difficulty
     */
    private void createCompetitionDropDown(){
        ComboBox competitionLevels = (ComboBox) root.lookup("#difficultyComboBox");

        for(Map.Entry<String, Integer[]> difficulty: getCompetitionDifficultyLevels().entrySet())
            competitionLevels.getItems().add(difficulty.getKey());
    }

    /**
     * Load the FXML template
     *
     * @throws IOException The FXML was not found
     */
    private void loadFXML() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
                getClass().getResource("/fxml/iterationPopup.fxml")
        );
        loader.load();

        root = loader.getRoot();
    }

    public Parent getParent(){
        return root;
    }

    public static String getSimulationMode() {
        return simulationMode;
    }

    public static Simulation getSimulation() {
        return simulation;
    }

    public static Stage getParentWindow() {
        return parentWindow;
    }
}
