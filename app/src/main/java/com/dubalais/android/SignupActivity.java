package com.dubalais.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dubalais.android.models.Chore;
import com.dubalais.android.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity implements OnClickListener{

    private EditText inputEmail, inputPassword;
    private Button btnSignUp2, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private static DatabaseReference db;

    private String typePersonne="";

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        //initializing views
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        btnSignUp2 = (Button) findViewById(R.id.sign_up_button2);
        inputEmail = (EditText) findViewById(R.id.txtemail);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUP();
            }
        });
        btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignUP();
            }
        });
        // Gesture detection
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };


        PlaceholderFragment fragment = new PlaceholderFragment();

        FragmentManager  fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.contain_tache, fragment);
        fragmentTransaction.commit();

    }

    private void btnSignUP(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            createNewUser(task.getResult().getUser());
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void createNewUser(FirebaseUser userRegistered){
        // Create new user at users/
        String username = userRegistered.getEmail();
        String userId = userRegistered.getUid();

        User user = new User(username);

        db.child("users").child(userId).setValue(user);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // gauche à droite
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    // droite à gauche
                    Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.droite_a_gauche_in, R.anim.droite_a_gauche_out);
                }
            } catch (Exception e) {
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
    public void onClick(View v) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void onRadioButtonClicked(View view) {
        RadioButton rdF = findViewById(R.id.fournisseur);
        RadioButton rdD = findViewById(R.id.demandeur);;

        switch(view.getId()) {
            case R.id.fournisseur:
                if (rdF.isChecked()) {
                    typePersonne = "fournisseur";
                    rdD.setChecked(false);
                }
                break;
            case R.id.demandeur:
                if (rdD.isChecked()) {
                    typePersonne = "demandeur";
                    rdF.setChecked(false);
                }
                break;

        }
    }

    public void suiteClicked(View view){
        ConstraintLayout cl = findViewById(R.id.inscr1);
        ConstraintLayout cF = findViewById(R.id.incr_fourn);
        ConstraintLayout cD = findViewById(R.id.incr_dem);

        switch(typePersonne){
            case "fournisseur":
                cl.setVisibility(View.GONE);
                cF.setVisibility(View.VISIBLE);
                break;
            case "demandeur":
                cl.setVisibility(View.GONE);
                cD.setVisibility(View.VISIBLE);
                break;
            case "":
                Toast.makeText(getApplicationContext(), "Choisissez un type", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //final ConstraintLayout cl = findViewById();
            final View view = inflater.inflate(R.layout.activity_signup, container, false);
            //View view = new View(SignupActivity.this, null);
            final FrameLayout cl = view.findViewById(R.id.contain_tache);

            db.child("chores").addValueEventListener(new ValueEventListener() {
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

                        Log.d("ceciestuntag", "jedebug1");
                        cl.addView(check);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return view;
        }
    }
}