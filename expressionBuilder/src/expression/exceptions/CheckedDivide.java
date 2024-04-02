package expression.exceptions;

import expression.AbstractBinary;
import expression.ExpressionObject;

import java.math.BigDecimal;
import java.util.List;

public class CheckedDivide extends AbstractBinary {
    public CheckedDivide(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    public void checkException(int a, int b){
        if(b == 0) throw new DivisionByZeroException();
        if(a == Integer.MIN_VALUE && b == -1) throw new OverflowException();
    }

    @Override
    public int evaluate(int x) {
        int a = first.evaluate(x);
        int b = second.evaluate(x);
        checkException(a, b);
        return a / b;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = first.evaluate(x, y, z);
        int b = second.evaluate(x, y, z);
        checkException(a, b);
        return a / b;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int getPrior() {
        return 5;
    }

    @Override
    public int getDepend() {
        return 2;
    }

    @Override
    public String getSign() {
        return "/";
    }

    @Override
    public boolean needLeftBrackets() {
        return this.getPrior() > first.getPrior() && first.getPrior() != 0;
    }

    @Override
    public boolean needRightBrackets() {
        if(second.getPrior() > this.getPrior()) return false;
        return second.getPrior() != 0;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int a = first.evaluate(variables);
        int b = second.evaluate(variables);
        checkException(a, b);
        return a / b;
    }
}
