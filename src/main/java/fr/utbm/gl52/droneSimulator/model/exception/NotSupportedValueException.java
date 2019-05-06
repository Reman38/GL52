package fr.utbm.gl52.droneSimulator.model.exception;

public class NotSupportedValueException extends RuntimeException {
    public NotSupportedValueException(String message){
        super("NotSupportedValueException: "+message);
    }
}

