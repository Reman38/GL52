package fr.utbm.gl52.droneSimulator.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "DRONE")
public class DBDrone {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsimu")
    private int idSimu;

    @Column(name = "iditeration")
    private int idIteration;

    @Column(name = "chargingtime")
    private int chargingtime;

    @Column(name = "batterycapacity")
    private int batteryCapacity;

    @Column(name = "weightcapacity")
    private int weightCapacity;

    @Column(name = "kilometers")
    private int kilometers;

    public DBDrone() {}

    public DBDrone(int idIteration, int chargingtime, int batteryCapacity, int weightCapacity, int kilometers) {
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
