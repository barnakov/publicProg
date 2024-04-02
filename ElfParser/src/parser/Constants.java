package parser;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    // Elf Header
    static final int e_shoff = 0x20;
    static final int e_shentsize = 0x2E;
    static final int e_shnum = 0x30;
    static final int e_shstrndx = 0x32;

    // Section header
    static final int sh_name = 0x00;
    static final int sh_offset = 0x10;
    static final int sh_size = 0x14;

    // Symtab
    static int symtab_size;
    static int symtab_offset;

    // Strtab
    static int strtab_size;
    static int strtab_offset;

    // Text
    static int text_size;
    static int text_offset;

    static Map<Integer, String> lables = new HashMap<>();
    static int map_size;
}
