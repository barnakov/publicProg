import expression.*;
import expression.exceptions.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ExpressionParser ex = new ExpressionParser();
        ToMiniString toMiniString = ex.parse("1 + 2)");
        System.out.println(toMiniString);
    }

    // overflow (a $ b) - a > 0 && b > 0, a $ b < 0
    // or a < 0 && b < 0, a $ b > 0


// (x + x)
}