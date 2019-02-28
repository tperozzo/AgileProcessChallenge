package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

/*
 * Measure Class
 * Created by Talles Perozzo
 */

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
