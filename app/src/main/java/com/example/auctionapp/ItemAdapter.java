package com.example.auctionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ItemAdapter extends ArrayAdapter<Item> {

    private Activity activity;
//    public static List<Item> itemList;


    public ItemAdapter(Activity activity, List<Item> itemList) {
        super(activity, 0, itemList);
        this.activity = activity;
//        this.itemList = itemList;
    }

    public void addDataFromFirebase()
    {

    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_list_item, parent, false);
        }



        Item item = itemArray.itemList.get(position);

        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);



        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        TextView itemDescriptionTextView = convertView.findViewById(R.id.itemDescriptionTextView);
        TextView itemPriceTextView = convertView.findViewById(R.id.itemPriceTextView);
        TextView itemTimeRemaining = convertView.findViewById(R.id.itemTimeRemaining);
        TextView itemcurrentWinnerName = convertView.findViewById(R.id.itemcurrentWinnerName);
        TextView itemCategoryTextView = convertView.findViewById(R.id.itemCategoryTextView);

        // Set the item's attributes in the views
//        itemImageView.setImageURI(item.getImgUri());
//        itemImageView.setImageResource(item.getPictureResource());
        itemNameTextView.setText(item.getName());
        itemDescriptionTextView.setText(item.getDescription());
        itemPriceTextView.setText(String.format(Locale.US, "Tk%.2f", item.getCurrentPrice()));
        itemTimeRemaining.setText(String.format(Locale.US,"time %s", item.getRemainingTime() ));
        itemcurrentWinnerName.setText(String.format(Locale.US,"Highest Bidder : %s", item.getCurrentWinnerName()));
        itemCategoryTextView.setText(String.format("Category : %s" ,itemArray.categoryString[item.getCategory()]));



//        String fileKey= item.getItemKey();
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference("Upload");
//        StorageReference fileRef = storageRef.child(fileKey);
//        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(itemImageView);
//                Glide.with(getContext().getApplicationContext()).load(uri).into(itemImageView);
//            }
//        });


        String fileKey = item.getItemKey();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("Upload");
        final StorageReference fileRef = storageRef.child(fileKey);

// Check if the local file exists
        File localFile = new File(getContext().getFilesDir(), fileKey);

        if (localFile.exists()) {
            // If the local file exists, load it using Picasso or Glide
            Picasso.get().load(localFile).into(itemImageView);
            // Alternatively, use Glide
            // Glide.with(getContext().getApplicationContext()).load(localFile).into(itemImageView);
        } else {
            // If the local file does not exist, download from the downloadUri
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Load the image using Picasso or Glide
                    Picasso.get().load(uri).into(itemImageView);
                    // Alternatively, use Glide
                    // Glide.with(getContext().getApplicationContext()).load(uri).into(itemImageView);

                    // Download the image to local storage
                    downloadImageToLocal(fileRef, localFile);
                }

                private void downloadImageToLocal(StorageReference fileRef, final File localFile) {
                    fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // File downloaded successfully to local storage
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure( Exception exception) {
                            // Handle the error if the file download fails
                        }
                    });
                }

            });
        }





        // Changing the Price
        Button changePriceButton = convertView.findViewById(R.id.changePriceButton);
        changePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePriceDialog(position);
            }
        });

        Button removeItemButton = convertView.findViewById(R.id.removeItemButton);
        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removebuttonwork(position);
            }
        });

        return convertView;
    }

    private void removebuttonwork(final int position){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(Objects.equals(getItem(position).getOwnerID(), user.getUid())){
            itemArray.itemList.get(position).removeFromDatabase();
            itemArray.itemList.remove(position);
            Toast.makeText(getContext(), "owner", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "not owner", Toast.LENGTH_SHORT).show();
        }

        notifyDataSetChanged();
    }

    private void showChangePriceDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Change Price");

        // Set up the layout for the dialog
        View viewInflated = LayoutInflater.from(activity).inflate(R.layout.dialog_change_price, null);
        final EditText newPriceEditText = viewInflated.findViewById(R.id.editTextNewPrice);
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPriceText = newPriceEditText.getText().toString();
                if (!newPriceText.isEmpty()) {
                    double newPrice = Double.parseDouble(newPriceText);
                    if (newPrice > getItem(position).getCurrentPrice()) {
                        // Update the item's price and notify the adapter
                        getItem(position).setCurrentPrice(newPrice,FirebaseAuth.getInstance().getCurrentUser());
                        getItem(position).setCurrentWinner();
                        getItem(position).updatePriceToDatabase();
                        notifyDataSetChanged();

                    } else {
                        // Show an error toast if the new price is not greater
                        Toast.makeText(activity, "New price must be greater than the current price", Toast.LENGTH_SHORT).show();
                    }
                }
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

