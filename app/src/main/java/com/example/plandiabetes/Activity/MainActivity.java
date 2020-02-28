package com.example.plandiabetes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;

import com.example.plandiabetes.R;
import com.example.plandiabetes.Room.Database;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
EditText edtheigth,edtage,edtweight;
Button btnstart;
Database database;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(this, StartPage.class);
        startActivity(intent);
        finish();

    }


}
