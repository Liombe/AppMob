package com.dubalais.android.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leach on 03/03/2018.
 */

@IgnoreExtraProperties
public class Chore {
    public String uid;
    public String title;
    public String description;
    public String lieu;

    public Chore(){

    }

    public Chore(String uid, String desc, String title, String place){
        this.uid = uid;
        this.description = desc;
        this.title = title;
        this.lieu = place;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("description", description);
        result.put("title", title);
        result.put("lieu", lieu);

        return result;
    }
}
