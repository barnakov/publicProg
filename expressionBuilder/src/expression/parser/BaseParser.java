package expression.parser;

public class BaseParser {
    public static final char END = '\0';
    protected CharSource source;
    private int index = 0;
    private char ch = 0xffff;

    public BaseParser(CharSource source) {
        this.source = source;
        take();
    }

    protected boolean takeWhiteSpace(){
        if(Character.isWhitespace(ch)){
            take();
            return true;
        } else {
            return false;
        }
    }

    public boolean is(char expected){
        return expected == ch;
    }

    public boolean between(char min, char max){
        return min <= ch && ch <= max;
    }

    public int getIndex() {
        return index;
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        index++;
        return result;
    }
    protected boolean take(char expected){
        if(ch == expected){
            take();
            return true;
        } else {
            return false;
        }
    }
}
