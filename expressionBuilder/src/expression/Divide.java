package expression;

import java.math.BigDecimal;
import java.util.List;

public class Divide extends AbstractBinary {
    public Divide(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x) {
        return first.evaluate(x) / second.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return first.evaluate(x, y, z) / second.evaluate(x, y, z);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return first.evaluate(x).divide(second.evaluate(x));
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
        return 0;
    }
}
