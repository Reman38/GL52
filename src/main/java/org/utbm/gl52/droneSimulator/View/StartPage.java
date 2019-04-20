package org.utbm.gl52.droneSimulator.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class StartPage {

    private Parent root;


    public StartPage() throws IOException {
        root= FXMLLoader.load(getClass().getResource("/org/utbm/gl52/droneSimulator/View/StartPage.fxml"));
    }

    public Parent getParent(){
        return root;
    }
}
