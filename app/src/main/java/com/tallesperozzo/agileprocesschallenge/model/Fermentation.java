package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

public class Fermentation implements Serializable {
    Measure temp;

    public Fermentation(Measure temp) {
        this.temp = temp;
    }

    public Measure getTemp() {
        return temp;
    }
}
