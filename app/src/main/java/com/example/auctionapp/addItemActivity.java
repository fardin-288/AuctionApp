package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.fragment.app.FragmentManager;
////import com.example.myapp.MyFragment;

public class addItemActivity extends  AppCompatActivity{
    private EditText productNameEditText;
    private EditText descriptionEditText;
    private EditText minimumBidEditText;
    private Button uploadImageButton;
    private Button addItemButton;
    private Button cancelButton;
    private ImageView selectedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

//         Initialize UI elements
        productNameEditText = findViewById(R.id.editTextProductName);
        descriptionEditText = findViewById(R.id.editTextDescription);
        minimumBidEditText = findViewById(R.id.editTextMinimumBid);
        uploadImageButton = findViewById(R.id.buttonUploadImage);
        selectedImageView = findViewById(R.id.imageViewSelectedImage);
        addItemButton = findViewById(R.id.addItemButton);
        cancelButton = findViewById(R.id.cancelButton);
//
////         Set a click listener for the upload button
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read values from EditText fields
                String productName = productNameEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String minimumBid = minimumBidEditText.getText().toString();
                Float minimumBidFLoat = Float.valueOf(minimumBid);

                Item newItem = new Item(productName,description, minimumBidFLoat,System.currentTimeMillis()); // Modify as needed

                itemArray.itemList.add(newItem);

//                HomeFragment.adapter.notifyDataSetChanged();

                Intent intent = new Intent(addItemActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });
//
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(addItemActivity.this, HomeFragment.class);
                startActivity(intent);
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Input Picture", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(addItemActivity.this, HomeFragment.class);
//                startActivity(intent);
            }
        });
    }
}
