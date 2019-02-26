package com.tallesperozzo.agileprocesschallenge.model;

public class Measure {
    int value;
    String unit;

    public Measure(int value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public int getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
