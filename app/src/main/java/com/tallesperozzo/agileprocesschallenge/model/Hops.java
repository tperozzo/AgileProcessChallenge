package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

/*
 * Hops Class
 * Created by Talles Perozzo
 */

@SuppressWarnings("unused")
public class Hops implements Serializable {
    private String name;
    private Measure amount;
    private String add;
    private String attribute;

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
