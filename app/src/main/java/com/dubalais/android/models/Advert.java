package com.dubalais.android.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leach on 22/02/2018.
 */

@IgnoreExtraProperties
public class Advert {
    public String uid;
    public String advertiser;
    public String title;
    public Double price;
    public List<Chore> chores;
    public Address address;
    public String date;

    public Advert(){

    }

    public Advert(String uid, String advertiser, String title, Double price, List<Chore> chores, Address address){
        this.uid = uid;
        this.advertiser = advertiser;
        this.title = title;
        this.price = price;
        this.chores = chores;
        this.address = address;
        this.date = "07/04/2018";
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("advertiser", advertiser);
        result.put("title", title);
        result.put("price", price);
        result.put("chores", chores);
        result.put("address", address);

        return result;
    }

    public String getDate() {
        return this.date;
    }

    public Double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }
}
