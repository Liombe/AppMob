package com.dubalais.android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.wenchao.cardstack.CardStack;

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

    ArrayList<String> card_list;
    CardStack cardstack;
    SwipeCardAdapter swipe_card_adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseDatabase database;
    DatabaseReference myRef;

    private OnFragmentInteractionListener mListener;

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
        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("annonce");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_annoncefragment, container, false);
        card_list = new ArrayList<>();

        card_list.add("card 1");
        card_list.add("card 2");
        card_list.add("card 3");
        card_list.add("card 4");
        card_list.add("card 5");

        cardstack = (CardStack) v.findViewById(R.id.container);
        cardstack.setContentResource(R.layout.layout_card);
        cardstack.setStackMargin(18);
        cardstack.setListener(this);

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                ArrayList<annonce> Datalistannonce = new ArrayList<annonce>();

          /* This method is called once with the initial value and again whenever data at this location is updated.*/
                long value=dataSnapshot.getChildrenCount();
                Log.d("comptfils","no of children: "+value);

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Datalistannonce.add(child.getValue(annonce.class));
                }

                for(int i=0;i<Datalistannonce.size();i++){
                    Toast.makeText(getActivity().getApplicationContext(),"TaskTitle = "+Datalistannonce.get(i).getDescription(),Toast.LENGTH_LONG).show();
                }
                swipe_card_adapter = new SwipeCardAdapter(getActivity().getApplicationContext(),0,Datalistannonce);
                cardstack.setAdapter(swipe_card_adapter);
            }

            @Override
            public void onCancelled(DatabaseError error){
                // Failed to read value
                Log.w("erreurdb","Failed to read value.",error.toException());
            }
        });

        return v;
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
        return (distance>600)? true : false;    }

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
        int swiped_card_postion = mIndex -1;

        //getting the string attached with the card
        String swiped_card_text = card_list.get(swiped_card_postion).toString();


        if (direction == 1) {

            Toast.makeText(getActivity().getApplicationContext(),swiped_card_text+" Swipped to right",Toast.LENGTH_SHORT).show();

        } else if (direction == 0) {

            Toast.makeText(getActivity().getApplicationContext(),swiped_card_text+" Swipped to Left",Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(getActivity().getApplicationContext(),swiped_card_text+" Swipped to Bottom",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void topCardTapped() {
        Toast.makeText(getActivity().getApplicationContext(),"Clicked top card",Toast.LENGTH_SHORT).show();

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
