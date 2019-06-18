package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.service.entity.DbChargingStation;

public class DbChargingStationService extends AbstractService<DbChargingStation> {

    public DbChargingStationService() {
        super(DbChargingStation.class);
    }

    public void save(int idSimu,  float x, float y) {
        DbChargingStation dbChargingStation = new DbChargingStation(idSimu, x, y);

        save(dbChargingStation);
    }
}