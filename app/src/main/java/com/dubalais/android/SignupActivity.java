package com.dubalais.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dubalais.android.models.Address;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements OnClickListener{

    private EditText inputEmail, inputPassword;
    private Button btnSignUp2, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private static DatabaseReference db;


    private String typePersonne="";
    private List<Chore> chores = new ArrayList<Chore>();
    private List<EditText> prices = new ArrayList<EditText>();
    private List<CheckBox> checkBoxes = new ArrayList<CheckBox>();
    private Map<String, Object> fournisseur = new HashMap<String, Object>();
    private Map<String, Object> demandeur = new HashMap<String, Object>();

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


        //Create Chores price tags and checkboxes
        createChoresCheckboxes();

    }

    private void createChoresCheckboxes() {

        final LinearLayout fl = findViewById(R.id.contain_tache);

        db.child("chores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    String chId = child.getKey();

                    LinearLayout ll = new LinearLayout(SignupActivity.this);
                    int id = Integer.parseInt(chId);
                    ll.setId(id);

                    Chore ch = child.getValue(Chore.class);
                    ch.uid=chId;
                    CheckBox check = new CheckBox(SignupActivity.this);
                    String title = ch.title.substring(0,1).toUpperCase() + ch.title.substring(1);
                    check.setId(id);
                    check.setText(title);
                    check.setTextColor(ContextCompat.getColor(SignupActivity.this, R.color.grisClair));
                    check.setLeft(100);
                    checkBoxes.add(check);

                    final EditText price = new EditText(SignupActivity.this);
                    price.setId(id+20);
                    price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    price.setHint("Prix");
                    price.setTextSize(14);
                    price.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
                    price.setWidth(100);
                    price.setEnabled(false);
                    prices.add(price);

                    check.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            onClickCheckBox(v, price);
                        }
                    });

                    ll.addView(price,0);
                    ll.addView(check,1);

                    fl.addView(ll);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignupActivity.this, ""+databaseError, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void onClickCheckBox(View v, EditText price) {
        final View view = v;
        final EditText t = price;
        boolean checked = ((CheckBox) view).isChecked();
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
                            t.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(view.getContext(), ""+databaseError, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            if(chores != null) {
                for (Chore c : chores) {
                    if (c.uid.equals(view.getId())) {
                        chores.remove(c);
                    }
                }

                t.setEnabled(false);
            }
        }
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

        if(typePersonne.equals("fournisseur") && !checkF()) {
            return;
        }

        if(typePersonne.equals("demandeur") && !checkD()) {
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

    private boolean checkF(){
        EditText inputName = SignupActivity.this.findViewById(R.id.txtnom);
        EditText inputFirstname = SignupActivity.this.findViewById(R.id.txtprenom);
        EditText inputVoie = SignupActivity.this.findViewById(R.id.txtVoieF);
        EditText inputZip = SignupActivity.this.findViewById(R.id.txtZipCode);
        EditText inputCity = SignupActivity.this.findViewById(R.id.txtCityF);
        EditText inputRayon = SignupActivity.this.findViewById(R.id.txtrayon);
        EditText inputPhone = SignupActivity.this.findViewById(R.id.txttel);

        if(TextUtils.isEmpty(inputName.getText().toString().trim())){
            inputName.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputFirstname.getText().toString().trim())){
            inputFirstname.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputVoie.getText().toString().trim())){
            inputVoie.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputZip.getText().toString().trim())){
            inputZip.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputCity.getText().toString().trim())){
            inputCity.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputRayon.getText().toString().trim())){
            inputRayon.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputPhone.getText().toString().trim())){
            inputPhone.setError("required");
            return false;
        }

        if (chores.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vous devez au moins sélectionner une tâche ménagère!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkPrices()) {
            Toast.makeText(getApplicationContext(), "Vous devez renseigner des prix!", Toast.LENGTH_SHORT).show();
            return false;
        }

        fournisseur.put("name", inputName.getText().toString().trim());
        fournisseur.put("firstname", inputFirstname.getText().toString().trim());
        Address address = new Address(
                inputVoie.getText().toString().trim(),
                Long.parseLong(inputZip.getText().toString().trim()),
                inputCity.getText().toString().trim());
        fournisseur.put("address", address);
        fournisseur.put("rayon", inputRayon.getText().toString().trim());
        fournisseur.put("phone", inputPhone.getText().toString().trim());
        fournisseur.put("prices",getPrices());
        fournisseur.put("chores", chores);

        return true;
    }

    private List<Double> getPrices() {
        List<Double> p = new ArrayList<Double>();
        for(int i = 0; i<prices.size(); i++){
            if(prices.get(i).isEnabled())
                p.add(Double.parseDouble(prices.get(i).getText().toString().trim()));
        }
        return p;
    }

    private boolean checkD(){
        EditText inputName = SignupActivity.this.findViewById(R.id.txtnomD);
        EditText inputFirstname = SignupActivity.this.findViewById(R.id.txtprenomD);
        EditText inputVoie = SignupActivity.this.findViewById(R.id.txtVoieD);
        EditText inputZip = SignupActivity.this.findViewById(R.id.txtZipCodeD);
        EditText inputCity = SignupActivity.this.findViewById(R.id.txtCityD);
        EditText inputPhone = SignupActivity.this.findViewById(R.id.txttelD);

        if(TextUtils.isEmpty(inputName.getText().toString().trim())){
            inputName.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputFirstname.getText().toString().trim())){
            inputFirstname.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputVoie.getText().toString().trim())){
            inputVoie.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputZip.getText().toString().trim())){
            inputZip.setError("required");
            return false;
        }

        if(TextUtils.isEmpty(inputCity.getText().toString().trim())){
            inputCity.setError("required");
            return false;
        }
        if(TextUtils.isEmpty(inputPhone.getText().toString().trim())){
            inputPhone.setError("required");
            return false;
        }

        fournisseur.put("name", inputName.getText().toString().trim());
        fournisseur.put("firstname", inputFirstname.getText().toString().trim());
        Address address = new Address(
                inputVoie.getText().toString().trim(),
                Long.parseLong(inputZip.getText().toString().trim()),
                inputCity.getText().toString().trim());
        fournisseur.put("address", address);
        fournisseur.put("phone", inputPhone.getText().toString().trim());

        return true;
    }

    private boolean checkPrices() {
        for(int i=0; i<prices.size(); i++){
            if(checkBoxes.get(i).isChecked() && prices.get(i).getText() == null){
                return false;
            }
        }
        return true;
    }

    private void createNewUser(FirebaseUser userRegistered){
        // Create new user at users/
        String username = userRegistered.getEmail();
        String userId = userRegistered.getUid();

        User user = new User(username, typePersonne);

        if(typePersonne.equals("fournisseur")){
            user.fromMap(fournisseur);
        }else{
            user.fromMap(demandeur);
        }

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

        switch(typePersonne){
            case "fournisseur":
                cl.setVisibility(View.GONE);
                cF.setVisibility(View.VISIBLE);
                break;
            case "demandeur":
                cD.setVisibility(View.VISIBLE);
                cl.setVisibility(View.GONE);
                break;
            case "":
                Toast.makeText(getApplicationContext(), "Choisissez un type", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}