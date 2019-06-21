package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
public class DbChargingStation extends MyEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int idSimu;

    @Column
    private double x;

    @Column
    private double y;

    public DbChargingStation(int idSimu, double x, double y) {
        this.idSimu = idSimu;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSimu() {
        return idSimu;
    }

    public void setIdSimu(int idSimu) {
        this.idSimu = idSimu;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
