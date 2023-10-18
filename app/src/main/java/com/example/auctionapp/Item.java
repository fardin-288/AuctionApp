package com.example.auctionapp;

import android.content.Context;
import android.net.ProxyInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item implements Serializable {

    private String name;
    private String description;
    private int category;
    private double currentPrice;
    private long startTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds
    private String itemKey;
    private String ownerID;
    private String currentWinner = null;
    private String currentWinnerName;
    private int auctionTimeHours;

    public Item() {

    }

    public Item(String name, String description, double currentPrice, long startTime, Uri imgUri,int category,int auctionTimeHours) {
        this.name = name;
        this.description = description;
        this.currentPrice = currentPrice;
        this.startTime = (startTime + (long) auctionTimeHours *60*60*1000)/1000; // we want seconds
        this.remainingTime = (long) auctionTimeHours *60*60*1000;// milli seconds
        this.currentWinner = null;
//        this.imgUri = imgUri;
        this.category = category;
        this.ownerID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.currentWinner = ownerID;
        this.currentWinnerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        this.auctionTimeHours = auctionTimeHours;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getCurrentWinner() {
        return currentWinner;
    }

    public void setCurrentWinner(String currentWinner) {
        this.currentWinner = currentWinner;
    }

    public void setCurrentWinner(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String FirebaseUserId = user.getUid();

        this.currentWinner = FirebaseUserId;
    }

    public String getCurrentWinnerName() {
        return currentWinnerName;
    }

    public void setCurrentWinnerName(String currentWinnerName) {
        this.currentWinnerName = currentWinnerName;
    }

    public int getAuctionTimeHours() {
        return auctionTimeHours;
    }

    public void setAuctionTimeHours(int auctionTimeHours) {
        this.auctionTimeHours = auctionTimeHours;
    }

    public void updateRemainingTime() {
        remainingTime = remainingTime - 1;
    }

    public String addToDatabase(Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList");
        String key = databaseReference.push().getKey();
        this.itemKey = key;
        databaseReference.child(key).setValue(this);
        return key;
    }

    public void removeFromDatabase(){
        String key = this.getItemKey();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList").child(key);
        databaseReference.removeValue();
    }

    public void updatePriceToDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList").child(this.getItemKey());
        databaseReference.child("currentPrice").setValue(this.getCurrentPrice());
        databaseReference.child("currentWinnerName").setValue(this.getCurrentWinnerName());
        databaseReference.child("currentWinner").setValue(this.getCurrentWinner());

    }
}
class itemArray{
    static long total = 0;
    public static final String[] categoryString = new String[]{"electronic", "antiques", "instrument"};

    public static List<Item> itemList = new ArrayList<Item>();

    public static void incrementTotal(){
        total++;
    }

    public static boolean ItemArrayDecreaseStatus = false;

    public static long getTotal(){
        return total;
    }

    public static void updateAllitem(){
        for(Item a: itemArray.itemList){
            a.updateRemainingTime();
        }
    }

    public static void ItemUpdateTimeRunnable(){
        if(ItemArrayDecreaseStatus)return;
        ItemArrayDecreaseStatus = true;
        Thread backgroundThreadItemListTime = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    updateAllitem();
                    try {
                        Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        backgroundThreadItemListTime.start();

    }
}