package expression.exceptions;

import expression.AbstractBinary;

public abstract class AbstractParseException extends RuntimeException{
    private final String message;

    public AbstractParseException(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
