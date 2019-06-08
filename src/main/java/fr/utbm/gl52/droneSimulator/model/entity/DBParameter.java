package fr.utbm.gl52.droneSimulator.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "PARAMETER")
public class DBParameter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsimu")
    private int idSimu;

    @Column(name = "duration")
    private int duration;

    @Column(name = "nbiteration")
    private int nbIteration;

    public DBParameter(int duration, int nbIteration) {
        this.duration = duration;
        this.nbIteration = nbIteration;
    }

    public DBParameter() {}

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
