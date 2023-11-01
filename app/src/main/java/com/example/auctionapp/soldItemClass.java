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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class soldItemClass extends AppCompatActivity {
    private RecyclerView soldItemRecycleView;
    private UserSoldItemAdapter userSoldItemAdapter;
    private Button goBackbuttonFromSoldItemView;
    private SwipeRefreshLayout SoldItemRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_item_list_view); // Replace 'your_layout' with the layout XML file name

        soldItemRecycleView = findViewById(R.id.soldItemRecycleView);
        soldItemRecycleView.setLayoutManager(new LinearLayoutManager(this)); // Pass the context as an argument

        UserArray.RetrieveFromDatabaseSoldItemOfUser();
        userSoldItemAdapter = new UserSoldItemAdapter(UserArray.UserSoldItemMap);
        soldItemRecycleView.setAdapter(userSoldItemAdapter);

        goBackbuttonFromSoldItemView = findViewById(R.id.goBackbuttonFromSoldItemView);
        goBackbuttonFromSoldItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SoldItemRefresh = findViewById(R.id.SoldItemRefresh);
        SoldItemRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserArray.RetrieveFromDatabaseSoldItemOfUser();
                SoldItemRefresh.setRefreshing(false);
            }
        });

    }


}
