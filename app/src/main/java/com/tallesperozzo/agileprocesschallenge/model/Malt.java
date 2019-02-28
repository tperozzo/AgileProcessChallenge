package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Malt implements Serializable {
    private String name;
    private Measure amount;

    public String getName() {
        return name;
    }

    public Measure getAmount() {
        return amount;
    }
}
