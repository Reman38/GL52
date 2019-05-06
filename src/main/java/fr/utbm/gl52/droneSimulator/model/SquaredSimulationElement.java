package fr.utbm.gl52.droneSimulator.model;

public class SquaredSimulationElement extends SimulationElement{
    private Float size = 0.5f;

    public SquaredSimulationElement(Float _size){
        size = _size;
    }

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