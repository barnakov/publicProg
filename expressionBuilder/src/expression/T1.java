package expression;

import java.math.BigDecimal;
import java.util.List;

public class T1 extends AbstractUnary{
    public T1(ExpressionObject eo) {
        super(eo);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        return Integer.numberOfTrailingZeros(~eo.evaluate(x));
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
        return "t1";
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Integer.numberOfTrailingZeros(~eo.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return 0;
    }
}
