package cojocaru.alin.juniochallange;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Alin on 021 21 02 2016.
 */
public class Field implements Serializable{
    private String name;
    private String brand;
    private String id;
    private double proteins;
    private double surgars;
    private double calories;

    public Field(String id){
        this.id = id;
    }

    public Field() {
    }

    public Field(String id, String name, String brand, double proteins, double surgars, double calories) {
        this.name = name;
        this.brand = brand;
        this.id = id;
        this.proteins = proteins;
        this.surgars = surgars;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getId() {
        return id;
    }


    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getSurgars() {
        return surgars;
    }

    public void setSurgars(double surgars) {
        this.surgars = surgars;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Field fromJSON(JSONObject json){
        try {
            this.name = json.getString("item_name");
            this.brand = json.getString("brand_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Field fromDetailsJSON(JSONObject json){
        try {
            this.calories = json.getDouble("nf_calories");
            this.proteins = json.getDouble("nf_protein");
            this.surgars = json.getDouble("nf_sugars");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

}
