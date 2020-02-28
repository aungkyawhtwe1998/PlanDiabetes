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

public class SecondScreen extends AppCompatActivity {
    EditText txtweight;
    MaterialButton btnLbs,btnkg,btnnext2;
    double kgtolb=2.20462262;
    double lbtokg=0.45359237;
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
        setContentView(R.layout.activity_second_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtweight=findViewById(R.id.txtweight);
        btnLbs=(MaterialButton) findViewById(R.id.btnlbs);
        btnnext2=findViewById(R.id.btnNext2);
        txtweight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(getIntent()!=null){
            dob=getIntent().getStringExtra("dob");
            //txtweight.setText(""+dob);
        }

        if(txtweight.getText()!=null) {
            btnLbs.setEnabled(true);


        }else {
            txtweight.setError("Fill your Weight Value");
            txtweight.requestFocus();
        }
        btnLbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtweight.getText().toString().equals("")){
                    double inputKg=Double.parseDouble(String.valueOf(txtweight.getText()));
                    double pound=(inputKg*kgtolb);
                    txtweight.setText(String.format("%.2f", pound));
                }else {
                    txtweight.setError("Fill your Weight Value");
                    txtweight.requestFocus();
                }

            }
        });
        btnnext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtweight.getText().toString().isEmpty()){
                    txtweight.setError("Please fill your weight in lb");
                    txtweight.requestFocus();
                    return;
                }else {
                    Intent intent1=new Intent(SecondScreen.this,ThirdScreen.class);
                    intent1.putExtra("dob",dob);
                    intent1.putExtra("wei",Double.parseDouble(String.valueOf(txtweight.getText())));
                    startActivity(intent1);
                }

            }
        });
    }
}
