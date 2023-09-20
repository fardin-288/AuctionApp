package com.example.auctionapp;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class ItemAdapter extends ArrayAdapter<Item> {

    private Activity activity;
//    public static List<Item> itemList;


    public ItemAdapter(Activity activity, List<Item> itemList) {
        super(activity, 0, itemList);
        this.activity = activity;
//        this.itemList = itemList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(R.layout.item_list_item, parent, false);
        }

//        Thread backgroundThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    updateAllitem();
//                    try {
//                        Thread.sleep(1000); // Sleep for 1 second (adjust as needed)
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        backgroundThread.start();

        Item item = itemArray.itemList.get(position);

//        ImageView itemImageView = convertView.findViewById(R.id.itemImageView);
        TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
        TextView itemDescriptionTextView = convertView.findViewById(R.id.itemDescriptionTextView);
        TextView itemPriceTextView = convertView.findViewById(R.id.itemPriceTextView);
        TextView itemTimeRemaining = convertView.findViewById(R.id.itemTimeRemaining);
        TextView itemcurrentWinnerName = convertView.findViewById(R.id.itemcurrentWinnerName);

        // Set the item's attributes in the views
//        itemImageView.setImageResource(item.getPictureResource());
        itemNameTextView.setText(item.getName());
        itemDescriptionTextView.setText(item.getDescription());
        itemPriceTextView.setText(String.format(Locale.US, "Tk%.2f", item.getCurrentPrice()));
        itemTimeRemaining.setText(String.format(Locale.US,"time %s", item.getRemainingTime() ));
        itemcurrentWinnerName.setText(String.format(Locale.US,"Highest Bidder : %s", item.getCurrentWinnerName()));

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

        // Adding Image
//        Button buttonUploadPicture = convertView.findViewById(R.id.selectImageButton);
//        buttonUploadPicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Open an image picker to select an image
//                openImagePicker(position);
//            }
//        });

//        handler.post(updateTimeRunnable);

        return convertView;
    }

//    public static final Handler handler = new Handler();
//
    public static void updateAllitem(){
        for(Item a: itemArray.itemList){
            a.updateRemainingTime();
        }
    }
//
//    static Runnable updateTimeRunnable = new Runnable() {
//        @Override
//        public void run() {
//            // Update the time here
//            updateAllitem();
//            // Schedule the runnable to run again after a delay (e.g., 1 second)
//            handler.postDelayed(this, 1000);
//        }
//    };

//    public static void main(String[] args) {
//        handler.post(updateTimeRunnable);
//    }

    private void removebuttonwork(final int position){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(getItem(position).getOwnerid() == user ){
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
                        getItem(position).setCurrentPrice(newPrice);
                        getItem(position).setCurrentWinner();
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

    // Function to open an image picker
    private void openImagePicker(final int position) {
        // Use an image picker library or Android's built-in image picker
        // Here, we're using Android's built-in image picker for simplicity
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, position);
    }

    // Handle image selection result
    public void handleImageSelectionResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode >= 0 && requestCode < itemArray.itemList.size()) {
                Uri selectedImageUri = data.getData();
                String imagePath = getPathFromUri(selectedImageUri);

                if (imagePath != null) {
                    // Set the selected image resource ID in the item and update the view
                    int imageResourceId = Integer.parseInt(imagePath); // Convert the imagePath to an integer
                    getItem(requestCode).setPictureResource(imageResourceId);
                    notifyDataSetChanged();
                }
            }
        }
    }

    // Utility function to get the file path from a URI
    private String getPathFromUri(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(columnIndex);
            cursor.close();
            return imagePath;
        }
        return null;
    }

}

