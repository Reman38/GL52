package fr.utbm.gl52.droneSimulator.model;

public class NotSupportedValueException extends Exception {
    public NotSupportedValueException(String message){
        super("OutOfMainAreaException: "+message);
    }
}

