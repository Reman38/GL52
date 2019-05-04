package fr.utbm.gl52.droneSimulator.model;

public class OutOfMainAreaException extends Exception {
    public OutOfMainAreaException(String message){
        super("OutOfMainAreaException: "+message);
    }
}
