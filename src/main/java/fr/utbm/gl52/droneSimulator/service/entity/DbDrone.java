package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
public class DbDrone extends MyEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSimu;

    @Column
    private int idIteration;

    @Column
    private int chargingtime;

    @Column
    private int batteryCapacity;

    @Column
    private int weightCapacity;

    @Column
    private int kilometers;

    public DbDrone() {}

    public DbDrone(int idIteration, int chargingtime, int batteryCapacity, int weightCapacity, int kilometers) {
        this.idIteration = idIteration;
        this.chargingtime = chargingtime;
        this.batteryCapacity = batteryCapacity;
        this.weightCapacity = weightCapacity;
        this.kilometers = kilometers;
    }

    public int getIdSimu() {
        return idSimu;
    }

    public void setIdSimu(int idSimu) {
        this.idSimu = idSimu;
    }

    public int getIdIteration() {
        return idIteration;
    }

    public void setIdIteration(int idIteration) {
        this.idIteration = idIteration;
    }

    public int getChargingtime() {
        return chargingtime;
    }

    public void setChargingtime(int chargingtime) {
        this.chargingtime = chargingtime;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public int getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }
}
