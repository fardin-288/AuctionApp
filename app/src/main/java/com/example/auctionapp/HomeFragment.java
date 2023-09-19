package com.example.auctionapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int ADD_ITEM_REQUEST_CODE = 1;
//    private ArrayList<Item> itemList = new ArrayList<>();
    private ItemAdapter adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the adapter and ListView
        adapter = new ItemAdapter(requireActivity(), itemArray.itemList);
        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Handle "Add Item" button click to show a dialog
        rootView.findViewById(R.id.btnAddItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog();
            }
        });

        return rootView;
    }


    // Function to show the Add Item dialog
    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Item");

        // Set up the layout for the dialog
        View viewInflated = LayoutInflater.from(requireContext()).inflate(R.layout.dialogue_add_item, null);
        final EditText itemNameEditText = viewInflated.findViewById(R.id.editTextItemName);
        final EditText itemNameEditPrice = viewInflated.findViewById(R.id.editStartingPrice);
        final EditText itemDescriptionText = viewInflated.findViewById(R.id.editDescription);
        // Add more EditText fields for description, price, and picture URL if needed

        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the item details from the dialog
                String itemName = itemNameEditText.getText().toString();
                String itemPriceString = itemNameEditPrice.getText().toString();
                Float itemPriceFloat = Float.valueOf(itemPriceString);
                String itemDescription = itemDescriptionText.getText().toString();
                long currentTime = System.currentTimeMillis();
                // Add more code to retrieve description, price, and picture URL

                if (!itemName.isEmpty()) {
                    // Create a new Item object and add it to the list
                    Item newItem = new Item(itemName,itemDescription, itemPriceFloat,currentTime); // Modify as needed

                    itemArray.itemList.add(newItem);
                    adapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
