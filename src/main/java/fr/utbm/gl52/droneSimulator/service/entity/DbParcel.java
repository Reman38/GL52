package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
public class DbParcel extends MyEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int idSimu;

    @Column
    private int idIteration;

    @Column
    private String event;

    @Column
    private long delta;

    public DbParcel(){}

    public DbParcel(int idSimu, int idIteration, String event, long delta) {
        this.idSimu = idSimu;
        this.idIteration = idIteration;
        this.event = event;
        this.delta = delta;
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

    public int getIdIteration() {
        return idIteration;
    }

    public void setIdIteration(int idIteration) {
        this.idIteration = idIteration;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public long getDelta() {
        return delta;
    }

    public void setDelta(long delta) {
        this.delta = delta;
    }
}