package com.example.plandiabetes.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.example.plandiabetes.Activity.MedicationActi;
import com.example.plandiabetes.Adapter.MedicationAdapter;
import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.Interface.MedicationItemCLickListener;
import com.example.plandiabetes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicationListView extends AppCompatActivity implements MedicationItemCLickListener {
    RecyclerView medi_recview;
    int REQ_MED=1;
    MedicationAdapter mediAdapter;
    FirebaseAuth firebaseAuth;
    DatabaseReference userdatabase;
    List<Medication> medicationList;
    String userid;
    String personemail;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.glu_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.add_menu:
                gotoMedActi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list_view);
        if(savedInstanceState==null){
            userdatabase=FirebaseDatabase.getInstance().getReference("User");
            userdatabase.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Medication List");
        medi_recview = findViewById(R.id.medication_recview);
        medi_recview.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        medi_recview.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();

    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                medicationList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Medication medication=dataSnapshot1.getValue(Medication.class);
                    medicationList.add(medication);
                    mediAdapter=new MedicationAdapter(medicationList,MedicationListView.this);
                    medi_recview.setAdapter(mediAdapter);
                    mediAdapter.notifyDataSetChanged();
                    Log.d("GlucoseRetrieve","amount:"+medication.getName());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser acct=firebaseAuth.getCurrentUser();
        if(acct!=null){
            personemail=acct.getEmail();
            //Toast.makeText(this,"email:"+personemail,Toast.LENGTH_SHORT).show();
            userdatabase.orderByChild("email").equalTo(personemail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //fireUser=child.getValue(FireUser.class);
                        userid=child.getKey();
                        Query query=userdatabase.child(userid).child("medication");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        Log.d("userid",userid);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Setting", "Failed to read value.", databaseError.toException());
                }
            });
        }

    }
    public void gotoMedActi(){
        Intent intent=new Intent(this, MedicationActi.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(Medication medication) {
        Intent intent=new Intent(this, MedicationActi.class);
        intent.putExtra("obj",(Serializable) medication);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEdit(Medication medication, int index) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ပြင်ဆင်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ြပင်မလား");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(MedicationListView.this,MedicationActi.class);
                intent.putExtra("obj",(Serializable)medication);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent,REQ_MED);
                finish();
                dialog.dismiss();

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
    public void onDelete(Medication medication, int index) {

        //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User");
       /* AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userdatabase.child(userid).child("medication").child(medication.getMid()).removeValue();
                binData();
                dialog.dismiss();
                Toast.makeText(MedicationListView.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
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
        dialog.show();*/
    }

}
