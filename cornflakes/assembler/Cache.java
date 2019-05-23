/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assembler;

import static assembler.primary_memory.mem;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
public class Cache {

    int cache_size;
    int block_size;
    int cache_type;
    int no_sets;
    public int access;
    public int miss;
    public int cold_miss;
    public int conflict_miss;
    public int capacity_miss;

    public class block {

        int tag;
        boolean valid;
        String[] bytes;

        block() {
            tag = 0;
            valid = false;
            bytes = new String[block_size * 4];
        }
    }

    class set {

        Deque<block> dq_fas = new LinkedList();
        HashSet<Integer> tag_fas;

        set() {
            dq_fas = new LinkedList();
            this.tag_fas = new HashSet<>();
        }
    }
    block[] cache_dm;
    Deque<block> FullyAssociativeCache = new LinkedList<>(); //fully associative cache
    HashSet<Integer> TagMapFACache;
    set[] set_cache;

    Cache(int cache_size,
            int block_size,
            int cache_type,
            int no_sets) {
        this.cache_size = cache_size;
        this.block_size = block_size;
        this.cache_type = cache_type;
        this.no_sets = no_sets;

        cache_dm = new block[cache_size];
        set_cache = new set[no_sets];
        this.TagMapFACache = new HashSet<>();
        for (int i = 0; i < cache_size; i++) {
            cache_dm[i] = new block();
        }
        for (int i = 0; i < no_sets; i++) {
            set_cache[i] = new set();
        }
    }
    int off, tag, index;

    public static int binlog(int bits) // returns 0 for bits=0
    {
        int log = 0;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }

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
                tag = Integer.parseInt(bin_line.substring(0, 32 - n), 2);
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

    public block f_LRU() {
        if (!TagMapFACache.contains(tag)) {
            if (FullyAssociativeCache.size() == cache_size) {
                block temp = FullyAssociativeCache.removeLast();
                TagMapFACache.remove(temp.tag);
            }
            return null;
        } else {
            int i = 0;
            block temp = new block();
            Iterator<block> itr = FullyAssociativeCache.iterator();
            while (itr.hasNext()) {
                temp = itr.next();
                if (temp.tag == tag) {
                    break;
                }
                i++;
            }
            FullyAssociativeCache.remove(temp);
            return temp;
        }
    }

