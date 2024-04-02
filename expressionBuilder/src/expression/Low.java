package expression;

import java.math.BigDecimal;
import java.util.List;

public class Low extends AbstractUnary{
    public Low(ExpressionObject eo) {
        super(eo);
    }

    public static int low(int num) {
        return num & -num;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        return low(eo.evaluate(x));
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
        return "low";
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return low(eo.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return 0;
    }
}
