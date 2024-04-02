package expression;

import java.math.BigDecimal;
import java.util.List;

public class L1 extends AbstractUnary{
    public L1(ExpressionObject eo) {
        super(eo);
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
        return "l1";
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        return Integer.numberOfLeadingZeros(~eo.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Integer.numberOfLeadingZeros(~eo.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return 0;
    }
}
