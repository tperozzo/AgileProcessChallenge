package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;

/*
 * Fermentation Class
 * Created by Talles Perozzo
 */

@SuppressWarnings("unused")
public class Fermentation implements Serializable {
    private Measure temp;

    public Measure getTemp() {
        return temp;
    }
}
