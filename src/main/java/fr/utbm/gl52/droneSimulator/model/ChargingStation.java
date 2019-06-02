package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ChargingStation extends CenteredAndSquaredSimulationElement {

    // constantes
    static final private Integer visibleDistance = 100000;

    // attributs
    private Boolean isBusy;
    private Drone reloadedDrone;

    public ChargingStation(Integer id) {
        super(id,1f);

        isBusy = false;
    }

    public static ChargingStation createRandomizedChargingStations(Integer id) {
        ChargingStation chargingStation = new ChargingStation(id);
        try {
            chargingStation.randomize();
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
        return chargingStation;
    }

    public void randomize() throws OutOfMainAreaException {
        setRandCoord();
    }

    public void reloadDrone(Drone drone,Long deltaT) {
        reloadedDrone = drone;
        reloadedDrone.chargeBatteryDuring(deltaT);
        isBusy = true;
    }

    public void freeChargingStation(){
        reloadedDrone = null;
        isBusy = false;
    }

    public Boolean isCurrentDroneReloading(Drone drone){
        return reloadedDrone == drone;
    }

    public void setRandCoord() throws OutOfMainAreaException {
        setRandCoord(Simulation.getMainArea());
    }

    public Integer getDetectionRange() {
        return visibleDistance;
    }

    public Boolean isBusy() {
        return isBusy;
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "isBusy=" + isBusy +
                ", coord=" + Arrays.toString(coord) +
                '}';
    }
}
