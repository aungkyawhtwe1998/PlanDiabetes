package com.example.plandiabetes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plandiabetes.R;

public class GenderAskActi extends AppCompatActivity {
String dob,type;
Double wei,hei;
String typcolor;
Button genbtn;
TextView txtmale,txtfemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_ask);
        Intent intent=getIntent();
        txtmale=findViewById(R.id.txtmale);
        txtfemale=findViewById(R.id.txtfemale);
        genbtn=findViewById(R.id.btngender);
        if(intent!=null){
            dob=intent.getStringExtra("dob");
            wei=intent.getDoubleExtra("wei",0.0);
            hei=intent.getDoubleExtra("hei",0.0);
            type=intent.getStringExtra("type");
        }
        txtmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtmale.setTextColor(Color.parseColor("#2ae0af"));
                txtfemale.setTextColor(Color.parseColor("#A6A6AA"));
                typcolor= "male";
            }
        });

        txtfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtfemale.setTextColor(Color.parseColor("#2ae0af"));
                txtmale.setTextColor(Color.parseColor("#A6A6AA"));
                typcolor="female";
            }
        });
        genbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.isEmpty()){
                    Toast.makeText(GenderAskActi.this,"Please choose type",Toast.LENGTH_SHORT).show();
                    txtmale.requestFocus();
                    txtfemale.requestFocus();
                    return;
                }else {
                    //userProfiles=new UserProfile(wei,hei,type,dob);
                    //database.userDao().insert(userProfiles);
                    Toast.makeText(GenderAskActi.this,"Finish",Toast.LENGTH_SHORT).show();
                    Intent intent4=new Intent(GenderAskActi.this,Register.class);
                    intent4.putExtra("dob",dob);
                    intent4.putExtra("wei",wei);
                    intent4.putExtra("hei",hei);
                    intent4.putExtra("type",type);
                    intent4.putExtra("gender",typcolor);
                    startActivity(intent4);
                }

            }
        });
    }
}
