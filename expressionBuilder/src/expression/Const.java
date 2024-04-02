package expression;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Const implements ExpressionObject {
    public final int value;
    public final BigDecimal BDvalue;
    public Const(int value) {
        this.value = value;
        BDvalue = new BigDecimal(value);
    }
    public Const(BigDecimal value) {
        this.value = value.intValue();
        BDvalue = value;
    }

    @Override
    public String toString(){
        return BDvalue.toString();
    }

    @Override
    public int evaluate(int x) {
        return value;
    }

    @Override
    public int getPrior() {
        return 0;
    }

    @Override
    public int getDepend() {
        return 0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == this.getClass()){
            Const c = (Const) obj;
            return this.getValue() == c.getValue();
        }
        return false;
    }

    @Override
    public String toMiniString() {
        return BDvalue.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return BDvalue;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return value;
    }
}
