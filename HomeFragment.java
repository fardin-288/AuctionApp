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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import android.content.ContentResolver;
import android.content.*;
public class HomeFragment extends Fragment {
    StorageTask uploadTask;
    private static final int IMAGE_REQUEST =1;
    private Uri imageuri;
    private Item tempItem;
    private Uri final_uri;
    private ImageView imageview;
    private FirebaseAuth auth;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Upload");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Upload");
    public static final int ADD_ITEM_REQUEST_CODE = 1;
//    private ArrayList<Item> itemList = new ArrayList<>();
    public static ItemAdapter adapter;
    public static ListView listView;
    private Spinner spinnerCategory;
    public static final int PICK_IMAGE_REQUEST = 1;

    private Handler handler = new Handler();


//    EditText itemNameEditText;
//    EditText itemNameEditPrice ;
//    EditText itemDescriptionText ;
    void showAddItemDialog(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Item");

        // Set up the layout for the dialog
        View   viewInflated = LayoutInflater.from(context).inflate(R.layout.dialogue_add_item, null);
     final EditText    itemNameEditText = viewInflated.findViewById(R.id.editTextItemName);
      final EditText    itemNameEditPrice = viewInflated.findViewById(R.id.editStartingPrice);
       final EditText  itemDescriptionText = viewInflated.findViewById(R.id.editDescription);
       Spinner   spinnerCategory = viewInflated.findViewById(R.id.spinnerCategory);
        Button buttonUploadPicture = viewInflated.findViewById(R.id.buttonUploadPicture);
        // Add more EditText fields for description, price, and picture URL if needed
//        imageview = findViewById(R.id.productImgView);

        databaseReference= FirebaseDatabase.getInstance().getReference("Upload");

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
                     tempItem=newItem;
                    newItem.addToDatabase(getContext());

                    HomeFragment.adapter.notifyDataSetChanged();

                    itemArray.incrementTotal();
                    if(uploadTask!=null && uploadTask.isInProgress())
                    {
                        Toast.makeText(context.getApplicationContext(), "Upload is in progress", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
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
//                saveData();
            }


        });


        builder.show();
    }

    public String getFileExtension(Uri imageuri)
    {
        ContentResolver contentResolver = this.getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));

    }


    void saveData(){

        StorageReference ref= storageReference.child(tempItem.getName());

        ref.putFile(final_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image is stored successfully ", Toast.LENGTH_SHORT).show();
                        String uploadId= databaseReference.push().getKey();
                        MyItem myItem = new MyItem(tempItem.getName(),tempItem.getDescription(),tempItem.getCategory(),tempItem.getCurrentPrice(),System.currentTimeMillis(),taskSnapshot.getStorage().getDownloadUrl().toString(), auth.getCurrentUser());
                         databaseReference.child(uploadId).setValue(myItem);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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

//        public void refresh(){
//            Thread backgroundThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                            myHomeFragment.onCreateView();
//                        try {
//                            Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            backgroundThread.start();
//
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Time Update
        itemArray.ItemUpdateTimeRunnable();

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

/*        RefreshClass refreshClass = new RefreshClass();
        refreshClass.refresh(rootView,getActivity());*/

//         Initialize the adapter and ListView
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


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 100);
            }
        }, 100);

        return rootView;
    }
}

class RefreshClass{

    public void refresh(View rootView,Activity activity){
        Thread backgroundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    HomeFragment.adapter = new ItemAdapter(activity, itemArray.itemList);
                    HomeFragment.listView = rootView.findViewById(R.id.listView);
                    HomeFragment.listView.setAdapter(HomeFragment.adapter);
                    try {
                        Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        backgroundThread.start();

    }
}
