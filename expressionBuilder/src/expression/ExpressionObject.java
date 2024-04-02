package expression;

public interface ExpressionObject extends Expression, TripleExpression, BigDecimalExpression, ListExpression{
    int getPrior();
    int getDepend();
    String toString();
}
