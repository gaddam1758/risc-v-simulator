package assembler;

import java.io.Serializable;
import java.util.*;

public class primary_memory implements Serializable{

    //
    public static Map<Integer, String> mem;
    //private  String[] memory;
    int stack_start = 0x7FFFFFF0;//2^28-3-1
    final int heap_start = 0x10007FE8;//2^28
    final int instruction_start = 0;//no reserved,obvio !!
    public int data_start = 0x10000000;
    public int[] register;
    public String ir, irt;
    public int ra, rb, rm, rx, ry, rz, pc, iv;
    public int rat, rbt, rmt, rxt, ryt, rzt, pct, ivt;
    String n = "00000000000000000000000000000000";
    int c_size;
    int b_size;
    int c_type;
    int n_sets;
    Cache d_cache;
    Cache i_cache;

    public int access;
    public int miss;
    public int cold_miss;
    public int conflict_miss;
    public int capacity_miss;
   
    public int parseint(String binaryInt, int t) {
        //Check if the number is negative.
        //We know it's negative if it starts with a 1
        if (binaryInt.charAt(0) == '1') {
            //Call our invert digits method
            String invertedInt = invertDigits(binaryInt);
            //Change this to decimal format.
            int decimalValue = Integer.parseInt(invertedInt, 2);
            //Add 1 to the curernt decimal and multiply it by -1
            //because we know it's a negative number
            decimalValue = (decimalValue + 1) * -1;
            //return the final result
            return decimalValue;
        } else {
            //Else we know it's a positive number, so just convert
            //the number to decimal base.
            return Integer.parseInt(binaryInt, 2);
        }
    }

    public String invertDigits(String binaryInt) {
        String result = binaryInt;
        result = result.replace("0", " "); //temp replace 0s
        result = result.replace("1", "0"); //replace 1s with 0s
        result = result.replace(" ", "1"); //put the 1s back in
        return result;
    }

    //the above values are chosen arbitrarily
    public primary_memory() {
        /*memory=new String[268435]; //max-memory=2^28;
        for(int i=0;i< 268435;i++) memory[i]="00000000";*/
        register = new int[32];
        mem = new HashMap<>();

        register[2] = stack_start;
        register[3] = heap_start;
        pc = ra = rb = rx = ry = rz = 0;
        ///////////////////////////////
        //memory[heap_start]="00000001";
        ///////////////////////////////
    }
//removing elements in memory for every new run 

    public void set_primary_memory(int c_size,
            int b_size,
            int c_type1,
            int c_type2,
            int n_sets) {
        /*memory=new String[268435]; //max-memory=2^28;
        for(int i=0;i< 268435;i++) memory[i]="00000000";*/
        //register = new int[32];
        mem = new HashMap<>(); 
        this.c_size = c_size;
        this.b_size = b_size;
        this.c_type = c_type1;
        this.n_sets = n_sets;
        i_cache = new Cache(c_size, b_size, c_type, n_sets);
        this.c_type = c_type2;
        d_cache = new Cache(c_size, b_size, c_type, n_sets);
        for (int i = 0; i < 32; i++) {
            register[i] = 0;
        }

        register[2] = stack_start;
        register[3] = heap_start;
        pc = ra = rb = rx = ry = rz = 0;
        //mem.clear();
        ///////////////////////////////
        //memory[heap_start]="00000001";
        ///////////////////////////////
    }

    //////////////////////////////////////////////////////////////////
    //  input is binary string  //
    public void Cache(int c_size,
            int b_size,
            int c_type1,
            int c_type2,
            int n_sets) {
        this.c_size = c_size;
        this.b_size = b_size;
        this.c_type = c_type1;
        this.n_sets = n_sets;
        i_cache = new Cache(c_size, b_size, c_type, n_sets);
        this.c_type = c_type2;
        d_cache = new Cache(c_size, b_size, c_type, n_sets);

    }

    public String loadbytestr(int addr) {

//        return mem.getOrDefault(addr, n);
        if (addr < data_start) {
            return i_cache.loadbytestr(addr);
        } else {
            return d_cache.loadbytestr(addr);
        }

    }

    public String loadhalfstr(int addr) {
//        if (!mem.containsKey(addr)) {
//            return n;
//        }
//        return mem.get(addr + 1) + mem.get(addr);
        if (addr < data_start) {
            return i_cache.loadhalfstr(addr);
        } else {
            return i_cache.loadhalfstr(addr);
        }
    }

    public String loadwordstr(int addr) {

        // System.out.println(addr+memory[addr+3]+memory[addr+2]+memory[addr+1]+memory[addr]);
//        if (!mem.containsKey(addr)) {
//            return n;
//        }
//        boolean d = mem.containsKey(addr);
//        String a = mem.get(addr + 3) + mem.get(addr + 2) + mem.get(addr + 1) + mem.get(addr);
//        return a;
        if (addr < data_start) {
            return i_cache.loadwordstr(addr);
        } else {
            return d_cache.loadwordstr(addr);
        }
    }

