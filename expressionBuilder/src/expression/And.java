package expression;

import java.math.BigDecimal;
import java.util.List;

public class And extends AbstractBinary{
    public And(ExpressionObject first, ExpressionObject second) {
        super(first, second);
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return null;
    }

    @Override
    public int evaluate(int x) {
        return first.evaluate(x) & second.evaluate(x);
    }

    @Override
    public String getSign() {
        return "&";
    }

    @Override
    public boolean needLeftBrackets() {
        return first.getPrior() < this.getPrior() && first.getPrior() != 0;
    }

    @Override
    public boolean needRightBrackets() {
        return second.getPrior() < this.getPrior() && second.getPrior() != 0;
    }

    @Override
    public int getPrior() {
        return 3;
    }

    @Override
    public int getDepend() {
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return first.evaluate(x, y, z) & second.evaluate(x, y, z);
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return 0;
    }
}
