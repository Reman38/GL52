package fr.utbm.gl52.droneSimulator.service.entity;

import javax.persistence.*;

@Entity
@Table(name = "PARAMETER")
public class DbParameter extends MyEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSimu;

    @Column
    private int duration;

    @Column
    private int nbIteration;

    public DbParameter(int duration, int nbIteration) {
        this.duration = duration;
        this.nbIteration = nbIteration;
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
}
