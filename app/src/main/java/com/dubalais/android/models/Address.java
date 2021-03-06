package com.dubalais.android.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leach on 08/03/2018.
 */

@IgnoreExtraProperties
public class Address {
    private String voie;
    private long zipcode;
    private String city;

    public Address(){

    }

    public Address(String voie, long zipcode, String city){
        this.voie = voie;
        this.zipcode = zipcode;
        this.city = city;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("voie", voie);
        result.put("zipcode", zipcode);
        result.put("city", city);

        return result;
    }

    public String getCity(){
        return city;
    }

    public String getVoie() {
        return voie;
    }

    public Long getzipcode(){
        return zipcode;

    }
}


