package fr.utbm.gl52.droneSimulator;

import fr.utbm.gl52.droneSimulator.service.DbDroneService;
import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;

public class Main {
    public static void main(String[] args) {
        var simulationId = 1;

        var dbDroneService = new DbDroneService();
        var dbDrones = dbDroneService.getAllFromSimulationId(simulationId);

        for (DbDrone dbDrone : dbDrones) {
            System.out.println(dbDrone.toString());
        }

//        GuiMain.main(args);
    }

}
