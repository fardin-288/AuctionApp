package com.example.auctionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.firebase.ui.database.FirebaseListOptions;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    StorageTask uploadTask;
    String key;
    MenuItem menuItem;
    public static SearchView searchView;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;
    private Item tempItem;
    private Uri final_uri;

    private ImageView imageview;
    private FirebaseAuth auth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Upload");
    public static final int ADD_ITEM_REQUEST_CODE = 1;
    public static ItemAdapter adapter,temporaryAdapter;
    public ArrayList<Item> temp = new ArrayList<Item>();
    public static ListView listView;
    private Spinner spinnerCategory;
    public static final int PICK_IMAGE_REQUEST = 1;
    private static boolean UIRefreshStatus = false;
    private View rootView;

    void showAddItemDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Item");

        // Set up the layout for the dialog
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialogue_add_item, null);
        final EditText itemNameEditText = viewInflated.findViewById(R.id.editTextItemName);
        final EditText itemNameEditPrice = viewInflated.findViewById(R.id.editStartingPrice);
        final EditText itemDescriptionText = viewInflated.findViewById(R.id.editDescription);
        final EditText itemAuctionHoursTimeEditText = viewInflated.findViewById(R.id.editAuctionHours);
        spinnerCategory = viewInflated.findViewById(R.id.spinnerCategory);
        Button buttonUploadPicture = viewInflated.findViewById(R.id.buttonUploadPicture);
        imageview = viewInflated.findViewById(R.id.productImgView);
        builder.setView(viewInflated);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getContext(), R.layout.category_spinner_text, R.id.categoryTextView, itemArray.categoryString);
        spinnerCategory.setAdapter(adapterCategory);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the item details from the dialog
                String itemName = itemNameEditText.getText().toString();
                String itemPriceString = itemNameEditPrice.getText().toString();
                Float itemPriceFloat = Float.valueOf(itemPriceString);
                String itemDescription = itemDescriptionText.getText().toString();
                int itemAuctionHoursTime = Integer.parseInt(itemAuctionHoursTimeEditText.getText().toString());
                String imguri;
                long currentTime = System.currentTimeMillis();

                if (!itemName.isEmpty()) {
                    // Create a new Item object and add it to the list
                    final int category_Item = spinnerCategory.getSelectedItemPosition();
                    Item newItem = new Item(itemName, itemDescription, itemPriceFloat, currentTime, final_uri, category_Item, itemAuctionHoursTime);

                    itemArray.itemList.add(newItem);
                    tempItem = newItem;
                    key = newItem.addToDatabase(getContext());

                    HomeFragment.adapter.notifyDataSetChanged();

                    itemArray.incrementTotal();
                    if (uploadTask != null && uploadTask.isInProgress()) {
                        Toast.makeText(context.getApplicationContext(), "Upload is in progress", Toast.LENGTH_SHORT).show();
                    } else {
                        saveData();
                    }
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

        // Add picture here
        buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        builder.show();
    }

    void saveData() {
        StorageReference ref = storageReference.child(key);
        ref.putFile(final_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image is stored successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image is not stored successfully" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            imageview.setImageURI(imageuri);
            final_uri = imageuri;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
        HomeFragment.adapter = new ItemAdapter(getActivity(),itemArray.itemList);

        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        HomeFragment.temporaryAdapter = new ItemAdapter(getActivity(),itemArray.itemList);

        // Handle "Add Item" button click to show a dialog
        searchView = rootView.findViewById(R.id.HomeSearchViewId);
        List<Item> temp = new ArrayList<Item>();
        temp.addAll(itemArray.itemList);

        //Refresh when swiping down
        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RetrieveDataFromFirebase.RetrieveDataFromDatabaseStatus = false;
                RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Item> newlist = new ArrayList<Item>();
                List<Item> temp = new ArrayList<Item>();

                if (s.length()>0)

                {
                    for(Item x: itemArray.itemList)
                    {
                        if (x.getName().toLowerCase().contains(s.toLowerCase())) {
//                            Toast.makeText(getActivity(), x.getName() + " " + x.getDescription(), Toast.LENGTH_SHORT).show();
                            newlist.add(x);
                        }

                    }

                    itemArray.itemList.clear();
                    itemArray.itemList.addAll(newlist);
                    temporaryAdapter = new ItemAdapter(getActivity(), itemArray.itemList);
//                    temporaryAdapter = new ItemAdapter(getActivity(), newlist);

                    listView.setAdapter(temporaryAdapter);
                }
                else
                {
                    itemArray.itemList.clear();
                    itemArray.itemList.addAll(temp);
                    adapter= new ItemAdapter(getActivity(),itemArray.itemList);
                    listView.setAdapter(adapter);
                    startRefresh();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                List<Item> newlist = new ArrayList<Item>();

                if (s.length()>0)

                {
                    for(Item x: itemArray.itemList)
                    {
                        if (x.getName().toLowerCase().contains(s.toLowerCase())) {
//                            Toast.makeText(getActivity(), x.getName() + " " + x.getDescription(), Toast.LENGTH_SHORT).show();
                            newlist.add(x);
                        }
                    }

                    itemArray.itemList.clear();
                    itemArray.itemList.addAll(newlist);
                    temporaryAdapter = new ItemAdapter(getActivity(), itemArray.itemList);

                    listView.setAdapter(temporaryAdapter);
                }
                else
                {
                    itemArray.itemList.clear();
                    itemArray.itemList.addAll(temp);
                    adapter= new ItemAdapter(getActivity(),itemArray.itemList);
                    listView.setAdapter(adapter);
                    startRefresh();
                }
                return true;
            }
        });

        rootView.findViewById(R.id.btnAddItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(getContext());
            }
        });
        startRefresh();
        return rootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }



    private void mySearch(String s) {
        List<Item> filteredList = new ArrayList<>();

        for (Item item : itemArray.itemList) {
            if (item.getName().toLowerCase().contains(s.toLowerCase())) {
                filteredList.add(item);
                Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRefresh();
    }

    void startRefresh() {
        RefreshClass.refresh(rootView, getActivity());
    }

    void stopRefresh() {
        RefreshClass.stopRefresh();
    }
}

class RefreshClass {
    private static boolean refreshStatus = false;
    private static Thread backgroundThread;

//    ItemAdapter itemAdapter = new ItemAdapter(act)

    public static void refresh(View rootView, Activity activity) {
        itemArray.ItemUpdateTimeRunnable();
        if (!refreshStatus) {
            refreshStatus = true;
            backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (refreshStatus) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
                                if(itemArray.itemList.size() == 0)return;
//                                HomeFragment.adapter = new ItemAdapter(activity,itemArray.itemList);
                                // Get the range of visible items in the ListView
                                int firstVisiblePosition = HomeFragment.listView.getFirstVisiblePosition();
                                int lastVisiblePosition = HomeFragment.listView.getLastVisiblePosition();

//                                int firstVisiblePositionSearch = HomeFragment.searchView.getFirstVisiblePosition();
//                                int lastVisiblePositionSearch = HomeFragment.searchView.getLastVisiblePosition();
//                                Log.d("listview first last",HomeFragment.listView.getFirstVisiblePosition() + " " + HomeFragment.listView.getLastVisiblePosition());

                                // Iterate through visible items and update their data
                                for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {

                                    //if item removed realtime
                                    if(i <= itemArray.itemList.size()){
                                        break;
                                    }
//                                    itemArray.itemList.get(i).RetrieveItemPriceFromDatabase();

                                    Item item = HomeFragment.adapter.getItem(i);
                                    item.RetrieveItemPriceFromDatabase();
//                                    item.setRemainingTime(item.getRemainingTime());
                                }

                                // Notify the adapter that the data has changed for visible items
                                HomeFragment.adapter.notifyDataSetChanged();
                                HomeFragment.temporaryAdapter.notifyDataSetChanged();
                                Log.d("listview refreshed",HomeFragment.listView.getFirstVisiblePosition() + " " + HomeFragment.listView.getLastVisiblePosition());

                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            backgroundThread.start();
        }
    }

//    public static void refresh(View rootView, Activity activity) {
//
//        if (!refreshStatus) {
//            refreshStatus = true;
//            backgroundThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    while (refreshStatus) {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                itemArray.ItemUpdateTimeRunnable();
//                                RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
//                                HomeFragment.adapter = new ItemAdapter(activity, itemArray.itemList);
//                                HomeFragment.listView = rootView.findViewById(R.id.listView);
//                                HomeFragment.listView.setAdapter(HomeFragment.adapter);
//                                HomeFragment.adapter.notifyDataSetChanged();
//                                Log.d("weeee","okkkk");
//                                refreshStatus=false;
//                            }
//                        });
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            backgroundThread.start();
//        }
//    }

    public static void stopRefresh() {
        refreshStatus = false;
        if (backgroundThread != null) {
            backgroundThread.interrupt();
        }
    }
}
