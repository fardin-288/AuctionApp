package com.example.auctionapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private String name;
    private String description;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    private int category;
    public static final String[] categoryString = new String[]{"electronic", "antiques", "instrument"};
    private double currentPrice;
    private long startTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds
    private Uri imgUri = null;

    private FirebaseUser ownerID;

    private FirebaseUser currentWinner = null;

    public Item() {

    }

    public Item(String name, String description, double currentPrice, long startTime, Uri imgUri,int category) {
        this.name = name;
        this.description = description;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.remainingTime = (startTime + 100000) - System.currentTimeMillis();
        this.currentWinner = null;
        this.imgUri = imgUri;
        this.category = category;
        this.ownerID = FirebaseAuth.getInstance().getCurrentUser();
    }

//    public Item(String name, String description, Item item, double currentPrice, long startTime, Uri imgUri) {
//        this.name = name;
//        this.description = description;
//        this.
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getRemainingTime() {
        return remainingTime + "";
    }

    public FirebaseUser getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(FirebaseUser ownerID) {
        this.ownerID = ownerID;
    }

    public void setCurrentWinner() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        this.currentWinner = user;
    }

    public FirebaseUser getCurrentWinner() {
        return this.currentWinner;
    }

    public String getCurrentWinnerName() {
        if (currentWinner != null) {
            return currentWinner.getDisplayName();
        }
        return "";
    }

    public void updateRemainingTime() {
        remainingTime = remainingTime - 1;
    }

    public void addToDatabase(Context context) {
//        DatabaseReference itemReference = databaseReference.push();
//        String key = databaseReference.push().getKey();
//        databaseReference.child(key).setValue(this);
//        itemReference.setValue(this);
//        Item item1 = new Item("amid", "okokoko",10.33,500,null);
////        System.out.println(item1.name);
//        Toast.makeText(context,this.name,Toast.LENGTH_LONG).show();
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList");
//        databaseReference.push().setValue("nirjhar","helloo world");
    }
}
class itemArray{
     static long total = 0;
    public static List<Item> itemList = new ArrayList<Item>();

    public static void incrementTotal(){
        total++;
    }

    public static long getTotal(){
        return total;
    }

    public static void ItemUpdateTimeRunnable(){
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ItemAdapter.updateAllitem();
//                    getView();
                    try {
                        Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        backgroundThread.start();

    }
}
