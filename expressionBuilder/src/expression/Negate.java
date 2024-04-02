package expression;

import java.math.BigDecimal;
import java.util.List;

public class Negate extends AbstractUnary {
    public Negate(ExpressionObject eo) {
        super(eo);
    }

    @Override
    public int getPrior(){
        return 6;
    }

    @Override
    public String getSign(){
        return "-";
    }

    @Override
    public int getDepend(){
        return 0;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        return -eo.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -eo.evaluate(x, y, z);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return 0;
    }
}
