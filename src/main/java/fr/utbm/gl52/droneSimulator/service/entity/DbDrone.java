package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
public class DbDrone extends MyEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int idDrone;

    @Column
    private int idSimu;

    @Column
    private int idIteration;

    @Column
    private Integer chargingTime;

    @Column
    private double batteryCapacity;

    @Column
    private double weightCapacity;

    @Column
    private double kilometers;

    @Column
    private double x;

    @Column
    private double y;

    public DbDrone(int idSimu, int idIteration, int idDrone, int chargingTime, float batteryCapacity, float weightCapacity, float kilometers, float x, float y) {
        this.idSimu = idSimu;
        this.idIteration = idIteration;
        this.idDrone = idDrone;
        this.chargingTime = chargingTime;
        this.batteryCapacity = batteryCapacity;
        this.weightCapacity = weightCapacity;
        this.kilometers = kilometers;
        this.x = x;
        this.y = y;
    }

    public DbDrone() {
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

    public int getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(int idDrone) {
        this.idDrone = idDrone;
    }

    public double getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(Integer chargingTime) {
        this.chargingTime = chargingTime;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(float batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public double getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(float weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public double getKilometers() {
        return kilometers;
    }

    public void setKilometers(float kilometers) {
        this.kilometers = kilometers;
    }

    public double getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
