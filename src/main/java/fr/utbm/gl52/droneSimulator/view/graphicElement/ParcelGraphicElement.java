package fr.utbm.gl52.droneSimulator.view.graphicElement;

import fr.utbm.gl52.droneSimulator.model.Parcel;
import fr.utbm.gl52.droneSimulator.model.SimulationElement;
import fr.utbm.gl52.droneSimulator.view.SimulationWindowView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;

import static fr.utbm.gl52.droneSimulator.controller.ControllerHelper.isSameCoord;
import static fr.utbm.gl52.droneSimulator.view.SimulationWindowView.getParcelGraphicElements;
import static fr.utbm.gl52.droneSimulator.view.SimulationWindowView.removeParcelGraphicIfExists;

public class ParcelGraphicElement extends CenteredAndErgonomicGraphicElement {
    public ParcelGraphicElement(SimulationElement se) {
        super(se);

        setColor("green");
        isFilled(false);
    }

    public static void removeParcelGraphicAtCoord(Float[] coord) {
        ParcelGraphicElement parcelToRemove;

        parcelToRemove = findParcelGraphicWithParcelCoord(coord);
        Parcel parcel = (Parcel) parcelToRemove.getSimulationElement();


    }

    public static ParcelGraphicElement findParcelGraphicWithParcelCoord(Float[] coord) {
        ParcelGraphicElement parcelToRemove = null;
        ParcelGraphicElement parcelGraphicElement;
        SimulationElement parcel;
        Iterator<ParcelGraphicElement> iterator = getParcelGraphicElements().iterator();

        while(iterator.hasNext() && parcelToRemove == null){
            parcelGraphicElement = iterator.next();
            parcel = parcelGraphicElement.getSimulationElement();

            if(isSameCoord(coord, new Float[] {parcel.getX(), parcel.getY()})){
                parcelToRemove = parcelGraphicElement;
            }
        }
        return parcelToRemove;
    }
}