package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.plandiabetes.Class.Foods;
import com.example.plandiabetes.Class.Glucose;
import com.example.plandiabetes.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class firebasetest extends AppCompatActivity {
    EditText edtfirename;
    Spinner spinner;
    MaterialButton btnadd;
    DatabaseReference fooddatabase;
    List<Foods> foodsList;
    EditText edtname,edtcal,edtcarb,edtsugar,edtserving;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                Intent intent=new Intent(this,SearchFood.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebasetest);
        if(savedInstanceState==null){
            fooddatabase= FirebaseDatabase.getInstance().getReference("Food");
            fooddatabase.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Adding food");

        btnadd = findViewById(R.id.btnfirebase);
        edtname=findViewById(R.id.edtname);
        edtcal=findViewById(R.id.edtcal);
        edtsugar=findViewById(R.id.edtsugar);
        edtcarb=findViewById(R.id.edtCarbs);
        edtserving=findViewById(R.id.edtserving);
        spinner=findViewById(R.id.spinner_foodtype);

        List<String> listperod = new ArrayList<>();
        listperod.add("ဟင်းသီးဟင်းရွက်");
        listperod.add("ေဖျာ်ရည်");
        listperod.add("အသီး");
        listperod.add("အသားငါး");
        listperod.add("အစာေပြ");
        listperod.add("ပဲအမျိုးမျိုး");
        listperod.add("နို့ထွက်ပစ္စည်း နှင့် ဥအမျိုးမျိုး");
        listperod.add("ထမင်းနှင့်ဂျုံ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listperod);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        edtcarb.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtserving.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtsugar.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtcal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);



        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gid=fooddatabase.push().getKey();
                String type=spinner.getSelectedItem().toString();
                String name=edtname.getText().toString();
                Double cal=Double.parseDouble(String.valueOf(edtcal.getText().toString()));
                Double carb=Double.parseDouble(String.valueOf(edtcarb.getText().toString()));
                Double sugar=Double.parseDouble(String.valueOf(edtsugar.getText().toString()));
                Double serving=Double.parseDouble(String.valueOf(edtserving.getText().toString()));
                Foods f=new Foods(name,type,cal,carb,sugar,serving);
                fooddatabase.child(gid).setValue(f);
                Toast.makeText(firebasetest.this,"Saved",Toast.LENGTH_SHORT).show();
                edtcarb.setText("");
                edtserving.setText("");
                edtcal.setText("");
                edtname.setText("");
                edtsugar.setText("");
                //databaseglu.child(gid).setValue(glucose);


            }
        });
    }
}
