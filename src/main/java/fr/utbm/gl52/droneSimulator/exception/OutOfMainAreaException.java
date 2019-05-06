package fr.utbm.gl52.droneSimulator.exception;

public class OutOfMainAreaException extends RuntimeException {
    public OutOfMainAreaException(String message){
        super("OutOfMainAreaException: "+message);
    }
}
