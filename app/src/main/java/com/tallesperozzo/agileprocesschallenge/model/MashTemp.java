package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

public class MashTemp implements Serializable {
    Measure temp;
    String duration;

    public MashTemp(Measure temp, String duration) {
        this.temp = temp;
        this.duration = duration;
    }

    public Measure getTemp() {
        return temp;
    }

    public String getDuration() {
        return duration;
    }
}
