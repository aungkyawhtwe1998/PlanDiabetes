package com.example.plandiabetes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    SignInButton btngoogle;
    EditText txtemail,txtpassword;
    MaterialButton btngetStart;
    FirebaseAuth mAuth;
    ProgressDialog pDialog;
    GoogleSignInClient mGoogleSignInClient;
    UserProfile userProfile;
    public static final int RC_SIGN_IN=777;
    String dob,type;
    Double wei,hei;
    List<UserProfile> userProfileList;
    Database database;
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
        setContentView(R.layout.activity_login);

        btngetStart=findViewById(R.id.btngetstart);
        txtpassword=findViewById(R.id.txtpass);
        txtemail=findViewById(R.id.txtusername);
        btngoogle=findViewById(R.id.signInButton);
        btngoogle.setVisibility(View.INVISIBLE);
        btngoogle.setEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database=Database.getDatabase(this);

        pDialog=new ProgressDialog(this);
        pDialog.setMessage("Signing In..");
        mAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
         mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
         btngoogle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 pDialog.show();
                 signIn();
             }
         });
         btngetStart.setOnClickListener(new View.OnClickListener() {
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

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pDialog.dismiss();
                    updateUI();

                } else {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                            //progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();

                            updateUI();
                        } else {
                            pDialog.dismiss();
                            //progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, "Fuilure: " + task.getException(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Login.this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
