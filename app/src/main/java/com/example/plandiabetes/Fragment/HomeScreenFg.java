package com.example.plandiabetes.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.plandiabetes.Activity.ExerciseActi;
import com.example.plandiabetes.Activity.FirstScreen;
import com.example.plandiabetes.Activity.MealActi;
import com.example.plandiabetes.Activity.MedicationActi;
import com.example.plandiabetes.Activity.SearchFood;
import com.example.plandiabetes.Activity.WeightActi;
import com.example.plandiabetes.Adapter.ExerciseAdapter;
import com.example.plandiabetes.Class.Exercise;
import com.example.plandiabetes.Class.FireUser;
import com.example.plandiabetes.Class.Glucose;
import com.example.plandiabetes.Class.Meal;
import com.example.plandiabetes.Class.Medication;
import com.example.plandiabetes.Class.MonthDayData;
import com.example.plandiabetes.ListView.ExerciseListView;
import com.example.plandiabetes.ListView.GlucolseListView;
import com.example.plandiabetes.Activity.GlucoseActi;
import com.example.plandiabetes.ListView.MealListView;
import com.example.plandiabetes.ListView.MedicationListView;
import com.example.plandiabetes.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.AxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenFg extends Fragment {

    LinearLayout linSuggest;
    MaterialRippleLayout glu,exe,meal,medi,btnWeight;

    TextView txtlastglu,txtlasttime,txtcaneat,txtExeCal,txtperiod,txtnextCanEat,txttodaymeal,txttodayglu,txtGluPdown;
    FirebaseAuth firebaseAuth;
    String personemail,userid;
    BarChart chart;
    List<Glucose> glucoseList;
    List<Meal> mealList;
    List<Exercise> exerciseList;
    ArrayList<BarEntry> barEntryArrayList;
    DecimalFormat decimalFormat;
    ArrayList<String> labelsName;
    DatabaseReference databaseuser;
    FireUser fireUser;
    public HomeScreenFg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen_fg, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState==null){
            databaseuser=FirebaseDatabase.getInstance().getReference("User");
            databaseuser.keepSynced(true);
        }
        firebaseAuth=FirebaseAuth.getInstance();
        decimalFormat=new DecimalFormat("0.00");

        txtlastglu=view.findViewById(R.id.txt_lastglu);
        txtlasttime=view.findViewById(R.id.txt_lasttime);
        txtperiod=view.findViewById(R.id.txt_period);

        glu=view.findViewById(R.id.mrl_home_glucose);
        meal=view.findViewById(R.id.mrl_home_mela);
        exe=view.findViewById(R.id.mrl_home_exe);
        medi=view.findViewById(R.id.mrl_home_med);

        linSuggest=view.findViewById(R.id.layout_suggest);
        txttodayglu=view.findViewById(R.id.txt_todayglu);
        txttodaymeal=view.findViewById(R.id.txt_todayeat);
        txtcaneat=view.findViewById(R.id.txtcaneat);
        txtGluPdown=view.findViewById(R.id.txt_gluUpDow);
        txtnextCanEat=view.findViewById(R.id.txtnext_caneat);
        txtExeCal=view.findViewById(R.id.txtExerciseCal);
        btnWeight=view.findViewById(R.id.btnWeight);


        Log.d("home","Type:"+diatype);



        meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchFood.class);
                startActivity(intent);
            }
        });
        glu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), GlucoseActi.class);
                startActivity(intent);
            }
        });
        exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ExerciseActi.class);
                startActivity(intent);
            }
        });
        medi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MedicationActi.class);
                startActivity(intent);
            }
        });

        chart=view.findViewById(R.id.barchart);

        btnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoWeight();
            }
        });


    }

    private void gotoWeight() {
        Intent intent=new Intent(getContext(), WeightActi.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");

        String today = sdf.format(Calendar.getInstance().getTime());
        //String oneMonthAgo = sdf.format(Calendar.getInstance().add(MONTH, -1).getTime());
        FirebaseUser acct = firebaseAuth.getCurrentUser();
        if (acct != null) {
            personemail = acct.getEmail();
            //Toast.makeText(this,"email:"+personemail,Toast.LENGTH_SHORT).show();
            databaseuser.orderByChild("email").equalTo(personemail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //fireUser=child.getValue(FireUser.class);
                        userid = child.getKey();
                        fireUser=child.getValue(FireUser.class);
                        Query query = databaseuser.child(userid).child("glucose").orderByKey().limitToLast(7);
                        query.addListenerForSingleValueEvent(valueEventListener);
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => " + c.getTime());
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        String formattedDate = df.format(c.getTime());

                        Query query1 = databaseuser.child(userid).child("meal").orderByChild("date").equalTo(formattedDate);
                        query1.addListenerForSingleValueEvent(suggestionEventListener);
                        suggstType(fireUser.getType(),fireUser.getGender(),fireUser.getWeight(),fireUser.getHeight());

                        Query query2=databaseuser.child(userid).child("exercise").orderByChild("date").equalTo(formattedDate);
                        query2.addListenerForSingleValueEvent(calExerciseListener);

                        Log.d("userid", userid);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("Setting", "Failed to read value.", databaseError.toException());
                }
            });

        }
    }
    public String diatype,usergender;
    public Double userweight,userheight;
    public Double minamountOfCarb=0.0;
    public Double maxamountOfCarb=0.0;
    public Double todayCarb=0.0;
    public Double canEatAmount=0.0;
    public Double extraEatAmount=0.0;
    public Double sumExeCal=0.0;
    public Double sumExeCarb=0.0;

    public void suggstType(String type,String gender,Double weight,Double height){
        diatype=type;
        usergender=gender;
        userweight=weight;
        userheight=height;
        if (type.equals("type1")&& gender.equals("female")) {
            diatype=type;
            maxamountOfCarb = 180.0;
            minamountOfCarb = 135.0;
            txtGluPdown.setText("သင်ဟာType1 diabetes(အမျိူးသမီး)ဖြစ်သည့်အတွက် တစ်ရက်စာစားသောက်ရန် Carbohydratesပမာဏမှာ"+minamountOfCarb+"မှ "+maxamountOfCarb+"အထိဖြစ်ပါသည်။");
        }
        else if (type.equals("type1")&&gender.equals("male")){
            diatype=type;
            maxamountOfCarb = 225.0;
            minamountOfCarb = 180.0;
            txtGluPdown.setText("သင်ဟာType1 diabetes(အမျိူးသား)ဖြစ်သည့်အတွက် တစ်ရက်စာစားသောက်ရန် Carbohydratesပမာဏမှာ "+minamountOfCarb+" မှ "+maxamountOfCarb+" အထိဖြစ်ပါသည်။");

        }
        else if (type.equals("type2") && gender.equals("female")) {
            diatype=type;
            maxamountOfCarb = 180.0;
            minamountOfCarb = 135.0;
            txtGluPdown.setText("သင်ဟာType2 diabetes(အမျိူးသမီး)ဖြစ်သည့်အတွက် တစ်ရက်စာစားသောက်ရန် Carbohydratesပမာဏမှာ"+minamountOfCarb+"မှ "+maxamountOfCarb+"အထိဖြစ်ပါသည်။");
        }
        else if (type.equals("type2") && gender.equals("male")) {
            diatype=type;
            maxamountOfCarb = 225.0;
            minamountOfCarb = 180.0;
            txtGluPdown.setText("သင်ဟာType2 diabetes(အမျိူးသား)ဖြစ်သည့်အတွက် တစ်ရက်စာစားသောက်ရန် Carbohydratesပမာဏမှာ "+minamountOfCarb+" မှ "+maxamountOfCarb+" အထိဖြစ်ပါသည်။");
        }
    }

    public void todayEat(Double carb){
        todayCarb=carb;
        /*if(todayCarb!=0.0){
            txttodaymeal.setText(decimalFormat.format(todayCarb)+"grams");
        }else {
            txttodaymeal.setText("သင် ယနေ့ဘာမှမစားရသေးပါ။");
        }*/

        if(todayCarb!=0.0){
            txttodaymeal.setText(decimalFormat.format(todayCarb)+" Carbs");
            Log.d("home", "MaxAmount"+maxamountOfCarb);
            if(todayCarb<maxamountOfCarb){
                canEatAmount=maxamountOfCarb-todayCarb;
                txtcaneat.setText(decimalFormat.format(canEatAmount)+" Carbs");
            }else if(todayCarb>maxamountOfCarb){
                extraEatAmount=todayCarb-maxamountOfCarb;
                Log.d("Home","canEat:"+extraEatAmount);
                txtcaneat.setText("သင် "+decimalFormat.format(extraEatAmount)+" Carbs ပိုစားထားပါတယ်။");
                txtcaneat.setTextColor(Color.parseColor("#FC0000"));
            }
        }else {
            txttodaymeal.setText("သင် ယနေ့ဘာမှမစားရသေးပါ။");
            //txtcaneat.setText(decimalFormat.format(maxamountOfCarb));
        }
    }

    private void todayExercise(Double exeCalSum) {
        sumExeCal=exeCalSum;
        sumExeCarb=sumExeCal/4;
        Double nextCanEat;
       /* if(sumExeCarb!=0.0 ){
            nextCanEat=sumExeCarb;

            txtnextCanEat.setText(decimalFormat.format(sumExeCarb)+" grams");
        }
        else */
        txtExeCal.setText(decimalFormat.format(sumExeCal)+" Calories");
       /*if(extraEatAmount!=0.0 && extraEatAmount>sumExeCarb){
           txtcaneat.setVisibility(View.GONE);
           txtnextCanEat.setText(decimalFormat.format(extraEatAmount-sumExeCarb)+" grams ပိုစားထားပါတယ်။");
       }else if (extraEatAmount!=0.0 && extraEatAmount<sumExeCarb){
           txtcaneat.setVisibility(View.GONE);
           txtnextCanEat.setText(decimalFormat.format(sumExeCarb-extraEatAmount)+" grams ထပ်စားနိုင်ပါတယ်။");
       }else if(canEatAmount!=0.0){
           txtcaneat.setVisibility(View.GONE);
           txtnextCanEat.setText(decimalFormat.format(canEatAmount+sumExeCarb)+" grams");
       }
       else {
           txtnextCanEat.setText(decimalFormat.format(maxamountOfCarb+sumExeCarb));
       }*/

    }

    ValueEventListener calExerciseListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            exerciseList = new ArrayList<>();
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                Exercise lastExe = dataSnapshot1.getValue(Exercise.class);
                exerciseList.add(lastExe);
                //Log.d("lastMeal",exerciseList.get().toString());
            }
            Double sum=0.0;
            for (int i=0;i<exerciseList.size();i++){
                sum+=exerciseList.get(i).getCal();
                //calsum=mealList.get(i).getTotcal();
            }
            todayExercise(sum);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };



    ValueEventListener suggestionEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                mealList = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Meal lastmeal = dataSnapshot1.getValue(Meal.class);
                    mealList.add(lastmeal);
                    Log.d("lastMeal",lastmeal.getTotcarb().toString());
                }
                Double sum=0.0;
                for (int i=0;i<mealList.size();i++){
                    sum+=mealList.get(i).getTotcarb();
                    //calsum=mealList.get(i).getTotcal();
                }
                todayEat(sum);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){

                glucoseList=new ArrayList<>();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Glucose glucose=dataSnapshot1.getValue(Glucose.class);
                    glucoseList.add(glucose);
                    //binData();
                    Log.d("GlucoseRetrieve","amount:"+glucose.getGlucoseLevel());
                }
                barEntryArrayList=new ArrayList();
                labelsName=new ArrayList<>();
                int i;
                for (i=0;i<glucoseList.size();i++){
                    String mont=glucoseList.get(i).getDate();
                    Double sales=glucoseList.get(i).getGlucoseLevel();
                    barEntryArrayList.add(new BarEntry(i, (float) Double.parseDouble(String.valueOf(sales))));
                    labelsName.add(mont);
                }
                Log.d("hOme", "i: "+i);

                txtlastglu.setText(glucoseList.get(i-1).getGlucoseLevel()+" "+glucoseList.get(i-1).getUnit());
                txtlasttime.setText(glucoseList.get(i-1).getDate()+">>"+glucoseList.get(i-1).getTime());
                Double glucoselevel=glucoseList.get(i-1).getGlucoseLevel();
                String glucoseTime=glucoseList.get(i-1).getPeriod();
                String unit=glucoseList.get(i-1).getUnit();
                if(unit.equals("mmol/L")){
                    glucoselevel=glucoselevel*18;

                }else {
                    //glucoselevel=glucoselevel;
                }
                Log.d("MMOL", "value: "+glucoselevel);
                if(glucoseTime.equals("အစာမစားခင်")){
                    txtperiod.setText("အစာမစားခင်");
                    if(glucoselevel>=90 && glucoselevel<=130){
                        txtlastglu.setTextColor(Color.parseColor("#208B03"));
                        txttodayglu.setText("သင့် Glucose Level မှာ ပုံမှန် ဖြစ်ပါတယ်");
                    }
                    if(glucoselevel<90){
                        txtlastglu.setTextColor(Color.parseColor("#7D038B"));
                        txttodayglu.setText("သင့် Glucose Level မှာ နည်း ေနပါတယ်");
                    }
                    if(glucoselevel>130){
                        txtlastglu.setTextColor(Color.parseColor("#F30000"));
                        txttodayglu.setText("သင့် Glucose Level မှာ များ ေနပါတယ်");
                    }
                }

                if(glucoseTime.equals("အစာစားပြီး")){
                    txtperiod.setText("အစာစားပြီး");
                    if(glucoselevel==180){

                        txtlastglu.setTextColor(Color.parseColor("#208B03"));
                        txttodayglu.setText("သင့် Glucose Level မှာ ပုံမှန် ဖြစ်ပါတယ်");
                    }
                    if(glucoselevel<180){
                        txtlastglu.setTextColor(Color.parseColor("#7D038B"));
                        txttodayglu.setText("သင့် Glucose Level မှာ နည်း ေနပါတယ်");
                    }
                    if(glucoselevel>180){
                        txtlastglu.setTextColor(Color.parseColor("#E05B49"));
                        txttodayglu.setText("သင့် Glucose Level မှာ များ ေနပါတယ်");
                    }
                }
                BarDataSet barDataSet=new BarDataSet(barEntryArrayList,"Glucose Level");

                //barDataSet.setColor(Col);
                Description description=new Description();
                description.setText("Glucose");
                chart.setDescription(description);
                BarData barData=new BarData(barDataSet);
                chart.setData(barData);
                XAxis xAxis=chart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f);
                xAxis.setLabelCount(labelsName.size());
                xAxis.setLabelRotationAngle(270);
                chart.animateY(2000);
                chart.invalidate();
                //BarData barData1=new BarData(barDataSet);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}

