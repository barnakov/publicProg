package expression.exceptions;

public class UnknownSymbolException extends AbstractParseException{
    public UnknownSymbolException(String message) {
        super(message);
    }
}
