package com.example;

import java.util.HashMap;
import java.util.LinkedList;

public class Cache {
    // Private fields
    private HashMap<Integer, Cacheblock> cache; //holds the 16 cache lines
    private LinkedList<Integer> accessOrder; //maintains FIFO
    private static final int MAX_CACHE_LINES = 16;

    // Constructor
    public Cache() {
        cache = new HashMap<>();
        accessOrder = new LinkedList<>();
    }

    // Getter for cache
    public HashMap<Integer, Cacheblock> getCache() {
        return cache;
    }

    public boolean containsAddress(int address){
        if(this.cache.containsKey(address)){
            return true;
        }else{
            return false;
        }
    }

    // Retrieve value from cache
    public Cacheblock getCacheBlock(int address) {
        if(cache.get(address) != null){
            //System.out.println(address + " Retrieved Cacheblock from Cache");
        }else{
            //System.out.println(address + " Address not in cache");
        }
        return cache.get(address);
    }

    // Set new cache line with FIFO replacement policy
    //if we need to do a write back, return Cacheblock, else return null
    public Cacheblock setCacheBlock(int address, int value) {
        if (cache.size() >= MAX_CACHE_LINES) {
            int oldestAddress = accessOrder.removeFirst(); // Remove oldest entry
            Cacheblock c = this.cache.get(oldestAddress);
            cache.remove(oldestAddress);
            //add below to logging
            //System.out.println("Cache is full. Removed oldest cache block with address: " + oldestAddress);
            return c;
        }
        Cacheblock block = new Cacheblock(address, value);
        cache.put(address, block);
        accessOrder.addLast(address); // Track access order
        return null;
    }

    // Optional method to print cache contents
    public void printCache() {
        for (Cacheblock block : cache.values()) {
            System.out.println(block);
        }
    }

    // Optional method to print cache contents
    public String displayCacheBlocks() {
        String output = "";
        for (Cacheblock block : cache.values()) {
            output = output + block + '\n';
        }
        return output;
    }



}
