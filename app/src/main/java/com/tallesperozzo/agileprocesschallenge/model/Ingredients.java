package com.tallesperozzo.agileprocesschallenge.model;

import java.util.ArrayList;

public class Ingredients {
    ArrayList<Malt> malt;
    ArrayList<Hops> hops;
    String yeast;

    public Ingredients(ArrayList<Malt> malt, ArrayList<Hops> hops, String yeast) {
        this.malt = malt;
        this.hops = hops;
        this.yeast = yeast;
    }

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
