package fr.utbm.gl52.droneSimulator.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class ParameterWindowView {
    private Parent root;

    public ParameterWindowView() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
                getClass().getResource("/fxml/ParameterWindow.fxml")
        );
        loader.load();

        root = loader.getRoot();
    }

    public javafx.scene.Parent getParent(){
        return root;
    }
}
