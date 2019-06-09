package fr.utbm.gl52.droneSimulator.model;

import static fr.utbm.gl52.droneSimulator.view.SimulationWindowView.logDroneEventInTab;

public class Message {

    static void chooseClosestParcel(Drone drone, Integer parcelId)
    {
        String message = "Decides to load parcel " + parcelId + " because it's the nearest that fit with it weight capacity";
        sendMessageToTab(drone, message);
    }

    static void parcelDisappear(Drone drone, Integer parcelId){
        String message = "Remove parcel " + parcelId + " from target because it becomes unavailable";
        sendMessageToTab(drone, message);
    }

    static void loadParcel(Drone drone, Integer parcelId){
        String message = "Load parcel " + parcelId;
        sendMessageToTab(drone,message);
    }

    static void deliverParcel(Drone drone, Integer parcelId){
        String message = "deliver parcel " + parcelId;
        sendMessageToTab(drone, message);
    }

    static void targetChargingStation(Drone drone, Integer id) {
        String message = "Target charging station " + id + " because it would not have enough battery to join it after.";
        sendMessageToTab(drone, message);
    }

    static void startToCharge(Drone drone) {
        String message = "starting to charge battery";
        sendMessageToTab(drone, message);
    }

    static void endOfCharge(Drone drone) {
        String message = "End of charge; resume delivery";
        sendMessageToTab(drone, message);
    }

    static void outOfBattery(Drone drone) {
        String message = "Out of battery";
        sendMessageToTab(drone, message);
    }

    /**
     * Add the message in the appropriate drone tab
     *
     * @param drone Drone linked to the event
     * @param message Message to print
     */
    private static void sendMessageToTab(Drone drone, String message){
        String msg = formatSimulationDuration() + " (battery: "+ String.format("%.2f",drone.getBatteryCapacity()) + " min remaining): " + message;
        logDroneEventInTab(drone, msg);
    }

    /**
     * Format simulation duration in mm ss human readable time
     *
     * @return Formatted string
     */
    private static String formatSimulationDuration(){
        long timeInSec = Simulation.getElapsedTime() / 1000;
        long min = timeInSec / 60;
        long s  = timeInSec % 60;
        return min + "min " + s + "s ";
    }
}
