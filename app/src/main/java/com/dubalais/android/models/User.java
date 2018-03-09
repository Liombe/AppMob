package com.dubalais.android.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by leach on 22/02/2018.
 */
@IgnoreExtraProperties
public class User {

    public String username;
    public Address address;

    public User(){

    }

    public User(String username){
        this.username = username;
    }
    public Address getAddress(){return address;};
}
