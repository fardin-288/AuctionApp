package com.example.auctionapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

//public class RegistrationActivity extends AppCompatActivity {
//
//    private EditText email, password, name;
//    private Button mRegister;
//    private TextView existaccount;
//    private ProgressDialog progressDialog;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_registration);
////        ActionBar actionBar = getSupportActionBar();
////        actionBar.setTitle("Create Account");
////        actionBar.setDisplayShowHomeEnabled(true);
////        actionBar.setDisplayHomeAsUpEnabled(true);
//        email = findViewById(R.id.register_email);
//        name = findViewById(R.id.register_name);
//        password = findViewById(R.id.register_password);
//        mRegister = findViewById(R.id.register_button);
//        existaccount = findViewById(R.id.homepage);
//        mAuth = FirebaseAuth.getInstance();
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Register");
//
//        mRegister.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String emaill = email.getText().toString().trim();
//                String uname = name.getText().toString().trim();
//                String pass = password.getText().toString().trim();
//                if (!Patterns.EMAIL_ADDRESS.matcher(emaill).matches()) {
//                    email.setError("Invalid Email");
//                    email.setFocusable(true);
//                } else if (pass.length() < 6) {
//                    password.setError("Length Must be greater than 6 character");
//                    password.setFocusable(true);
//                } else {
//                    registerUser(emaill, pass, uname);
//                }
//            }
//        });
//        existaccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RegistrationActivity.this, loginActivity.class));
//            }
//        });
//    }
//
//    private void registerUser(String emaill, final String pass, final String uname) {
//        progressDialog.show();
//        mAuth.createUserWithEmailAndPassword(emaill, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    progressDialog.dismiss();
//                    FirebaseUser user = mAuth.getCurrentUser();
//                    String email = user.getEmail();
//                    String uid = user.getUid();
//                    HashMap<Object, String> hashMap = new HashMap<>();
//                    hashMap.put("email", email);
//                    hashMap.put("uid", uid);
//                    hashMap.put("name", uname);
//                    hashMap.put("onlineStatus", "online");
//                    hashMap.put("typingTo", "noOne");
//                    hashMap.put("image", "");
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference reference = database.getReference("Users");
//                    reference.child(uid).setValue(hashMap);
//                    Toast.makeText(RegistrationActivity.this, "Registered User " + user.getEmail(), Toast.LENGTH_LONG).show();
//                    Intent mainIntent = new Intent(RegistrationActivity.this, DashboardActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(mainIntent);
//                    finish();
//                } else {
//                    progressDialog.dismiss();
//                    Toast.makeText(RegistrationActivity.this, "Error", Toast.LENGTH_LONG).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
//                Toast.makeText(RegistrationActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return super.onSupportNavigateUp();
//    }
//}

//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, passwordEditText2;
    private TextView registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.register_name);
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        passwordEditText2 = findViewById(R.id.register_password2);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String password2 = passwordEditText2.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && isPasswordSame()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Update user's display name (optional)
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // User profile updated successfully\
                                        showMessage("User created");
                                        Intent mainIntent = new Intent(RegistrationActivity.this, DashboardActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    } else {
                                        showMessage("Something went wrong");
                                    }
                                }
                            });

                            // Registration and profile update success
                        } else {

                            if(!isPasswordSame()){
                                showMessage("Password did not match");
                            }
                            // Registration failed
                            // You can handle the registration failure here
                            // For example, show an error message to the user
                        }
                    }
                });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isPasswordSame(){
        if(passwordEditText.getText().toString().equals(passwordEditText2.getText().toString()))
            return true;
        else
            return false;
    }
}
