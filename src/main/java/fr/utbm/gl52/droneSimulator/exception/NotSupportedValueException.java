package fr.utbm.gl52.droneSimulator.exception;

public class NotSupportedValueException extends RuntimeException {
    public NotSupportedValueException(String message){
        super("NotSupportedValueException: "+message);
    }
}

