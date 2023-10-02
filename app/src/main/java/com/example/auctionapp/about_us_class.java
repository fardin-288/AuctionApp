package com.example.auctionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class about_us_class extends AppCompatActivity {
    private Button goBackbutton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        goBackbutton = findViewById(R.id.goBackbutton);

        goBackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(about_us_class.this, ProfileFragment.class);
                startActivity(intent);
            }
        });
    }
}
