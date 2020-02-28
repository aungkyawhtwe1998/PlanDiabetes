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

import com.example.plandiabetes.Activity.GlucoseActi;
import com.example.plandiabetes.Adapter.Glucose_Adapter;
import com.example.plandiabetes.Class.FireUser;
import com.example.plandiabetes.Class.Glucose;
import com.example.plandiabetes.Interface.glucoseItemClickListener;
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
import java.util.Locale;

public class GlucolseListView extends AppCompatActivity implements glucoseItemClickListener {

    DatabaseReference userdatabase;
    int REQ_GLUCOSE=1;
    FirebaseAuth firebaseAuth;
    String personemail;
    List<Glucose> glucoseList;
    RecyclerView glucose_recview;
    Glucose_Adapter gAdapter;
    public static String userid;
    private final String TAG="Glucose list";

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
                gotogActi();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK && requestCode==REQ_GLUCOSE && data!=null){
            binData();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glucolse_list_view);
        if(savedInstanceState==null){
            userdatabase=FirebaseDatabase.getInstance().getReference("User");
            userdatabase.keepSynced(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Glucose Record List");
        glucose_recview = findViewById(R.id.glucose_recview);
        glucose_recview.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        glucose_recview.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();
        //glucose_recview.setAdapter(gAdapter);

    }

    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                glucoseList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Glucose glucose=dataSnapshot1.getValue(Glucose.class);
                    glucoseList.add(glucose);
                    binData();
                    Log.d("GlucoseRetrieve","amount:"+glucose.getGlucoseLevel());

                }
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void binData(){
        gAdapter=new Glucose_Adapter(glucoseList,GlucolseListView.this);
        glucose_recview.setAdapter(gAdapter);
        gAdapter.notifyDataSetChanged();
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
                        Query query=userdatabase.child(userid).child("glucose");
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

    public void gotogActi(){
        Intent intent=new Intent(this, GlucoseActi.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onItemClick(Glucose glucose) {

    }

    @Override
    public void onEdit(Glucose glucose, int index) {

        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ပြင်ဆင်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ြပင်မလား");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(GlucolseListView.this,GlucoseActi.class);
                intent.putExtra("obj",(Serializable)glucose);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent,REQ_GLUCOSE);
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
    public void onDelete(Glucose glucose, int index) {

        /*DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User");
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(userid).child("glucose").child(glucose.getId()).removeValue();
                binData();
                dialog.dismiss();
                Toast.makeText(GlucolseListView.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
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
