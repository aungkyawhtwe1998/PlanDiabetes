package com.example.plandiabetes.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plandiabetes.Activity.FirstScreen;
import com.example.plandiabetes.Activity.Home;
import com.example.plandiabetes.Activity.Login;
import com.example.plandiabetes.Activity.MainActivity;
import com.example.plandiabetes.Activity.StartPage;
import com.example.plandiabetes.Class.FireUser;
import com.example.plandiabetes.R;
import com.example.plandiabetes.Room.Database;
import com.example.plandiabetes.Room.UserDao;
import com.example.plandiabetes.Room.UserProfile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Constants;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFg extends Fragment  {
TextView txtname,txtdob,txthei,txtwei,txtType,txtgender;
ImageView imageView;
Button btnlogout;
Database database;
List<FireUser> fireUserList;
FirebaseAuth mAuth;
GoogleSignInClient mGoogleSignInClient;
DatabaseReference databaseUser;
    public SettingFg() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_fg, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtname=view.findViewById(R.id.txtname);
        txtdob=view.findViewById(R.id.txtdob);
        txthei=view.findViewById(R.id.txthei);
        txtwei=view.findViewById(R.id.txtwei);
        txtType=view.findViewById(R.id.txttype);
        btnlogout=view.findViewById(R.id.btnlogout);
        txtgender=view.findViewById(R.id.txtgender);
        imageView=view.findViewById(R.id.imageView2);
        if(savedInstanceState==null){
            databaseUser=FirebaseDatabase.getInstance().getReference("User");
            databaseUser.keepSynced(true);
        }

        fireUserList=new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(),gso);
        FirebaseUser acct = mAuth.getCurrentUser();
        if (acct != null) {
            String personEmail =acct.getEmail();
            txtname.setText(personEmail);
            String personId =acct.getUid();
            /*userProfiles=database.userDao().retrieveStartUser(personEmail);
            for(int i=0;i<userProfiles.size();i++){
                if(userProfiles.get(i).getEmail().equals(txtname.getText().toString())){
                    dob=userProfiles.get(i).getDob();
                    wei=userProfiles.get(i).getWeight();
                    hei=userProfiles.get(i).getHeight();
                    type=userProfiles.get(i).getType();
                    txtdob.setText("Date of Birth : "+dob);
                    txthei.setText("Height : "+hei+" ft");
                    txtwei.setText("Weight : "+wei+" lbs");
                    txtType.setText("Type : "+type);
                    Toast.makeText(getContext(),"Dob:"+dob,Toast.LENGTH_SHORT).show();
                }
            }*/

            Uri personPhoto = acct.getPhotoUrl();
            if(personPhoto!=null){
                Picasso.get().load(personPhoto).into(imageView);
            }else {
                imageView.setImageResource(R.drawable.ic_sugar_blood_level);
            }
            if (acct.isEmailVerified()) {
                //Toast.makeText(getContext(), "Email Verified", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(getContext(), "Email Not Verified", Toast.LENGTH_SHORT).show();
            }
        }
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }
    void logout(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) getContext(), task ->{
                    Intent intent=new Intent(getContext(), StartPage.class);
                    startActivity(intent);
                    getActivity().finish();

                });
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseUser.orderByChild("email").equalTo(txtname.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());
                    Log.d("User ref", child.getRef().toString());
                    Log.d("User val", child.getValue().toString());
                    FireUser fireUser=child.getValue(FireUser.class);
                    txthei.setText("Height         : "+fireUser.getHeight()+" ft");
                    txtdob.setText("Date Of Birth  : "+fireUser.getDob());
                    txtwei.setText("Weight         : "+fireUser.getWeight());
                    txtType.setText("Diabetes Type: "+fireUser.getType());
                    txtgender.setText("Gender     : "+fireUser.getGender());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Setting", "Failed to read value.", databaseError.toException());
            }
        });

    }
}
