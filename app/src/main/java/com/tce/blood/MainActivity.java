package com.tce.blood;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public ProgressBar progressBar;
    FirebaseAuth ma;
    String num;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar=findViewById(R.id.progressBar);
        ma=FirebaseAuth.getInstance();










        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){


                        startActivity(new Intent(MainActivity.this,Login.class));
                        progressBar.setVisibility(View.INVISIBLE);
                        finish();



                }
                else {
                    num=ma.getCurrentUser().getPhoneNumber();
                    DatabaseReference dbref=FirebaseDatabase.getInstance().getReference("Members");
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(num)){
                                startActivity(new Intent(MainActivity.this,Bloodhome.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();

                            }
                            else{
                                startActivity(new Intent(MainActivity.this,Home.class));
                                progressBar.setVisibility(View.INVISIBLE);
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {



                        }
                    });


                }
            }
        },3000);

    }

}
