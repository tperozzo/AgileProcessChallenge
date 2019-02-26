package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

public class Malt implements Serializable {
    String name;
    Measure amount;

    public Malt(String name, Measure amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Measure getAmount() {
        return amount;
    }
}
