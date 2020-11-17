package com.tce.blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static com.tce.blood.Login.langg;

public class Bloodhome extends AppCompatActivity {
    public static  BottomNavigationView bottomNavigationView;
    int numberforlabel;
    String Language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bloodhome);
        bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        FirebaseAuth fa=FirebaseAuth.getInstance();
        String numofuser=fa.getCurrentUser().getPhoneNumber();
        DatabaseReference dreference=FirebaseDatabase.getInstance().getReference("Members").child(numofuser);
        dreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("Language")){
                Language=dataSnapshot.child("Language").getValue().toString();
                setLocale(Language);

                loadLocale();


                }
                else{
                    Toast.makeText(Bloodhome.this, "You can change the language of the app in profile page", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
    }

    @Override
    public void onBackPressed() {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedfragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_dashboard:
                    selectedfragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    selectedfragment=new NotificationsFragment();
                    break;
                case R.id.navigation_profile:
                    selectedfragment=new ProfileFragment();
                    break;
                case R.id.navigation_event:
                    selectedfragment=new EventFragment();
                    break;


            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedfragment).commit();
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase datab12=FirebaseDatabase.getInstance();
        DatabaseReference datagetref12=datab12.getReference().child("Event");
        datagetref12.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numberforlabel=(int)dataSnapshot.getChildrenCount();
                BadgeDrawable badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.navigation_event);
                badgeDrawable.setBackgroundColor(Color.RED);
                badgeDrawable.setBadgeTextColor(Color.WHITE);
                badgeDrawable.setNumber(numberforlabel);

                badgeDrawable.setVisible(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase datab123=FirebaseDatabase.getInstance();
        DatabaseReference datagetref123=datab123.getReference().child("Postdetails");
        datagetref123.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int numberforlabel123=(int)dataSnapshot.getChildrenCount();
                BadgeDrawable badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
                badgeDrawable.setBackgroundColor(Color.RED);
                badgeDrawable.setBadgeTextColor(Color.WHITE);
                badgeDrawable.setNumber(numberforlabel123);

                badgeDrawable.setVisible(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
