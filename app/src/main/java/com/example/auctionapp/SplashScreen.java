package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    ImageView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        title=findViewById(R.id.title);
        title.setTranslationY(0);
        float v=0;
        title.setAlpha(v);
        title.animate().translationY(-400).alpha(1).setDuration(1000).setStartDelay(400).start();



        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(SplashScreen.this, loginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreen.this, DashboardActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, 5000);
    }
}



//package com.example.auctionapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.ImageView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.airbnb.lottie.LottieAnimationView;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class SplashScreen extends AppCompatActivity {
//
//    FirebaseUser currentUser;
//    private FirebaseAuth mAuth;
//    ImageView title;
//    LottieAnimationView lottieSplash;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
//
//        title = findViewById(R.id.title);
//        title.setTranslationY(0);
//        float v = 0;
//        title.setAlpha(v);
//        title.animate().translationY(-400).alpha(1).setDuration(1000).setStartDelay(400).start();
//
//        lottieSplash = findViewById(R.id.lottiesplash);
//        // Start the lottie animation immediately
//        lottieSplash.playAnimation();
//
//        mAuth = FirebaseAuth.getInstance();
//        if (mAuth != null) {
//            currentUser = mAuth.getCurrentUser();
//        }
//
//        // Delay the transition to the next activity
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                FirebaseUser user = mAuth.getCurrentUser();
//                if (user == null) {
//                    Intent intent = new Intent(SplashScreen.this, loginActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Intent mainIntent = new Intent(SplashScreen.this, DashboardActivity.class); // Complete this code
//                    startActivity(mainIntent);
//                    finish();
//                }
//            }
//        }, 5000); // Adjust the delay as needed
//    }
//}




