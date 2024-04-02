import parser.Parser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2){
            System.err.println("Not correct program arguments");
        }

        Parser parser = new Parser(args[0], args[1]);
        try {
            parser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}