package com.example;

public class Cacheblock {
    // Private fields
    private int address;
    private int value;
    private int dirtyBit; //indicates this cache block has been written into so should be written out to memory before being discarded

    // Constructor
    public Cacheblock(int address, int value) {
        this.address = address;
        this.value = value;
        this.dirtyBit = 0;
    }

    public int getDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit(int dirtyBit) {
        this.dirtyBit = dirtyBit;
    }

    // Getter for address
    public int getAddress() {
        return address;
    }

    // Setter for address
    public void setAddress(int address) {
        this.address = address;
    }

    // Getter for value
    public int getValue() {
        return value;
    }

    // Setter for value
    public void setValue(int value) {
        this.value = value;
    }

    // Optional toString method for debugging
    @Override
    public String toString() {
        return "CacheBlock{address=" + address + ", value=" + value + "}";
    }
} 