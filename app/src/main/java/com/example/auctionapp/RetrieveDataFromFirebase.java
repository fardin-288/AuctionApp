package com.example.auctionapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class RetrieveDataFromFirebase {

    public static boolean RetrieveDataFromDatabaseStatus = false;

    public static void RetrieveDataFromDatabaseAction() {

        if(RetrieveDataFromDatabaseStatus)return;

        // Define a reference to your Firebase Realtime Database.
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList");
        RetrieveDataFromDatabaseStatus = true;

        itemArray.itemList.clear();

        // Add a listener to retrieve data.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemsnapshot  :dataSnapshot.getChildren()){

                    Item item = itemsnapshot.getValue(Item.class);
                    item.setRemainingTime(item.getRemainingTime());
                    itemArray.itemList.add(item);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}