package com.example.auctionapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UserBidItemAdapter extends ArrayAdapter<Item> {
    private    File localFile;
    private Activity activity;
    private int FinalPosition;
//    public static List<Item> itemList;


    public UserBidItemAdapter(Activity activity, List<Item> itemList) {
        super(activity, 0, itemList);
        this.activity = activity;
//        this.itemList = itemList;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_list_item, parent, false);
        }

        RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();
        Item item = getItem(position);

        for(int i=0; i< itemArray.itemList.size(); i++){
            if(Objects.equals(item.getItemKey(), itemArray.itemList.get(i).getItemKey())){
                FinalPosition = i;
                break;
            }
        }

        if(Objects.equals(item.getCurrentWinner(), UserArray.currentUser.getKey())){
            convertView.setBackgroundColor(Color.parseColor("#9AF78D"));
        }else{
            convertView.setBackgroundColor(Color.parseColor("#FF5353"));
        }


        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        TextView itemDescriptionTextView = convertView.findViewById(R.id.itemDescriptionTextView);
        TextView itemPriceTextView = convertView.findViewById(R.id.itemPriceTextView);
        TextView itemTimeRemaining = convertView.findViewById(R.id.itemTimeRemaining);
        TextView itemcurrentWinnerName = convertView.findViewById(R.id.itemcurrentWinnerName);
        TextView itemCategoryTextView = convertView.findViewById(R.id.itemCategoryTextView);
        itemNameTextView.setText(item.getName());
        itemDescriptionTextView.setText(item.getDescription());

        long TimeRemainingInSeconds = item.getRemainingTime();
        String timeInStandardFormat = itemArray.TimeSecondToStandardStringFormat(TimeRemainingInSeconds);

        NumberFormat priceFormat = NumberFormat.getNumberInstance(Locale.US);

        itemPriceTextView.setText(String.format(Locale.US, "%s", priceFormat.format(item.getCurrentPrice())));
        itemTimeRemaining.setText(String.format(Locale.US,"%s", timeInStandardFormat ));
        itemcurrentWinnerName.setText(String.format(Locale.US,"%s", item.getCurrentWinnerName()));
        itemCategoryTextView.setText(String.format("%s" ,itemArray.categoryString[item.getCategory()]));





        String fileKey = item.getItemKey();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("Upload");
        final StorageReference fileRef = storageRef.child(fileKey);

        // Check if the local file exists
        localFile = new File(getContext().getFilesDir(), fileKey);

        if (false) {
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


        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(item);
            }
        });


        // Changing the Price
        Button changePriceButton = convertView.findViewById(R.id.changePriceButton);
        changePriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for testing it is commented out
                showChangePriceDialog(FinalPosition);
            }
        });

        Button contactSellerButton = convertView.findViewById(R.id.contactSellerButton);
        contactSellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactSellerFunc(position, getContext());
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

    void openDialog(Item item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.details_dailog, null);
        dialogBuilder.setView(dialogView);

        // Get references to views in the custom dialog layout
        ImageView dialogImage = dialogView.findViewById(R.id.dialogImage);
        TextView dialogText = dialogView.findViewById(R.id.dialogText);
        Button dialogButton = dialogView.findViewById(R.id.dialogButton);

        String fileKey = item.getItemKey();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("Upload");
        final StorageReference fileRef = storageRef.child(fileKey);

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Load the image using Picasso or Glide
                Picasso.get().load(uri).into(dialogImage);
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
//                            Toast.makeText(getContext(), "Image Downloaded" + localFile.getName(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure( Exception exception) {
                        // Handle the error if the file download fails
//                            Toast.makeText(getContext(), "Image Download Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

//        File tempLocalFile = new File(getContext().getFilesDir(), fileKey);
//        Picasso.get().load(tempLocalFile).into(dialogImage);

        dialogText.setText(item.getName());
        AlertDialog dialog = dialogBuilder.create();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void contactSellerFunc(int position, Context context) {
        String sellerEmail = itemArray.itemList.get(position).getOwnerEmailId();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"+sellerEmail));
        context.startActivity(emailIntent);
    }

    private void removebuttonwork(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirm Removal");
        builder.setMessage("Are you sure you want to remove this item from Bids List?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserBidItemsCurrent.removeItemFromUserCurrentBidItemListDatabase(getItem(position));
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showChangePriceDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //builder.setTitle("Change Price");

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
                    if (newPrice > itemArray.itemList.get(position).getCurrentPrice()) {

                        // Update the item's price and notify the adapter
                        itemArray.itemList.get(position).setCurrentPrice(newPrice,FirebaseAuth.getInstance().getCurrentUser());
                        itemArray.itemList.get(position).setCurrentWinner();
                        itemArray.itemList.get(position).updatePriceToDatabase();
                        UserBidItemsCurrent.addItemToUserCurrentBidItemListDatabase(itemArray.itemList.get(position));
                        notifyDataSetChanged();

                        RetrieveDataFromFirebase.RetrieveDataFromDatabaseStatus = false;
                        RetrieveDataFromFirebase.RetrieveDataFromDatabaseAction();

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

