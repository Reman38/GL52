module org.utbm.gl52.droneSimulator {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.utbm.gl52.droneSimulator to javafx.fxml;
    exports org.utbm.gl52.droneSimulator;
    exports org.utbm.gl52.droneSimulator.controller;
}