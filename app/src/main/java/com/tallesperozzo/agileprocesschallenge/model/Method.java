package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Method Class
 * Created by Talles Perozzo
 */

@SuppressWarnings("unused")
public class Method implements Serializable {
    private ArrayList<MashTemp> mash_temp;
    private Fermentation fermentation;
    private String twist;

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
