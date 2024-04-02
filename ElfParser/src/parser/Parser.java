package parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Parser {
    private final File elfLocation;
    private byte[] array;
    private FileInputStream fis;
    private int size;

    public Parser(File elfLocation) throws Exception{
        this.elfLocation = elfLocation;
        fis = new FileInputStream(elfLocation);
        size = fis.available();
        array = new byte[size];
    }

    public void parse() throws Exception {
        fis.read(array);
        ByteBuffer bb = ByteBuffer.wrap(array);
        System.out.println(new String(array));


    }
}
