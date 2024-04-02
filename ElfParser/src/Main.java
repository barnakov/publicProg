import parser.Parser;

import java.io.*;

public class Main {
    public static void main(String[] args){
        try {
            Parser parser = new Parser(new File("test_elf"));
            parser.parse();
        } catch (Exception e) {
            throw new RuntimeException("some problems in file");
        }

    }
}
