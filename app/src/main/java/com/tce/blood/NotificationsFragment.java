package com.tce.blood;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.telephony.SmsManager;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;;import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.tce.blood.Bloodhome.bottomNavigationView;

public class NotificationsFragment extends Fragment {
    LinearLayoutManager mLinearLayoutManager;
    public View view;

    TextView Aplus,Aminus,ABplus;
    MaterialButtonToggleGroup toggleButton;
    private DatabaseReference databaseReference;
    FirebaseAuth authcount;



    private  RecyclerView mRecyclerView;
    ProgressBar progressBar;
    String grp123="",location="",mobilenumber;
    ChipGroup chipGroup,Chl;


    public NotificationsFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_notifications,container,false);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBarpost);
        chipGroup=(ChipGroup) view.findViewById(R.id.chipgrp);
        Chl=(ChipGroup) view.findViewById(R.id.chipgrploc);
        Chl.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {

                Chip chip=Chl.findViewById(checkedId);
                if(chip!=null){
                    if(grp123.equals("")){
                        Toast.makeText(getActivity(), "Select blood group", Toast.LENGTH_SHORT).show();
                        chipGroup.setVisibility(View.VISIBLE);
                    }

                        location=chip.getText().toString();
                        chipGroup.setVisibility(View.VISIBLE);
                        databaseReference= FirebaseDatabase.getInstance().getReference().child("POST").child(grp123).child(location);
                        databse();

                }


            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);


                if (chip != null)
                    if(location.equals(""))
                    {
                        Toast.makeText(getActivity(), "Select location", Toast.LENGTH_SHORT).show();
                    }
                else{
                    grp123= (String) chip.getText();
                    databaseReference= FirebaseDatabase.getInstance().getReference().child("POST").child(grp123).child(location);
                    databse();
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int nfl=(int)dataSnapshot.getChildrenCount();
                BadgeDrawable badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.navigation_notifications);
                badgeDrawable.setBackgroundColor(Color.RED);
                badgeDrawable.setBadgeTextColor(Color.WHITE);
                badgeDrawable.setNumber(nfl);

                badgeDrawable.setVisible(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });}






            }
        });










        mRecyclerView=(RecyclerView)view.findViewById(R.id.rvfornotifications);
       mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));









        return view;
    }

    @Override
    public void onStart() {
        super.onStart();





    }
    public void  databse(){ FirebaseRecyclerOptions<Modelfornoti>options=new FirebaseRecyclerOptions.Builder<Modelfornoti>()
            .setQuery(databaseReference,Modelfornoti.class).build();
        FirebaseRecyclerAdapter<Modelfornoti,Notificationsfragmentvh>adapter=new FirebaseRecyclerAdapter<Modelfornoti, Notificationsfragmentvh>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Notificationsfragmentvh notificationsfragmentvh, int i, @NonNull Modelfornoti modelfornoti) {
                notificationsfragmentvh.setDetails(getContext(),modelfornoti.getName(),modelfornoti.getAge(),modelfornoti.getBloodgroup()
                        ,modelfornoti.getNumber(),modelfornoti.getTime(),modelfornoti.getTimelimit(),modelfornoti.getUnit(),modelfornoti.getHour(),modelfornoti.getMinute(),modelfornoti.getCdfe(),modelfornoti.getDate(),modelfornoti.getLoca(),modelfornoti.getAddress());



            }

            @NonNull
            @Override
            public Notificationsfragmentvh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.noificationrow,parent,false);

                Notificationsfragmentvh notificationsfragmentvh=   new Notificationsfragmentvh(view);
                return notificationsfragmentvh;
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();








    }
    public  class Notificationsfragmentvh extends RecyclerView.ViewHolder{
        TextView namereq,bloodreq,agereq,timeposted1,timelimm,unitt,setreminder,dateposted,locationofrequester;
        TextInputEditText unitspossible;
        TextInputLayout layup;
        LinearLayout lldown;
        ImageView share,ddbutton1;
        String donername;
        CardView cardView;
        ProgressBar progressBardonate;
        Button donate;
        String phnum;
        String couni;
        int flag;
        int m;
        int difftim;




        public Notificationsfragmentvh(@NonNull View itemView) {
            super(itemView);


            namereq=itemView.findViewById(R.id.nameodrequester);
            bloodreq=itemView.findViewById(R.id.bloodgroupofrequester);
            timeposted1=itemView.findViewById(R.id.timeposted);
            locationofrequester=itemView.findViewById(R.id.locationofrequester);

            layup=itemView.findViewById(R.id.layup);
            lldown=itemView.findViewById(R.id.dropdownlayout);
            agereq=itemView.findViewById(R.id.ageofrequester);
            timelimm=itemView.findViewById(R.id.timelimitinpost);
            unitt=itemView.findViewById(R.id.unittttt);
            cardView=itemView.findViewById(R.id.cardvw);
            dateposted=itemView.findViewById(R.id.dateposted);
            unitspossible=itemView.findViewById(R.id.unitspossible);
            donate=itemView.findViewById(R.id.donate);
            share=itemView.findViewById(R.id.share);
            ddbutton1=itemView.findViewById(R.id.ddbutton);
            setreminder=itemView.findViewById(R.id.reminder);
            progressBardonate=itemView.findViewById(R.id.progressBardonate);




        }
        @SuppressLint("ResourceAsColor")
        public void setDetails(final Context ctx, final String name, String age, final String grp, final String number, String time, final String timelim, final String unit, final String hour1, final String minute55,final  String cdfe1,final String dateposted41,final String locad,final String addrs)  {

            namereq.setText(name);
            locationofrequester.setText(addrs);
            Date date1;
            long secondsInMilli,minutesInMilli,different,hoursInMilli,daysInMilli,elapsedDays = 0,elapsedHours=0;
            int timel=Integer.parseInt(timelim);
            try {
                date1=new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",Locale.US).parse(cdfe1);
                DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",Locale.US);
                Date dateobj = new Date();
                df.format(dateobj);
                 secondsInMilli = 1000;
                 minutesInMilli = secondsInMilli * 60;
                 different = dateobj.getTime() - date1.getTime();
                 hoursInMilli = minutesInMilli * 60;
                 daysInMilli = hoursInMilli * 24;
                 elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                 elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;
                //Toast.makeText(ctx, ""+date1, Toast.LENGTH_SHORT).show();
               //Toast.makeText(getContext(), ""+(int) elapsedHours, Toast.LENGTH_SHORT).show();


            } catch (ParseException e) {
                e.printStackTrace();
            }
            int timeexpirylimit=(int)elapsedDays*24 + (int)elapsedHours;
            //Toast.makeText(ctx, ""+timeexpirylimit, Toast.LENGTH_SHORT).show();



            if(timeexpirylimit>timel){
                timeposted1.setText("Expired");
                donate.setVisibility(View.INVISIBLE);
                share.setVisibility(View.INVISIBLE);
                setreminder.setVisibility(View.INVISIBLE);
                cardView.setCardElevation(0);
                layup.setVisibility(View.INVISIBLE);


            }
            else{
                timeposted1.setText(time);

            }



            int postedtime=Integer.parseInt(hour1);
            dateposted.setText(dateposted41);




            Calendar calendar = Calendar.getInstance();
            int chour = calendar.get(Calendar.HOUR_OF_DAY);

            if(chour<postedtime){
                chour+=24;
                difftim=chour-postedtime;


            }
            else{
                 difftim=postedtime-chour;

            }



            /*if(difftim>timel){



            }
            else{

            }*/
            int timebefore=postedtime+timel;
            if(timebefore>24){
                timebefore=timebefore-24;
            }

            timelimm.setText("Needed before "+timebefore+":"+minute55+" ("+timelim+" Hours)");
            agereq .setText("Age "+age);
            unitt.setText(unit+" Units required ");
            bloodreq.setText(grp);
            phnum=number;
            FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
            final String donernumber=firebaseAuth.getCurrentUser().getPhoneNumber();
            DatabaseReference dbr=FirebaseDatabase.getInstance().getReference("Members").child(donernumber);
            dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    donername= (String) dataSnapshot.child("name").getValue();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ddbutton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lldown.getVisibility()==View.GONE){
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        lldown.setVisibility(view.VISIBLE);
                        ddbutton1.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp);

                    }
                    else{
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        lldown.setVisibility(view.GONE);
                        ddbutton1.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp);

                    }

                }
            });


            donate.setOnClickListener(new View.OnClickListener() {





                @Override
                public void onClick(View v) {





                    progressBardonate.setVisibility(View.VISIBLE);
                    String uptest=unitspossible.getText().toString();
                    if (uptest.equals("0")||uptest.equals("")){
                        progressBardonate.setVisibility(View.GONE);
                        Toast.makeText(ctx, "Enter Units possible", Toast.LENGTH_SHORT).show();
                    }
                    else{

                    final MaterialAlertDialogBuilder builderdonate=new MaterialAlertDialogBuilder(getContext());
                    builderdonate.setTitle("Blood");
                    builderdonate.setMessage("Are you wiling to donate ?");
                    builderdonate.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                        if(unitspossible.equals("")){
                            progressBardonate.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Enter units possible", Toast.LENGTH_SHORT).show();

                        }

                        else {
                            String up1 = unitspossible.getText().toString();
                            int u = Integer.parseInt(unit);
                            int upossible = Integer.parseInt(up1);
                            int remaining = u-upossible;
                            String remainingunits = Integer.toString(remaining);


                            if (upossible < u) {






                                progressBardonate.setVisibility(View.GONE);
                                try {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(phnum, null, donername + " is ready to donate " + up1 + " units of "+grp+" blood"+"\nFor contact:"+donernumber, null, null);
                                    Toast.makeText(getContext(), "Message Sent",
                                            Toast.LENGTH_LONG).show();



                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Members").child(donernumber);

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int nm=Integer.parseInt(dataSnapshot.child("count").getValue().toString());
                                            String cs1=String.valueOf(nm+1);
                                            DatabaseReference databaseReference55=FirebaseDatabase.getInstance().getReference("Members").child(donernumber).child("count");


                                           databaseReference55.setValue(cs1);





                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });



                                    DatabaseReference updateref = FirebaseDatabase.getInstance().getReference("POST").child(grp).child(locad).child(phnum).child("unit");
                                    updateref.setValue(remainingunits);






                                } catch (Exception ex) {
                                    Toast.makeText(getContext(),"Allow SMS in App permissions",
                                            Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }

                               /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.putExtra("address", phnum);
                                sendIntent.putExtra("sms_body", donername + " is ready to donate " + up1 + " units of blood"+grp+"\nFor contact:"+donernumber);
                                sendIntent.setType("vnd.android-dir/mms-sms");
                                startActivity(sendIntent);*/





                                }

                            else if (u == upossible ) {

                                progressBardonate.setVisibility(View.GONE);
                                try {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(phnum, null, donername + " is ready to donate " + up1 + " units of "+grp+" blood"+"\nFor contact:"+donernumber, null, null);
                                    Toast.makeText(getActivity(), "Message Sent",
                                            Toast.LENGTH_LONG).show();


                                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Members").child(donernumber);

                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            int nm=Integer.parseInt(dataSnapshot.child("count").getValue().toString());

                                            String cs=String.valueOf(nm+1);
                                            DatabaseReference databaseReference41=FirebaseDatabase.getInstance().getReference("Members").child(donernumber).child("count");



                                            databaseReference41.setValue(cs);





                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {


                                        }
                                    });


                                    DatabaseReference deleteref = FirebaseDatabase.getInstance().getReference("POST").child(grp).child(locad).child(phnum);
                                    deleteref.removeValue();
                                    DatabaseReference delpd=FirebaseDatabase.getInstance().getReference("Postdetails").child(phnum);
                                    delpd.removeValue();











                                } catch (Exception ex) {
                                    Toast.makeText(getContext(),"Allow sms in app permissions",
                                            Toast.LENGTH_LONG).show();
                                    ex.printStackTrace();
                                }
                                /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.putExtra("address", phnum);
                                sendIntent.putExtra("sms_body", donername + " is ready to donate " + up1 + " units of "+grp+" blood"+"\nFor contact:"+donernumber);
                                sendIntent.setType("vnd.android-dir/mms-sms");
                                startActivity(sendIntent);*/

                            } else if (upossible > u) {
                                progressBardonate.setVisibility(View.GONE);

                                Toast.makeText(getActivity(), "Just " + unit + " units of blood is needed", Toast.LENGTH_SHORT).show();

                            }
                    }
                        }
                    });
                    builderdonate.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBardonate.setVisibility(View.GONE);

                        }
                    });
                    builderdonate.show();

                }}

            });
            setreminder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressBardonate.setVisibility(View.VISIBLE);

                        Calendar calendar1 = Calendar.getInstance();
                        int time=Integer.parseInt(minute55);



                        int ph = Integer.parseInt(hour1);

                        int tl=Integer.parseInt(timelim);

                        int finalhour=ph+tl;
                        if(finalhour>24){
                            finalhour=finalhour-24;
                        }


                        progressBardonate.setVisibility(View.INVISIBLE);



                        Intent intent=new Intent(AlarmClock.ACTION_SET_ALARM);

                        intent.putExtra(AlarmClock.EXTRA_HOUR,finalhour);
                        intent.putExtra(AlarmClock.EXTRA_MINUTES,time);
                        startActivity(intent);


                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent whatsappIntent1 = new Intent(Intent.ACTION_SEND);
                    whatsappIntent1.setType("text/plain");

                    whatsappIntent1.putExtra(Intent.EXTRA_TEXT, "Blood app \n"+name+" is in need of "+unit+" units of "+grp+" blood"+"\nFor donating please call "+number);
                    startActivity(Intent.createChooser(whatsappIntent1, "Share using"));

                }
            });


        }

    }

}
