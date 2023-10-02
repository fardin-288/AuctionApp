package com.example.auctionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

//public class addItemActivity extends  AppCompatActivity{
//
//    private static final int IMAGE_REQUEST =1;
//    private Uri imageuri;
//    private ImageView imageview;
//
//    // Function to show the Add Item dialog
//     void showAddItemDialog(Context context) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Add Item");
//
//        // Set up the layout for the dialog
//        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialogue_add_item, null);
//        final EditText itemNameEditText = viewInflated.findViewById(R.id.editTextItemName);
//        final EditText itemNameEditPrice = viewInflated.findViewById(R.id.editStartingPrice);
//        final EditText itemDescriptionText = viewInflated.findViewById(R.id.editDescription);
//        Button buttonUploadPicture = viewInflated.findViewById(R.id.buttonUploadPicture);
//        // Add more EditText fields for description, price, and picture URL if needed
////        imageview = findViewById(R.id.productImgView);
//
//        builder.setView(viewInflated);
//
//        // Set up the buttons
//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Get the item details from the dialog
//                String itemName = itemNameEditText.getText().toString();
//                String itemPriceString = itemNameEditPrice.getText().toString();
//                Float itemPriceFloat = Float.valueOf(itemPriceString);
//                String itemDescription = itemDescriptionText.getText().toString();
//                long currentTime = System.currentTimeMillis();
//                // Add more code to retrieve description, price, and picture URL
//
//                if (!itemName.isEmpty()) {
//                    // Create a new Item object and add it to the list
//                    Item newItem = new Item(itemName,itemDescription, itemPriceFloat,currentTime); // Modify as needed
//
//                    newItem.setUserid();
//
//                    itemArray.itemList.add(newItem);
//                    HomeFragment.adapter.notifyDataSetChanged();
//
//                    itemArray.incrementTotal();
//                }
//                dialog.dismiss();
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        // Add picture here
//        buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//             openImageChooser();
////                Toast.makeText(context, "This is a Toast message", Toast.LENGTH_SHORT).show();
//////                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////////                startActivityForResult(context, HomeFragment.PICK_IMAGE_REQUEST);
//            }
//        });
//
//
//        builder.show();
//    }
//
//    private  void openImageChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            imageuri = data.getData();
//            imageview.setImageURI(imageuri);
////            profileImageView.setImageURI(imageUri);
////            uploadImageToFirebase();
//        }
//    }
//}

