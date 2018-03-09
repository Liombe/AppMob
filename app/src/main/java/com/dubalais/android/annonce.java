package com.dubalais.android;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nikola on 01/03/18.
 */

public class annonce {
    private String advertiser;
    private String title;
    private Long price;
    private String uid;
    private HashMap<String,Object> chores;

    public annonce(String advertiser, Long price,String titre, String uid,HashMap<String,Object>e){
        this.title=titre;
        this.advertiser=advertiser;
        //this.price=Long.toString(price);
        this.uid=uid;
        this.chores=e;


    }
    public annonce(){

    }

    public String getadvertiser() {
        return advertiser;
    }

    public void setadvertiser(String description)
    {
        this.advertiser = description;
    }

    public Long getPrice() {

        return price;
    }

    public String getstringprice(){
        return Long.toString(price);
    }

    public void setPrice(Long price) {

        this.price = price;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String titre) {

        this.title = titre;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
