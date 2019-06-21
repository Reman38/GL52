package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.service.entity.DbParcel;

import java.util.List;

public class DbParcelService extends AbstractService<DbParcel> {

    public DbParcelService() {
        super(DbParcel.class);
    }

    public List<DbParcel> getAllFromSimulationId(int simulationId, String event) {
        return getH2Dao().list(simulationId, event);
    }
}