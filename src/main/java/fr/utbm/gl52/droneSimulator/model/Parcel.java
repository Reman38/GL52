package fr.utbm.gl52.droneSimulator.model;

import fr.utbm.gl52.droneSimulator.model.exception.OutOfMainAreaException;
import fr.utbm.gl52.droneSimulator.view.graphicElement.ParcelGraphicElement;
import javafx.application.Platform;

import java.util.Arrays;
import java.util.Date;

import static fr.utbm.gl52.droneSimulator.view.SimulationWindowView.removeParcelGraphicIfExists;
import static fr.utbm.gl52.droneSimulator.view.graphicElement.ParcelGraphicElement.findParcelGraphicWithParcelCoord;

public class Parcel extends CenteredAndSquaredSimulationElement {

    private Date popTime;
    private Float weight;
    private Float timeDeliveryGoal;
    private Float[] destCoord = new Float[2];
    private boolean isInJourney = false;
    private Float timeToDisappear;

    /**
     * Construct a nex parcel with the given Id
     *
     * @param id Id of the parcel
     */
    public Parcel(Integer id) {
        super(id, .5f);
        popTime = new Date();
    }

    /**
     * Create a parcel with a random weight, position and destination
     *
     * @param id Id of th parcel
     *
     * @return Parcel
     */
    static Parcel createRandomized(Integer id) {
        Parcel parcel = new Parcel(id);
        parcel.randomize();
        return parcel;
    }

    static Parcel createRandomized(Integer id, Long elapsedTime) {
        Parcel parcel = new Parcel(id);
        parcel.randomize(elapsedTime);
        return parcel;
    }

    /**
     * Randomize the weight, position and destination of the parcel
     */
    public void randomize() {
        setRandWeight();
        try {
            setRandCoord(Simulation.getMainArea()); // après setWeight car size nécessaire pour le controle de contrainte de mainArea
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }

        try {
            setRandDestCoord(Simulation.getMainArea()); // après setWeight car size nécessaire pour le controle de contrainte de mainArea
        } catch (OutOfMainAreaException e) {
            e.printStackTrace();
        }

        setRandTimeToDisappear(0L);
        setRandTimeConstraints(0L);
    }

    public void randomize(Long elapsedTime){
        randomize();

        setRandTimeToDisappear(elapsedTime);
        setRandTimeConstraints(elapsedTime);
    }

    /**
     * Random time to make parcel disappear
     *
     * @param elapsedTime Elapsed time since the beginning of the iteration.
     */
    private void setRandTimeToDisappear(Long elapsedTime) {
        timeToDisappear = elapsedTime + RandomHelper.getRandFloat((float)Simulation.getParcelTimeToDisappearRange()[0], (float)Simulation.getParcelTimeToDisappearRange()[1]);
    }

    /**
     * Random time constraints
     *
     * @param elapsedTime Elapsed time since the beginning of the iteration.
     */
    private void setRandTimeConstraints(Long elapsedTime) {
        timeDeliveryGoal = elapsedTime + RandomHelper.getRandFloat((float)Simulation.getParcelTimeToDisappearRange()[0], (float)Simulation.getParcelTimeToDisappearRange()[1]);
    }

    /**
     * Load on the drone the parcel that is at the given coordinates
     *
     * @param coord coordinates of the parcel
     */
    static void loadParcelAtCoord(Float[] coord){
        ParcelGraphicElement parcelToRemove;

        parcelToRemove = findParcelGraphicWithParcelCoord(coord);
        Parcel parcel = (Parcel) parcelToRemove.getSimulationElement();
        synchronized (Drone.class) {
            parcel.setInJourney(true);
        }

        Platform.runLater(() -> removeParcelGraphicIfExists(parcelToRemove));
    }

    /**
     * Set a rand weight to the parcel
     */
    private void setRandWeight() {
        setWeight(RandomHelper.getRandFloat(0f, 20f));
    }

    /**
     * Set a rand abscissa within the area boundaries
     *
     * @param area Area in which the abscissa should be set
     *
     * @throws OutOfMainAreaException The abscissa exceeds the area boundaries
     */
    private void setRandDestX(Area area) throws OutOfMainAreaException {
//        setDestX(RandomHelper.getRandFloat(area.getX(), (area.getX() + area.getWidth())));
//        setDestX(area.getWidth()/2);
        setDestX(RandomHelper.getRandFloat(area.getX() + getWidth() / 2, (area.getX() + area.getWidth()) - getWidth() / 2));
    }

    /**
     * Set a rand ordinates within the area boundaries
     *
     * @param area Area in which the ordinates should be set
     *
     * @throws OutOfMainAreaException The ordinates exceeds the area boundaries
     */
    private void setRandDestY(Area area) throws OutOfMainAreaException {
//        setDestY(RandomHelper.getRandFloat(area.getY(), (area.getY()+ area.getHeight())));
//        setDestY(area.getHeight()/2);
        setDestY(RandomHelper.getRandFloat(area.getY() + getHeight() / 2, (area.getY() + area.getHeight()) - getHeight() / 2));
    }

    /**
     * Set random coordinates within the area boundaries
     *
     * @param area Area in which the coordinates should be set
     *
     * @throws OutOfMainAreaException The coordinates exceeds the area boundaries
     */
    private void setRandDestCoord(Area area) throws OutOfMainAreaException {
        setRandDestX(area);
        setRandDestY(area);
    }

    /* getteurs et setteurs triviaux */

    /**
     * Get the pop time on the area of the parcel
     *
     * @return pop time of the drone
     */
    Date getPopTime() {
        return popTime;
    }

    public Float getWeight() {
        return weight;
    }

    /**
     * Set the weight of the parcel
     *
     * @param weight Weight in kilograms
     */
    public void setWeight(Float weight) {
        this.weight = weight;
    }

    /**
     * Get the destination coordinates of the parcel
     *
     * @return Array [x,y]
     */
    Float[] getDestCoord() {
        return destCoord;
    }

    /**
     * Set the abscissa of the element
     *
     * @param x the abscissa position in meters
     *
     * @throws OutOfMainAreaException x is not in the main area
     */
    private void setDestX(float x) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaXBoundary(x))
            throw new OutOfMainAreaException("x out of mainArea boundary : " + x);
        else
            destCoord[0] = x;
    }

    /**
     * Set the ordinate of the element
     *
     * @param y the ordinate position in meters
     *
     * @throws OutOfMainAreaException x is not in the main area
     */
    private void setDestY(float y) throws OutOfMainAreaException {
        if (Simulation.getMainArea().isInAreaYBoundary(y))
            throw new OutOfMainAreaException("y out of mainArea boundary : " + y);
        else
            destCoord[1] = y;
    }

    /**
     * Check if the parcel is loaded on a drone
     *
     * @return True if the parcel is in journey
     */
    boolean isInJourney() {
        return isInJourney;
    }

    /**
     * Set parcel in journey
     *
     * @param inJourney True if the parcel is in journey
     */
    private void setInJourney(boolean inJourney) {
        isInJourney = inJourney;
    }

    Float getTimeToDisappear() {
        return timeToDisappear;
    }

    @Override
    public String toString() {
        return "Parcel{" +
                "popTime=" + popTime +
                ", timeToDisappear= " + timeToDisappear +
                ", weight=" + weight +
                ", timeDeliveryGoal=" + timeDeliveryGoal +
                ", destCoord=" + Arrays.toString(destCoord) +
                ", isInJourney=" + isInJourney +
                '}';
    }
}
