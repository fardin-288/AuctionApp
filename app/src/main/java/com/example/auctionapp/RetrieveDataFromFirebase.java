package com.example.auctionapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RetrieveDataFromFirebase {

    public static void RetrieveDataFromDatabaseAction() {

        // Define a reference to your Firebase Realtime Database.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList");

        // Add a listener to retrieve data.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemsnapshot  :dataSnapshot.getChildren()){
                    ItemforDatabaseupload itemforDatabaseupload = itemsnapshot.getValue(ItemforDatabaseupload.class);

                    Item item = new Item(itemforDatabaseupload);
//                    itemArray.itemList.add(item);
                    Log.d("this:", item.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}