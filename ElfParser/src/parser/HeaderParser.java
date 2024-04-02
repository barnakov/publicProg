package parser;

import java.nio.ByteBuffer;

public class HeaderParser {
    private final ByteBuffer buffer;

    public HeaderParser(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    protected int getOffset() {
        buffer.position(Constants.e_shoff);
        return buffer.getInt();
    }

    protected short getSectionNum() {
        buffer.position(Constants.e_shnum);
        return buffer.getShort();
    }

    protected short getSectionStrNdx() {
        buffer.position(Constants.e_shstrndx);
        return buffer.getShort();
    }

}
