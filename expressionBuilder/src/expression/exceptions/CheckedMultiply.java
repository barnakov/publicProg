package expression.exceptions;

import expression.AbstractBinary;
import expression.ExpressionObject;

import java.math.BigDecimal;
import java.util.List;

public class CheckedMultiply extends AbstractBinary {
    public CheckedMultiply(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    public void checkException(int a, int b){
        if((a > 0 && b > 0 && a > Integer.MAX_VALUE / b)
            || (a > 0 && b < 0 && b < Integer.MIN_VALUE / a)
            || (a < 0 && b > 0 && a < Integer.MIN_VALUE / b)
            || (a < 0 && b < 0 && a < Integer.MAX_VALUE / b)){
            throw new OverflowException();
        }
    }

    @Override
    public int evaluate(int x) {
        int a = first.evaluate(x);
        int b = second.evaluate(x);
        checkException(a, b);
        return a * b;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = first.evaluate(x, y, z);
        int b = second.evaluate(x, y, z);
        checkException(a, b);
        return a * b;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return first.evaluate(x).multiply(second.evaluate(x));
    }

    @Override
    public int getPrior() {
        return 5;
    }

    @Override
    public int getDepend() {
        return 0;
    }

    @Override
    public String getSign() {
        return "*";
    }

    @Override
    public boolean needLeftBrackets() {
        return first.getPrior() < this.getPrior() && first.getPrior() != 0;
    }

    @Override
    public boolean needRightBrackets() {
        if(second.getPrior() == this.getPrior() && second.getDepend() == 2){
            return true;
        }
        return second.getPrior() < this.getPrior() && second.getPrior() != 0;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int a = first.evaluate(variables);
        int b = second.evaluate(variables);
        checkException(a, b);
        return a * b;
    }
}
