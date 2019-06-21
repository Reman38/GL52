package fr.utbm.gl52.droneSimulator.model.exception;

public class NotSupportedValueException extends Exception {
    public NotSupportedValueException(String message){
        super("NotSupportedValueException: "+message);
    }
}

