package com.dubalais.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dubalais.android.models.Chore;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceholderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceholderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceholderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatabaseReference db;

    private OnFragmentInteractionListener mListener;

    public PlaceholderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceholderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceholderFragment newInstance(String param1, String param2) {
        PlaceholderFragment fragment = new PlaceholderFragment();
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

        db = FirebaseDatabase.getInstance().getReference("chores");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("TAGATBEGIN", "cc");

        final View view = inflater.inflate(R.layout.fragment_placeholder, container, false);

        final LinearLayout cl = view.findViewById(R.id.frag_ctn_tache);

        db.addValueEventListener(new ValueEventListener() {
        //query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAGBEFOREFOR", "ok");
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Log.d("TAGONFOR", "ok");
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
                            //onClickCheckBox(v);
                        }
                    });
                    cl.addView(check);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), ""+databaseError, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    /*private void onClickCheckBox(View v) {
        final View view = v;
        boolean checked = ((CheckBox) v).isChecked();
        if(checked){
            db.child("chores").addValueEventListener(new ValueEventListener() {
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
    }*/

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
