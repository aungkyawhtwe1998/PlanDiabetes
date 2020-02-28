package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plandiabetes.Adapter.ViewPagerAdapter;
import com.example.plandiabetes.Fragment.GuideFg;
import com.example.plandiabetes.Fragment.HomeScreenFg;
import com.example.plandiabetes.Fragment.SettingFg;
import com.example.plandiabetes.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
TextView txtdob,txtwei,txthei;
ViewPagerAdapter viewPagerAdapter;
ViewPager viewPager;
TabLayout tabs;
FirebaseAuth mAuth;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.calculator,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            case R.id.cal_menu:
                gotoCal();

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPager=findViewById(R.id.viewPager);
        tabs=findViewById(R.id.tabLayout);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        HomeScreenFg homeScreenFg=new HomeScreenFg();
        viewPagerAdapter.addFragment(homeScreenFg,"Home");

        GuideFg guideFg=new GuideFg();
        viewPagerAdapter.addFragment(guideFg,"Guide");

        SettingFg settingFg=new SettingFg();
        viewPagerAdapter.addFragment(settingFg,"Setting");
        viewPager.setAdapter(viewPagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));





        /*
*/
    }
    public void gotoCal(){
        Intent intent=new Intent(this,SearchFood.class);
        startActivity(intent);
        finish();
    }
}
