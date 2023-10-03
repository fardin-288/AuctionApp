package com.example.auctionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private static final int IMAGE_REQUEST =1;
    private Uri imageuri;
    private Uri final_uri;
    private ImageView imageview;
    private FirebaseAuth auth;

    public static final int ADD_ITEM_REQUEST_CODE = 1;
//    private ArrayList<Item> itemList = new ArrayList<>();
    public static ItemAdapter adapter;
    public static ListView listView;
    private Spinner spinnerCategory;
    public static final int PICK_IMAGE_REQUEST = 1;


    void showAddItemDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Item");

        // Set up the layout for the dialog
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.dialogue_add_item, null);
        final EditText itemNameEditText = viewInflated.findViewById(R.id.editTextItemName);
        final EditText itemNameEditPrice = viewInflated.findViewById(R.id.editStartingPrice);
        final EditText itemDescriptionText = viewInflated.findViewById(R.id.editDescription);
        final Spinner spinnerCategory = viewInflated.findViewById(R.id.spinnerCategory);
        Button buttonUploadPicture = viewInflated.findViewById(R.id.buttonUploadPicture);
        // Add more EditText fields for description, price, and picture URL if needed
//        imageview = findViewById(R.id.productImgView);

        imageview = viewInflated.findViewById(R.id.productImgView);
        builder.setView(viewInflated);

        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getContext(),R.layout.category_spinner_text,R.id.categoryTextView,Item.categoryString);
        spinnerCategory.setAdapter(adapterCategory);
//        final int category_Item = spinnerCategory.getSelectedItemPosition();

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
                    Item newItem = new Item(itemName,itemDescription, itemPriceFloat,currentTime,final_uri,category_Item); // Modify as needed

                    itemArray.itemList.add(newItem);
                    newItem.addToDatabase(getContext());
                    HomeFragment.adapter.notifyDataSetChanged();

                    itemArray.incrementTotal();
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
//                Toast.makeText(context, "This is a Toast message", Toast.LENGTH_SHORT).show();
////                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//////                startActivityForResult(context, HomeFragment.PICK_IMAGE_REQUEST);
            }
        });


        builder.show();
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
            final_uri=imageuri;
//            profileImageView.setImageURI(imageUri);
//            uploadImageToFirebase();
        }
    }


    private void uploadImageToFirebase() {
        if (imageuri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profile_images") // You can customize this storage path
                    .child(auth.getCurrentUser().getUid() + ".jpg");

            storageReference.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // You can save the download URL to Firebase Database or perform other actions
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle image upload failure
                        }
                    });
        }
    }




    public static void main(String[] args) {

        HomeFragment myHomeFragment = new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
//        listView = rootView.findViewById(R.id.listView);
//        ArrayAdapter<Item> adapter = new ArrayAdapter<>(
//                getContext(),
//                R.layout.fragment_home,
//                R.layout.item_list_item,
//                itemArray.itemList
//        );

//        Time Update
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
                showAddItemDialog(getContext());
            }
        });

        return rootView;
    }
}
