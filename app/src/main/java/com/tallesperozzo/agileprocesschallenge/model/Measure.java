package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Measure implements Serializable {
    private float value;
    private String unit;

    public float getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }
}
