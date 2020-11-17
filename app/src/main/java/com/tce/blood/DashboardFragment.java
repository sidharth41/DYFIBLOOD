package com.tce.blood;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    TextInputEditText name1,age1,number1,timelimit,units1,addressmanual;
    TextInputLayout bldgrp1,locationpost,layoutaddress,Namepl,Agepl;
    String cdfe;
    String bgrpfordelete,locationfordelete;
    String locationdata;

    Button post;
    String newDateStr;
    String number;
    String currenthour,currentminute;
    ProgressBar progressBar;
    Toolbar topappbar;
    AutoCompleteTextView dropdown,dropdown1;

    ToggleButton toggleButton;
    RadioGroup rg;
    View v;
    RadioButton rb;
    String valueaddress="";

    Postdetails postdetails;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_dashboard,container,false);
        progressBar=(ProgressBar)v.findViewById(R.id.progressBarpost);
        progressBar.setVisibility(View.VISIBLE);
        name1=(TextInputEditText)v.findViewById(R.id.Namepost);
        addressmanual=(TextInputEditText)v.findViewById(R.id.Addressmanual);
        locationpost=(TextInputLayout)v.findViewById(R.id.locationpost);
        layoutaddress=(TextInputLayout)v.findViewById(R.id.tlayoutaddress);
        Namepl=(TextInputLayout)v.findViewById(R.id.Namepostlayout);
        Agepl=(TextInputLayout)v.findViewById(R.id.Agepostlayput);
        rg=(RadioGroup) v.findViewById(R.id.radioGroup);
        age1=(TextInputEditText)v.findViewById(R.id.Agepost);
        dropdown1=(AutoCompleteTextView) v.findViewById(R.id.locddown);
        FirebaseAuth mauth= FirebaseAuth.getInstance();
         number=mauth.getCurrentUser().getPhoneNumber();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton radioButton=(RadioButton) v.findViewById(checkedId);
                valueaddress=radioButton.getText().toString();
                if(valueaddress.equals("Available")){
                    locationpost.setVisibility(View.VISIBLE);
                    layoutaddress.setVisibility(View.GONE);

                    Toast.makeText(getContext(), " "+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();


                }
                else if(valueaddress.equals("Other")){
                    layoutaddress.setVisibility(View.VISIBLE);
                    locationpost.setVisibility(View.GONE);

                    Toast.makeText(getContext(), " "+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();


                }

            }
        });


        toggleButton=(ToggleButton)v.findViewById(R.id.toggleButton);
        bldgrp1=(TextInputLayout)v.findViewById(R.id.Bloodgrppost);
        dropdown=(AutoCompleteTextView) v.findViewById(R.id.ddown);


        String[] BLoodgrouptypes = new String[] {"A+","A-", "B+", "B-", "O+","O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_item,
                        BLoodgrouptypes);
        dropdown.setAdapter(adapter);
        String[] location = new String[] {"Tiruppur (North)",
                "Tiruppur (South)",
               "Avinashi",
                "Kangeyam",
                "Dharapuram",
                "Madathukulam",
                "Palladam",
                "Udumalaipettai"};
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_item,
                        location);
        dropdown1.setAdapter(adapter1);

        timelimit=(TextInputEditText)v.findViewById(R.id.timelimit);
        units1=(TextInputEditText)v.findViewById(R.id.units);
        post=(Button)v.findViewById(R.id.post);
       final Button deletep=(Button)v.findViewById(R.id.deletepost);

        final DatabaseReference deletepost=FirebaseDatabase.getInstance().getReference("Postdetails");
                deletepost.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(number)){
                            bgrpfordelete=dataSnapshot.child(number).child("bg").getValue().toString();
                            locationfordelete=dataSnapshot.child(number).child("location").getValue().toString();
                            deletep.setVisibility(View.VISIBLE);
                            post.setVisibility(View.GONE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else{
                            deletep.setVisibility(View.INVISIBLE);
                            post.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        deletep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Blood");
                builder.setMessage("Are you sure that you want to delete ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletep.setVisibility(View.INVISIBLE);
                      DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("POST");
                      databaseReference1.child(bgrpfordelete).child(locationfordelete).child(number).removeValue();
                      DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference("Postdetails");
                      databaseReference2.child(number).removeValue();




                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "You have cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();

            }
        });





        LinearLayout linearLayout=(LinearLayout)v.findViewById(R.id.othersll);

        postdetails=new Postdetails();


        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.isChecked()){

                    Namepl.setVisibility(View.GONE);
                    Agepl.setVisibility(View.GONE);
                    bldgrp1.setVisibility(View.GONE);



                }
                else{
                    Namepl.setVisibility(View.VISIBLE);
                    Agepl.setVisibility(View.VISIBLE);
                    bldgrp1.setVisibility(View.VISIBLE);

                }
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                builder.setTitle("Blood");

                builder.setMessage("Are you sure that you want to post ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upload();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "You have cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();


            }
        });

        return v;


    }


    // handle button activities

    private void upload()
    {
        progressBar.setVisibility(View.VISIBLE);
        String name=name1.getText().toString();
        SimpleDateFormat sdf1 = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
        cdfe = sdf1.format(new Date());
        locationdata=dropdown1.getText().toString();


        String age=age1.getText().toString();
        String bldgrp=dropdown.getText().toString();
        final String timelim=timelimit.getText().toString();
        final String unitbl=units1.getText().toString();
        Boolean bool=toggleButton.isChecked();
        Calendar calendar = Calendar.getInstance();
        int hour24hrs = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
         currenthour=Integer.toString(hour24hrs);
         currentminute=Integer.toString(minutes);


        if(bool==false) {

            if (name.equals("") || age.equals("") || bldgrp.equals("") || number.equals("")||timelim.equals("")||unitbl.equals("")) {
                progressBar.setVisibility(View.GONE);

                Toast.makeText(getActivity(), "Enter proper credentials", Toast.LENGTH_SHORT).show();

            }


            else {

               final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
               /* databaseReference.child("Members").child(number);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("Count")){
                        int n;
                         n=Integer.parseInt((String) dataSnapshot.child("Count").getValue());
                         n+=1;
                         String couni=String.valueOf(n);
                         databaseReference.child("Count").setValue(couni);
                        }
                        else{
                            databaseReference.child("Count").setValue("1");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa",Locale.US);
                String currentDateandTime = sdf.format(new Date());


                SimpleDateFormat sdf41 = new SimpleDateFormat("yyyy.MM.dd",Locale.US);
                newDateStr = sdf41.format(new Date());


                postdetails.setName(name);
                postdetails.setAge(age);
                postdetails.setBloodgroup(bldgrp);
                postdetails.setNumber(number);
                postdetails.setTime(currentDateandTime);
                postdetails.setTimelimit(timelim);
                postdetails.setUnit(unitbl);
                postdetails.setHour(currenthour);
                postdetails.setMinute(currentminute);
                postdetails.setCdfe(cdfe);
                postdetails.setDate(newDateStr);

                if(valueaddress.equals("Other")){
                    String addforpost=addressmanual.getText().toString();
                    postdetails.setLoca("Other");
                    postdetails.setAddress(addforpost);
                    databaseReference.child("POST").child(bldgrp).child("Other").child(number).setValue(postdetails);
                    databaseReference.child("Postdetails").child(number);
                    databaseReference.child("Postdetails").child(number).child("location").setValue("Other");
                    databaseReference.child("Postdetails").child(number).child("bg").setValue(bldgrp);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                    post.setVisibility(View.GONE);



                }
                else if(valueaddress.equals("Available")){
                    postdetails.setLoca(locationdata);
                    postdetails.setAddress(locationdata);
                    databaseReference.child("POST").child(bldgrp).child(locationdata).child(number).setValue(postdetails);
                    databaseReference.child("Postdetails").child(number);
                    databaseReference.child("Postdetails").child(number).child("location").setValue(locationdata);
                    databaseReference.child("Postdetails").child(number).child("bg").setValue(bldgrp);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                    post.setVisibility(View.GONE);


                }
                else {
                    Toast.makeText(getActivity(), "Enter address", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }






            }
        }
        else {
            if(unitbl.equals("")||timelim.equals("")){
                progressBar.setVisibility(View.GONE);


                Toast.makeText(getActivity(), "Enter proper credentials", Toast.LENGTH_SHORT).show();
            }
            else{
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Members").child(number);

            // Read from the database
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    String name = (String) dataSnapshot.child("name").getValue();
                    String age = (String) dataSnapshot.child("age").getValue();
                    String bloodgroup = (String) dataSnapshot.child("bloodgroup").getValue();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    SimpleDateFormat sdf = new SimpleDateFormat( "hh:mm aa",Locale.US);
                    String currentDateandTime = sdf.format(new Date());

                    SimpleDateFormat sdf41 = new SimpleDateFormat("yyyy.MM.dd",Locale.US);
                    newDateStr = sdf41.format(new Date());



                    postdetails.setName(name);
                    postdetails.setAge(age);
                    postdetails.setBloodgroup(bloodgroup);
                    postdetails.setNumber(number);
                    postdetails.setTime(currentDateandTime);
                    postdetails.setTimelimit(timelim);
                    postdetails.setUnit(unitbl);
                    postdetails.setHour(currenthour);
                    postdetails.setMinute(currentminute);
                    postdetails.setCdfe(cdfe);
                    postdetails.setDate(newDateStr);
                    if(valueaddress.equals("Other")){
                        String addforpost=addressmanual.getText().toString();
                        postdetails.setLoca("Other");
                        postdetails.setAddress(addforpost);
                        databaseReference.child("POST").child(bloodgroup).child("Other").child(number).setValue(postdetails);

                        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Postdetails");
                        db.child(number).child("location").setValue("Other");
                        db.child(number).child("bg").setValue(bloodgroup);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                        post.setVisibility(View.GONE);



                    }
                    else if(valueaddress.equals("Available")){
                        postdetails.setLoca(locationdata);
                        postdetails.setAddress(locationdata);
                        databaseReference.child("POST").child(bloodgroup).child(locationdata).child(number).setValue(postdetails);


                        DatabaseReference db=FirebaseDatabase.getInstance().getReference("Postdetails");
                        db.child(number).child("location").setValue(locationdata);
                        db.child(number).child("bg").setValue(bloodgroup);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                        post.setVisibility(View.GONE);


                    }
                    else{
                        Toast.makeText(getActivity(), "Enter address ", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }








                }

                @Override
                public void onCancelled(DatabaseError error) {
                    progressBar.setVisibility(View.GONE);

                }
            });
            }



        }



    }

}
