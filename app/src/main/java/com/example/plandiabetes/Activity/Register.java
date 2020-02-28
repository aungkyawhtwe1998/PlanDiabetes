package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plandiabetes.Class.FireUser;
import com.example.plandiabetes.R;
import com.example.plandiabetes.Room.Database;
import com.example.plandiabetes.Room.UserProfile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Register extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    SignInButton btngoogle;
    MaterialButton btnlogin;
    EditText txtemail,txtpassword;
    FirebaseAuth mAuth;
    List<UserProfile> userProfileList;
    UserProfile userProfile;
    Database database;
    ProgressDialog pDialog;
    String personEmail;
    GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN=777;
    String dob,type,gender;
    Double wei,hei;
    DatabaseReference databaseUser;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");
        btngoogle=findViewById(R.id.signInButton1);
        btnlogin=findViewById(R.id.btngetstart1);
        txtemail=findViewById(R.id.txtusername1);
        txtpassword=findViewById(R.id.txtpass1);
        btngoogle.setVisibility(View.INVISIBLE);
        btngoogle.setEnabled(false);
        pDialog=new ProgressDialog(this);

        Intent intent=getIntent();
        if(intent!=null){
            dob=intent.getStringExtra("dob");
            wei=intent.getDoubleExtra("wei",0.0);
            hei=intent.getDoubleExtra("hei",0.0);
            type=intent.getStringExtra("type");
            gender=intent.getStringExtra("gender");
        }

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        mAuth=FirebaseAuth.getInstance();
        databaseUser= FirebaseDatabase.getInstance().getReference("User");
       // database=Database.getDatabase(this);

        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        String email = txtemail.getText().toString().trim();
        String password = txtpassword.getText().toString().trim();
        if (email.isEmpty()) {
            txtemail.setError("Email is required");
            txtemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtemail.setError("Please enter a valid email");
            txtemail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            txtpassword.setError("Password is required");
            txtpassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            txtpassword.setError("Minimum length of password should be 6");
            txtpassword.requestFocus();
            return;
        }
        pDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"User Registered Successful",Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                       pDialog.dismiss();
                       Toast.makeText(getApplicationContext(),"You are already registered",Toast.LENGTH_SHORT).show();
                    }else {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });
    }
    private void signIn(){
        Intent signIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pDialog.dismiss();
                            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            updateUI();
                        } else {
                            pDialog.dismiss();
                            //progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Register.this, "Fuilure: " + task.getException(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI();
                        }
                        //progressBar.setVisibility(View.INVISIBLE);

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    private void updateUI() {
        FirebaseUser acct = mAuth.getCurrentUser();
        if (acct != null) {
            personEmail =acct.getEmail();
            String userId=databaseUser.push().getKey();
            FireUser fireUser=new FireUser(userId,personEmail,txtpassword.getText().toString(),type,dob,wei,hei,gender);
            databaseUser.child(userId).setValue(fireUser);
            Intent intent = new Intent(Register.this, Home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
