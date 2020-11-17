package com.tce.blood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Aboutpage extends AppCompatActivity {
    TextView sidharth,aravind,rajaguru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutpage);
        sidharth=findViewById(R.id.sidharth);
        aravind=findViewById(R.id.aravind);
        rajaguru=findViewById(R.id.rajaguru);
        sidharth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.linkedin.com/in/sidharth-govindarajan-7b965b1a5"));
                startActivity(viewIntent);
            }
        });
        aravind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://twitter.com/SAravind_14?s=08"));
                startActivity(viewIntent);
            }
        });
        rajaguru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://instagram.com/rajaguru_selvaraj?igshid=11diqde4fo9mh"));
                startActivity(viewIntent);
            }
        });
    }
}
