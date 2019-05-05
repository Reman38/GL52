package fr.utbm.gl52.droneSimulator.model;

public class SquaredSimulationElement extends SimulationElement{
    private Float size = 0.5f;

    public Float getSize() {
        return size;
    }

    public Float getWidth() {
        return getSize();
    }

    public Float getHeight() {
        return getSize();
    }

}
