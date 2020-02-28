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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.plandiabetes.Class.FireUser;
import com.example.plandiabetes.Class.Glucose;
import com.example.plandiabetes.ListView.GlucolseListView;
import com.example.plandiabetes.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GlucoseActi extends AppCompatActivity {
    EditText edtdate,edttime,edtnote,edtglucose;
    Spinner spinerperoid,unitspinner;
    MaterialButton btnsave;
    DatabaseReference glucoseDatabase;
    FirebaseAuth mAuth;
    List<Glucose> glucoseList;
    List<FireUser> fireUserList;
    public String personEmail,userid;

    private static final String TAG = "Glucose Acti";
    DatabaseReference databaseuser;
    String intentId;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.id_list:
                gotoRecview();
                return true;
            case R.id.id_delete:
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucoseacti);


        if(savedInstanceState==null){
            databaseuser=FirebaseDatabase.getInstance().getReference("User");
            databaseuser.keepSynced(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Glucose");
        init();
        Intent intent=getIntent();
        final boolean isEdit=intent.getBooleanExtra("isEdit",false);
        if(isEdit){
            btnsave.setText("ြပင်မည်");
            getSupportActionBar().setTitle("ြပင်ရန်");
            Glucose gintent=(Glucose)getIntent().getSerializableExtra("obj");
            edtglucose.setText(gintent.getGlucoseLevel().toString());
            edtdate.setText(gintent.getDate().toString());
            edttime.setText(gintent.getTime().toString());
            edtnote.setText(gintent.getNotes().toString());

            intentId=gintent.getId();
            Toast.makeText(this,"id"+intentId,Toast.LENGTH_SHORT).show();
            String compareValue = gintent.getPeriod();
            spinerperoid.setSelection(getIndex(spinerperoid,compareValue));
            String unitcompareValue=gintent.getUnit();
            unitspinner.setSelection(getIndex(unitspinner,unitcompareValue));
        }


        glucoseList=new ArrayList<>();
        fireUserList=new ArrayList<>();
        mAuth=FirebaseAuth.getInstance();

        //Database database = Database.getDatabase(HotelActivity.this);
        //List<Hotel> hotel = database.hotelDao().retrieveAllHotel();
        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int myear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mday = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog=new DatePickerDialog(GlucoseActi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //SimpleDateFormat fmt = new SimpleDateFormat("dd:");
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
                TimePickerDialog timePickerDialog=new TimePickerDialog(GlucoseActi.this, new TimePickerDialog.OnTimeSetListener() {
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

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit){
                    Glucose glucose=new Glucose(intentId, edtdate.getText().toString(), edttime.getText().toString(),spinerperoid.getSelectedItem().toString(), edtnote.getText().toString(),unitspinner.getSelectedItem().toString(), Double.parseDouble(String.valueOf(edtglucose.getText())));
                    databaseuser.child(userid).child("glucose").child(intentId).setValue(glucose);
                    Toast.makeText(GlucoseActi.this,"Updated Success",Toast.LENGTH_SHORT).show();
                    gotoRecview();

                }else if(!edtglucose.getText().toString().equals("")&& !edtnote.getText().toString().equals("")){
                    String gid=databaseuser.child(userid).child("glucose").push().getKey();
                    Glucose glucose=new Glucose(gid, edtdate.getText().toString(), edttime.getText().toString(),spinerperoid.getSelectedItem().toString(), edtnote.getText().toString(),unitspinner.getSelectedItem().toString(),Double.parseDouble(String.valueOf(edtglucose.getText())));
                    databaseuser.child(userid).child("glucose").child(gid).setValue(glucose);
                    Toast.makeText(GlucoseActi.this,"Success",Toast.LENGTH_SHORT).show();
                    gotoRecview();
                }else {
                    edtglucose.setError("Fill Glucose");
                    edtglucose.requestFocus();
                    edtnote.requestFocus();
                    edtnote.setError("Fill note");
                }


                //Glucose glucose=new Glucose(gid, edtdate.getText().toString(), edttime.getText().toString(),spinerperoid.getSelectedItem().toString(), edtnote.getText().toString(), Double.parseDouble(String.valueOf(edtglucose.getText())));
                //databaseglu.child(gid).setValue(glucose);


            }
        });
    }

    public void init(){
        edtdate=findViewById(R.id.edt_date2);
        edttime=findViewById(R.id.edt_time2);
        edtglucose=findViewById(R.id.edt_calories);
        spinerperoid=findViewById(R.id.spinersport);
        edtnote=findViewById(R.id.edt_note2);
        btnsave=findViewById(R.id.btnsavemedi);
        edtglucose.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edtdate.setText(getCurrentDate());
        edttime.setText(getCurrentTime());
        unitspinner=findViewById(R.id.spinner_unit);
        List<String> listperod = new ArrayList<>();
        listperod.add("အစာစားပြီး");
        listperod.add("အစာမစားခင်");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listperod);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerperoid.setAdapter(dataAdapter);

        List<String> listunit = new ArrayList<>();
        listunit.add("mg/dL");
        listunit.add("mmol/L");
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listunit);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitspinner.setAdapter(unitAdapter);


    }

    public String getCurrentDate()
    {
        String am_pm;
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu,menu);
        if(intentId!=null){
            menu.findItem(R.id.id_delete).setVisible(true);
        }
        return true;
    }
    private void delete() {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseuser.child(userid).child("glucose").child(intentId).removeValue();
                dialog.dismiss();
                Toast.makeText(GlucoseActi.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
                gotoRecview();
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
    public void onStart() {
        super.onStart();
        FirebaseUser acct = mAuth.getCurrentUser();
        if (acct != null) {
            personEmail = acct.getEmail();
            databaseuser.orderByChild("email").equalTo(personEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //fireUser=child.getValue(FireUser.class);
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

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinerperoid.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    private int getunitIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (unitspinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    public void gotoRecview(){
        Intent intent=new Intent(this, GlucolseListView.class);
        startActivity(intent);
        finish();
    }
}
