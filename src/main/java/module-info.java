module fr.utbm.gl52.droneSimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens fr.utbm.gl52.droneSimulator to javafx.fxml;
    exports fr.utbm.gl52.droneSimulator;
    exports fr.utbm.gl52.droneSimulator.controller;

}