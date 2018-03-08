package com.dubalais.android;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Accueil extends AppCompatActivity implements annoncefragment.OnFragmentInteractionListener {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        annonce a=new annonce("Salon à nettoyer","Mon salon est sale venez svp");
        mDatabase.child("annonce").child("an2").setValue(a);

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = annoncefragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_contain, fragment).commit();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}