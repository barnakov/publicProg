package expression;

import java.math.BigDecimal;
import java.util.List;

public class High extends AbstractUnary{
    public High(ExpressionObject eo) {
        super(eo);
    }

    public static int high(int num) {
        if (num == 0) {
            return 0;
        }
        int highestBit = 31 - Integer.numberOfLeadingZeros(num);
        return 1 << highestBit;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        return high(eo.evaluate(x));
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
        return "high";
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return high(eo.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return 0;
    }
}
