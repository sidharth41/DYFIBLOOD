package com.tce.blood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import static com.tce.blood.Login.langg;

public class Home extends AppCompatActivity {
    FirebaseAuth mauth;
    String uid;
    TextInputEditText name,age,bldgrp,addre,eaddress;
    ProgressBar progressBar;

    Registermember registermember;
    TextInputLayout bg;
    AutoCompleteTextView bgdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mauth=FirebaseAuth.getInstance();
        setLocale("ta");
        loadLocale();

        uid=mauth.getCurrentUser().getPhoneNumber();
        name=(TextInputEditText) findViewById(R.id.Name);
        age=(TextInputEditText) findViewById(R.id.Age);
        addre=(TextInputEditText) findViewById(R.id.Address);
        eaddress=(TextInputEditText) findViewById(R.id.Email);

        Button reg=findViewById(R.id.reg);
        progressBar=findViewById(R.id.progressBardetailspage);
        registermember=new Registermember();
        bg=(TextInputLayout)findViewById(R.id.Bloodgrpregister);
        bgdd=(AutoCompleteTextView) findViewById(R.id.ddownbloodgrp);
        String[] BLoodgrouptypes = new String[] {"A+","A-", "B+", "B-", "O+","O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_item,
                        BLoodgrouptypes);
        bgdd.setAdapter(adapter);


        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name1=name.getText().toString();
                String age1=age.getText().toString();
                String bldgrp1=bgdd.getText().toString();
                String emailstring=eaddress.getText().toString();
                String addressstring=addre.getText().toString();

                if(name1.equals("") || age1.equals("") || bldgrp1.equals("")||emailstring.equals("")||addressstring.equals("")){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Home.this, "Enter proper credentials", Toast.LENGTH_SHORT).show();

                }
                else{

                    registermember.setName(name.getText().toString());
                    registermember.setAge(age1);
                    registermember.setBloodgroup(bldgrp1);
                    registermember.setEmailaddress(emailstring);
                    registermember.setAddress(addressstring);
                    registermember.setCount(0);
                    databaseReference.child("Members").child(uid).setValue(registermember);
                     progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(Home.this,Bloodhome.class);

                    startActivity(intent);

                }



            }
        });



    }
    private  void  setLocale(String lan){
        Locale locale=new Locale(lan);


        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lan);
        editor.apply();




    }
    public void loadLocale(){
        SharedPreferences prefs=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }

}
