package com.example.auctionapp;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Item implements Serializable {

    private String name;
    private String description;
    private int category;
    private double currentPrice;
    private long endTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds
    private String itemKey;
    private String ownerID;
    private String currentWinner;
    private String currentWinnerName;
    private int auctionTimeHours;
    private String currentWinnerEmail;

    public Item() {
    }

    public Item(String name, String description, double currentPrice, long endTime, Uri imgUri, int category, int auctionTimeHours) {
        this.name = name;
        this.description = description;
        this.currentPrice = currentPrice;
        this.endTime = (System.currentTimeMillis() + (long) auctionTimeHours*60*60*1000); // we want seconds
        this.remainingTime = (long) auctionTimeHours*60*60 ;// seconds
        this.category = category;
        this.ownerID =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.currentWinner = "noHighestBidder";
        this.currentWinnerName = "No Bids Yet";
        this.auctionTimeHours = auctionTimeHours;
        this.currentWinnerEmail = "admin@gmail.com";
        this.itemKey = "noHighestBidder";
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

    public void setCurrentPrice(double currentPrice, FirebaseUser user){
        this.currentPrice = currentPrice;
        this.currentWinner = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.currentWinnerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        this.currentWinnerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

//    we want seconds
    public long getRemainingTime() {
        return ( this.endTime - System.currentTimeMillis())/1000;
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

    public String getCurrentWinnerEmail() {
        return currentWinnerEmail;
    }

    public void setCurrentWinnerEmail(String currentWinnerEmail) {
        this.currentWinnerEmail = currentWinnerEmail;
    }

    public void updateRemainingTime() {

        remainingTime = remainingTime - 1;
        if(remainingTime <= 0){
            winActionAfterTime();
        }
    }

    public void removeItemLocalItemList(){

        for(int i=0; i<itemArray.itemList.size(); i++){
            if(this == itemArray.itemList.get(i)){
                itemArray.itemList.remove(i);
                break;
            }
        }
    }

    public void winActionAfterTime(){
        //add winner data to Database
//        UserArray.RetrieveFromDatabaseWinSoldItems(this.currentWinner);
//        UserArray.UserWonItemMap.add(this);
//        UserArray.AddToDatabaseWinSoldItems(this.currentWinner);
        UserArray.AddToDatabaseWinSoldItems(this,this.currentWinner);
        removeFromDatabase();
        removeItemLocalItemList();

        //Restore Database to Current User
//        UserArray.RetrieveFromDatabaseWinSoldItems();
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