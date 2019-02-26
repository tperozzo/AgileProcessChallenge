package com.tallesperozzo.agileprocesschallenge.model;

public class Hops {
    String name;
    Measure amount;
    String add;
    String attribute;

    public Hops(String name, Measure amount, String add, String attribute) {
        this.name = name;
        this.amount = amount;
        this.add = add;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public Measure getAmount() {
        return amount;
    }

    public String getAdd() {
        return add;
    }

    public String getAttribute() {
        return attribute;
    }
}
