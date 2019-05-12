package fr.utbm.gl52.droneSimulator.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;

import java.io.IOException;

public class ErrorPopupView {
    private Parent root;

    public ErrorPopupView() throws IOException{
        loadFXML();
    }

    public ErrorPopupView(String errorMsg) throws IOException {
        loadFXML();
        SetErrorMsg(errorMsg);
    }

    private void SetErrorMsg(String errorMsg) {
        Text text = (Text) root.lookup("#errorMsg");
        text.setText(errorMsg);
    }

    private void loadFXML() throws IOException {
        FXMLLoader loader =  new FXMLLoader(
                getClass().getResource("/fxml/errorPopup.fxml")
        );
        loader.load();

        root = loader.getRoot();
    }

    public Parent getParent(){
        return root;
    }
}
