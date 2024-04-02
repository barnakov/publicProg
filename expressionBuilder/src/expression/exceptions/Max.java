package expression.exceptions;

import expression.AbstractBinary;
import expression.ExpressionObject;

import java.math.BigDecimal;
import java.util.List;

public class Max extends AbstractBinary {
    public Max(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        int a = first.evaluate(x);
        int b = second.evaluate(x);
        if(a >= b)
            return a;
        else
            return b;
    }

    @Override
    public String getSign() {
        return "max";
    }

    @Override
    public boolean needLeftBrackets() {
        //return first.getPrior() < second.getPrior() && first.getPrior() != 0;
        return false;
    }

    @Override
    public boolean needRightBrackets() {
      //  return this.getPrior() < second.getPrior() && second.getPrior() != 0;
        return this.getPrior() == second.getPrior() && this.getDepend() != second.getDepend();
    }

    @Override
    public int getPrior() {
        return 1;
    }

    @Override
    public int getDepend() {
        return 1;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = first.evaluate(x, y, z);
        int b = second.evaluate(x, y, z);
        if(a >= b)
            return a;
        else
            return b;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        int a = first.evaluate(variables);
        int b = second.evaluate(variables);
        if(a >= b)
            return a;
        else
            return b;
    }
}
