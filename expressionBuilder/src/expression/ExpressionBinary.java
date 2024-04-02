package expression;

public interface ExpressionBinary extends ExpressionObject {
    String getSign();
    ExpressionObject getFirst();
    ExpressionObject getSecond();
    boolean needLeftBrackets();
    boolean needRightBrackets();
}
