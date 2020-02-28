package com.example.plandiabetes.ListView;

import androidx.annotation.NonNull;
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
import com.example.plandiabetes.Activity.WeightActi;
import com.example.plandiabetes.Adapter.MedicationAdapter;
import com.example.plandiabetes.Adapter.WeightAdapter;
import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.Class.Weight;
import com.example.plandiabetes.Interface.WeightItemClickListener;
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

public class WeightListView extends AppCompatActivity implements WeightItemClickListener {

    FirebaseAuth firebaseAuth;
    DatabaseReference userdatabase;
    String userid,personemail;

    RecyclerView weight_recview;
    int REQ_W=1;
    WeightAdapter weightAdapter;
    List<Weight> weightList;
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
                gotoWActi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void gotoWActi() {
        Intent intent=new Intent(this, WeightActi.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_list_view);
        if(savedInstanceState==null){
            userdatabase= FirebaseDatabase.getInstance().getReference("User");
            userdatabase.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Weight List");
        weight_recview = findViewById(R.id.weight_recview);
        weight_recview.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        weight_recview.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
    }

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
                        Query query=userdatabase.child(userid).child("updateweight");
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
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                weightList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Weight weight = dataSnapshot1.getValue(Weight.class);
                    weightList.add(weight);
                    weightAdapter = new WeightAdapter(weightList, WeightListView.this);
                    weight_recview.setAdapter(weightAdapter);
                    weightAdapter.notifyDataSetChanged();
                    Log.d("WeightRetrieve", "weight:" + weight.getWeight());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public void OnEdit(Weight w, int index) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ပြင်ဆင်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ြပင်မလား");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(WeightListView.this,WeightActi.class);
                intent.putExtra("obj",(Serializable)w);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent,REQ_W);
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
    public void OnDelete(Weight w, int index) {

        //DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User");
        /*AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userdatabase.child(userid).child("medication").child(w.getId()).removeValue();
                dialog.dismiss();
                Toast.makeText(WeightListView.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
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
