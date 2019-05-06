package fr.utbm.gl52.droneSimulator.model.exception;

public class OutOfMainAreaException extends RuntimeException {
    public OutOfMainAreaException(String message){
        super("OutOfMainAreaException: "+message);
    }
}
