package com.example.auctionapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserWonItemAdapter userWonItemAdapter;
    private List<Item> itemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize itemList with your data (e.g., fetch it from Firebase)
        // itemList = fetchItemListFromFirebase();

//        Toast.makeText(getActivity(),UserArray.UserWonItemMap.size()+"",Toast.LENGTH_SHORT);

        UserArray.RetrieveFromDatabaseWinSoldItems();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Use getActivity() to get the context in a Fragment

        userWonItemAdapter = new UserWonItemAdapter(UserArray.UserWonItemMap);
        recyclerView.setAdapter(userWonItemAdapter);

        return view;
    }

    // Fetch the item list from Firebase or any other data source
    // You can implement your code to retrieve the item list here
    // For example, use Firebase Realtime Database to fetch the data.
    // Return a list of Item objects.
}
