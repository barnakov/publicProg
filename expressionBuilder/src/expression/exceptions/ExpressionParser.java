package expression.exceptions;


import expression.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringCharSource;
import expression.parser.TripleParser;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class ExpressionParser implements TripleParser {
    public static TripleExpression parse(final CharSource source){
        return new Parser(source).parse();
    }


    public TripleExpression parse(final String string){
//        System.err.println("====================================");
//        System.err.println(string);
//        TripleExpression te = parse(new StringCharSource(string));
//        System.err.println(te.toMiniString() + " |||||| " + te);
//        System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        return parse(new StringCharSource(string));
    }

    public ListExpression parse(final String string, final List<String> variables){
//        System.err.println("====================================");
//        System.err.println(string);
//        ListExpression te = new Parser(new StringCharSource(string), variables).parse();
//        System.err.println(te.toMiniString() + " |||||| " + te);
//        System.err.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        return new Parser(new StringCharSource(string), variables).parse();
    }

    private static class Parser extends BaseParser {

        private boolean neg = false;
        private boolean list = false;
        private int balance = 0;
        private Deque<Character> brackets;

        private List<String> variables;

        public Parser(CharSource source) {
            super(source);
            brackets = new ArrayDeque<>();
        }

        public Parser(CharSource source, List<String> variables){
            super(source);
            this.variables = variables;
            brackets = new ArrayDeque<>();
            list = true;
        }

        public ExpressionObject parse() {
            return parseExpression();
        }

        //    expr : plusminus* EOF ;
        private ExpressionObject parseExpression() {
            skipWhiteSpace();
            ExpressionObject result = parseShifts();
            skipWhiteSpace();
            if(take(')')){
                balance--;
                if(brackets.isEmpty()){
                    throw new NotCorrectOrderOfBracketsException("Miss some opening parenthesis on index " + getIndex());
                }
                char c = brackets.getLast();
                if(balance < 0 || c != '(')
                    throw new NotCorrectOrderOfBracketsException("Miss one or more open brackets on index " + getIndex());
                brackets.pollLast();
            } else if(take('}')){
                balance--;
                char c = brackets.getLast();
                if(balance < 0 || c != '{')
                    throw new NotCorrectOrderOfBracketsException("Miss one or more open brackets on index " + getIndex());
                brackets.pollLast();
            } else if(take(']')){
                balance--;
                char c = brackets.getLast();
                if(balance < 0 || c != '[')
                    throw new NotCorrectOrderOfBracketsException("Miss one or more open brackets on index " + getIndex());
                brackets.pollLast();
            } else if(take(END) && balance > 0){
                throw new NotCorrectOrderOfBracketsException("Miss one or more close brackets on index " + getIndex());
            }
            return result;
        }

        // shifts: minmax ( ( '>>' | '<<' | '>>>' ) minmax )*
        // у шифтов приоритет такой же, как у минимума и максимума
        // Объяснение, да это правда, но если например нам больше не нужна это операция, то мы можем просто удалить
        // функцию и изменить ровно одну строчку кода. А если поместить их в одну строчку, то придётся переписывать
        // в разы больше кода

        private ExpressionObject parseShifts(){
            ExpressionObject result = parseMinMax();
            do{
                skipWhiteSpace();
                if(take('>')){
                    if(take('>')){
                        if(take('>')){
                            skipWhiteSpace();
                            result = new ArithmeticShift(result, parseMinMax());
                            skipWhiteSpace();
                        } else{
                            skipWhiteSpace();
                            result = new RightShift(result, parseMinMax());
                            skipWhiteSpace();
                        }
                    } else{
                        throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                    }
                } else if(take('<')){
                    if(take('<')){
                        skipWhiteSpace();
                        result = new LeftShift(result, parseMinMax());
                        skipWhiteSpace();
                    } else {
                        throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                    }
                } else{
                  break;
                }
            }while(!take(END));

            return result;
        }

        //  minmax: plusminus ( ( 'min' | 'max ) plusminus )*

        private ExpressionObject parseMinMax(){
            ExpressionObject result = parsePlusMinus();
            do{
                skipWhiteSpace();
                if(take('m')){
                    if(take('a')){
                        if(take('x')){
                            if(!takeWhiteSpace() && !take(END) && !is(')') && !is('+')
                                    && !is('-') && !is('*') && !is('/') && !is('(')){
                                throw new MissArgumentException("No space on index " + getIndex());
                            }
                            skipWhiteSpace();
                            result = new Max(result, parsePlusMinus());
                            skipWhiteSpace();
                        } else{
                            throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                        }
                    } else if(take('i')){
                        if(take('n')){
                            if(!takeWhiteSpace() && !take(END) && !is(')') && !is('+')
                                    && !is('-') && !is('*') && !is('/') && !is('(')){
                                throw new MissArgumentException("No space on index " + getIndex());
                            }
                            skipWhiteSpace();
                            result = new Min(result, parsePlusMinus());
                            skipWhiteSpace();
                        } else{
                            throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                        }
                    } else{
                        throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                    }
                } else if (between('a', 'z') || between('A', 'Z')){
                    throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                } else {
                    break;
                }
            }while(!take(END));
            return result;
        }

        //    plusminus: multdiv ( ( '+' | '-' ) multdiv )* ;
        private ExpressionObject parsePlusMinus() {
            ExpressionObject result = parseMultDiv();
            do {
                skipWhiteSpace();
                if(take('+')){
                    skipWhiteSpace();
                    result = new CheckedAdd(result, parseMultDiv());
                    skipWhiteSpace();
                } else if(take('-')){
                    skipWhiteSpace();
                    result = new CheckedSubtract(result, parseMultDiv());
                    skipWhiteSpace();
                } else {
                    break;
                }
                skipWhiteSpace();
            } while(!take(END));
            return result;
        }

        //    multdiv : factor ( ( '*' | '/' ) factor )* ;
        private ExpressionObject parseMultDiv() {
            ExpressionObject result = parseFactor();
            do {
                skipWhiteSpace();
                if(take('*')){
                    skipWhiteSpace();
                    result = new CheckedMultiply(result, parseFactor());
                    skipWhiteSpace();
                } else if(take('/')){
                    skipWhiteSpace();
                    result = new CheckedDivide(result, parseFactor());
                    skipWhiteSpace();
                } else if(!is('+') && !is('-') && !is(')') && !is(END) && !between('a', 'z')
                        && !between('A', 'Z') && !is('<') && !is('>') && !is(']') && !is('}')) {
                    throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                } else {
                    break;
                }
                skipWhiteSpace();
            } while(!take(END));

            return result;
        }

        //    factor : NEGATE
        private ExpressionObject parseFactor(){
            if(take('(')){
                balance++;
                brackets.add('(');
                return parseExpression();
            } else if(take('[')){
                balance++;
                brackets.add('[');
                return parseExpression();
            } else if(take('{')){
                balance++;
                brackets.add('{');
                return parseExpression();
            } else if(between('0', '9')){
                return parseNumber();
            } else if(between('a', 'z') || between('A', 'Z') || is('$')){
                return parseString();
            } else if(take('-')){
                return parseNegate();
            } else if(take(')')){
                throw new ArgumentsInBracketsException("Empty or not so much arguments between brackets on index " + getIndex());
            } else if(take(']')){
                throw new ArgumentsInBracketsException("Empty or not so much arguments between brackets on index " + getIndex());
            } else if(take('}')){
                throw new ArgumentsInBracketsException("Empty or not so much arguments between brackets on index" + getIndex());
            } else if(take(END)){
                throw new MissArgumentException("Not completed expression");
            } else if(!takeWhiteSpace()){
                throw new MissArgumentException(String.format("On index %s symbol %s in not correct argument", getIndex(), take()));
            }
            return null;
        }

        private ExpressionObject parseString() {
            StringBuilder sb = new StringBuilder();
            boolean takeDollar = false;
            do{
                if((!is('x') && !is('y') && !is('z') && !list) || (!between('0', '9') && takeDollar &&  !is('$') && list)){
                    throw new UnknownSymbolException(String.format("On index %s unknown symbol %s", getIndex(), take()));
                }
                if(is('$')) takeDollar = true;
                sb.append(take());
            }while(between('a', 'z') || between('A', 'Z') || between('0', '9') || is('$'));
            skipWhiteSpace();
            if(sb.charAt(0) == '$'){
                return new Variable(Integer.parseInt(sb.substring(1)));
            }
            return new Variable(sb.toString());
        }

        private ExpressionObject parseNumber(){
            StringBuilder sb = new StringBuilder();
            if(neg){
                sb.append('-');
                neg = false;
            }
            do{
                sb.append(take());
            }while(between('0', '9'));
            checkOverflow(sb);
            if(!takeWhiteSpace() && !take(END) && !is(')') && !is('+') && !is('-')
                    && !is('*') && !is('/') && !is('}') && !is(']')){
                throw new UnknownSymbolException("No space on index " + getIndex());
            }
            skipWhiteSpace();
            return new Const(Integer.parseInt(sb.toString()));
        }

        // NEGATE : ( '-' )  '(' factor ')';

        private ExpressionObject parseNegate(){
            if(takeWhiteSpace()){
                skipWhiteSpace();
                return new CheckedNegate(parseFactor());
            }
            if(take('(')){
                brackets.add('(');
                balance++;
                return new CheckedNegate(parseExpression());
            }
            if(take('{')){
                brackets.add('{');
                balance++;
                return new CheckedNegate(parseExpression());
            }
            if(take('[')){
                brackets.add('[');
                balance++;
                return new CheckedNegate(parseExpression());
            }
            if(between('a', 'z') || between('A', 'Z') || is('$')){
                return new CheckedNegate(parseString());
            }
            neg = true;
            return parseFactor();
        }

        public void checkOverflow(StringBuilder numberToCheck){
            StringBuilder numberWhichCheck;
            if(numberToCheck.charAt(0) == '-'){
                numberWhichCheck = new StringBuilder(Integer.toString(Integer.MIN_VALUE));
            } else {
                numberWhichCheck = new StringBuilder(Integer.toString(Integer.MAX_VALUE));
            }
            if(numberToCheck.length() > numberWhichCheck.length() || numberToCheck.compareTo(numberWhichCheck) > 0
                    && numberToCheck.length() == numberWhichCheck.length()){
                throw new OverflowException();
            }
        }

        private void skipWhiteSpace() {
            while(takeWhiteSpace()){}
        }
    }
}
