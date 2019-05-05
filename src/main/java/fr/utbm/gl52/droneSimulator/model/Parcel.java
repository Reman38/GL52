package fr.utbm.gl52.droneSimulator.model;

public class Parcel extends SimulationElement {
    private Integer size;
    private String type;
    private Float weight;
    // TODO delivery time constraint

    public Parcel(Integer x, Integer y) {
        super();
        setX(x);
        setY(y);
    }

    public Parcel() {
        super();
    }

    public static Parcel createRandomized() {
        Parcel parcel = new Parcel();
        parcel.randomize();
        return parcel;
    }

    public void randomize() {
        setRandWeight();

        // après le setRandWeight car set la size pour la vue ; sinon contrainte de zone pas prise en compte
        setRandX();
        setRandY();
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer t) {
        size = t;
    }

    public void setRandWeight() {
        setSize(RandomHelper.getRandInt(0, 20));
    }

    public String toString() {
        String s =
            super.toString() +
            "size: " + getSize() + System.getProperty("line.separator");
        return s;
    }

    // TODO peut etre refactor
    public Float getWidth() {
        return getSize();
    }

    public Float getHeight() {
        return getSize();
    }
}
