package fr.utbm.gl52.droneSimulator.model;

public class Area extends SimulationElement {
    private float width;
    private float height;

    public Area(float x, float y, float caseWidth, float caseHeight) {
        setX(x);
        setY(y);
        setWidth(caseWidth);
        setHeight(caseHeight);
    }

    public float getWidth() {
        return width;
    }
    public float getHeight() {
        return height;
    }

    public void setWidth(float w) {
        width = w;
    }
    public void setHeight(float h) {
        height = h;
    }

}
