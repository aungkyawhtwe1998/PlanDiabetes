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
import com.example.plandiabetes.Activity.Home;
import com.example.plandiabetes.Activity.MealActi;
import com.example.plandiabetes.Activity.SearchFood;
import com.example.plandiabetes.Adapter.MealActiAdapter;
import com.example.plandiabetes.Class.Exercise;
import com.example.plandiabetes.Class.Meal;
import com.example.plandiabetes.Interface.MealActiItemClickListener;
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

public class MealListView extends AppCompatActivity implements MealActiItemClickListener {
    RecyclerView meallist_recview;
    MealActiAdapter mealActiAdapter;
    DatabaseReference userdatabase;
    FirebaseAuth firebaseAuth;
    List<Meal> mealList;
    public static String personemail,userid;
    int REQ_MEAL=1;
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
                gotoSearchfood();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_OK && requestCode==REQ_MEAL && data!=null){
            binData();
            //gAdapter=new Glucose_Adapter(glucoseList,GlucolseListView.this);
            //glucose_recview.setAdapter(gAdapter);
            //gAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list_view);
        if(savedInstanceState==null){
            userdatabase= FirebaseDatabase.getInstance().getReference("User");
            userdatabase.keepSynced(true);
        }
        getSupportActionBar().setTitle("Meal List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        meallist_recview=findViewById(R.id.rec_mealView);
        meallist_recview.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        meallist_recview.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();

    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                mealList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Meal meal =dataSnapshot1.getValue(Meal.class);
                    mealList.add(meal);
                    binData();
                    Log.d("GlucoseRetrieve","amount:"+meal.getTotcarb());
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
                        Query query=userdatabase.child(userid).child("meal");
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
    public void gotoMealAdd(){
        Intent intent=new Intent(MealListView.this, MealActi.class);
        startActivity(intent);
        finish();
    }
    public void binData() {
        mealActiAdapter=new MealActiAdapter(mealList,MealListView.this);
        meallist_recview.setAdapter(mealActiAdapter);
        mealActiAdapter.notifyDataSetChanged();
    }


    @Override
    public void OnEdit(Meal meal, int index) {
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ပြင်ဆင်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ြပင်မလား");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(MealListView.this, MealActi.class);
                intent.putExtra("obj",(Serializable)meal);
                intent.putExtra("isEdit", true);
                startActivityForResult(intent,REQ_MEAL);
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
    public void OnDelete(Meal meal, int index) {

        /*DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User");
        AlertDialog.Builder buidler = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
        buidler.setTitle("ဖျက်ရန်");
        buidler.setMessage("ေရွးချယ်ထားေသာ itemကို ဖျက်မလား။");
        buidler.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference.child(userid).child("meal").child(meal.getId()).removeValue();
                binData();
                dialog.dismiss();
                Toast.makeText(MealListView.this,"ဖျက်ပြီးပါပြီ။",Toast.LENGTH_SHORT).show();
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
    public void gotoSearchfood(){
        Intent intent=new Intent(this, SearchFood.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
