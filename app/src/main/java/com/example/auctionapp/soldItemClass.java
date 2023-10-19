package com.example.auctionapp;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class soldItemClass extends AppCompatActivity {
    private RecyclerView soldItemRecycleView;
    private UserWonItemAdapter userWonItemAdapter;
    private Button goBackbuttonFromSoldItemView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_item_list_view); // Replace 'your_layout' with the layout XML file name

        soldItemRecycleView = findViewById(R.id.soldItemRecycleView);
        soldItemRecycleView.setLayoutManager(new LinearLayoutManager(this)); // Pass the context as an argument

        UserArray.RetrieveFromDatabaseSoldItemOfUser();
        Log.d("map size",UserArray.UserSoldItemMap.size()+"");
        userWonItemAdapter = new UserWonItemAdapter(UserArray.UserSoldItemMap);
        soldItemRecycleView.setAdapter(userWonItemAdapter);

//        Toast.makeText(this,UserArray.currentUser.firebaseUserid,Toast.LENGTH_SHORT).show();

//        Log.d("size of sold items", UserArray.UserSoldItemMap.size()+"");

        goBackbuttonFromSoldItemView = findViewById(R.id.goBackbuttonFromSoldItemView);
        goBackbuttonFromSoldItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


}
