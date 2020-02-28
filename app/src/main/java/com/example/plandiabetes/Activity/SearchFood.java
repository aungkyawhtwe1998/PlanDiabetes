package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plandiabetes.Adapter.Foodadapter;
import com.example.plandiabetes.Class.Foods;
import com.example.plandiabetes.Interface.RecyclerItemSelectedListener;
import com.example.plandiabetes.ListView.MealListView;
import com.example.plandiabetes.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFood extends AppCompatActivity implements RecyclerItemSelectedListener {
    private RecyclerView recyclerView;
    private Foodadapter foodadapter;
    private List<Foods> foods=new ArrayList<>();
    private EditText userinput;
    private ChipGroup mchipGroup,mchipGroup1;
    private MaterialButton btnchoose;
    private DatabaseReference foodatabase,userdatabase;
    private final String TAG="SearchFood";
    private TextView txtdbname;
    private List<String> chiplist=new ArrayList<>();
    private Chip chip;
    private String foodType;
    MaterialButton addtoCal;
    FirebaseAuth mAuth;
    int i;
    String personEmail,userid;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                //onBackPressed();
                //gotoActi();
                gotoHome();
                return true;
            case R.id.id_list:
                gotoList();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        if(savedInstanceState==null){
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            foodatabase=FirebaseDatabase.getInstance().getReference("Food");
            userdatabase=FirebaseDatabase.getInstance().getReference("User");
            userdatabase.keepSynced(true);
            foodatabase.keepSynced(true);
        }

        mAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Meal");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtdbname=findViewById(R.id.txtdbname);
        addtoCal=findViewById(R.id.btn_selectfoo);
        recyclerView=findViewById(R.id.food_recview);
        userinput=findViewById(R.id.edt_searchfood);
        mchipGroup=(ChipGroup)findViewById(R.id.chipGroup);
        mchipGroup1=(ChipGroup)findViewById(R.id.chipGroup1);
        foodatabase.addListenerForSingleValueEvent(valueEventListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        getFoodType();


        //foodadapter=new Foodadapter(this,foods);
        //recyclerView.setAdapter(foodadapter);
        userinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String userinput=s.toString();
                List<Foods> newFood=new ArrayList<>();
                for(Foods food:foods){
                    if(food.getName().contains(userinput)){
                        newFood.add(food);
                    }
                }
                foodadapter=new Foodadapter(SearchFood.this,newFood);
                recyclerView.setAdapter(foodadapter);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //final ArrayList<String> list = new ArrayList<>();

        addtoCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mchipGroup.getChildCount()!=0 && mchipGroup.getChildCount()<7){
                    for (i=0; i<mchipGroup.getChildCount();i++){
                        chip = (Chip)mchipGroup.getChildAt(i);
                        chiplist.add(chip.getText().toString());
                    }
                    Intent intent=new Intent(SearchFood.this,FoodCalculation.class);
                    intent.putExtra("chiplist", (Serializable) chiplist);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(SearchFood.this,"အနည်းဆုံး ၁ခုမှ အများဆုံး ၆ခုအထိ ရွေးချယ်ပါ။",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    /*public void showDialog(String name){

        final AlertDialog dialogBuilder = new AlertDialog.Builder(SearchFood.this).create();
        LayoutInflater inflater= getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);

        TextView textView=dialogView.findViewById(R.id.txtfoodname);
        textView.setText("Enter gram of "+name);
        final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
        Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //List<Double> gramList=new ArrayList<>();
                dialogBuilder.dismiss();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }*/
    private String getFoodType(){
        for(int i = 0; i < mchipGroup1.getChildCount(); i++) {
            Chip chip = (Chip) mchipGroup1.getChildAt(i);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        foods.clear();
                        foodType = buttonView.getText().toString();
                        txtdbname.setText(foodType);
                        foodatabase.orderByChild("type").equalTo(foodType).addListenerForSingleValueEvent(valueEventListener);
                        Toast.makeText(SearchFood.this, foodType,Toast.LENGTH_SHORT).show();
                        mchipGroup1.clearCheck();
                        //list.add(buttonView.getText().toString());
                    }
                    if(buttonView.getText().toString().equals("အားလုံး")){
                        foods.clear();
                        foodatabase.addListenerForSingleValueEvent(valueEventListener);
                        //list.remove(buttonView.getText().toString());
                    }else{
                        foods.clear();
                    }
                    /*if(!list.isEmpty()){
                        Toast.makeText(SearchFood.this, list.toString(),Toast.LENGTH_LONG).show();
                    }*/
                }
            });
        }return foodType;
    }
    @Override
    public void onItemSelected(Foods foods) {

        List<String> tagList=new ArrayList<String>(Collections.singleton(foods.getName()));
        //final ChipGroup chipGroup = findViewById(R.id.chipGroup);
        for (int index = 0; index < tagList.size(); index++) {
            final String tagName = tagList.get(index);
            final Chip chip = new Chip(this);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
            chip.setText(tagName);
            //chip.setCloseIconResource(R.drawable.ic_action_navigation_close);
            chip.setCloseIconEnabled(true);
            //Added click listener on close icon to remove tag from ChipGroup
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tagList.remove(tagName);
                    mchipGroup.removeView(chip);
                }
            });
            mchipGroup.addView(chip);
        }
    }

    @Override
    public void OnClick(Foods foods, int index) {
        /*Intent intent=new Intent(SearchFood.this,Calculator.class);
        intent.putExtra("foodname", foods);
        startActivity(intent);
        finish();*/
    }

    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Foods f=dataSnapshot1.getValue(Foods.class);
                    foods.add(f);
                    foodadapter=new Foodadapter(SearchFood.this,foods);
                    recyclerView.setAdapter(foodadapter);
                    foodadapter.notifyDataSetChanged();
                   // Log.d("GlucoseRetrieve","amount:"+f.getName());
                }
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void goToAdd(){
        Intent intent=new Intent(this,firebasetest.class);
        //intent.putExtra("chiplist", (Serializable) chiplist);
        startActivity(intent);
        finish();
    }
    private void gotoActi(){
        Intent intent=new Intent(this,MealActi.class);
        //intent.putExtra("chiplist", (Serializable) chiplist);
        startActivity(intent);
        finish();
    }
    private void gotoHome(){
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,Home.class);
        startActivity(intent);
        finish();
    }
    public void gotoList(){
        Intent intent=new Intent(this, MealListView.class);
        startActivity(intent);
        finish();
    }
    /*public void setup() {
        // [START get_firestore_instance]

        db = FirebaseFirestore.getInstance();
        // [END get_firestore_instance]

        // [START set_firestore_settings]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // [END set_firestore_settings]
    }

    public void setupCacheSize() {
        // [START fs_setup_cache]
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);
        // [END fs_setup_cache]
    }*/
}

