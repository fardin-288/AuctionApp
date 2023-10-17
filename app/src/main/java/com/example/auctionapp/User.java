package com.example.auctionapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable {
    String firebaseUserid;
    String name;
    String email;
    String key;

    public String getFirebaseUserid() {
        return firebaseUserid;
    }

    public void setFirebaseUserid(String firebaseUserid) {
        this.firebaseUserid = firebaseUserid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User(){

    }

    public User(String firebaseUserid, String name, String email, String key) {
        this.firebaseUserid = firebaseUserid;
        this.name = name;
        this.email = email;
        this.key = key;
    }

    public User(FirebaseUser firebaseUser) {
        this.firebaseUserid = firebaseUser.getUid();
        this.name = firebaseUser.getDisplayName();
        this.email = firebaseUser.getEmail();
        this.key = firebaseUser.getUid();
    }

    public static void AddUserToDatabase(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User newuser = new User(firebaseUser);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllDataList");
        databaseReference.child(newuser.getFirebaseUserid()).setValue(newuser);
    }

    public static void RetrieveUserFromDatabase(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("AllDataList");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot  :dataSnapshot.getChildren()){

                    User user = userSnapshot.getValue(User.class);
                    UserArray.userArrayList.add(user);

//                    Log.d("userlist",user.getName() + " " + user.getFirebaseUserid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

class UserArray{

    public static ArrayList<User> userArrayList = new ArrayList<>();
    public static User currentUser = new User();
    public static ArrayList<Item> UserWonItemMap = new ArrayList<>();
    public static ArrayList<Item> UserSoldItemMap = new ArrayList<>();

    public static void getCurrentUser() {

        FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();

        currentUser.setName(userAuth.getDisplayName());
        currentUser.setEmail(userAuth.getEmail());
        currentUser.setKey(userAuth.getUid());
        currentUser.setFirebaseUserid(userAuth.getUid());
    }

    public static void RetrieveFromDatabaseWinSoldItems(String UserId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WinnerItemKeyList").child(UserId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserWonItemMap.clear();

                for(DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    Item item = itemSnapshot.getValue(Item.class);
                    UserWonItemMap.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void RetrieveFromDatabaseWinSoldItems(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WinnerItemKeyList").child(currentUser.getFirebaseUserid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserWonItemMap.clear();

                for(DataSnapshot itemSnapshot : dataSnapshot.getChildren()){
                    Item item = itemSnapshot.getValue(Item.class);
                    UserWonItemMap.add(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Log.d("data win retrieved",UserWonItemMap.size()+"");
    }

    public static void AddToDatabaseWinSoldItems(String Uid){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WinnerItemKeyList");
        databaseReference.removeValue();

        databaseReference.child(Uid).setValue(UserWonItemMap);

    }

    public static void AddToDatabaseWinSoldItems(){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WinnerItemKeyList");
        databaseReference.removeValue();

        databaseReference.child(currentUser.getKey()).setValue(UserWonItemMap);

    }

    public static void UpdateUserListFromDatabase() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                userArrayList.clear();
                User.RetrieveUserFromDatabase();

                try {
                    Thread.sleep(1000 * 60 * 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
//
//class UserItems{
//
//    public static ArrayList<String> UserWonItemMap;
//    public static ArrayList<String> UserSoldItemMap;
//
//    public static void RetrieveFromDatabaseWinSoldItems(){
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WinnerItemKeyList");
//
//    }
//
////    public static void AddToDatabaseWinSoldItems(){
////        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("WinnerItemKeyList");
////        DatabaseReference.child(UserArray.)
////    }
//};
