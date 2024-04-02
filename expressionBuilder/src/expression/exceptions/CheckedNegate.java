package expression.exceptions;

import expression.AbstractUnary;
import expression.ExpressionObject;
import expression.exceptions.OverflowException;

import java.math.BigDecimal;
import java.util.List;

public class CheckedNegate extends AbstractUnary {

    public CheckedNegate(ExpressionObject eo) {
        super(eo);
    }

    private void checkException(int num){
        if(num == Integer.MIN_VALUE){
            throw new OverflowException();
        }
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        int num = eo.evaluate(x);
        checkException(num);
        return -num;
    }

    @Override
    public int getPrior() {
        return 6;
    }

    @Override
    public int getDepend() {
        return 0;
    }

    @Override
    public String getSign() {
        return "-";
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int num = eo.evaluate(x, y, z);
        checkException(num);
        return -num;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int num = eo.evaluate(variables);
        checkException(num);
        return -num;
    }
}
