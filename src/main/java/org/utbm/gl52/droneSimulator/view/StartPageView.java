package org.utbm.gl52.droneSimulator.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.utbm.gl52.droneSimulator.controller.StartPageController;

import java.io.IOException;

public class StartPageView {

    private Parent root;

    public StartPageView() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
                getClass().getResource("/fxml/StartPage.fxml")
        );
        loader.load();

        root = loader.getRoot();

    }

    public Parent getParent(){
        return root;
    }
}
