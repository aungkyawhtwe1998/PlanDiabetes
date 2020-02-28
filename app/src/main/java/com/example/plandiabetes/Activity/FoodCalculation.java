package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.plandiabetes.Class.Foods;
import com.example.plandiabetes.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodCalculation extends AppCompatActivity {
    TextView txt1,txt2,txt3,txt4,txt5,txt6,txt7,txtresult,txtcalresult;
    EditText edt1,edt2,edt3,edt4,edt5,edt6,edt7;
    LinearLayout l1,l2,l3,l4,l5,l6,l7;
    DatabaseReference foodatabase;
    ArrayList<String> list;
    Double totalSum,totalCal;
    Double sum1,sum2,sum3,sum4,sum5,sum6,sum7;
    Double cal1,cal2,cal3,cal4,cal5,cal6;

    Double temps1=0.0,temps2=0.0,temps3=0.0,temps4=0.0,temps5=0.0,temps6=0.0,temps7=0.0;
    Double tempcal1,tempcal2,tempcal3,tempcal4,tempcal5,tempcal6;

    DecimalFormat decformat;
    Foods foods;
    Button btn;
    public static Double carb,cal;
    private final String TAG="FoodCal";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                //onBackPressed();
                //gotoActi();
                gotoList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_calculation);
        if(savedInstanceState==null){
            foodatabase= FirebaseDatabase.getInstance().getReference("Food");
            //userdatabase=FirebaseDatabase.getInstance().getReference("User");
            foodatabase.keepSynced(true);
            //userdatabase.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        decformat=new DecimalFormat("0.00");
        totalSum=0.0;
        init();
        list=(ArrayList<String>) getIntent().getSerializableExtra("chiplist");
        ChipGroup mchipGroup=findViewById(R.id.cal_chip);

        if(list.size()==1){
            txt1.setText(""+list.get(0));
            l1.setVisibility(View.VISIBLE);
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                        sum1=0.0;
                        foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                        carb = foods.getCarbs();
                                       // cal=foods.getCal();
                                    }

                                    Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                    sum1 += (usergram  * carb)/ 100;
                                    txtresult.setText(decformat.format(sum1));
                                    totalSum+=sum1;
                                    temps1=sum1;
                                    sum1=0.0;

                                    /*cal1+=(usergram*cal)/100;
                                    txtcalresult.setText(decformat.format(cal1));
                                    totalCal+=cal1;
                                    tempcal1=cal1;
                                    cal1=0.0;*/
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(""+0.0);
                        sum1=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }else if (list.size()==2){
            txt1.setText(list.get(0));
            txt2.setText(list.get(1));
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!edt2.getText().equals("")||!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps1;
                    }
                    if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                        sum1=0.0;
                        //cal1=0.0;
                        //edt2.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    //cal=foods.getCal();

                                    Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                    sum1 += (usergram  * carb)/ 100;


                                    totalSum+=sum1;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps1=sum1;
                                    sum1=0.0;

                                    /*cal1+=(usergram*cal)/100;
                                    txtcalresult.setText(decformat.format(cal1));
                                    totalCal+=cal1;
                                    tempcal1=cal1;
                                    cal1=0.0;*/


                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt2.setEnabled(false);
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        sum1=0.0;
                        temps1=0.0;
                        txtresult.setText(decformat.format(totalSum));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum2=0.0;
                    //totalSum=temps1;
                    if(!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps2;
                    }

                    if(!TextUtils.isEmpty(edt2.getText().toString().trim())) {
                        edt3.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt2.getText()));
                                    sum2 += (usergram  * carb)/ 100;


                                    totalSum+=sum2;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps2=sum2;
                                    sum2=0.0;

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        sum2=0.0;
                        temps2=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


        }else if (list.size()==3){
            txt1.setText(list.get(0));
            txt2.setText(list.get(1));
            txt3.setText(list.get(2));
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!edt2.getText().equals("")||!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps1;
                    }
                    if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                        sum1=0.0;
                        //cal1=0.0;
                        //edt2.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    //cal=foods.getCal();

                                    Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                    sum1 += (usergram  * carb)/ 100;


                                    totalSum+=sum1;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps1=sum1;
                                    sum1=0.0;

                                    /*cal1+=(usergram*cal)/100;
                                    txtcalresult.setText(decformat.format(cal1));
                                    totalCal+=cal1;
                                    tempcal1=cal1;
                                    cal1=0.0;*/


                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt2.setEnabled(false);
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        sum1=0.0;
                        temps1=0.0;
                        txtresult.setText(decformat.format(totalSum));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum2=0.0;
                    //totalSum=temps1;
                    if(!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps2;
                    }

                    if(!TextUtils.isEmpty(edt2.getText().toString().trim())) {
                        edt3.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt2.getText()));
                                    sum2 += (usergram  * carb)/ 100;


                                    totalSum+=sum2;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps2=sum2;
                                    sum2=0.0;

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        sum2=0.0;
                        temps2=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum3=0.0;
                    if(!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps3;
                    }
                    //totalSum=temps1+temps2;
                    //txtresult.setText(totalSum.toString());
                    if(!TextUtils.isEmpty(edt3.getText().toString().trim())) {

                        edt4.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(2)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt3.getText()));
                                    sum3 += (usergram  * carb)/ 100;


                                    totalSum+=sum3;
                                    temps3=sum3;
                                    sum3=0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt6.setEnabled(false);
                        //edt5.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        //txtresult.setHint("Enter gram");
                        sum3=0.0;
                        temps3=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else if (list.size()==4){
            txt1.setText(list.get(0));
            txt2.setText(list.get(1));
            txt3.setText(list.get(2));
            txt4.setText(list.get(3));
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l4.setVisibility(View.VISIBLE);
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!edt2.getText().equals("")||!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps1;
                    }
                    if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                        sum1=0.0;
                        //cal1=0.0;
                        //edt2.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    //cal=foods.getCal();

                                    Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                    sum1 += (usergram  * carb)/ 100;


                                    totalSum+=sum1;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps1=sum1;
                                    sum1=0.0;

                                    /*cal1+=(usergram*cal)/100;
                                    txtcalresult.setText(decformat.format(cal1));
                                    totalCal+=cal1;
                                    tempcal1=cal1;
                                    cal1=0.0;*/


                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt2.setEnabled(false);
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        sum1=0.0;
                        temps1=0.0;
                        txtresult.setText(decformat.format(totalSum));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum2=0.0;
                    //totalSum=temps1;
                    if(!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps2;
                    }

                    if(!TextUtils.isEmpty(edt2.getText().toString().trim())) {
                        edt3.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt2.getText()));
                                    sum2 += (usergram  * carb)/ 100;


                                    totalSum+=sum2;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps2=sum2;
                                    sum2=0.0;

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        sum2=0.0;
                        temps2=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum3=0.0;
                    if(!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps3;
                    }
                    //totalSum=temps1+temps2;
                    //txtresult.setText(totalSum.toString());
                    if(!TextUtils.isEmpty(edt3.getText().toString().trim())) {

                        edt4.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(2)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt3.getText()));
                                    sum3 += (usergram  * carb)/ 100;


                                    totalSum+=sum3;
                                    temps3=sum3;
                                    sum3=0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt6.setEnabled(false);
                        //edt5.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        //txtresult.setHint("Enter gram");
                        sum3=0.0;
                        temps3=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    sum4 = 0.0;
                    if(!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps4;
                    }
                    //totalSum = temps1 + temps2+temps3;
                    //txtresult.setText(totalSum.toString());
                    if (!TextUtils.isEmpty(edt4.getText().toString().trim())) {
                        edt5.setEnabled(true);

                        foodatabase.orderByChild("name").equalTo(list.get(3)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt4.getText()));
                                    sum4 += (usergram * carb) / 100;


                                    totalSum += sum4;
                                    temps4 = sum4;
                                    sum4 = 0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(decformat.format(totalSum));
                        sum4=0.0;
                        temps4=0.0;
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else if(list.size()==5){
            txt1.setText(list.get(0));
            txt2.setText(list.get(1));
            txt3.setText(list.get(2));
            txt4.setText(list.get(3));
            txt5.setText(list.get(4));
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l4.setVisibility(View.VISIBLE);
            l5.setVisibility(View.VISIBLE);
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!edt2.getText().equals("")||!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps1;
                    }
                    if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                        sum1=0.0;
                        //cal1=0.0;
                        //edt2.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    //cal=foods.getCal();

                                    Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                    sum1 += (usergram  * carb)/ 100;


                                    totalSum+=sum1;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps1=sum1;
                                    sum1=0.0;

                                    /*cal1+=(usergram*cal)/100;
                                    txtcalresult.setText(decformat.format(cal1));
                                    totalCal+=cal1;
                                    tempcal1=cal1;
                                    cal1=0.0;*/


                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt2.setEnabled(false);
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        sum1=0.0;
                        temps1=0.0;
                        txtresult.setText(decformat.format(totalSum));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum2=0.0;
                    //totalSum=temps1;
                    if(!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps2;
                    }

                    if(!TextUtils.isEmpty(edt2.getText().toString().trim())) {
                        edt3.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt2.getText()));
                                    sum2 += (usergram  * carb)/ 100;


                                    totalSum+=sum2;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps2=sum2;
                                    sum2=0.0;

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        sum2=0.0;
                        temps2=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum3=0.0;
                    if(!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps3;
                    }
                    //totalSum=temps1+temps2;
                    //txtresult.setText(totalSum.toString());
                    if(!TextUtils.isEmpty(edt3.getText().toString().trim())) {

                        edt4.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(2)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt3.getText()));
                                    sum3 += (usergram  * carb)/ 100;


                                    totalSum+=sum3;
                                    temps3=sum3;
                                    sum3=0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt6.setEnabled(false);
                        //edt5.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        //txtresult.setHint("Enter gram");
                        sum3=0.0;
                        temps3=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    sum4 = 0.0;
                    if(!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps4;
                    }
                    //totalSum = temps1 + temps2+temps3;
                    //txtresult.setText(totalSum.toString());
                    if (!TextUtils.isEmpty(edt4.getText().toString().trim())) {
                        edt5.setEnabled(true);

                        foodatabase.orderByChild("name").equalTo(list.get(3)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt4.getText()));
                                    sum4 += (usergram * carb) / 100;


                                    totalSum += sum4;
                                    temps4 = sum4;
                                    sum4 = 0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(decformat.format(totalSum));
                        sum4=0.0;
                        temps4=0.0;
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    sum5 = 0.0;
                    if(!edt6.getText().equals("")){
                        totalSum-=temps5;
                    }
                    //totalSum = temps1 + temps2+temps3+temps4;
                    //edt6.setEnabled(true);
                    //txtresult.setText(totalSum.toString());
                    if (!TextUtils.isEmpty(edt5.getText().toString().trim())) {
                        edt6.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(4)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt5.getText()));
                                    sum5 += (usergram * carb) / 100;

                                    totalSum += sum5;
                                    temps5 = sum5;
                                    sum5 = 0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(decformat.format(totalSum));
                        sum5=0.0;
                        temps5=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }else if (list.size()==6){
            txt1.setText(list.get(0));
            txt2.setText(list.get(1));
            txt3.setText(list.get(2));
            txt4.setText(list.get(3));
            txt5.setText(list.get(4));
            txt6.setText(list.get(5));
            l1.setVisibility(View.VISIBLE);
            l2.setVisibility(View.VISIBLE);
            l3.setVisibility(View.VISIBLE);
            l4.setVisibility(View.VISIBLE);
            l5.setVisibility(View.VISIBLE);
            l6.setVisibility(View.VISIBLE);
            edt1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!edt2.getText().equals("")||!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps1;
                    }
                    if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                        sum1=0.0;
                        //cal1=0.0;
                        //edt2.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    //cal=foods.getCal();

                                    Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                    sum1 += (usergram  * carb)/ 100;


                                    totalSum+=sum1;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps1=sum1;
                                    sum1=0.0;

                                    /*cal1+=(usergram*cal)/100;
                                    txtcalresult.setText(decformat.format(cal1));
                                    totalCal+=cal1;
                                    tempcal1=cal1;
                                    cal1=0.0;*/


                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt2.setEnabled(false);
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        sum1=0.0;
                        temps1=0.0;
                        txtresult.setText(decformat.format(totalSum));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum2=0.0;
                    //totalSum=temps1;
                    if(!edt3.getText().equals("")||!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps2;
                    }

                    if(!TextUtils.isEmpty(edt2.getText().toString().trim())) {
                        edt3.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt2.getText()));
                                    sum2 += (usergram  * carb)/ 100;


                                    totalSum+=sum2;
                                    txtresult.setText(decformat.format(totalSum));
                                    temps2=sum2;
                                    sum2=0.0;

                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt3.setEnabled(false);
                        //edt4.setEnabled(false);
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        sum2=0.0;
                        temps2=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sum3=0.0;
                    if(!edt4.getText().equals("")||!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps3;
                    }
                    //totalSum=temps1+temps2;
                    //txtresult.setText(totalSum.toString());
                    if(!TextUtils.isEmpty(edt3.getText().toString().trim())) {

                        edt4.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(2)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {
                                    //carbsList=new ArrayList<Double>();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);

                                        //Log.d(TAG,"Carb1"+carb1);
                                        //carbList.setValue(foods.getCarbs());
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt3.getText()));
                                    sum3 += (usergram  * carb)/ 100;


                                    totalSum+=sum3;
                                    temps3=sum3;
                                    sum3=0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        //edt6.setEnabled(false);
                        //edt5.setEnabled(false);
                        txtresult.setText(decformat.format(totalSum));
                        //txtresult.setHint("Enter gram");
                        sum3=0.0;
                        temps3=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    sum4 = 0.0;
                    if(!edt5.getText().equals("")||!edt6.getText().equals("")){
                        totalSum-=temps4;
                    }
                    //totalSum = temps1 + temps2+temps3;
                    //txtresult.setText(totalSum.toString());
                    if (!TextUtils.isEmpty(edt4.getText().toString().trim())) {
                        edt5.setEnabled(true);

                        foodatabase.orderByChild("name").equalTo(list.get(3)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt4.getText()));
                                    sum4 += (usergram * carb) / 100;


                                    totalSum += sum4;
                                    temps4 = sum4;
                                    sum4 = 0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(decformat.format(totalSum));
                        sum4=0.0;
                        temps4=0.0;
                        //edt5.setEnabled(false);
                        //edt6.setEnabled(false);
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    sum5 = 0.0;
                    if(!edt6.getText().equals("")){
                        totalSum-=temps5;
                    }
                    //totalSum = temps1 + temps2+temps3+temps4;
                    //edt6.setEnabled(true);
                    //txtresult.setText(totalSum.toString());
                    if (!TextUtils.isEmpty(edt5.getText().toString().trim())) {
                        edt6.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(4)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt5.getText()));
                                    sum5 += (usergram * carb) / 100;

                                    totalSum += sum5;
                                    temps5 = sum5;
                                    sum5 = 0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(decformat.format(totalSum));
                        sum5=0.0;
                        temps5=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            edt6.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    sum6 = 0.0;
                    totalSum = temps1 + temps2+temps3+temps4+temps5;
                    //edt6.setEnabled(true);
                    //txtresult.setText(totalSum.toString());
                    if (!TextUtils.isEmpty(edt6.getText().toString().trim())) {
                        //edt6.setEnabled(true);
                        foodatabase.orderByChild("name").equalTo(list.get(5)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        foods = dataSnapshot1.getValue(Foods.class);
                                    }
                                    carb = foods.getCarbs();
                                    Double usergram = Double.parseDouble(String.valueOf(edt6.getText()));
                                    sum6 += (usergram * carb) / 100;

                                    totalSum += sum6;
                                    temps6 = sum6;
                                    sum6 = 0.0;
                                    txtresult.setText(decformat.format(totalSum));
                                }


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }else {
                        txtresult.setText(decformat.format(totalSum));
                        sum6=0.0;
                        temps6=0.0;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FoodCalculation.this,MealActi.class);
                intent.putExtra("totcarb",totalSum);
                intent.putExtra("listfood",list);
                startActivity(intent);



            }
        });

    }

    public void init(){
        txt1=findViewById(R.id.txtfood1);
        txt2=findViewById(R.id.txtfood2);
        txt3=findViewById(R.id.txtfood3);
        txt4=findViewById(R.id.txtfood4);
        txt5=findViewById(R.id.txtfood5);
        txt6=findViewById(R.id.txtfood6);

        edt1=findViewById(R.id.edtfood1);
        edt2=findViewById(R.id.edtfood2);
        edt3=findViewById(R.id.edtfood3);
        edt4=findViewById(R.id.edtfood4);
        edt5=findViewById(R.id.edtfood5);
        edt6=findViewById(R.id.edtfood6);

        //edt2.setEnabled(false);
        //edt3.setEnabled(false);
        //edt4.setEnabled(false);
        //edt5.setEnabled(false);
        //edt6.setEnabled(false);

        l1=findViewById(R.id.l1);
        l2=findViewById(R.id.l2);
        l3=findViewById(R.id.l3);
        l4=findViewById(R.id.l4);
        l5=findViewById(R.id.l5);
        l6=findViewById(R.id.l6);

        btn=findViewById(R.id.btn);
        txtresult=findViewById(R.id.txtresult);
        txtcalresult=findViewById(R.id.txtcalresult);
        edt1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edt2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edt3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edt4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edt5.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edt6.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        /*edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(edt1.getText().toString().trim())) {
                    sum1=0.0;
                    foodatabase.orderByChild("name").equalTo(list.get(0)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                //carbsList=new ArrayList<Double>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    foods = dataSnapshot1.getValue(Foods.class);

                                    //Log.d(TAG,"Carb1"+carb1);
                                    //carbList.setValue(foods.getCarbs());
                                }
                                carb1 = foods.getCarbs();
                                Double usergram = Double.parseDouble(String.valueOf(edt1.getText()));
                                sum1 += (usergram  * carb1)/ 100;
                                txtresult.setText("" + sum1);
                                totalSum+=sum1;
                                temps1=sum1;
                                sum1=0.0;
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    txtresult.setText(""+0);
                    sum1=0.0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sum2=0.0;
                totalSum=temps1;

                if(!TextUtils.isEmpty(edt2.getText().toString().trim())) {

                    foodatabase.orderByChild("name").equalTo(list.get(1)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                //carbsList=new ArrayList<Double>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    foods = dataSnapshot1.getValue(Foods.class);

                                    //Log.d(TAG,"Carb1"+carb1);
                                    //carbList.setValue(foods.getCarbs());
                                }
                                carb2 = foods.getCarbs();
                                Double usergram = Double.parseDouble(String.valueOf(edt2.getText()));
                                sum2 += (usergram  * carb2)/ 100;

                                totalSum+=sum2;
                                temps2=sum2;
                                sum2=0.0;
                                txtresult.setText("" + totalSum);
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    txtresult.setText(totalSum.toString());
                    sum2=0.0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sum3=0.0;
                totalSum=temps1+temps2;
                txtresult.setText(totalSum.toString());
                if(!TextUtils.isEmpty(edt3.getText().toString().trim())) {

                    foodatabase.orderByChild("name").equalTo(list.get(2)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                //carbsList=new ArrayList<Double>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    foods = dataSnapshot1.getValue(Foods.class);

                                    //Log.d(TAG,"Carb1"+carb1);
                                    //carbList.setValue(foods.getCarbs());
                                }
                                carb3 = foods.getCarbs();
                                Double usergram = Double.parseDouble(String.valueOf(edt3.getText()));
                                sum3 += (usergram  * carb3)/ 100;

                                totalSum+=sum3;
                                temps3=sum3;
                                sum3=0.0;
                                txtresult.setText("" + totalSum);
                            }


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    txtresult.setText(totalSum.toString());
                    txtresult.setHint("Enter gram");
                    sum2=0.0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    public void gotoList(){
        Intent intent=new Intent(this,SearchFood.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,SearchFood.class);
        startActivity(intent);
        finish();
    }
}
