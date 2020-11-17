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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;;import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.tce.blood.Bloodhome.bottomNavigationView;

public class EventFragment extends Fragment {
    LinearLayoutManager meLinearLayoutManager;
    public View eview;
    String num,name;
    int countofp;


    private DatabaseReference databaseReference;



    private  RecyclerView meRecyclerView;



    public EventFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eview= inflater.inflate(R.layout.fragment_event,container,false);












        meRecyclerView=(RecyclerView)eview.findViewById(R.id.rvforevents);
        meRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));









        return eview;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference= FirebaseDatabase.getInstance().getReference("Event");
        database();





    }
    public void  database(){ FirebaseRecyclerOptions<modelforevent>options=new FirebaseRecyclerOptions.Builder<modelforevent>()
            .setQuery(databaseReference,modelforevent.class).build();
        FirebaseRecyclerAdapter<modelforevent,Notificationsfragmentvh>adapter=new FirebaseRecyclerAdapter<modelforevent, Notificationsfragmentvh>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Notificationsfragmentvh notificationsfragmentvh, int i, @NonNull modelforevent modelfornoti1) {
                notificationsfragmentvh.setDetails(getContext(),modelfornoti1.getDay(),modelfornoti1.getMonth(),modelfornoti1.getYear(),modelfornoti1.getDescription(),modelfornoti1.getAdnum(),modelfornoti1.getCount(),modelfornoti1.getImgurl(),modelfornoti1.getAdname(),modelfornoti1.getMonthinname(),modelfornoti1.getLocation());



            }

            @NonNull
            @Override
            public Notificationsfragmentvh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.eventrow,parent,false);

                Notificationsfragmentvh notificationsfragmentvh=   new Notificationsfragmentvh(view);
                return notificationsfragmentvh;
            }
        };
        meRecyclerView.setAdapter(adapter);
        adapter.startListening();








    }
    public  class Notificationsfragmentvh extends RecyclerView.ViewHolder{
        TextView desc1,coun,location,adn,calopen;
        ImageView im;
        ImageView interest,shareev;






        public Notificationsfragmentvh(@NonNull View itemView) {
            super(itemView);
            FirebaseAuth eauth;
            eauth= FirebaseAuth.getInstance();
            num=eauth.getCurrentUser().getPhoneNumber();
            DatabaseReference erefer=FirebaseDatabase.getInstance().getReference("Members").child(num);
            erefer.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name=dataSnapshot.child("name").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            coun=itemView.findViewById(R.id.countparticipant);
            desc1=itemView.findViewById(R.id.nameevent);
            location=itemView.findViewById(R.id.evlocation);
            adn=itemView.findViewById(R.id.adminname);
            im=itemView.findViewById(R.id.imv);
            calopen=itemView.findViewById(R.id.calendaropen);
            shareev=itemView.findViewById(R.id.shareev);
            interest=itemView.findViewById(R.id.interested);




        }
        @SuppressLint("ResourceAsColor")
        public void setDetails(final Context ctx, final String dayst, final String monthst, final String yearst, final String des, final String adno,final String cop,final String url,final String nameadmin,final String montheve,final String locationevent)  {


            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            int day = calendar.get(Calendar.DATE);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            int day41=Integer.parseInt(dayst);
            int month41=Integer.parseInt(monthst);
            int year41=Integer.parseInt(yearst);
            if(day>day41 ){
                if((month+1>=month41&&year>=year41) || (month+1<month41 && year>year41)){
                FirebaseStorage fb=FirebaseStorage.getInstance();
                StorageReference StorageRef = fb.getReferenceFromUrl(url);

                StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(ctx, "Not deleted", Toast.LENGTH_SHORT).show();

                    }
                });
                DatabaseReference db=FirebaseDatabase.getInstance().getReference("Event").child(adno);
                db.removeValue();
                final DatabaseReference ddelparticipation=FirebaseDatabase.getInstance().getReference("Members").child(num);
                ddelparticipation.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("participation"))
                        {
                            final DatabaseReference dref1=FirebaseDatabase.getInstance().getReference("Members").child(num).child("participation");
                                    dref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.hasChild(des)){


                                                dref1.child(des).removeValue();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                }


            }
            else if(day<day41){
                if((month+1>month41&&year>=year41) ||(month<=month41&&year>year41)){

                    FirebaseStorage fb=FirebaseStorage.getInstance();
                    StorageReference StorageRef = fb.getReferenceFromUrl(url);

                    StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(ctx, "Not deleted", Toast.LENGTH_SHORT).show();

                        }
                    });
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference("Event").child(adno);
                    db.removeValue();
                    final DatabaseReference ddelparticipation=FirebaseDatabase.getInstance().getReference("Members").child(num);
                    ddelparticipation.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("participation"))
                            {
                              final DatabaseReference dref2=FirebaseDatabase.getInstance().getReference("Members").child(num).child("participation");
                              dref2.addListenerForSingleValueEvent(new ValueEventListener() {
                                  @Override
                                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                      if(dataSnapshot.hasChild(des)){


                                          dref2.child(des).removeValue();

                                      }

                                  }

                                  @Override
                                  public void onCancelled(@NonNull DatabaseError databaseError) {

                                  }
                              });


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }







            }



            DatabaseReference Dcheck=FirebaseDatabase.getInstance().getReference("Members").child(num).child("participation");
            Dcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.hasChild(des)){
                        interest.setImageResource(R.drawable.heart);
                        interest.setClickable(false);
                        coun.setText(cop);
                        desc1.setText(des);
                        location.setText(montheve+" "+dayst+" at "+locationevent);
                        adn.setText("Organised by "+nameadmin);

                        Picasso.get().load(url).into(im);



                    }
                    else{
                        coun.setText(cop);
                        desc1.setText(des);
                        interest.setImageResource(R.drawable.heart_unfill);
                        interest.setClickable(true);
                        location.setText(montheve+" "+dayst+" at "+locationevent);
                        adn.setText("Organised by "+nameadmin);

                        Picasso.get().load(url).into(im);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });







           calopen.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Calendar cal = Calendar.getInstance();
                   Intent intent = new Intent(Intent.ACTION_EDIT);
                   intent.setType("vnd.android.cursor.item/event");
                   intent.putExtra("beginTime", cal.getTimeInMillis());
                   intent.putExtra("allDay", false);
                   intent.putExtra("rrule", "FREQ=DAILY");

                   intent.putExtra("title", des);
                   startActivity(intent);

               }
           });
            shareev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");

                    whatsappIntent.putExtra(Intent.EXTRA_TEXT,url+"\n"+des+" on "+montheve+" "+dayst+" at "+locationevent+" by "+nameadmin );
                    startActivity(Intent.createChooser(whatsappIntent, "Share using"));

                }
            });







            interest.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
                    builder.setTitle("Blood");

                    builder.setMessage("Are you sure you are interested ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(adno, null, "Myself "+name+",I am interested in participating in this event"+"\nFor contact"+" "+num, null, null);
                                Toast.makeText(getContext(), "Message Sent",
                                        Toast.LENGTH_LONG).show();
                                DatabaseReference dref=FirebaseDatabase.getInstance().getReference("Members").child(num).child("participation");
                                dref.child(des).setValue(des);
                                DatabaseReference db1=FirebaseDatabase.getInstance().getReference("Event").child(adno);
                                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        countofp=Integer.parseInt(dataSnapshot.child("count").getValue().toString());
                                        DatabaseReference db2=FirebaseDatabase.getInstance().getReference("Event").child(adno).child("count");
                                        String newcop=String.valueOf(countofp+1);
                                        db2.setValue(newcop);



                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                interest.setClickable(false);
                                interest.setImageResource(R.drawable.heart);





                            } catch (Exception ex) {
                                Toast.makeText(getContext(),"Allow sms in app permissions",
                                        Toast.LENGTH_LONG).show();
                                ex.printStackTrace();
                            }






                            /*Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                            sendIntent.putExtra("address", adno);
                            sendIntent.putExtra("sms_body","Myself "+name+",I am interested in this event"+"\nFor contact"+" "+num);
                            sendIntent.setType("vnd.android-dir/mms-sms");
                            startActivity(sendIntent);*/



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


        }

    }

}
