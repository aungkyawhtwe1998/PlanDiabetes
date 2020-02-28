package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.Class.Weight;
import com.example.plandiabetes.ListView.MedicationListView;
import com.example.plandiabetes.ListView.WeightListView;
import com.example.plandiabetes.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeightActi extends AppCompatActivity {
EditText edtwdate,edtwtime,edtwweight;
MaterialButton btnsave;
FirebaseAuth mAuth;
String personEmail,userid;
Calendar c;
String intentId;
    DatabaseReference databaseruser;
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
                gotoRecView();
                return true;
            case R.id.id_list:
                gotoRecView();
                return true;
            case R.id.id_delete:
                delete();
                return true;
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
                databaseruser.child(userid).child("updateweight").child(intentId).removeValue();
                dialog.dismiss();
                Toast.makeText(WeightActi.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
                gotoRecView();
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

        setContentView(R.layout.activity_weight);
        if(savedInstanceState==null){
            databaseruser= FirebaseDatabase.getInstance().getReference("User");
            databaseruser.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Weight");

        //init
        edtwdate=findViewById(R.id.edtwdate);
        edtwtime=findViewById(R.id.edtwtime);
        edtwweight=findViewById(R.id.edtwweight);
        edtwweight.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        btnsave=findViewById(R.id.btbsaveWeight);
        edtwdate.setText(getCurrentDate());
        edtwtime.setText(getCurrentTime());

        Intent intent=getIntent();
        final boolean isEdit=intent.getBooleanExtra("isEdit",false);
        if(isEdit){
            btnsave.setText("ြပင်မည်");
            getSupportActionBar().setTitle("ြပင်ရန်");
            Weight wintent= (Weight) intent.getSerializableExtra("obj");
            edtwdate.setText(wintent.getDate().toString());
            edtwweight.setText(wintent.getWeight().toString());
            edtwtime.setText(wintent.getTime().toString());
            intentId=wintent.getId();
        }


        mAuth= FirebaseAuth.getInstance();

        edtwdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int myear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mday = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(WeightActi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtwdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);

                    }
                },myear,mMonth,mday);
                datePickerDialog.show();

            }
        });

        edtwtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int mhour=cal.get(Calendar.HOUR_OF_DAY);
                int mMinute=cal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(WeightActi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c=Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);
                        updateTimeText(c);
                        /*String time = hourOfDay + ":" + minute;
                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = fmt.parse(time );
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }
                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                        String formattedTime=fmtOut.format(date);

                        edtmtime.setText(formattedTime);*/
                    }
                },mhour,mMinute,false);
                timePickerDialog.show();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isEdit){
                    Weight w=new Weight(intentId,edtwdate.getText().toString(),edtwtime.getText().toString(),Double.parseDouble(edtwweight.getText().toString()));
                    databaseruser.child(userid).child("updateweight").child(intentId).setValue(w);
                    //saveReminder();

                    Toast.makeText(WeightActi.this,"Update Success",Toast.LENGTH_SHORT).show();
                    gotoRecView();


                }else if(!edtwweight.getText().toString().equals("") ){
                    String wid=databaseruser.child(userid).child("updateweight").push().getKey();
                    Weight w=new Weight(wid,edtwdate.getText().toString(),edtwtime.getText().toString(),Double.parseDouble(edtwweight.getText().toString()));
                    databaseruser.child(userid).child("updateweight").child(wid).setValue(w);
                    databaseruser.child(userid).child("weight").setValue(w.getWeight());
                    Toast.makeText(WeightActi.this,"Success",Toast.LENGTH_SHORT).show();
                    gotoRecView();
                    //    gotoReminder();
                    //updateTimeText(c);
                    //startAlarm(c);
                }else {
                    edtwweight.requestFocus();
                }
            }
        });
    }

    private void updateTimeText(Calendar c) {
        String timeText= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        edtwtime.setText(timeText);
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

    public void gotoRecView(){
        Intent intent=new Intent(this, WeightListView.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser acct = mAuth.getCurrentUser();
        if (acct != null) {
            personEmail = acct.getEmail();
            databaseruser.orderByChild("email").equalTo(personEmail).addListenerForSingleValueEvent(new ValueEventListener() {
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


}
