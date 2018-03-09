package com.dubalais.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dubalais.android.models.Address;
import com.dubalais.android.models.Advert;
import com.dubalais.android.models.Chore;
import com.dubalais.android.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostAdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostAdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostAdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    private Button btnSubmit;
    private EditText txtTitle;
    private EditText txtPrice;
    private List<Chore> chores = new ArrayList<Chore>();
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private OnFragmentInteractionListener mListener;

    public PostAdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostAdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostAdFragment newInstance(String param1, String param2) {
        PostAdFragment fragment = new PostAdFragment();
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
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_postadfragment, container, false);

        //Checkboxes container
        final LinearLayout ll = (LinearLayout) view.findViewById(R.id.choresLayout);

        //Viewswitcher
        final ViewSwitcher vs = (ViewSwitcher) view.findViewById(R.id.viewSwitcher);
        final Button btnNext = (Button) view.findViewById(R.id.switch1_button);
        final Button btnPrev = (Button) view.findViewById(R.id.switch2_button);

        //Declare Animation in context
        Animation in = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_out_right);

        //Put Animation to views in Viewswitcher
        vs.setInAnimation(in);
        vs.setOutAnimation(out);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vs.showNext();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vs.showPrevious();
            }
        });



        myRef.child("chores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    String chId = child.getKey();
                    Chore ch = child.getValue(Chore.class);
                    ch.uid=chId;
                    CheckBox check = new CheckBox(view.getContext());
                    String title = ch.title.substring(0,1).toUpperCase() + ch.title.substring(1);
                    check.setId(Integer.parseInt(ch.uid));
                    check.setText(title);
                    check.setTextColor(ContextCompat.getColor(view.getContext(), R.color.grisClair));
                    check.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            onClickCheckBox(v);
                        }
                    });
                    ll.addView(check);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtTitle = view.findViewById(R.id.txtTitle);
        txtPrice = view.findViewById(R.id.txtPrice);
        btnSubmit = view.findViewById(R.id.submit_button);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAdvert();
            }
        });
        return view;
    }

    private void onClickCheckBox(View v) {
        final View view = v;
        boolean checked = ((CheckBox) v).isChecked();
        if(checked){
            myRef.child("chores").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot child: dataSnapshot.getChildren()) {
                        String chId = child.getKey();
                        Chore ch = child.getValue(Chore.class);
                        ch.uid=chId;
                        if(ch.uid.equals(Integer.toString(view.getId()))){
                            chores.add(ch);
                            Toast.makeText(view.getContext(), "chore n°"+ch.uid+" added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{
            if(chores != null) {
                for (Chore c : chores) {
                    if (c.uid.equals(view.getId())) {
                        chores.remove(c);
                    }
                }
            }
        }
    }

    private void submitAdvert() {

        final String title = txtTitle.getText().toString();
        final String p = txtPrice.getText().toString();

        if(TextUtils.isEmpty(title)){
            txtTitle.setError("required");
            return;
        }

        if(TextUtils.isEmpty(p)){
            txtPrice.setError("required");
            return;
        }

        if(chores.isEmpty()){
            Toast.makeText(getContext(), "Vous devez sélectionner au moins une tâche ménagère.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Double price = Double.parseDouble(txtPrice.getText().toString());

        setEditingEnabled(false);
        Toast.makeText(getContext(), "Mise en ligne...", Toast.LENGTH_SHORT).show();

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        myRef.child("users").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Log.e("NewAdFragment", "User " + uid + " is unexpectedly null");
                            Toast.makeText(getContext(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            postNewAdvert(uid, title, price, user.username, chores, new Address("28 avenue de la République", 9200, "Nanterre"));
                        }
                        setEditingEnabled(true);
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();

                        AccueilFragment fragment = new AccueilFragment();
                        fragmentTransaction.replace(R.id.contain_fragment, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("PostAdAdvert", "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                }
        );

    }

    private void setEditingEnabled(boolean enabled) {
        txtTitle.setEnabled(enabled);
        txtPrice.setEnabled(enabled);
        if (enabled) {
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.GONE);
        }
    }

    private void postNewAdvert(String uid, String title, Double price, String advertiser, List<Chore> chores, Address address){
        String key = myRef.child("adverts").push().getKey();
        Advert ad = new Advert(uid, advertiser, title, price, chores, address);
        Map<String,Object> advalues = ad.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/adverts/"+key, advalues);
        childUpdates.put("/users-adverts/"+uid+"/"+key, advalues);

        myRef.updateChildren(childUpdates);
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
}
