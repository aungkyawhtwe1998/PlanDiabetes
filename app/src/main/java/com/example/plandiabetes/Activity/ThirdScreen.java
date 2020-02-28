package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.plandiabetes.R;
import com.google.android.material.button.MaterialButton;

public class ThirdScreen extends AppCompatActivity {
    EditText txtheight;
    MaterialButton btnft,btnnext3;
    double cmtoft=0.0328084;
    Double weight;
    String dob;

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
        setContentView(R.layout.activity_third_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtheight=findViewById(R.id.txtheight);
        btnft=findViewById(R.id.btnft);
        btnnext3=findViewById(R.id.btnNext3);
        txtheight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Intent intent=getIntent();
        if(intent!=null){
            dob=intent.getStringExtra("dob");
            weight=intent.getDoubleExtra("wei",0.0);
            //txtheight.setText(dob+","+weight);
        }

        if(txtheight.getText()!=null) {
            btnft.setEnabled(true);

        }else {
            txtheight.setError("Fill your Height Value");
            txtheight.requestFocus();
        }
        btnft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtheight.getEditableText().toString().equals("")){
                    double inputKg=Double.parseDouble(String.valueOf(txtheight.getText()));
                    double pound=(inputKg*cmtoft);
                    txtheight.setText(String.format("%.2f", pound));
                }else {
                    txtheight.setError("Fill your Height Value");
                    txtheight.requestFocus();
                }

            }
        });
        btnnext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtheight.getText().toString().isEmpty()){
                    txtheight.setError("Please select date");
                    txtheight.requestFocus();
                    return;
                }else {
                    Intent intent2=new Intent(ThirdScreen.this,FourthScreen.class);
                    intent2.putExtra("dob",dob);
                    intent2.putExtra("wei",weight);
                    intent2.putExtra("hei",Double.parseDouble(String.valueOf(txtheight.getText())));
                    startActivity(intent2);
                    finish();
                }
            }
        });
    }
}
