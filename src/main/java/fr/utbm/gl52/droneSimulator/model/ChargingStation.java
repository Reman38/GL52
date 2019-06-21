package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;

import java.util.Arrays;

public class ChargingStation extends CenteredAndSquaredSimulationElement {

    // constantes
    static final private Integer visibleDistance = 100000;

    // attributs
    private Boolean isBusy;
    private Drone reloadedDrone;

    /**
     * Construct a charging station with an Id
     *
     * @param id Id of the charging station, must be unique.
     */
    public ChargingStation(Integer id) {
        super(id,1f);

        isBusy = false;
    }

    /**
     * Create a random instance of charging station with an Id
     *
     * @param id Id of the charging station
     *
     * @return Instance of a random charging station
     */
    public static ChargingStation createRandomizedChargingStations(Integer id) {
        ChargingStation chargingStation = new ChargingStation(id);
        try {
            chargingStation.randomize();
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }
        return chargingStation;
    }

    /**
     * Randomize the position of a charging station
     *
     * @throws OutOfMainAreaException The charging station is out of the main area
     */
    public void randomize() throws OutOfMainAreaException {
        setRandCoord();
    }

    /**
     * Reload drone battery
     *
     * @param drone The Drone to reload
     * @param deltaT The duration of the reloading
     */
    public void reloadDrone(Drone drone,Long deltaT) {
        reloadedDrone = drone;
        reloadedDrone.chargeBatteryDuring(deltaT);
        isBusy = true;
    }

    /**
     * Free the charging station
     */
    public void freeChargingStation(){
        reloadedDrone = null;
        isBusy = false;
    }

    /**
     * check if the drone in parameter is the one that is reloading on this charging station
     *
     * @param drone drone th check
     *
     * @return True if this drone is reloading on this station
     */
    public Boolean isCurrentDroneReloading(Drone drone){
        return reloadedDrone == drone;
    }

    /**
     * Set rand coordinates
     *
     * @throws OutOfMainAreaException The element is out of the main area
     */
    public void setRandCoord() throws OutOfMainAreaException {
        setRandCoord(Simulation.getMainArea());
    }

    public Integer getDetectionRange() {
        return visibleDistance;
    }

    /**
     * Check wether the station is occupied by a drone or not.
     * @return
     */
    public Boolean isBusy() {
        return isBusy;
    }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "isBusy=" + isBusy +
                ", reloadedDrone=" + reloadedDrone +
                ", coord=" + Arrays.toString(coord) +
                '}';
    }
}
