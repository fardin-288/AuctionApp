package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class about_us_class extends AppCompatActivity {
    private Button goBackbutton;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        about_us_class aboutUsClass = new about_us_class();

        goBackbutton = findViewById(R.id.goBackbutton);

        goBackbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onBackPressed();
//                Intent intent = new Intent(about_us_class.this, ProfileFragment.class);
//                startActivity(intent);
            }
        });
    }
}
