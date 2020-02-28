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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.allyants.notifyme.NotifyMe;
import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.ListView.MedicationListView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MedicationActi extends AppCompatActivity{
    EditText edtmdate,edtmtime,edtmname,edtmpower,edtmnote;
    Spinner spinreminder,spnrmediunit;
    MaterialButton btnmedisav;
    DatabaseReference databaseruser;
    FirebaseAuth mAuth;
    String personEmail,userid;
    String intentId;
    Calendar c=Calendar.getInstance();
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
                databaseruser.child(userid).child("medication").child(intentId).removeValue();
                dialog.dismiss();
                Toast.makeText(MedicationActi.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_medication);
        if(savedInstanceState==null){
            databaseruser= FirebaseDatabase.getInstance().getReference("User");
            databaseruser.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Medication");
        init();
        Intent intent=getIntent();
        final boolean isEdit=intent.getBooleanExtra("isEdit",false);

        //createNotificationChanel();
        if(isEdit){
            btnmedisav.setText("ြပင်မည်");
            getSupportActionBar().setTitle("ြပင်ရန်");
            Medication mintent= (Medication) intent.getSerializableExtra("obj");
            edtmdate.setText(mintent.getDate().toString());
            edtmpower.setText(mintent.getPower().toString());
            edtmtime.setText(mintent.getTime().toString());
            edtmname.setText(mintent.getName().toString());
            edtmnote.setText(mintent.getNote().toString());
            //String compareValue=mintent.getReminder();
            String compareValue1=mintent.getUnit();
            //spinreminder.setSelection(getIndex(spinreminder,compareValue));
            spnrmediunit.setSelection(getIndex1(spnrmediunit,compareValue1));
            intentId=mintent.getMid();
        }

        mAuth=FirebaseAuth.getInstance();

        edtmdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int myear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mday = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(MedicationActi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edtmdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);


                    }
                },myear,mMonth,mday);
                datePickerDialog.show();

            }
        });

        edtmtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int mhour=cal.get(Calendar.HOUR_OF_DAY);
                int mMinute=cal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog=new TimePickerDialog(MedicationActi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //c=Calendar.getInstance();
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

        btnmedisav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String power=edtmpower.getText().toString();
                String unit=spnrmediunit.getSelectedItem().toString();
                if(isEdit){
                    Medication medication=new Medication(intentId, edtmname.getText().toString(),unit, power,edtmdate.getText().toString(), edtmtime.getText().toString(), edtmnote.getText().toString());
                    databaseruser.child(userid).child("medication").child(intentId).setValue(medication);
                    //saveReminder();
                    updateTimeText(c);
                    startAlarm(c);
                    Toast.makeText(MedicationActi.this,"Update Success",Toast.LENGTH_SHORT).show();
                    gotoRecView();


                }else if(!edtmtime.getText().toString().equals("") && !edtmname.getText().toString().equals("") && !edtmnote.getText().toString().equals("")){
                    String meid=databaseruser.child(userid).child("medication").push().getKey();
                    Medication medication=new Medication(meid, edtmname.getText().toString(),unit, power,edtmdate.getText().toString(), edtmtime.getText().toString(), edtmnote.getText().toString());
                    databaseruser.child(userid).child("medication").child(meid).setValue(medication);
                    Toast.makeText(MedicationActi.this,"Success",Toast.LENGTH_SHORT).show();
                    updateTimeText(c);
                    startAlarm(c);
                    gotoRecView();
                    //    gotoReminder();

                }else {
                    edtmname.requestFocus();
                    edtmname.requestFocus();
                }

                //databasemedi.child(meid).setValue(medication);
            }
        });

    }


    public void updateTimeText(Calendar c){
        String timeText= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        edtmtime.setText(timeText);
    }

    public void startAlarm(Calendar c){
        String meddetail=edtmname.getText().toString()+", "+edtmpower.getText().toString();
        NotifyMe notifyMe=new NotifyMe.Builder(getApplicationContext())
                .title("ေဆးေသာက်ရန်")
                .content(meddetail)
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(c.getTime())
                .addAction(new Intent(),"Dismiss",true,false)
                .addAction(new Intent(),"Done")
                .large_icon(R.drawable.ic_medicines)
                .build();


       /* AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent1=new Intent(this,ReminderBroadcast.class);
        intent1.putExtra("meddetail",meddetail );
        PendingIntent pendingIntent1= PendingIntent.getBroadcast(this,0,intent1,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent1);*/
    }


    public void init(){
        edtmdate=(EditText)findViewById(R.id.edt_medate);
        edtmname=(EditText)findViewById(R.id.edt_mename);
        edtmtime=(EditText)findViewById(R.id.edt_metime);
        edtmpower=(EditText)findViewById(R.id.edt_mepower);
        edtmnote=(EditText)findViewById(R.id.edt_menote);
        btnmedisav=(MaterialButton) findViewById(R.id.btnsavemedi);
        spnrmediunit=(Spinner)findViewById(R.id.spinner_meunit);
        spinreminder=(Spinner)findViewById(R.id.spnr_mereminder);
        edtmdate.setText(getCurrentDate());
        //edtmtime.setText(getCurrentTime());
        edtmpower.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        List<String> listperod = new ArrayList<>();
        listperod.add("mg");
        listperod.add("unit");
        listperod.add("tablet");
        listperod.add("mL");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listperod);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrmediunit.setAdapter(dataAdapter);

        List<String> lstreminder=new ArrayList<>();
        lstreminder.add("10 minutes");
        lstreminder.add("30 minutes");
        lstreminder.add("45 minutes");
        lstreminder.add("1 hour");
        lstreminder.add("2 hour");
        lstreminder.add("3 hours");
        lstreminder.add("4 hours");
        ArrayAdapter<String> minAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lstreminder);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinreminder.setAdapter(minAdapter);

    }

    /*private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinreminder.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }*/

    private int getIndex1(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spnrmediunit.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
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

    public void gotoRecView(){
        Intent intent=new Intent(this, MedicationListView.class);
        startActivity(intent);
        finish();
    }

}
