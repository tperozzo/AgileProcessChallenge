package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * Beer Class
 * Created by Talles Perozzo
 */

@SuppressWarnings("unused")
public class Beer implements Serializable {
    private final int id;
    private final String name;
    private final String tagline;
    private final String first_brewed;
    private String description;
    private final String image_url;
    private final float abv;
    private  float ibu;
    private int target_fg;
    private float target_og;
    private  int ebc;
    private float srm;
    private float ph;
    private float attenuation_level;
    private Measure volume;
    private Measure boil_volume;
    private Method method;
    private Ingredients ingredients;
    private ArrayList<String> food_pairing;
    private String brewers_tips;
    private final String contributed_by;

    public Beer(int id, String name, String tagline, float abv, String image_url, String first_brewed, String contributed_by) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.first_brewed = first_brewed;
        this.image_url = image_url;
        this.abv = abv;
        this.contributed_by = contributed_by;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public String getFirst_brewed() {
        return first_brewed;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public float getAbv() {
        return abv;
    }

    public float getIbu() {
        return ibu;
    }

    public int getTarget_fg() {
        return target_fg;
    }

    public float getTarget_og() {
        return target_og;
    }

    public int getEbc() {
        return ebc;
    }

    public float getSrm() {
        return srm;
    }

    public float getPh() {
        return ph;
    }

    public float getAttenuation_level() {
        return attenuation_level;
    }

    public Measure getVolume() {
        return volume;
    }

    public Measure getBoil_volume() {
        return boil_volume;
    }

    public Method getMethod() {
        return method;
    }

    public ArrayList<String> getFood_pairing() {
        return food_pairing;
    }

    public String getBrewers_tips() {
        return brewers_tips;
    }

    public String getContributed_by() {
        return contributed_by;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }
}
