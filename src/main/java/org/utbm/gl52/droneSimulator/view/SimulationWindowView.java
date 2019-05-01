package org.utbm.gl52.droneSimulator.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class SimulationWindowView {

    private Parent root;

    public SimulationWindowView() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
                getClass().getResource("/fxml/SimulationWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
    }

    public javafx.scene.Parent getParent(){
        return root;
    }
}
