package expression;

import java.util.Objects;

public abstract class AbstractBinary implements ExpressionBinary {
    public final ExpressionObject first;
    public final ExpressionObject second;
    public AbstractBinary(ExpressionObject first, ExpressionObject second) {
        this.first = first;
        this.second = second;
    }


    @Override
    public String toString(){
        return "(" + first.toString() + " " + this.getSign() + " " + second.toString() + ")";
    }


    @Override
    public String toMiniString(){
        StringBuilder sb = new StringBuilder();

        if(this.needLeftBrackets()){
            sb.append("(").append(first.toMiniString()).append(")");
        } else {
            sb.append(first.toMiniString());
        }

        sb.append(" ").append(this.getSign()).append(" ");

        if(this.needRightBrackets()){
            sb.append("(").append(second.toMiniString()).append(")");
        } else {
            sb.append(second.toMiniString());
        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj.getClass() == this.getClass()){
            ExpressionBinary eb = (ExpressionBinary) obj;
            return first.equals(eb.getFirst()) && second.equals(eb.getSecond());
        }
        return false;
    }

    @Override
    public ExpressionObject getFirst(){
        return first;
    }

    @Override
    public ExpressionObject getSecond(){
        return second;
    }


    @Override
    public int hashCode(){
        return Objects.hash(first, second, this.getSign());
    }

}
