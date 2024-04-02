package expression.exceptions;

public class DivisionByZeroException extends RuntimeException{
    public String getMessage(){
        return "division by zero";
    }
}
