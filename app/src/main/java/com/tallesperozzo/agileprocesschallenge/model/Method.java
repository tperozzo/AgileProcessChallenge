package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Method implements Serializable {
    ArrayList<MashTemp> mash_temp;
    Fermentation fermentation;
    String twist;

    public Method(ArrayList<MashTemp> mash_temp, Fermentation fermentation, String twist) {
        this.mash_temp = mash_temp;
        this.fermentation = fermentation;
        this.twist = twist;
    }

    public ArrayList<MashTemp> getMash_temp() {
        return mash_temp;
    }

    public Fermentation getFermentation() {
        return fermentation;
    }

    public String getTwist() {
        return twist;
    }
}
