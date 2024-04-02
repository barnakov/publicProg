package expression;

public abstract class AbstractUnary implements ExpressionUnary {
    public final ExpressionObject eo;
    public AbstractUnary(ExpressionObject eo){
        this.eo = eo;
    }

    @Override
    public String toMiniString(){
        if(eo.getPrior() < getPrior() && eo.getPrior() != 0)
            return getSign() + "(" + eo.toMiniString() + ")";
//        if(eo.getPrior() == 0 && eo.evaluate(0) == 0)
//            return getSign() + "(" + eo.toMiniString() + ")";
        return getSign() + " " + eo.toMiniString();
    }

    @Override
    public String toString(){
        return getSign() + "(" +  eo.toString() + ")";
    }

}
