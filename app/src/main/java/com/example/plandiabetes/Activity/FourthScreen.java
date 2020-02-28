package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plandiabetes.R;
import com.example.plandiabetes.Room.Database;
import com.example.plandiabetes.Room.UserProfile;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class FourthScreen extends AppCompatActivity {
    TextView txttyp1,txttyp2;
    MaterialButton btnfinish;
    String dob,type;
    Double wei,hei;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_screen);
        txttyp1=findViewById(R.id.txttype1);
        txttyp2=findViewById(R.id.txtType2);
        btnfinish=findViewById(R.id.btnfinish);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        if(intent!=null){
            dob=intent.getStringExtra("dob");
            wei=intent.getDoubleExtra("wei",0.0);
            hei=intent.getDoubleExtra("hei",0.0);
        }

        txttyp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttyp1.setTextColor(Color.parseColor("#E56353"));
                txttyp2.setTextColor(Color.parseColor("#A6A6AA"));
                type= "type1";
            }
        });

        txttyp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txttyp2.setTextColor(Color.parseColor("#E56353"));
                txttyp1.setTextColor(Color.parseColor("#A6A6AA"));
                type="type2";
            }
        });
        //database=Database.getDatabase(this);

        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.isEmpty()){
                    Toast.makeText(FourthScreen.this,"Please choose type",Toast.LENGTH_SHORT).show();
                    txttyp1.requestFocus();
                    txttyp2.requestFocus();
                    return;
                }else {
                    //userProfiles=new UserProfile(wei,hei,type,dob);
                    //database.userDao().insert(userProfiles);
                    Toast.makeText(FourthScreen.this,"Finish",Toast.LENGTH_SHORT).show();
                    Intent intent4=new Intent(FourthScreen.this,GenderAskActi.class);
                    intent4.putExtra("dob",dob);
                    intent4.putExtra("wei",wei);
                    intent4.putExtra("hei",hei);
                    intent4.putExtra("type",type);
                    startActivity(intent4);
                }

            }
        });

    }
}
