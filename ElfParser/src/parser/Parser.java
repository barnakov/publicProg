package parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Parser {
    private final String in;
    private final String out;

    public Parser(String in, String out) {
        this.in = in;
        this.out = out;
    }

    public void parse() throws IOException {
        File file = new File(in);
        byte[] arr;
        try (FileInputStream fis = new FileInputStream(file)) {
            arr = new byte[(int) file.length()];
            fis.read(arr);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(arr);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        HeaderParser headerParser = new HeaderParser(byteBuffer);

        PrintWriter writer = new PrintWriter(new FileWriter(out));
        SectionParser sectionParser = new SectionParser(byteBuffer, "", writer);
        ArrayList<String> sym = sectionParser.parseSymtab();
        Constants.map_size = Constants.lables.size();
        ArrayList<String> txt = sectionParser.parseText();
        writer.print(txt.get(0));
        for(int i=1; i<txt.size(); ++i){
            String s = txt.get(i);
            int k = Integer.parseInt(s.substring(3, 8), 16);
            if(Constants.lables.containsKey(k)){
                String format = "\n%08x \t<%s>:\n";
                writer.print(String.format(format, k,
                        Constants.lables.get(k)));
                writer.flush();
            }
            writer.print(s);
            writer.flush();
        }

        writer.flush();
        writer.println();
        writer.println();
        writer.println(sym.get(0));
        for(int i = 1; i < sym.size(); ++i){
            writer.println(sym.get(i));
            writer.flush();
        }
    }
}
