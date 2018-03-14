package com.dubalais.android.models;

import android.util.Log;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leach on 22/02/2018.
 */
@IgnoreExtraProperties
public class User {

    public String uid;
    public String username;
    public Address address;
    public String type;
    public String name;
    public String firstname;
    public int rayon;
    public List<Double> prices;
    public List<Chore> chores;
    private String phone;


    public User(){

    }

    public User(String username, String type){
        this.username = username;
        this.type = type;
    }

    public Address getAddress(){
        return address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Chore> getChores() {
        return chores;
    }

    public void setChores(List<Chore> chores) {
        this.chores = chores;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRayon() {
        return rayon;
    }

    public void setRayon(int rayon) {
        this.rayon = rayon;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("type", type);
        result.put("name", name);
        result.put("firstname", name);
        result.put("address", address.toMap());
        result.put("phone", phone);
        if(type.equals("fournisseur")){
            result.put("chores", chores);
            result.put("prices", prices);
        }


        return result;
    }

    public void fromMap(Map<String, Object> input){
        this.name = (String) input.get("name");
        Log.e("clamerde", this.name);
        this.firstname = (String) input.get("firstname");
        this.address = (Address) input.get("address");
        this.phone = (String) input.get("phone");
        if(this.type.equals("fournisseur")){
           this.chores  = (ArrayList<Chore>) input.get("chores");
           this.prices  = (ArrayList<Double>) input.get("prices");
        }
    }
}
