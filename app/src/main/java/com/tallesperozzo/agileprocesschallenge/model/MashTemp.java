package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

/*
 * MashTemp Class
 * Created by Talles Perozzo
 */

@SuppressWarnings("unused")
public class MashTemp implements Serializable {
    private Measure temp;
    private String duration;

    public Measure getTemp() {
        return temp;
    }

    public String getDuration() {
        return duration;
    }
}
