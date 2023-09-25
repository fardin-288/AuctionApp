package com.example.auctionapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static final int ADD_ITEM_REQUEST_CODE = 1;
//    private ArrayList<Item> itemList = new ArrayList<>();
    public static ItemAdapter adapter;
    public static ListView listView;
    public static final int PICK_IMAGE_REQUEST = 1;

    public static void main(String[] args) {
        HomeFragment myHomeFragment = new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Time Update
        itemArray.ItemUpdateTimeRunnable();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the adapter and ListView
        adapter = new ItemAdapter(requireActivity(), itemArray.itemList);
        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Handle "Add Item" button click to show a dialog
        rootView.findViewById(R.id.btnAddItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItemActivity.showAddItemDialog(getContext());
            }
        });

        return rootView;
    }
}
