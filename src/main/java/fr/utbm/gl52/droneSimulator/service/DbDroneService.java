package fr.utbm.gl52.droneSimulator.service;

import fr.utbm.gl52.droneSimulator.service.entity.DbDrone;

import java.util.List;

public class DbDroneService extends AbstractService<DbDrone> {
    public DbDroneService() {
        super(DbDrone.class);
    }

    public void save(int idSimu, int idIteration, int idDrone, Integer chargingTime, float batteryCapacity, float weightCapacity, float kilometers, float x, float y) {
        DbDrone dbDrone = new DbDrone(idSimu, idIteration, idDrone, chargingTime, batteryCapacity, weightCapacity, kilometers, x, y);

        save(dbDrone);
    }

    public void merge(DbDrone dbDrone){
       getH2Dao().merge(dbDrone);
    }

    public List<DbDrone> getDronesInFirstIterationSimu(int idSimu) {
        return getH2Dao().droneListInFirstIteration(idSimu);
    }

    public DbDrone getDroneBy(Integer idSimu, Integer currentIteration, Integer droneId){
        return getH2Dao().getDroneBy(idSimu, currentIteration, droneId);
    }
}