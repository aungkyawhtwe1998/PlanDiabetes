package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.plandiabetes.Class.Foods;
import com.example.plandiabetes.Class.Meal;
import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.ListView.ExerciseListView;
import com.example.plandiabetes.ListView.MealListView;
import com.example.plandiabetes.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MealActi extends AppCompatActivity {
    EditText txtcarbs,txtcal,edtdate,edttime,edtnote;
    MaterialButton btnmealsave;
    DatabaseReference userdatabase;
    DecimalFormat decimalFormat;
    DatabaseReference foodatabase;
    FirebaseAuth mAuth;
    String personEmail,userid;
    String intentId;
    private final String TAG="MealActi";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu,menu);
        if(intentId!=null){
            menu.findItem(R.id.id_delete).setVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                gotoList();
                return true;
            case R.id.id_list:
                gotoList();
                return true;
            case R.id.id_delete:
                delete();
                default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void delete() {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userdatabase.child(userid).child("meal").child(intentId).removeValue();
                dialog.dismiss();
                Toast.makeText(MealActi.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
                gotoList();
            }
        });
        buidler.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(GlucolseListView.this, "This is the Negative button", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = buidler.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_meal);
        if(savedInstanceState==null){
            foodatabase= FirebaseDatabase.getInstance().getReference("Food");
            userdatabase=FirebaseDatabase.getInstance().getReference("User");
            foodatabase.keepSynced(true);
            userdatabase.keepSynced(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Meal");

        init();
        decimalFormat=new DecimalFormat("0.00");
        Intent intent=getIntent();
        Intent intent1=getIntent();
        Double totcarb=intent.getDoubleExtra("totcarb",0.0);
        txtcarbs.setText(decimalFormat.format(totcarb));
        final boolean isEdit=intent.getBooleanExtra("isEdit",false);
        if(isEdit){
            btnmealsave.setText("ြပင်မည်");
            getSupportActionBar().setTitle("ြပင်ရန်");
            Meal eintent=(Meal) getIntent().getSerializableExtra("obj");
            edtdate.setText(eintent.getDate().toString());
            edttime.setText(eintent.getTime().toString());
            edtnote.setText(eintent.getNote().toString());
            txtcarbs.setText(eintent.getTotcarb().toString());
            //txtcal.setText(eintent.getTotcal().toString());
            intentId=eintent.getId();
        }
        mAuth=FirebaseAuth.getInstance();
        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int myear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mday = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(MealActi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },myear,mMonth,mday);
                datePickerDialog.show();
            }
        });

        edttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                int mhour=c.get(Calendar.HOUR_OF_DAY);
                int mMinute=c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(MealActi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;

                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = fmt.parse(time );
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                        String formattedTime=fmtOut.format(date);

                        edttime.setText(formattedTime);

                    }
                },mhour,mMinute,false);
                timePickerDialog.show();
            }
        });


        /*ArrayList<String> list=(ArrayList<String>) getIntent().getSerializableExtra("chiplist");
        Toast.makeText(this,list.toString(),Toast.LENGTH_LONG).show();
        for(int i=0;i<list.size();i++){
            Chip chip = new Chip(this);
            chip.setText(list.get(i));
            mchipGroup.addView(chip);
            foodatabase.orderByChild("name").equalTo(list.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    calList.clear();
                    sugarList.clear();
                    carbsList.clear();
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        foods=dataSnapshot1.getValue(Foods.class);
                        calList.add(foods.getCal());
                        sugarList.add(foods.getSugar());
                        carbsList.add(foods.getCarbs());
                    }
                    Log.d(TAG,"Total Cal:"+calList);
                    for(Double d : calList) {
                        calSum += d;
                        Log.d(TAG,"Cal List:"+calSum);
                        txtcal.setText(format.format(calSum));
                    }
                    Log.d(TAG,"Carb List:"+carbsList);
                    for(Double c : carbsList) {
                        carbSum += c;
                        Log.d(TAG,"Carb Sum:"+carbSum);
                        txtcarbs.setText(format.format(carbSum));
                    }
                    Log.d(TAG,"Sugar List:"+sugarList);
                    for(Double b : sugarList) {
                        sugarSum += b;
                        Log.d(TAG,"Sugar sum:"+sugarSum);
                        txtsugar.setText(format.format(sugarSum));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }*/
        //Log.d(TAG,"List of selected:"+foods.getCal());
        //Log.d(TAG,"ListCal:"+calList);
        btnmealsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEdit){
                    Meal meal=new Meal(intentId,edtdate.getText().toString(), edttime.getText().toString(), edtnote.getText().toString(), Double.parseDouble(String.valueOf(txtcarbs.getText())));
                    userdatabase.child(userid).child("meal").child(intentId).setValue(meal);
                    Toast.makeText(MealActi.this,"Update Success",Toast.LENGTH_SHORT).show();
                    gotoList();

                }else if(!edtnote.getText().toString().equals("")){

                    String mealid = userdatabase.child(userid).child("meal").push().getKey();
                    Meal meal = new Meal(mealid, edtdate.getText().toString(), edttime.getText().toString(), edtnote.getText().toString(), Double.parseDouble(String.valueOf(txtcarbs.getText())));
                    userdatabase.child(userid).child("meal").child(mealid).setValue(meal);
                    Toast.makeText(MealActi.this, "Success", Toast.LENGTH_SHORT).show();
                    gotoList();

                }else {
                    edtnote.requestFocus();
                }

                //databasemedi.child(meid).setValue(medication);
            }

        });



    }

    private void init(){
        txtcarbs=findViewById(R.id.txt_totalCarbs);
        txtcal=findViewById(R.id.txt_mealtotalCal);
        edtdate=findViewById(R.id.edt_mealdate);
        edttime=findViewById(R.id.edt_mealtime);
        edtnote=findViewById(R.id.edt_mealnote);
        btnmealsave=findViewById(R.id.btnsavemeal);
        edtdate.setText(getCurrentDate());
        edttime.setText(getCurrentTime());


    }

    public String getCurrentDate()
    {
        String am_pm;
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        if (c.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";
        else if (c.get(Calendar.AM_PM) == Calendar.PM)
            am_pm = "PM";

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }

    public String getCurrentTime(){
        Calendar c=Calendar.getInstance();
        System.out.println("Current time"+c.getTime());
        SimpleDateFormat df=new SimpleDateFormat("hh:mm aa", Locale.US);
        String formatTime=df.format(c.getTime());
        return formatTime;
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser acct = mAuth.getCurrentUser();
        if (acct != null) {
            personEmail = acct.getEmail();
            userdatabase.orderByChild("email").equalTo(personEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //fireUser = child.getValue(FireUser.class);
                        userid=child.getKey();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Setting", "Failed to read value.", databaseError.toException());
                }
            });
        }
    }
    public void goToCalculator(){
        Intent intent=new Intent(MealActi.this,SearchFood.class);
        startActivity(intent);
        finish();
    }
    public void gotoList(){
        Intent intent=new Intent(MealActi.this,MealListView.class);
        startActivity(intent);
        finish();
    }
}
