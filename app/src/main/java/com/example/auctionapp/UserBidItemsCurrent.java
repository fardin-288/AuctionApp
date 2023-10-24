package com.example.auctionapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class UserBidItemsCurrent {

    public static ArrayList<Item> UserCurrentBidItemList = new ArrayList<>();

    public static void RetrieveUserCurrentBidItemFromDatabase(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCurrentBidItems").child(UserArray.currentUser.getKey());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserCurrentBidItemList.clear();

                for(DataSnapshot itemSnapshot : dataSnapshot.getChildren()){

                    Item item = itemSnapshot.getValue(Item.class);
                    UserCurrentBidItemList.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void updateItemToCurrentBidItemListDatabase(Item item){
        RetrieveUserCurrentBidItemFromDatabase();
        boolean flag = true;

        for(int i = 0; i< itemArray.itemList.size() && flag; i++){
            if(Objects.equals(itemArray.itemList.get(i).getItemKey(),item.getItemKey())){
                for(int j=0; j< UserBidItemsCurrent.UserCurrentBidItemList.size(); j++){
                    if(Objects.equals(item.getItemKey(),UserBidItemsCurrent.UserCurrentBidItemList.get(j).getItemKey())){
                        UserBidItemsCurrent.UserCurrentBidItemList.set(j,itemArray.itemList.get(i));
                        flag = false;
                        break;
                    }
                }
            }
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCurrentBidItems").child(UserArray.currentUser.getFirebaseUserid());
        databaseReference.setValue(UserCurrentBidItemList);
        Log.d("amake","amake dakse");
    }

    public static Item RefreshUserCurrentBidItemInfoFromItemListDatabase(Item item){
        RetrieveDataFromFirebase.RetrieveDataFromDatabaseStatus = false;
        RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();

        for(int i=0; i<itemArray.itemList.size(); i++){
            if(Objects.equals(itemArray.itemList.get(i).getItemKey(), item.getItemKey())){
                return itemArray.itemList.get(i);
            }
        }
        return item;
    }

    public static void addItemToUserCurrentBidItemListDatabase(Item item){
        RetrieveUserCurrentBidItemFromDatabase();

        for(Item selectedItem : UserCurrentBidItemList){
            if(Objects.equals(selectedItem.getItemKey(), item.getItemKey())){
//                updateItemToCurrentBidItemListDatabase(item);
                return;
            }
        }

        UserCurrentBidItemList.add(item);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCurrentBidItems").child(UserArray.currentUser.getFirebaseUserid());
        databaseReference.setValue(UserCurrentBidItemList);
    }

    public static void removeItemFromUserCurrentBidItemListDatabase(Item item){
        RetrieveUserCurrentBidItemFromDatabase();

        for(int i=0; i< UserCurrentBidItemList.size(); i++){
            if(Objects.equals(UserCurrentBidItemList.get(i).getItemKey(),item.getItemKey())){
                UserCurrentBidItemList.remove(i);
            }
        }

//        for(Item selectedItem : UserCurrentBidItemList){
//            if(Objects.equals(selectedItem.getItemKey(), item.getItemKey())){
//                UserCurrentBidItemList.remove(selectedItem);
//            }
//        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserCurrentBidItems").child(UserArray.currentUser.getFirebaseUserid());
        databaseReference.setValue(UserCurrentBidItemList);
    }
}
