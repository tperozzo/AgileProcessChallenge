package com.tallesperozzo.agileprocesschallenge.model;

public class Malt {
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
