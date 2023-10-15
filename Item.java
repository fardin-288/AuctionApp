package com.example.auctionapp;

import android.content.Context;
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


class ItemforDatabaseupload{
    String name;
    String description;
    private int category;
    private double currentPrice;
    private long startTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds
    private Uri imgUri = null;
    private String ItemKey;
    private String ownerID;
    private String currentWinner = null;

    public ItemforDatabaseupload(String name, String description, int category, double currentPrice, long startTime, long remainingTime, Uri imgUri, String itemKey, FirebaseUser ownerID, FirebaseUser currentWinner) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.remainingTime = remainingTime;
        this.imgUri = imgUri;
        this.ItemKey = itemKey;
        this.ownerID = ownerID.getUid();
        this.currentWinner = ownerID.getUid();
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

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public String getItemKey() {
        return ItemKey;
    }

    public void setItemKey(String itemKey) {
        ItemKey = itemKey;
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
}



class MyItem{
    String name;
    String description;
    private int category;
    private double currentPrice;
    private long startTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds
    private String imgUri = null;
    private String ItemKey;
    private String ownerID;
    private String currentWinner = null;

    public MyItem(String name, String description, int category, double currentPrice, long startTime, String imgUri, FirebaseUser ownerID) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.remainingTime = startTime;
        this.imgUri = imgUri;
        this.ownerID = ownerID.getUid();
        this.currentWinner = ownerID.getUid();
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

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getItemKey() {
        return ItemKey;
    }

    public void setItemKey(String itemKey) {
        ItemKey = itemKey;
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
}



public class Item implements Serializable {

    private String name;
    private String description;
    private int category;
    public static final String[] categoryString = new String[]{"electronic", "antiques", "instrument"};
    private double currentPrice;
    private long startTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds
    private Uri imgUri = null;
    private String ItemKey;
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
        this.ownerID =  FirebaseAuth.getInstance().getCurrentUser();
        this.currentWinner = ownerID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

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

    public String addToDatabase(Context context) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList");
        String key = databaseReference.push().getKey();
        this.ItemKey = key;

