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

import com.example.plandiabetes.Activity.ExerciseActi;
import com.example.plandiabetes.Activity.GlucoseActi;
import com.example.plandiabetes.Adapter.ExerciseAdapter;
import com.example.plandiabetes.Class.Exercise;
import com.example.plandiabetes.Interface.exeItemClickListener;
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

public class ExerciseListView extends AppCompatActivity implements exeItemClickListener {
    private RecyclerView exe_recview;
    int REQ_EXE=1;
    private FirebaseAuth firebaseAuth;
    private List<Exercise> exerciseList=new ArrayList<>();
    private DatabaseReference userdatabase;
    private ExerciseAdapter eAdaper;
    private String personemail;
    private String userid;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

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
                gotogEcti();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list_view);
        getSupportActionBar().setTitle("Exercise List");
        if(savedInstanceState==null){
            userdatabase=FirebaseDatabase.getInstance().getReference("User");
            userdatabase.keepSynced(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Activity List");

        exe_recview = findViewById(R.id.exercise_recview);
        exe_recview.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        exe_recview.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();

    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                exerciseList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Exercise exercise=dataSnapshot1.getValue(Exercise.class);
                    exerciseList.add(exercise);
                    binData();
                    Log.d("GlucoseRetrieve","amount:"+exercise.getCal());
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
                        Query query=userdatabase.child(userid).child("exercise");
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
    public void gotogEcti(){
        Intent intent=new Intent(this, ExerciseActi.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(Exercise exercise) {
        Toast.makeText(this,""+exercise.getDate(),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,ExerciseActi.class);
        intent.putExtra("obj", (Serializable) exercise);
        startActivity(intent);
        finish();
    }

    @Override
    public void onEdit(Exercise exercise, int index) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ပြင်ဆင်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ြပင်မလား");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(ExerciseListView.this, ExerciseActi.class);
                intent.putExtra("obj",(Serializable)exercise);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent,REQ_EXE);
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
    public void onDelete(Exercise exercise, int index) {

        /*DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User");
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(userid).child("exercise").child(exercise.getId()).removeValue();
                binData();
                dialog.dismiss();
                Toast.makeText(ExerciseListView.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
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

    private void binData() {
        eAdaper=new ExerciseAdapter(exerciseList,ExerciseListView.this);
        exe_recview.setAdapter(eAdaper);
        eAdaper.notifyDataSetChanged();
    }
}
