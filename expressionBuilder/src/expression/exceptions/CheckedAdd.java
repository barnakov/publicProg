package expression.exceptions;

import expression.AbstractBinary;
import expression.AbstractUnary;
import expression.ExpressionObject;

import java.math.BigDecimal;
import java.util.List;

public class CheckedAdd extends AbstractBinary {
    public CheckedAdd(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    private void checkException(int a, int b){
        if(a > Integer.MAX_VALUE - b && b > 0 || a < Integer.MIN_VALUE - b && b < 0){
            throw new OverflowException();
        }
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        int a = first.evaluate(x);
        int b = second.evaluate(x);
        checkException(a, b);
        return a + b;
    }

    @Override
    public String getSign() {
        return "+";
    }

    @Override
    public boolean needLeftBrackets() {
        return first.getPrior() < this.getPrior() && first.getPrior() != 0;
    }

    @Override
    public boolean needRightBrackets() {
        return second.getPrior() < this.getPrior() && second.getPrior() != 0;
    }

    @Override
    public int getPrior() {
        return 4;
    }

    @Override
    public int getDepend() {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = first.evaluate(x, y, z);
        int b = second.evaluate(x, y, z);
        checkException(a, b);
        return a + b;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int a = first.evaluate(variables);
        int b = second.evaluate(variables);
        checkException(a, b);
        return a + b;
    }
}
