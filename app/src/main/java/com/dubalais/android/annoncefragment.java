package com.dubalais.android;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dubalais.android.adapter.SwipeCardAdapter;
import com.dubalais.android.models.Advert;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wenchao.cardstack.CardStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link annoncefragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link annoncefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class annoncefragment extends Fragment implements CardStack.CardEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Advert ad;


    ArrayList<String> card_list;
    CardStack cardstack;
    SwipeCardAdapter swipe_card_adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private int swipecount = 0;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Advert> Datalistannonce = new ArrayList<Advert>();
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 300000;  /* 5 min */
    private long FASTEST_INTERVAL = 175000; /*3 min et quelque */
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng latLng;
    private LocationCallback mLocationCallback;
    private float distMAX = 1000;


    public annoncefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment annoncefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static annoncefragment newInstance(String param1, String param2) {
        annoncefragment fragment = new annoncefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("adverts");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_annoncefragment, container, false);
        card_list = new ArrayList<>();
        cardstack = (CardStack) v.findViewById(R.id.container);
        cardstack.setContentResource(R.layout.layout_card);
        //cardstack.setStackMargin(18);
        cardstack.setListener(this);
        startLocationUpdates();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean swipeEnd(int section, float distance) {
        return (distance > 600) ? true : false;
    }

    @Override
    public boolean swipeStart(int section, float distance) {
        return false;
    }

    @Override
    public boolean swipeContinue(int section, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void discarded(int mIndex, int direction) {
        int swiped_card_postion = mIndex - 1;

        //getting the string attached with the card
        // String swiped_card_text = card_list.get(swiped_card_postion).toString();

        if (direction == 1) {

            Toast.makeText(getActivity().getApplicationContext(),/*swiped_card_text+*/" Swipped to right", Toast.LENGTH_SHORT).show();

        } else if (direction == 0) {

            Toast.makeText(getActivity().getApplicationContext(), " Swipped to Left", Toast.LENGTH_SHORT).show();


        } else {

            Toast.makeText(getActivity().getApplicationContext(), " Swipped to Bottom", Toast.LENGTH_SHORT).show();

            Bundle b = new Bundle();
            b.putString("arrivee", Datalistannonce.get(swipecount).getaddresse());
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LieuFragment fragment = new LieuFragment();
            fragment.setArguments(b);
            fragmentTransaction.replace(R.id.contain_fragment, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        swipecount++;
    }

    @Override
    public void topCardTapped() {
        Toast.makeText(getActivity().getApplicationContext(), "TaskTitle = " + Datalistannonce.get(0).getville(), Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity().getApplicationContext(), "Clicked top card", Toast.LENGTH_SHORT).show();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setAd(Advert ad) {
        this.ad = ad;
    }

    private void recherchecarte() {

        Query query = myRef.orderByChild("title").equalTo("nettoyage salon");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                long value = dataSnapshot.getChildrenCount();
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    float[] results = new float[1];  //creer tableau où on stocke la distance
                    LatLng latlngadvert;  //OBjet qui stovke la latitude et la longitude de l'addresse de l'annonce
                    latlngadvert = getLocationFromAddress(getContext(), child.getValue(Advert.class).getaddresse()); //on calcule la latitude et longitude
                    if (latlngadvert == null || latLng==null) { //si différent de null on continue
                        Toast.makeText(getContext(), "Vérifier le signal GPS", Toast.LENGTH_LONG).show();

                    } else {
                        Location.distanceBetween(latLng.latitude, latLng.longitude,
                                latlngadvert.latitude, latlngadvert.longitude, results); //calcul de la distance entre la position de la personnes et le domicille du gars

                        Log.i("distance", Float.toString(results[0]));
                        if (results[0] < distMAX) { //SI LA DISTANCE EST INFERIEUR A DISTMAX ON STOCKE DANS ARRAYLIST ET ON AFFICHE
                            Datalistannonce.add(child.getValue(Advert.class));
                        }
                        //Toast.makeText(getActivity().getApplicationContext(), "TaskTitle = " + child.getValue(Advert.class).getaddresse(), Toast.LENGTH_LONG).show();
                    }

                    Log.i("taille", Integer.toString(Datalistannonce.size()));
                    swipe_card_adapter = new SwipeCardAdapter(getActivity().getApplicationContext(), 0, Datalistannonce);//AFFICHAGE
                    cardstack.setAdapter(swipe_card_adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("erreurdb", "Failed to read value.", error.toException());
            }
        });

    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation()); //si on a récupérer une localisation

            }
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability){
                if(locationAvailability.isLocationAvailable() ){ //on s'assure que la geolocalisation est activé
                    recherchecarte();
                }
                else {
                    Toast.makeText(getContext(), "Pas de signal,quittez et activez le GPS", Toast.LENGTH_LONG).show();
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback,Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void onResume() {
        super.onResume();
        startLocationUpdates();
    }
}