    public void storebytestr(int addr, String byte_in) {
        //cache.calculate_addr(addr, byte_in);

        mem.put(addr, byte_in.substring(0, 8));
        if (addr < data_start) {
            i_cache.storebytestr(addr, byte_in);
        } else {
            d_cache.storebytestr(addr, byte_in);
        }
        //memory[addr]=byte_in.substring(0,8);

    }

    public void storewordstr(int addr, String word_in) {
        //little endian//
        //System.out.println(word_in);
//        memory[addr]  =word_in.substring(24,32);
//        memory[addr+1]=word_in.substring(16,24);
//        memory[addr+2]=word_in.substring(8 ,16);
//        memory[addr+3]=word_in.substring(0 , 8);
        mem.put(addr + 3, word_in.substring(0, 8));
        mem.put(addr + 2, word_in.substring(8, 16));
        mem.put(addr + 1, word_in.substring(16, 24));
        mem.put(addr + 0, word_in.substring(24, 32));
        if (addr < data_start) {
            i_cache.storewordstr(addr, word_in);
        } else {
            d_cache.storewordstr(addr, word_in);
        }
        //memory[addr]=byte_in.substring(0,8);

        //System.out.println(memory[addr + 3] + memory[addr + 2] + memory[addr + 1] + memory[addr]+"-");
    }

    public void storehalfstr(int addr, String half_in) {
        mem.put(addr + 1, half_in.substring(0, 8));
        mem.put(addr, half_in.substring(8, 16));
//        memory[addr]=half_in.substring(8 ,16);
//        memory[addr+1]=half_in.substring(0 , 8);
        if (addr < data_start) {
            i_cache.storehalfstr(addr, half_in);
        } else {
            d_cache.storehalfstr(addr, half_in);
        }
    }

    ///////////////////////////// output is binary string /////////
    public int loadbyte(int addr) {
        if (addr < data_start) {
            int itemp = parseint(i_cache.loadbytestr(addr), 2);
            return itemp;
        } else {
            int itemp = parseint(d_cache.loadbytestr(addr), 2);
            return itemp;
        }
    }

    public int loadhalf(int addr) {

//        String rets = mem.get(addr + 1) + mem.get(addr);
        if (addr < data_start) {
            int itemp = parseint(i_cache.loadhalfstr(addr), 2);
            return itemp;
        } else {
            int itemp = parseint(d_cache.loadhalfstr(addr), 2);
            return itemp;
        }
    }

    public int loadword(int addr) {

        //System.out.println(addr+memory[addr+3]+memory[addr+2]+memory[addr+1]+memory[addr]);
//        String rets = mem.get(addr + 3) + mem.get(addr + 2) + mem.get(addr + 1) + mem.get(addr);
        if (addr < data_start) {
            int itemp = parseint(i_cache.loadwordstr(addr), 2);
            return itemp;
        } else {
            int itemp = parseint(d_cache.loadwordstr(addr), 2);
            return itemp;
        }
    }

    public void storebyte(int addr, int num) {
        String bin_line = Integer.toBinaryString(num);
        String temp = "";
        for (int i = 0; i < 8 - bin_line.length(); i++) {
            if (num >= 0) {
                temp = temp + "0";
            } else {
                temp = temp + "1";
            }
        }
        bin_line = temp + bin_line;
        bin_line = bin_line.substring(bin_line.length() - 8, bin_line.length());//15
        mem.put(addr, bin_line.substring(0, 8));
        if (addr < data_start) {
            i_cache.storebytestr(addr, bin_line);
        } else {
            d_cache.storebytestr(addr, bin_line);
        }

    }

    public void storeword(int addr, int num) {
        // little endian//
        // System.out.println(word_in);
        String bin_line = Integer.toBinaryString(num);
        String temp = "";
        for (int i = 0; i < 32 - bin_line.length(); i++) {
            if (num >= 0) {
                temp = temp + "0";
            } else {
                temp = temp + "1";
            }
        }
        bin_line = temp + bin_line;
        bin_line = bin_line.substring(bin_line.length() - 32, bin_line.length());
        mem.put(addr + 3, bin_line.substring(0, 8));
        mem.put(addr + 2, bin_line.substring(8, 16));
        mem.put(addr + 1, bin_line.substring(16, 24));
        mem.put(addr + 0, bin_line.substring(24, 32));
        // System.out.println(memory[addr + 3] + memory[addr + 2] + memory[addr + 1] +
        // memory[addr]+"-");
        if (addr < data_start) {
            i_cache.storewordstr(addr, bin_line);
        } else {
            d_cache.storewordstr(addr, bin_line);
        }

    }

    public void storehalf(int addr, int num) {
        String bin_line = Integer.toBinaryString(num);
        String temp = "";
        for (int i = 0; i < 16 - bin_line.length(); i++) {
            if (num >= 0) {
                temp = temp + "0";
            } else {
                temp = temp + "1";
            }
        }
        bin_line = temp + bin_line;
        bin_line = bin_line.substring(bin_line.length() - 16, bin_line.length());

        mem.put(addr + 1, bin_line.substring(0, 8));
        mem.put(addr, bin_line.substring(8, 16));
        if (addr < data_start) {
            i_cache.storehalfstr(addr, bin_line);
        } else {
            d_cache.storehalfstr(addr, bin_line);
        }
    }

    ///////////////////////////// output is binary string /////////
}