    public block s_LRU(Deque<block> dq_fas, HashSet<Integer> tag_fas) {
        if (!tag_fas.contains(tag)) {
            if (dq_fas.size() == cache_size) {
                block temp = dq_fas.removeLast();
                tag_fas.remove(temp.tag);
            }
            return null;
        } else {
            int i = 0;
            block temp = new block();
            Iterator<block> itr = dq_fas.iterator();
            while (itr.hasNext()) {
                temp = itr.next();
                if (temp.tag == tag) {
                    break;
                }
                i++;
            }
            dq_fas.remove(temp);
            return temp;
        }
    }

  
    public void storewordstr(int addr, String word_in) {
        calculate_addr(addr);
        access++;
        switch (cache_type) {
            case 0:
                if (tag == cache_dm[index].tag && cache_dm[index].valid) {
                    cache_dm[index].bytes[off] = word_in.substring(24, 32);
                    cache_dm[index].bytes[off + 1] = word_in.substring(16, 24);
                    cache_dm[index].bytes[off + 2] = word_in.substring(8, 16);
                    cache_dm[index].bytes[off + 3] = word_in.substring(0, 8);
                } else {
                    if (!cache_dm[index].valid) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    int n = binlog(block_size * 4);//calculating start of block
                    int bloc_addr = addr>>n;//
                    int addr1=bloc_addr<<n;
                    for (int i = 0; i < block_size * 4; i++) {
                        calculate_addr(addr1 + i);
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr1 + i, "00000000");
                        
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }
                }
                break;
            //System.out.println(memory[addr + 3] + memory[addr + 2] + memory[addr + 1] + memory[addr]+"-");
            case 1: {
                block temp = f_LRU();
                if (temp != null) {
                    temp.bytes[off] = word_in.substring(24, 32);
                    temp.bytes[off + 1] = word_in.substring(16, 24);
                    temp.bytes[off + 2] = word_in.substring(8, 16);
                    temp.bytes[off + 3] = word_in.substring(0, 8);
                } else {
                    if (FullyAssociativeCache.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        capacity_miss++;
                    }
                    temp = new block();
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr>>n;//calcualting starting addr of block
                    int addr1=bloc_addr<<n; //
                    //bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                         calculate_addr(addr1 + i);
                        temp.bytes[i] = mem.getOrDefault(addr1+ i, "00000000");
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                FullyAssociativeCache.push(temp);
                TagMapFACache.add(tag);
                break;
            }
            case 2: {
                block temp = s_LRU(set_cache[index].dq_fas, set_cache[index].tag_fas);
                if (temp != null) {
                    temp.bytes[off] = word_in.substring(24, 32);
                    temp.bytes[off + 1] = word_in.substring(16, 24);
                    temp.bytes[off + 2] = word_in.substring(8, 16);
                    temp.bytes[off + 3] = word_in.substring(0, 8);
                } else {
                    if (set_cache[index].dq_fas.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    temp = new block();
                    int n = binlog(block_size * 4);//calculating start of block
                    int bloc_addr = addr>>n;//
                    int addr1=bloc_addr<<n;
                    for (int i = 0; i < block_size * 4; i++) {
                        calculate_addr(addr1 + i);
                        temp.bytes[i] = mem.getOrDefault(addr1 + i, "00000000");
                        
                        temp.tag = tag;
                        temp.valid = true;
                    }

                }
                set_cache[index].dq_fas.push(temp);
                set_cache[index].tag_fas.add(tag);
                break;
            }
            default:
                break;
        }
    }

  
    public String loadwordstr(int addr) {

        calculate_addr(addr);
        System.out.println(addr);
        access++;
        switch (cache_type) {
            case 0:
                if (tag == cache_dm[index].tag && cache_dm[index].valid) {
                    return cache_dm[index].bytes[off + 3] + cache_dm[index].bytes[off + 2] + cache_dm[index].bytes[off + 1] + cache_dm[index].bytes[off];
                } else {
                    if (!cache_dm[index].valid) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr>>n;//calcualting starting addr of block
                    int addr1=bloc_addr<<n; //
                    //bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                         calculate_addr(addr1 + i);
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr1+ i, "00000000");
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }

                }
                break;
            case 1: {
                block temp;
                temp = f_LRU();
                if (temp != null) {
                    FullyAssociativeCache.push(temp);
                    TagMapFACache.add(tag);
                    return temp.bytes[off + 3] + temp.bytes[off + 2] + temp.bytes[off + 1] + temp.bytes[off];
                } else {
                    if (FullyAssociativeCache.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        capacity_miss++;
                    }
                    temp = new block();
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr>>n;//calcualting starting addr of block
                    int addr1=bloc_addr<<n; //
                    //bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                         calculate_addr(addr1 + i);
                        temp.bytes[i] = mem.getOrDefault(addr1+ i, "00000000");
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                FullyAssociativeCache.push(temp);
                TagMapFACache.add(tag);
                break;
            }
            case 2: {
                block temp;
                temp = s_LRU(set_cache[index].dq_fas, set_cache[index].tag_fas);
                if (temp != null) {
                    set_cache[index].dq_fas.push(temp);
                    set_cache[index].tag_fas.add(tag);
                    return temp.bytes[off + 3] + temp.bytes[off + 2] + temp.bytes[off + 1] + temp.bytes[off];
                } else {
                    if (set_cache[index].dq_fas.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }int n = binlog(block_size * 4);
                    int bloc_addr = addr>>n;//calcualting starting addr of block
                    int addr1=bloc_addr<<n; //
                    //bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                         calculate_addr(addr1 + i);
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr1+ i, "00000000");
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }
                }
                set_cache[index].dq_fas.push(temp);
                set_cache[index].tag_fas.add(tag);
                break;
            }
            default:
                break;
        }
        if (!mem.containsKey(addr)) {
            return "00000000000000000000000000000000";
        }
        String a = mem.get(addr + 3) + mem.get(addr + 2) + mem.get(addr + 1) + mem.get(addr);
        return a;
    }
  public void storebytestr(int addr, String byte_in) {
        calculate_addr(addr);
        access++;
        //mem.put(addr, byte_in.substring(0, 8));
        switch (cache_type) {
            case 0:
                if (tag == cache_dm[index].tag && cache_dm[index].valid) {
                    cache_dm[index].bytes[off] = byte_in.substring(0, 8);
                } else {
                    if (!cache_dm[index].valid) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr << (32 - n);
                    bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }
                }
                break;
            case 1: {
                block temp;
                temp = f_LRU();
                if (temp != null) {
                    temp.bytes[off] = byte_in.substring(0, 8);
                } else {
                    if (FullyAssociativeCache.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        capacity_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                FullyAssociativeCache.push(temp);
                TagMapFACache.add(tag);
                break;
            }
            case 2: {
                block temp;
                temp = s_LRU(set_cache[index].dq_fas, set_cache[index].tag_fas);
                if (temp != null) {
                    temp.bytes[off] = byte_in.substring(0, 8);
                } else {
                    if (set_cache[index].dq_fas.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                set_cache[index].dq_fas.push(temp);
                set_cache[index].tag_fas.add(tag);
                break;
            }
            default:
                break;
        }
    }
      public void storehalfstr(int addr, String half_in) {
        calculate_addr(addr);
        access++;
        switch (cache_type) {
            case 0:
                if (tag == cache_dm[index].tag && cache_dm[index].valid) {
                    cache_dm[index].bytes[off + 1] = half_in.substring(0, 8);
                    cache_dm[index].bytes[off] = half_in.substring(8, 16);
                } else {
                    if (!cache_dm[index].valid) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr << (32 - n);
                    bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }
                }
                break;
            case 1: {
                block temp = f_LRU();
                if (temp != null) {
                    temp.bytes[off] = half_in.substring(8, 16);
                    temp.bytes[off + 1] = half_in.substring(0, 8);

                } else {

                    if (FullyAssociativeCache.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        capacity_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                FullyAssociativeCache.push(temp);
                TagMapFACache.add(tag);
                break;
            }
            case 2: {
                block temp = s_LRU(set_cache[index].dq_fas, set_cache[index].tag_fas);
                if (temp != null) {
                    temp.bytes[off] = half_in.substring(8, 16);
                    temp.bytes[off + 1] = half_in.substring(0, 8);

                } else {
                    if (set_cache[index].dq_fas.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                set_cache[index].dq_fas.push(temp);
                set_cache[index].tag_fas.add(tag);
                break;
            }
            default:
                break;
        }

    }

    public String loadbytestr(int addr) {
        calculate_addr(addr);
        access++;
        switch (cache_type) {
            case 0:
                if (tag == cache_dm[index].tag && cache_dm[index].valid) {
                    return cache_dm[index].bytes[off];
                } else {
                    if (!cache_dm[index].valid) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr << (32 - n);
                    bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }
                }
                break;
            case 1: {
                block temp;
                temp = f_LRU();
                if (temp != null) {
                    FullyAssociativeCache.push(temp);
                    TagMapFACache.add(tag);
                    return temp.bytes[off];
                } else {
                    if (FullyAssociativeCache.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        capacity_miss++;
                    }

                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                FullyAssociativeCache.push(temp);
                TagMapFACache.add(tag);
                break;
            }
            case 2: {
                block temp;
                temp = s_LRU(set_cache[index].dq_fas, set_cache[index].tag_fas);
                if (temp != null) {
                    set_cache[index].dq_fas.push(temp);
                    set_cache[index].tag_fas.add(tag);
                    return temp.bytes[off];
                } else {
                    if (set_cache[index].dq_fas.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                set_cache[index].dq_fas.push(temp);
                set_cache[index].tag_fas.add(tag);
                break;
            }
            default:
                break;
        }

        return mem.getOrDefault(addr, "00000000000000000000000000000000");
    }

    public String loadhalfstr(int addr) {
        calculate_addr(addr);
        access++;
        switch (cache_type) {
            case 0:
                if (tag == cache_dm[index].tag && cache_dm[index].valid) {
                    return cache_dm[index].bytes[off + 1] + cache_dm[index].bytes[off];
                } else {
                    if (!cache_dm[index].valid) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    int n = binlog(block_size * 4);
                    int bloc_addr = addr << (32 - n);
                    bloc_addr = addr >> (32 - n);
                    for (int i = 0; i < block_size * 4; i++) {
                        cache_dm[index].bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        cache_dm[index].tag = tag;
                        cache_dm[index].valid = true;
                    }
                }
                break;
            case 1: {
                block temp;
                temp = f_LRU();
                if (temp != null) {
                    FullyAssociativeCache.push(temp);
                    TagMapFACache.add(tag);
                    return temp.bytes[off + 1] + temp.bytes[off];
                } else {
                    if (FullyAssociativeCache.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        capacity_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                FullyAssociativeCache.push(temp);
                TagMapFACache.add(tag);
                break;
            }
            case 2: {
                block temp;
                temp = s_LRU(set_cache[index].dq_fas, set_cache[index].tag_fas);
                if (temp != null) {
                    set_cache[index].dq_fas.push(temp);
                    set_cache[index].tag_fas.add(tag);

                    return temp.bytes[off + 1] + temp.bytes[off];
                } else {
                    if (set_cache[index].dq_fas.size() != cache_size - 1) {
                        cold_miss++;
                    } else {
                        conflict_miss++;
                    }
                    temp = new block();
                    for (int i = 0; i < block_size * 4; i++) {
                        temp.bytes[i] = mem.getOrDefault(addr + i, "00000000");
                        calculate_addr(addr + i);
                        temp.tag = tag;
                        temp.valid = true;
                    }
                }
                set_cache[index].dq_fas.push(temp);
                set_cache[index].tag_fas.add(tag);
                break;
            }
            default:
                break;
        }
        if (!mem.containsKey(addr)) {
            return "00000000000000000000000000000000";
        }
        return mem.get(addr + 1) + mem.get(addr);
    }


}
