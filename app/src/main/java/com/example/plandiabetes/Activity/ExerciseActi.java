package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.allyants.notifyme.NotifyMe;
import com.example.plandiabetes.Class.Exercise;
import com.example.plandiabetes.Class.FireUser;
import com.example.plandiabetes.ListView.ExerciseListView;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ExerciseActi extends AppCompatActivity {
    EditText edtdate,edttime,edtminute,edtcal,edtnote,edtkm;
    Spinner reminderspinner,edtexetypespinner;
    MaterialButton savebtn;
    FirebaseAuth mAuth;
    String personEmail;
    DatabaseReference databaseuser;
    String userid;
    String intentId;
    DecimalFormat decimalFormat;
    FireUser fireUser;
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
                databaseuser.child(userid).child("exercise").child(intentId).removeValue();
                dialog.dismiss();
                Toast.makeText(ExerciseActi.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
                gotoRecView();
            }
        });
        buidler.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(GlucolseListVieလုပ်ရန်w.this, "This is the Negative button", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_exercise);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activity");
        if (savedInstanceState == null) {
            databaseuser = FirebaseDatabase.getInstance().getReference("User");
            databaseuser.keepSynced(true);
        }
        decimalFormat=new DecimalFormat("0.00");
        mAuth = FirebaseAuth.getInstance();
        init();
        Intent intent = getIntent();
        final boolean isEdit = intent.getBooleanExtra("isEdit", false);
        if (isEdit) {
            savebtn.setText("ြပင်မည်");
            getSupportActionBar().setTitle("ြပင်ရန်");
            Exercise eintent = (Exercise) getIntent().getSerializableExtra("obj");
            edtcal.setText(eintent.getCal().toString());
            edtdate.setText(eintent.getDate().toString());
            edttime.setText(eintent.getTime().toString());
            edtminute.setText(eintent.getMinute().toString());
            edtnote.setText(eintent.getNote().toString());
            //String compareValue = eintent.getReminder();
            //reminderspinner.setSelection(getIndex(reminderspinner, compareValue));
            String compareValue1 = eintent.getType();
            edtexetypespinner.setSelection(getIndex1(edtexetypespinner, compareValue1));
            intentId = eintent.getId();
        }


        edtdate.setText(getCurrentDate());
        //edttime.setText(getCurrentTime());
        edtminute.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtcal.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtkm.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int myear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mday = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ExerciseActi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        edtdate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        c.set(Calendar.YEAR,year);
                        c.set(Calendar.MONTH,month);
                        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    }
                }, myear, mMonth, mday);
                datePickerDialog.show();
            }
        });

        edttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int mhour = cal.get(Calendar.HOUR_OF_DAY);
                int mMinute = cal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(ExerciseActi.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);
                        updateTimeText(c);
                        /*String time = hourOfDay + ":" + minute;

                        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm");
                        Date date = null;
                        try {
                            date = fmt.parse(time);
                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm aa");

                        String formattedTime = fmtOut.format(date);

                        edttime.setText(formattedTime);*/

                    }
                }, mhour, mMinute, false);
                timePickerDialog.show();
            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    Exercise exercise = new Exercise(intentId, edtdate.getText().toString(), edttime.getText().toString(), edtexetypespinner.getSelectedItem().toString(),edtnote.getText().toString(),  Double.parseDouble(String.valueOf(edtcal.getText())),Integer.parseInt(String.valueOf(edtminute.getText())));
                    databaseuser.child(userid).child("exercise").child(intentId).setValue(exercise);
                    Toast.makeText(ExerciseActi.this, "Updated Success", Toast.LENGTH_SHORT).show();
                    updateTimeText(c);
                    startAlarm(c);
                    gotoRecView();
                } else if (!edttime.getText().toString().equals("") && !edtminute.getText().toString().equals("") && !edtnote.getText().toString().equals("")) {
                    String exeid = databaseuser.child(userid).child("exercise").push().getKey();
                    Exercise exercise = new Exercise(exeid, edtdate.getText().toString(), edttime.getText().toString(), edtexetypespinner.getSelectedItem().toString(),edtnote.getText().toString(),  Double.parseDouble(String.valueOf(edtcal.getText())),Integer.parseInt(String.valueOf(edtminute.getText())));
                    databaseuser.child(userid).child("exercise").child(exeid).setValue(exercise);
                    Toast.makeText(ExerciseActi.this, "Success", Toast.LENGTH_SHORT).show();
                    updateTimeText(c);
                    startAlarm(c);
                    gotoRecView();

                } else {
                    Toast.makeText(ExerciseActi.this, "Please fill completely", Toast.LENGTH_SHORT).show();
                    edtnote.requestFocus();
                    edtminute.requestFocus();
                    edttime.requestFocus();
                }

            }
        });

    }

    public void updateTimeText(Calendar c){
        String timeText= DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        edttime.setText(timeText);
    }

    public void startAlarm(Calendar c){
        String meddetail=edtexetypespinner.getSelectedItem().toString()+", "+edtminute.getText().toString()+"minutes";
        NotifyMe notifyMe=new NotifyMe.Builder(getApplicationContext())
                .title("လေ့ကျင့်ခန်း လုပ်ရန်")
                .content(meddetail)
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(c.getTime())
                .addAction(new Intent(),"Dismiss",true,false)
                .addAction(new Intent(),"Done")
                .large_icon(R.drawable.ic_medicines)
                .build();
    }

    public void init(){
        edtcal=(EditText)findViewById(R.id.edt_calories);
        edtkm=findViewById(R.id.edt_km);
        edtdate=(EditText)findViewById(R.id.edt_date2);
        edttime=(EditText)findViewById(R.id.edt_time2);
        edtexetypespinner=(Spinner)findViewById(R.id.spinner_type);
        edtminute=(EditText)findViewById(R.id.edt_minute);
        //reminderspinner=(Spinner)findViewById(R.id.edt_minute);
        edtnote=(EditText)findViewById(R.id.edt_note2);
        savebtn=(MaterialButton) findViewById(R.id.btnsavemedi);



        List<String> listperod = new ArrayList<>();
        listperod.add("walking");
        listperod.add("running");
        listperod.add("bicycling");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listperod);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtexetypespinner.setAdapter(dataAdapter);

        /*List<Integer> lstreminder=new ArrayList<>();
        lstreminder.add(10);
        lstreminder.add(15);
        lstreminder.add(20);
        lstreminder.add(30);
        lstreminder.add(45);
        lstreminder.add(60);
        lstreminder.add(90);
        ArrayAdapter<Integer> minAdapter=new ArrayAdapter<Integer>(this,android.R.layout.simple_list_item_1,lstreminder);
        minAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderspinner.setAdapter(minAdapter);*/

    }

    /*private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (reminderspinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }*/

    private int getIndex1(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (edtexetypespinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
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
            databaseuser.orderByChild("email").equalTo(personEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        userid=child.getKey();
                        FireUser fireUser=child.getValue(FireUser.class);
                        Double weight=fireUser.getWeight();
                        Double height=fireUser.getHeight();
                        caluclateal(weight,height);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Setting", "Failed to read value.", databaseError.toException());
                }
            });
        }
    }

    private void caluclateal(Double weight,Double height) {
        Double finalWeight=weight*0.45359237;
        Double finalHeight=height/3.28;

        edtminute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(edtminute.getText().toString().trim())){
                    Double duration=Double.parseDouble(String.valueOf(edtminute.getText()));
                    Log.d("Exercise", "Height: "+finalHeight);
                    Log.d("Exercise", "Weight: "+finalWeight);
                    Log.d("Exercise", "duration: "+duration);
                    if (edtexetypespinner.getSelectedItem().equals("walking")) {
                        Double result=duration*((finalWeight*3.5*3)/200);
                        edtcal.setText(decimalFormat.format(result));
                    }
                    else if (edtexetypespinner.getSelectedItem().equals("running")){
                        Double result1=duration*((finalWeight*3.5*8)/200);
                        edtcal.setText(decimalFormat.format(result1));
                    }
                    else if (edtexetypespinner.getSelectedItem().equals("bicycling")){
                        Double result2=duration*((finalWeight*3.5*8)/200);
                        edtcal.setText(decimalFormat.format(result2));
                    }
                    else{
                    }
                }
                else {
                    edtcal.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void gotoRecView(){
        Intent intent=new Intent(this,ExerciseListView.class);
        startActivity(intent);
        finish();
    }
}
