package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.CenteredAndSquaredSimulationElement;
import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import javafx.scene.text.Text;

public abstract class CenteredAndErgonomicGraphicElement extends RectangleGraphicElement {
    private static Float zoomCoefficient = 200f;
    private Text number = new Text();

    public CenteredAndErgonomicGraphicElement(SimulationElement se) {
        super(se);
        CenteredAndSquaredSimulationElement sq = (CenteredAndSquaredSimulationElement) se;
        number.setX(getXId());
        number.setY(getYId());
        number.setText(sq.getId().toString());
    }

    public static Float getZoomCoefficient() {
        return zoomCoefficient;
    }

    public static void setZoomCoefficient(Float zoomCoefficient) {
        CenteredAndErgonomicGraphicElement.zoomCoefficient = zoomCoefficient;
    }

    public Float getHeight() {
        return super.getHeight() * getZoomCoefficient();
    }
    public Float getWidth() {
        return super.getWidth() * getZoomCoefficient();
    }

    public Float getX() {
        return super.getX()-getWidth()/2;
    }
    public Float getY() {
        return super.getY()-getHeight()/2;
    }

    public Text getGraphicalId(){
        return number;
    }

    public Float getXId() {
        return super.getX() + getWidth()/2;
    }
    public Float getYId() {
        return super.getY() + getHeight()/2;
    }
}