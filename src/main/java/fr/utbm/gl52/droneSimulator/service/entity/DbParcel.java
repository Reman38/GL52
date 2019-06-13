package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "PARCEL")
public class DbParcel extends MyEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSimu;

    @Column
    private int idIteration;

    @Column
    private int event;

    @Column
    private int delta;

    public DbParcel() {}

    public DbParcel(int idSimu, int idIteration, int event, int delta) {
        this.idSimu = idSimu;
        this.idIteration = idIteration;
        this.event = event;
        this.delta = delta;
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

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
}