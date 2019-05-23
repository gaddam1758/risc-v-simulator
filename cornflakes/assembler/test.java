/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import static assembler.primary_memory.binlog;

/**
 *
 * @author ramak
 */
public class test {
        int off, tag, index;
     int   cache_type=0;
     int cache_size=4;
     int block_size=4;
     int no_sets=4;
        public void calculate_addr(int num) {
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
            //checking type of cache
            switch (cache_type) {
                case 0: {
                    //int block_addr = addr / (block_size * 4);
                    //int index = block_addr % cache_size;

                    int n = binlog(block_size * 4);
                    int m = binlog(cache_size);
                    off = Integer.parseInt(bin_line.substring(32 - n, 32), 2);
                    index = Integer.parseInt(bin_line.substring(32 - n - m, 32 - n), 2);
                    tag = Integer.parseInt(bin_line.substring(0, 32 - n - m), 2);
                    break;
                }
                case 1: {
                    int n = binlog(block_size * 4);
                    off = Integer.parseInt(bin_line.substring(32 - n, 32), 2);
                    tag = Integer.parseInt(bin_line.substring(0,32 - n), 2);
                    break;
                }
                case 2: {
                    int n = binlog(block_size * 4);
                    off = Integer.parseInt(bin_line.substring(32 - n, 32), 2);
                    int m = binlog(no_sets);
                    index = Integer.parseInt(bin_line.substring(32 - n - m, 32 - n), 2);
                    tag = Integer.parseInt(bin_line.substring(0, 32 - n - m), 2);
                    break;
                }
                default:
                    break;
            } 
}
         public static void main(String args[])
         {
             test obj=new test();
             obj.calculate_addr(16);
             System.out.println(obj.off);
             System.out.println(obj.index);
             System.out.println(obj.tag);
         }
}