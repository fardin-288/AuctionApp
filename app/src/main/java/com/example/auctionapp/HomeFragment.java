package com.example.auctionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class HomeFragment extends Fragment {
    StorageTask uploadTask;
    String key;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;
    private Item tempItem;
    private Uri final_uri;
    private ImageView imageview;
    private FirebaseAuth auth;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Upload");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Upload");
    public static final int ADD_ITEM_REQUEST_CODE = 1;
    public static ItemAdapter adapter;
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
                String imguri;
                long currentTime = System.currentTimeMillis();

                if (!itemName.isEmpty()) {
                    // Create a new Item object and add it to the list
                    final int category_Item = spinnerCategory.getSelectedItemPosition();
                    Item newItem = new Item(itemName, itemDescription, itemPriceFloat, currentTime, final_uri, category_Item);

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

    public String getFileExtension(Uri imageuri) {
        ContentResolver contentResolver = this.getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
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

        // Handle "Add Item" button click to show a dialog
        rootView.findViewById(R.id.btnAddItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddItemDialog(getContext());
            }
        });

        startRefresh();
//        itemArray.ItemUpdateTimeRunnable();

        return rootView;
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

    public static void refresh(View rootView, Activity activity) {
//        if (!refreshStatus) {
//            refreshStatus = true;
//            backgroundThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (refreshStatus) {
//                        activity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                itemArray.ItemUpdateTimeRunnable();
//                                RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
//                                HomeFragment.adapter = new ItemAdapter(activity, itemArray.itemList);
//                                HomeFragment.listView = rootView.findViewById(R.id.listView);
//                                HomeFragment.listView.setAdapter(HomeFragment.adapter);
//                                HomeFragment.adapter.notifyDataSetChanged();
//                                Log.d("weeee","okkkk");
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
        itemArray.ItemUpdateTimeRunnable();
        RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
        HomeFragment.adapter = new ItemAdapter(activity, itemArray.itemList);
        HomeFragment.listView = rootView.findViewById(R.id.listView);
        HomeFragment.listView.setAdapter(HomeFragment.adapter);
    }

    public static void stopRefresh() {
        refreshStatus = false;
        if (backgroundThread != null) {
            backgroundThread.interrupt();
        }
    }
}
