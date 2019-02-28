package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Ingredients Class
 * Created by Talles Perozzo
 */

@SuppressWarnings("unused")
public class Ingredients implements Serializable {
    private ArrayList<Malt> malt;
    private ArrayList<Hops> hops;
    private String yeast;

    public ArrayList<Malt> getMalt() {
        return malt;
    }

    public ArrayList<Hops> getHops() {
        return hops;
    }

    public String getYeast() {
        return yeast;
    }
}