        ItemforDatabaseupload itemforDatabaseupload = new ItemforDatabaseupload(this.name,this.description,this.category,this.currentPrice,this.startTime,this.remainingTime,null,key,this.getOwnerID(),this.getCurrentWinner());
        databaseReference.child(key).setValue(itemforDatabaseupload);
        return key;
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





//package com.example.auctionapp;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.net.Uri;
//import android.util.Log;
//import android.webkit.MimeTypeMap;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.common.net.InternetDomainName;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//
//import java.io.Serializable;
//import java.time.Duration;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import android.content.*;
//class ItemforDatabaseupload{
//    String name;
//    String description;
//    private int category;
//    private double currentPrice;
//    private long startTime; // End time in milliseconds
//    private long remainingTime; // Remaining time in milliseconds
//    private String imgUri = null;
//    private String ItemKey;
//    private String ownerID;
//    private String currentWinner = null;
//
//    public ItemforDatabaseupload(String name, String description, int category, double currentPrice, long startTime, long remainingTime, String imgUri, String itemKey, FirebaseUser ownerID, FirebaseUser currentWinner) {
//        this.name = name;
//        this.description = description;
//        this.category = category;
//        this.currentPrice = currentPrice;
//        this.startTime = startTime;
//        this.remainingTime = remainingTime;
//        this.imgUri = imgUri;
//        this.ItemKey = itemKey;
//        this.ownerID = ownerID.getUid();
//        this.currentWinner = ownerID.getUid();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public int getCategory() {
//        return category;
//    }
//
//    public void setCategory(int category) {
//        this.category = category;
//    }
//
//    public double getCurrentPrice() {
//        return currentPrice;
//    }
//
//    public void setCurrentPrice(double currentPrice) {
//        this.currentPrice = currentPrice;
//    }
//
//    public long getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(long startTime) {
//        this.startTime = startTime;
//    }
//
//    public long getRemainingTime() {
//        return remainingTime;
//    }
//
//    public void setRemainingTime(long remainingTime) {
//        this.remainingTime = remainingTime;
//    }
//
//    public String getImgUri() {
//        return imgUri;
//    }
//
//    public void setImgUri(String imgUri) {
//        this.imgUri = imgUri;
//    }
//
//    public String getItemKey() {
//        return ItemKey;
//    }
//
//    public void setItemKey(String itemKey) {
//        ItemKey = itemKey;
//    }
//
//    public String getOwnerID() {
//        return ownerID;
//    }
//
//    public void setOwnerID(String ownerID) {
//        this.ownerID = ownerID;
//    }
//
//    public String getCurrentWinner() {
//        return currentWinner;
//    }
//
//    public void setCurrentWinner(String currentWinner) {
//        this.currentWinner = currentWinner;
//    }
//}
//
//public class Item implements Serializable {
//
//    private String name;
//    private String description;
//    private int category;
//    public static final String[] categoryString = new String[]{"electronic", "antiques", "instrument"};
//    private double currentPrice;
//    private long startTime; // End time in milliseconds
//    private long remainingTime; // Remaining time in milliseconds
//    private Uri imgUri = null;
//    private String ItemKey;
//    private FirebaseUser ownerID;
//    private FirebaseUser currentWinner = null;
//
//    public String getItemKey() {
//        return ItemKey;
//    }
//
//    public void setItemKey(String itemKey) {
//        ItemKey = itemKey;
//    }
//
//    public Item() {
//
//    }
//
//    public Item(String name, String description, double currentPrice, long startTime, Uri imgUri,int category) {
//        this.name = name;
//        this.description = description;
//        this.currentPrice = currentPrice;
//        this.startTime = startTime;
//        this.remainingTime = (startTime + 100000) - System.currentTimeMillis();
//        this.currentWinner = null;
//        this.imgUri = imgUri;
//        this.category = category;
//        this.ownerID =  FirebaseAuth.getInstance().getCurrentUser();
//        this.currentWinner = ownerID;
//    }
//
//    public int getCategory() {
//        return category;
//    }
//
//    public void setCategory(int category) {
//        this.category = category;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Uri getImgUri() {
//        return imgUri;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public double getCurrentPrice() {
//        return currentPrice;
//    }
//
//    public void setCurrentPrice(double currentPrice) {
//        this.currentPrice = currentPrice;
//    }
//
//    public String getRemainingTime() {
//        return remainingTime + "";
//    }
//
//    public FirebaseUser getOwnerID() {
//        return ownerID;
//    }
//
//    public void setOwnerID(FirebaseUser ownerID) {
//        this.ownerID = ownerID;
//    }
//
//    public void setCurrentWinner() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//
//        this.currentWinner = user;
//    }
//
//    public FirebaseUser getCurrentWinner() {
//        return this.currentWinner;
//    }
//
//    public String getCurrentWinnerName() {
//        if (currentWinner != null) {
//            return currentWinner.getDisplayName();
//        }
//        return "";
//    }
//
//    public void updateRemainingTime() {
//        remainingTime = remainingTime - 1;
//    }
//
//
//
////    public void addToDatabase(Context context,Uri final_url) {
////
////        StorageReference storageReference = FirebaseStorage.getInstance().getReference("upload");
////        StorageReference ref= storageReference.child(System.currentTimeMillis()+"."+ final_url.toString());
////
//////        Toast.makeText(g, "aisiii", Toast.LENGTH_SHORT).show();
//////
//////        ref.putFile(final_url)
//////                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//////                    @Override
//////                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//////                        Toast.makeText(getActivity().getApplicationContext(), "Image is stored successfully", Toast.LENGTH_SHORT).show();
//////                        String uploadId= databaseReference.push().getKey();
////////                        ItemforDatabaseupload itemforDatabaseupload = new ItemforDatabaseupload(tempItem.getName(),tempItem.getDescription(),tempItem.getCategory(),tempItem.getCurrentPrice(),System.currentTimeMillis(),System.currentTimeMillis(),taskSnapshot.getStorage().getDownloadUrl().toString(), uploadId, auth.getCurrentUser(), auth.getCurrentUser());
////////                        ItemforDatabaseupload itemforDatabaseupload = new ItemforDatabaseupload,this.description,this.category,this.currentPrice,this.startTime,this.remainingTime,imgUri,key,this.getOwnerID(),this.getCurrentWinner());
//////
//////                        databaseReference.child(uploadId).setValue(itemforDatabaseupload);
//////                    }
//////                }).addOnFailureListener(new OnFailureListener() {
//////                    @Override
//////                    public void onFailure(@NonNull Exception e) {
//////                        Toast.makeText(getActivity().getApplicationContext(), "Image is stored successfully", Toast.LENGTH_SHORT).show();
//////
//////
//////                    }
//////                });
////
////        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllItemList");
////
////        String key = databaseReference.push().getKey();
////        this.ItemKey = key;
////
////        ItemforDatabaseupload itemforDatabaseupload = new ItemforDatabaseupload(this.name,this.description,this.category,this.currentPrice,this.startTime,this.remainingTime,imgUri,key,this.getOwnerID(),this.getCurrentWinner());
////        databaseReference.child(key).setValue(itemforDatabaseupload);
////    }
//}
//class itemArray{
//     static long total = 0;
//    public static List<Item> itemList = new ArrayList<Item>();
//
//    public static void incrementTotal(){
//        total++;
//    }
//
//    public static long getTotal(){
//        return total;
//    }
//
//    public static void ItemUpdateTimeRunnable(){
//        Thread backgroundThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    ItemAdapter.updateAllitem();
//                    try {
//                        Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        backgroundThread.start();
//
//    }
//}