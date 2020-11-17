package com.tce.blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Login extends AppCompatActivity {
    Button generateotp;
    ProgressBar progressBar;
    TextView tv;
    Button cl;
    public static String langg;
   TextInputEditText number;
   String ts,ts1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loadLocale();
        generateotp=findViewById(R.id.generate);
        number=findViewById(R.id.number);


        cl=findViewById(R.id.clang);
        ts="<font color='#000000'>"+"உயிர்களுக்கு மதிப்பளிப்போம்,"+"</font>";
        ts1="<font color='@color/colorPrimary'>"+"உடனுக்குடன் ரத்ததானம் வழங்கி காப்போம் !!!"+"</font>";
        tv=findViewById(R.id.tvuew);
        tv.setText(Html.fromHtml(ts+ts1));
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        final ProgressBar progressBar=findViewById(R.id.progressBar1);
        generateotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(number.getText().toString())){
                    Toast.makeText(Login.this, "Enter Number", Toast.LENGTH_SHORT).show();
                }
                else if(number.getText().toString().replace(" ","").length()!=10){
                    Toast.makeText(Login.this, "Enter Correct No ...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(Login.this,Bloodmain.class);
                    intent.putExtra("number",number.getText().toString());
                    startActivity(intent);
                    finish();


                }
            }
        });





    }
        private void showChangeLanguageDialog(){
         final String[] languages={"English","தமிழ்"};
            AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
            builder.setTitle("Choose language");
            builder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        setLocale("en");
                        recreate();


                    }
                    else if(which==1){
                        setLocale("ta");
                        recreate();
                    }
                    dialog.dismiss();

                }
            });
            AlertDialog dialog= builder.create();
            dialog.show();
        }
        private  void  setLocale(String lan){
                Locale locale=new Locale(lan);
                langg=lan;

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


