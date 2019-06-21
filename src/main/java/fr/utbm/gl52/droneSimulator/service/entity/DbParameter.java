package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
public class DbParameter extends MyEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSimu;

    @Column
    private int duration;

    @Column
    private int nbIteration;

    @Column
    private Integer disappearingTimeMin;

    @Column
    private Integer disappearingTimeMax;

    public DbParameter(int duration, int nbIteration, Integer disappearingTimeMin, Integer disappearingTimeMax) {
        this.duration = duration;
        this.nbIteration = nbIteration;
        this.disappearingTimeMin = disappearingTimeMin;
        this.disappearingTimeMax = disappearingTimeMax;
    }

    public DbParameter() {}

    public int getIdSimu() {
        return idSimu;
    }

    public void setIdSimu(int idSimu) {
        this.idSimu = idSimu;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNbIteration() {
        return nbIteration;
    }

    public void setNbIteration(int nbIteration) {
        this.nbIteration = nbIteration;
    }

    public Integer getDisappearingTimeMin() {
        return disappearingTimeMin;
    }

    public void setDisappearingTimeMin(Integer disappearingTimeMin) {
        this.disappearingTimeMin = disappearingTimeMin;
    }

    public Integer getDisappearingTimeMax() {
        return disappearingTimeMax;
    }

    public void setDisappearingTimeMax(Integer disappearingTimeMax) {
        this.disappearingTimeMax = disappearingTimeMax;
    }
}
