package expression;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Variable implements ExpressionObject {
    public final String name;
    public final int index;

    public Variable(String name) {
        this.name = name;
        index = -1;
    }

    public Variable(int index) {
        this.name = "$" + index;
        this.index = index;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public int getPrior() {
        return 0;
    }

    @Override
    public int getDepend() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == this.getClass()){
            Variable v = (Variable) obj;
            return this.toString().equals(v.toString());
        }
        return false;
    }


    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (name) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> 0;
        };
    }

    @Override
    public BigDecimal evaluate(BigDecimal x) {
        return x;
    }

    @Override
    public int evaluate(List<Integer> variables) {
        if (index >= variables.size()){
            throw new IndexOutOfBoundsException(index);
        }
        return variables.get(index);
    }
}
