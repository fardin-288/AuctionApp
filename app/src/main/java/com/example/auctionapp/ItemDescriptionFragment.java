package com.example.auctionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ItemDescriptionFragment {
    private TextView itemNameDescriptionFragment;
    private TextView itemTimeDescriptionFragment;
    private Button exitDescriptionButton;
    private Handler handler;
    private int position;
    private Activity activity;

    public ItemDescriptionFragment(final int position, Activity activity){
        this.position = position;
        this.activity = activity;
        handler = new Handler(Looper.getMainLooper());
    }

    public ItemDescriptionFragment() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_description_fragment, container, false);
        itemNameDescriptionFragment = view.findViewById(R.id.itemNameDescriptionFragment); // Replace with actual IDs
        itemTimeDescriptionFragment = view.findViewById(R.id.itemTimeDescriptionFragment); // Replace with actual IDs
//        Refresh();
        return view;
    }

    @SuppressLint("SetTextI18n")
    public AlertDialog.Builder Build(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Details");

        // Set up the layout for the dialog
        View viewInflated = LayoutInflater.from(activity).inflate(R.layout.item_description_fragment, null);
        final TextView itemNameDescriptionFragment = viewInflated.findViewById(R.id.itemNameDescriptionFragment);
        final TextView itemTimeDescriptionFragment = viewInflated.findViewById(R.id.itemTimeDescriptionFragment);

        itemTimeDescriptionFragment.setText("Time Left : " + itemArray.itemList.get(position).getRemainingTime() + "");
        itemNameDescriptionFragment.setText("Name of Obj : " + itemArray.itemList.get(position).getName());

        builder.setView(viewInflated);

        Log.d("oweee","started");

        return builder;
    }

    private static void Refresh() {

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }
//        handler.postDelayed(new Runnable() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void run() {
//                // Update your UI components here
//                // For example, updating item name and time
//                itemTimeDescriptionFragment.setText("Time Left : " + itemArray.itemList.get(0).getRemainingTime() + "");
//                itemTimeDescriptionFragment.setText("Updated Item Time");
//
//                // Call Refresh() again after 1 second for continuous refreshing
//                Refresh();
//            }
//        }, 1000);
        // Refresh every 1 second (1000 milliseconds)
    }
}
