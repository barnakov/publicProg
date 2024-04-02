package parser;

import javax.sound.midi.Soundbank;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class SectionParser {
    private final ByteBuffer buffer;
    private final String sectionName;
    private final PrintWriter out;

    public SectionParser(ByteBuffer buffer, String sectionName, PrintWriter out) {
        this.buffer = buffer;
        this.sectionName = sectionName;
        this.out = out;
    }

    protected ArrayList<String> parseText() {
        getSection(".text");
        ArrayList<String> list = new ArrayList<>();
        list.add(".text\n");
        for (int i = 0; i < Constants.text_size; i += 0x4) {
            buffer.position(Constants.text_offset + i);
            list.add(getInstruction());
        }
        return list;
    }

    protected String getInstruction() {
        int pos = buffer.position() + 0x10000;
        int inst = buffer.getInt();
        String s = Integer.toBinaryString(inst);
        StringBuilder sb = new StringBuilder();
        while (s.length() + sb.length() < 32) {
            sb.append(0);
        }
        sb.append(s);
        s = sb.toString();
        if (s.startsWith("01101", 25)) {
            String rd = s.substring(20, 25);
            String imm = s.substring(0, 20);
            return twoArgInst(pos, inst, "lui", getRegister(Integer.parseInt(rd, 2)), "0x" + Integer.toHexString(Integer.parseInt(imm, 2)));
        } else if (s.startsWith("00101", 25)) {
            String rd = s.substring(20, 25);
            String imm = s.substring(0, 20);
            return twoArgInst(pos, inst, "auipc", getRegister(Integer.parseInt(rd, 2)), Integer.toHexString(Integer.parseInt(imm, 2)));
        } else if (s.startsWith("00100", 25)) {
            String instName = "";
            String rs1 = s.substring(12, 17);
            String rd = s.substring(20, 25);
            int imm = 0;
            if (s.startsWith("000", 17)) {
                instName = "addi";
                imm = inst >> 20;
            }
            if (s.startsWith("010", 17)) {
                instName = "slti";
                imm = inst >> 20;
            }
            if (s.startsWith("011", 17)) {
                instName = "sltui";
                imm = inst >> 20;
            }
            if (s.startsWith("100", 17)) {
                instName = "xori";
                imm = inst >> 20;
            }
            if (s.startsWith("110", 17)) {
                instName = "ori";
                imm = inst >> 20;
            }
            if (s.startsWith("111", 17)) {
                instName = "andi";
                imm = inst >> 20;
            }
            if (s.startsWith("001", 17)) {
                instName = "slli";
                imm = (inst >> 20) & ((1 << 6) - 1);
            }
            if (s.startsWith("101", 17)) {
                if (s.startsWith("00000")) {
                    instName = "srli";
                }
                if (s.startsWith("01000")) {
                    instName = "srai";
                }
                imm = (inst >> 20) & ((1 << 6) - 1);
            }

            return threeArgInst(pos, inst, instName, getRegister(Integer.parseInt(rd, 2)),
                    getRegister(Integer.parseInt(rs1, 2)), Integer.toString(imm));
        } else if (s.startsWith("01100", 25)) {
            String instName = "";
            String rd = s.substring(20, 25);
            String rs1 = s.substring(12, 17);
            String rs2 = s.substring(7, 12);
            if (s.startsWith("000", 17)) {
                if (s.startsWith("00000")) {
                    if (s.startsWith("00", 5)) instName = "add";
                    else instName = "mul";
                }
                if (s.startsWith("01000")) instName = "sub";
            } else if (s.startsWith("001", 17)) {
                if (s.startsWith("00", 5)) instName = "sll";
                else instName = "mulh";
            } else if (s.startsWith("010", 17)) {
                if (s.startsWith("00", 5)) instName = "slt";
                else instName = "mulhsu";
            } else if (s.startsWith("011", 17)) {
                if (s.startsWith("00", 5)) instName = "sltu";
                else instName = "mulhu";
            }
            else if (s.startsWith("100", 17)) {
                if (s.startsWith("00", 5)) instName = "xor";
                else instName = "div";
            }
            else if (s.startsWith("101", 17)) {
                if (s.startsWith("00", 5)) {
                    if (s.startsWith("00000")) instName = "srl";
                    if (s.startsWith("01000")) instName = "sra";
                }
                else instName = "divu";
            } else if (s.startsWith("110", 17)) {
                if (s.startsWith("00", 5)) instName = "or";
                else instName = "rem";
            }
            else if (s.startsWith("111", 17)) {
                if (s.startsWith("00", 5)) instName = "and";
                else instName = "remu";
            }
            return threeArgInst(pos, inst, instName, getRegister(Integer.parseInt(rd, 2)),
                    getRegister(Integer.parseInt(rs1, 2)), getRegister(Integer.parseInt(rs2, 2)));
        } else if (s.startsWith("00011", 25)) {
            String instName = "";
            if (s.startsWith("000", 17)) {
                String pred = s.substring(4, 8);
                StringBuilder pr = new StringBuilder();
                StringBuilder sc = new StringBuilder();
                String succ = s.substring(8, 12);
                String iorw = "iorw";
                for (int i = 0; i < 4; ++i) {
                    if (pred.charAt(i) == '1') pr.append(iorw.charAt(i));
                }
                for (int i = 0; i < 4; ++i) {
                    if (succ.charAt(i) == '1') sc.append(iorw.charAt(i));
                }
                if (pr.toString().equals("i") && sc.isEmpty()) {
                    return noArgs(pos, inst, "pause");
                } else {
                    return fence(pos, inst, "fence", pr.toString(), sc.toString());
                }
            } else {
                return noArgs(pos, inst, "fence.i");
            }
        } else if (s.startsWith("11100", 25)) {
            if (s.startsWith("000", 17)) {
                if (s.startsWith("00000", 7)) return noArgs(pos, inst, "ecall");
                if (s.startsWith("00001", 7)) return noArgs(pos, inst, "ebreak");
                if (s.startsWith("00010", 7)) {
                    if (s.startsWith("00000")) return noArgs(pos, inst, "uret");
                    if (s.startsWith("00010")) return noArgs(pos, inst, "sret");
                    if (s.startsWith("00110")) return noArgs(pos, inst, "mret");
                }
                if (s.startsWith("00101", 7)) {
                    if (s.startsWith("00", 5)) out.print(noArgs(pos, inst, "wfi"));
                    if (s.startsWith("01", 5)) {
                        String rd = s.substring(20, 25);
                        String rs1 = s.substring(12, 17);
                        String rs2 = s.substring(7, 12);
                        return threeArgInst(pos, inst, "sfence.vma", getRegister(Integer.parseInt(rd, 2)),
                                getRegister(Integer.parseInt(rs1, 2)),
                                getRegister(Integer.parseInt(rs2, 2)));
                    }
                }

            }
        } else if (s.startsWith("00000", 25)) {
            String instName = "";
            String offset = s.substring(0, 12);
            String rd = s.substring(20, 25);
            String rs1 = s.substring(12, 17);
            if (s.startsWith("000", 17)) instName = "lb";
            if (s.startsWith("001", 17)) instName = "lh";
            if (s.startsWith("010", 17)) instName = "lw";
            if (s.startsWith("100", 17)) instName = "lbu";
            if (s.startsWith("101", 17)) instName = "lhu";
            return lsJalr(pos, inst, instName, getRegister(Integer.parseInt(rd, 2)),
                    Integer.parseInt(offset, 2),
                    getRegister(Integer.parseInt(rs1, 2)));

        } else if (s.startsWith("01000", 25)) {
            String instName = "";
            String offset1 = s.substring(0, 7);
            String offset2 = s.substring(20, 25);
            String offset = offset1 + offset2;
            String rs1 = s.substring(12, 17);
            String rs2 = s.substring(7, 12);
            if (s.startsWith("000", 17)) instName = "sb";
            if (s.startsWith("001", 17)) instName = "sh";
            if (s.startsWith("010", 17)) instName = "sw";
            return lsJalr(pos, inst, instName,
                    getRegister(Integer.parseInt(rs2, 2)),
                    Integer.parseInt(offset, 2),
                    getRegister(Integer.parseInt(rs1, 2)));
        } else if (s.startsWith("11011", 25)) {
            String offset = s.substring(0, 20);
            String rd = s.substring(20, 25);
            String lable = "";
            if (Constants.lables.containsKey((int) Long.parseLong(getImmJ(offset), 2) + pos)) {
                lable = Constants.lables.get((int) Long.parseLong(getImmJ(offset), 2) + pos);
            } else {
                lable = "L" + (Constants.lables.size() - Constants.map_size);
                Constants.lables.put((int) Long.parseLong(getImmJ(offset), 2) + pos, lable);
            }
            return instJ(pos, inst, "jal", getRegister(Integer.parseInt(rd, 2)),
                    (int) Long.parseLong((getImmJ(offset)), 2) + pos, lable);
        } else if (s.startsWith("11001", 25)) {
            String offset = s.substring(0, 12);
            String rd = s.substring(20, 25);
            String rs1 = s.substring(12, 17);
            return lsJalr(pos, inst, "jalr", getRegister((Integer.parseInt(rd, 2))),
                    Integer.parseInt(offset, 2),
                    getRegister(Integer.parseInt(rs1, 2)));
        } else if (s.startsWith("11000", 25)) {
            String instName = "";
            String offset = s.substring(0, 7) + s.substring(20, 25);
            String rs1 = s.substring(12, 17);
            String rs2 = s.substring(7, 12);
            String lable = "";
            if (Constants.lables.containsKey((int) Long.parseLong(getImmB(offset), 2) + pos)) {
                lable = Constants.lables.get((int) Long.parseLong(getImmB(offset), 2) + pos);
            } else {
                lable = "L" + (Constants.lables.size() - Constants.map_size);
                Constants.lables.put((int) Long.parseLong(getImmB(offset), 2) + pos, lable);
            }
            if (s.startsWith("000", 17)) instName = "beq";
            if (s.startsWith("001", 17)) instName = "bne";
            if (s.startsWith("100", 17)) instName = "blt";
            if (s.startsWith("101", 17)) instName = "bge";
            if (s.startsWith("110", 17)) instName = "bltu";
            if (s.startsWith("110", 17)) instName = "bltu";
            if (s.startsWith("111", 17)) instName = "bgeu";
            return instB(pos, inst, instName, getRegister(Integer.parseInt(rs1, 2)),
                    getRegister(Integer.parseInt(rs2, 2)),
                    (int) Long.parseLong(getImmB(offset), 2) + pos,
                    lable);


        } else if (s.startsWith("01011", 25)){
            if(s.startsWith("00010")){
                String instName = "lr.w";
                String rd = s.substring(20, 25);
                String rs1 = s.substring(12, 17);
                return twoArgInst(pos, inst, instName, getRegister(Integer.parseInt(rd, 2)),
                        getRegister(Integer.parseInt(rs1, 2)));
            } else {
                String instName = "";
                String rd = s.substring(20, 25);
                String rs1 = s.substring(12, 17);
                String rs2 = s.substring(7, 12);
                if(s.startsWith("00011"))
                    return threeArgInst(pos, inst, "sc.w", getRegister(Integer.parseInt(rd, 2)),
                            getRegister(Integer.parseInt(rs1, 2)),
                            getRegister(Integer.parseInt(rs2, 2)));
                if(s.startsWith("00001")) instName = "amoswap.w";
                if(s.startsWith("00000")) instName = "amoadd.w";
                if(s.startsWith("00100")) instName = "amoxor.w";
                if(s.startsWith("01100")) instName = "amoand.w";
                if(s.startsWith("01000")) instName = "amoor.w";
                if(s.startsWith("10000")) instName = "amomin.w";
                if(s.startsWith("10100")) instName = "amomax.w";
                if(s.startsWith("11000")) instName = "amominu.w";
                if(s.startsWith("11100")) instName = "amomaxu.w";
                return dotW(pos, inst, instName, getRegister(Integer.parseInt(rd, 2)),
                        getRegister(Integer.parseInt(rs1, 2)),
                        getRegister(Integer.parseInt(rs2, 2)));
            }
        }
        return invalid(pos, inst);
    }


    protected String dotW(int addr, int hex, String name, String arg1, String arg2, String arg3){
        String format = "   %05x:\t%08x\t%7s\t%s, %s, (%s)\n";
        return String.format(format, addr, hex, name, arg1, arg2, arg3);
    }
    protected String lsJalr(int addr, int hex, String name, String arg1, int arg2, String arg3) {
        String format = "   %05x:\t%08x\t%7s\t%s, %d(%s)\n";
        return String.format(format, addr, hex, name, arg1, arg2, arg3);
    }

    protected String instB(int addr, int hex, String inst, String arg1, String arg2, int arg3, String arg4) {
        String format = "   %05x:\t%08x\t%7s\t%s, %s, 0x%x, <%s>\n";
        return String.format(format, addr, hex, inst, arg1, arg2, arg3, arg4);
    }

    protected String instJ(int addr, int hex, String inst, String arg1, int arg2, String arg3) {
        String format = "   %05x:\t%08x\t%7s\t%s, 0x%x <%s>\n";
        return String.format(format, addr, hex, inst, arg1, arg2, arg3);
    }

    protected String invalid(int addr, int hex) {
        String format = "   %05x:\t%08x\t%-7s\n";
        return String.format(format, addr, hex, "invalid_instruction");
    }

    protected String fence(int addr, int hex, String inst, String pred, String succ) {
        String format = "   %05x:\t%08x\t%7s\t%s, %s\n";
        return String.format(format, addr, hex, inst, pred, succ);
    }

    protected String noArgs(int addr, int hex, String inst) {
        String format = "   %05x:\t%08x\t%7s\n";
        return String.format(format, addr, hex, inst);
    }

    protected String twoArgInst(int addr, int hex, String inst, String arg1, String arg2) {
        String format = "   %05x:\t%08x\t%7s\t%s, %s\n";
        return String.format(format, addr, hex, inst, arg1, arg2);
    }

    protected String threeArgInst(int addr, int hex, String inst, String arg1, String arg2, String arg3) {
        String format = "   %05x:\t%08x\t%7s\t%s, %s, %s\n";
        return String.format(format, addr, hex, inst, arg1, arg2, arg3);
    }

    protected String getImmB(String offset) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<20; ++i){
            sb.append(offset.charAt(0));
        }
        sb.append(offset.charAt(11)).append(offset, 1, 7)
                .append(offset, 7, 11).append(0);
        return sb.toString();
    }

    protected String getImmJ(String offset) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<11; ++i){
            sb.append(offset.charAt(0));
        }
        sb.append(offset, 11, 20).append(offset.charAt(11))
                .append(offset, 1, 11).append(0);
        return sb.toString();
    }

    protected String getRegister(int register) {
        switch (register) {
            case 0:
                return "zero";
            case 1:
                return "ra";
            case 2:
                return "sp";
            case 3:
                return "gp";
            case 4:
                return "tp";
            case 5:
                return "t0";
            case 6:
                return "t1";
            case 7:
                return "t2";
            case 8:
                return "s0";
            case 9:
                return "s1";
            case 10:
                return "a0";
            case 11:
                return "a1";
            case 12:
                return "a2";
            case 13:
                return "a3";
            case 14:
                return "a4";
            case 15:
                return "a5";
            case 16:
                return "a6";
            case 17:
                return "a7";
            case 18:
                return "s2";
            case 19:
                return "s3";
            case 20:
                return "s4";
            case 21:
                return "s5";
            case 22:
                return "s6";
            case 23:
                return "s7";
            case 24:
                return "s8";
            case 25:
                return "s9";
            case 26:
                return "s10";
            case 27:
                return "s11";
            case 28:
                return "t3";
            case 29:
                return "t4";
            case 30:
                return "t5";
            case 31:
                return "t6";
            default:
                return "Unknown";
        }
    }

    protected ArrayList<String> parseSymtab() {
        ArrayList<String> list = new ArrayList<>();
        list.add(".symtab\n\nSymbol Value              Size Type     Bind     Vis       Index Name");
        getSection(".symtab");
        getSection(".strtab");
        for (int i = 0; i < Constants.symtab_size; i += 0x10) {
            buffer.position(Constants.symtab_offset + i);
            int st_name = buffer.getInt();
            int st_value = buffer.getInt();
            int st_size = buffer.getInt();
            int st_info = buffer.get();
            int st_other = buffer.get();
            short st_shndx = buffer.getShort();
            if (getType(st_info & 0xf).equals("FUNC")) {
                Constants.lables.put(st_value, getName(st_name));
            }
            String formattedString = String.format("[%4d] 0x%-15X %5d %-8s %-8s %-8s %6s %s",
                    i / 0x10, st_value, st_size, getType(st_info & 0xf), getBind(st_info >> 4)
                    , getVis(st_other), getIndex(st_shndx), getName(st_name));
            list.add(formattedString);
        }
        return list;
    }

    protected String getName(int st_name) {
        buffer.position(Constants.strtab_offset + st_name);
        StringBuilder sb = new StringBuilder();
        byte sym;
        while ((sym = buffer.get()) != 0) {
            sb.append((char) sym);
        }
        return sb.toString();
    }

    protected String getIndex(short st_index) {
        switch (st_index) {
            case 0:
                return "UNDEF";
            case (short) 0xff00:
                return "LOPROC";
            case (short) 0xff1f:
                return "HIPROC";
            case (short) 0xff20:
                return "LOOS";
            case (short) 0xff3f:
                return "HIOS";
            case (short) 0xfff1:
                return "ABS";
            case (short) 0xfff2:
                return "COMMON";
            case (short) 0xffff:
                return "HIRESERVE";
            default:
                return Integer.toString(st_index);
        }
    }

    protected String getBind(int st_bind) {
        switch (st_bind) {
            case 0:
                return "LOCAL";
            case 1:
                return "GLOBAL";
            case 2:
                return "WEAK";
            case 10:
                return "LOOS";
            case 12:
                return "HIOS";
            case 13:
                return "LOPROC";
            case 15:
                return "HIPROC";
            default:
                return "UNKNOWN";
        }
    }

    protected String getVis(int st_other) {
        switch (st_other) {
            case 0:
                return "DEFAULT";
            case 1:
                return "INTERNAL";
            case 2:
                return "HIDDEN";
            case 3:
                return "PROTECTED";
            default:
                return "UNKNOWN";
        }
    }

    protected String getType(int st_type) {
        switch (st_type) {
            case 0:
                return "NOTYPE";
            case 1:
                return "OBJECT";
            case 2:
                return "FUNC";
            case 3:
                return "SECTION";
            case 4:
                return "FILE";
            case 5:
                return "COMMON";
            case 6:
                return "TLS";
            case 10:
                return "LOOS";
            case 12:
                return "HIOS";
            case 13:
                return "LOPROC";
            case 15:
                return "HIPROC";
            default:
                return "UNKNOWN";
        }
    }


    protected void getSection(final String name) {
        HeaderParser headerParser = new HeaderParser(buffer);
        int offset = headerParser.getOffset();
        int num = headerParser.getSectionNum();
        int shstrndx = headerParser.getSectionStrNdx();
        int secSize = 0;
        int secOff = 0;
        for (int i = 1; i < num; ++i) {
            buffer.position(offset + 0x28 * i);
            int nameOff = buffer.getInt();
            String tmpName = getSectionName(offset, shstrndx, nameOff);
            if (tmpName.equals(name)) {
                buffer.position(offset + 0x28 * i + Constants.sh_size);
                secSize = buffer.getInt();
                buffer.position(offset + 0x28 * i + Constants.sh_offset);
                secOff = buffer.getInt();
                break;
            }
        }
        if (name.equals(".symtab")) {
            Constants.symtab_offset = secOff;
            Constants.symtab_size = secSize;
        } else if (name.equals(".strtab")) {
            Constants.strtab_offset = secOff;
            Constants.strtab_size = secSize;
        } else if (name.equals(".text")) {
            Constants.text_offset = secOff;
            Constants.text_size = secSize;
        }

    }

    public void getNames() {
        HeaderParser headerParser = new HeaderParser(buffer);
        int offset = headerParser.getOffset();
        int num = headerParser.getSectionNum();
        int shstrndx = headerParser.getSectionStrNdx();
        int pos = offset + shstrndx * 0x28 + 0x10;
        buffer.position(pos);
//        buffer.position(pos + 0x10);
        int dataOff = buffer.getInt();
        buffer.position(offset + shstrndx * 0x28 + 0x14);
        int dataSize = buffer.getInt();
        buffer.position(dataOff);
        for (int i = 0; i < dataSize; ++i) {
            System.out.print((char) buffer.get());
        }

//        int dataOff = getInt(pos);
//        System.out.println(dataOff);
    }

    private String getSectionName(int offset, int shstrndx, int nameoffset) {
        buffer.position(offset + 0x28 * shstrndx + Constants.sh_offset);
        buffer.position(buffer.getInt() + nameoffset);
        byte sym;
        StringBuilder sb = new StringBuilder();
        while ((sym = buffer.get()) != 0) {
            sb.append((char) sym);
        }
        return sb.toString();
    }
}
