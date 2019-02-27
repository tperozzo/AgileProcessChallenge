package com.tallesperozzo.agileprocesschallenge.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Beer implements Serializable {
    int id;
    String name;
    String tagline;
    String first_brewed;
    String description;
    String image_url;
    float abv;
    float ibu;
    int target_fg;
    float target_og;
    int ebc;
    float srm;
    float ph;
    float attenuation_level;
    Measure volume;
    Measure boil_volume;
    Method method;
    Ingredients ingredients;
    ArrayList<String> food_pairing;
    String brewers_tips;
    String contributed_by;

    public Beer(int id, String name, String tagline, String first_brewed, String description, String image_url, float abv, int ibu, int target_fg, int target_og, int ebc, float srm, float ph, float attenuation_level, Measure volume, Measure boil_volume, Method method, ArrayList<String> food_pairing, String brewers_tips, String contributed_by) {
        this.id = id;
        this.name = name;
        this.tagline = tagline;
        this.first_brewed = first_brewed;
        this.description = description;
        this.image_url = image_url;
        this.abv = abv;
        this.ibu = ibu;
        this.target_fg = target_fg;
        this.target_og = target_og;
        this.ebc = ebc;
        this.srm = srm;
        this.ph = ph;
        this.attenuation_level = attenuation_level;
        this.volume = volume;
        this.boil_volume = boil_volume;
        this.method = method;
        this.food_pairing = food_pairing;
        this.brewers_tips = brewers_tips;
        this.contributed_by = contributed_by;
    }

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
