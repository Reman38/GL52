package fr.utbm.gl52.droneSimulator.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "PARCEL")
public class DBParcel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsimu")
    private int idSimu;

    @Column(name = "iditeration")
    private int idIteration;

    @Column(name = "event")
    private int event;

    @Column(name = "delta")
    private int delta;

    public DBParcel() {}

    public DBParcel(int idSimu, int idIteration, int event, int delta) {
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