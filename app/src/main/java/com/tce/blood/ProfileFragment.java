package com.tce.blood;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth .FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    TextInputEditText namep;
    //final commit check
    TextInputEditText numberp;
    TextInputEditText bloodp;
    TextInputEditText countprofile,donum,add,eadd;
    String cnt;
    String numberprofile;
    String np,nump,bp,adds,eadds;
    Button feedback,clang,loadabout1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vpro= inflater.inflate(R.layout.fragment_profile,container,false);
        namep=(TextInputEditText)vpro.findViewById(R.id.prname);
        numberp=(TextInputEditText)vpro.findViewById(R.id.prnumber);
        bloodp=(TextInputEditText)vpro.findViewById(R.id.prblood);
        donum=(TextInputEditText)vpro.findViewById(R.id.donnum);
        eadd=(TextInputEditText)vpro.findViewById(R.id.ea);
        add=(TextInputEditText)vpro.findViewById(R.id.a);
        feedback=(Button)vpro.findViewById(R.id.fback41);
       loadabout1=(Button)vpro.findViewById(R.id.aboutpagebutton);
        clang=(Button)vpro.findViewById(R.id.chlang);
        loadabout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Aboutpage.class));
            }
        });

        countprofile=(TextInputEditText)vpro.findViewById(R.id.doncount);
        FirebaseAuth profileauth;
        profileauth= FirebaseAuth.getInstance();
        numberprofile=profileauth.getCurrentUser().getPhoneNumber();
        clang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });


        feedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String url = "https://forms.gle/Xxak6M9gA4VVinhNA";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference profileref=firebaseDatabase.getReference().child("Members").child(numberprofile);
        profileref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                np=dataSnapshot.child("name").getValue().toString();
                nump=dataSnapshot.child("age").getValue().toString();
                bp=dataSnapshot.child("bloodgroup").getValue().toString();
                eadds=dataSnapshot.child("emailaddress").getValue().toString();
                adds=dataSnapshot.child("address").getValue().toString();

                if(dataSnapshot.hasChild("count")){
                    cnt=dataSnapshot.child("count").getValue().toString();
                }
                namep.setText(np);
                numberp.setText(nump);
                bloodp.setText(bp);
                countprofile.setText(cnt);
                donum.setText(numberprofile);
                eadd.setText(eadds);
                add.setText(adds);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  vpro;

    }
    private void showChangeLanguageDialog(){
        final String[] languages={"English","தமிழ்"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("Choose language");
        builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    setLocale("en");
                    DatabaseReference databaseReferenceenglish=FirebaseDatabase.getInstance().getReference("Members").child(numberprofile).child("Language");
                    databaseReferenceenglish.setValue("en");
                    getActivity().recreate();



                }
                else if(which==1){
                    setLocale("ta");
                    DatabaseReference databaseReferencetamil=FirebaseDatabase.getInstance().getReference("Members").child(numberprofile).child("Language");
                    databaseReferencetamil.setValue("ta");
                    getActivity().recreate();

                }
                dialog.dismiss();

            }
        });
        AlertDialog dialog= builder.create();
        dialog.show();
    }
    private  void  setLocale(String lan){
        Locale locale=new Locale(lan);


        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getActivity().getResources().updateConfiguration(configuration,getActivity().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor=getActivity().getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_Lang",lan);
        editor.apply();




    }
    public void loadLocale(){
        SharedPreferences prefs=getActivity().getSharedPreferences("Settings", MODE_PRIVATE);
        String language=prefs.getString("My_Lang","");
        setLocale(language);
    }
}
