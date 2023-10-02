package com.example.auctionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileFragment extends Fragment {

    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private Button logoutButton,EmailButton,CallButton,AboutButton;

    private ImageView profileImageView;
    private Button selectImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileNameTextView = view.findViewById(R.id.profileName);
        profileEmailTextView = view.findViewById(R.id.profileEmail);
        logoutButton = view.findViewById(R.id.logoutButton);
        EmailButton = view.findViewById(R.id.EmailButton);
        AboutButton = view.findViewById(R.id.Aboutbutton);
        CallButton = view.findViewById(R.id.CallButton);

        auth = FirebaseAuth.getInstance();
        String userName = auth.getCurrentUser().getDisplayName();
        String userEmail = auth.getCurrentUser().getEmail();

        profileNameTextView.setText(userName);
        profileEmailTextView.setText(userEmail);

        profileImageView = view.findViewById(R.id.profileImageView);
        selectImageButton = view.findViewById(R.id.selectImageButton);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });


        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
            uploadImageToFirebase();
        }
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutPrompt();
            }
        });

        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent with the ACTION_DIAL action and the "tel:" scheme
                Intent callIntent = new Intent(Intent.ACTION_DIAL);

                // Set the phone number to dial
                 callIntent.setData(Uri.parse("tel:1234567890"));
                 startActivity(callIntent);
            }
        });

        EmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent with the ACTION_DIAL action and the "tel:" scheme
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

                // Set the phone number to dial
                emailIntent.setData(Uri.parse("mailto:"));
                startActivity(emailIntent);
            }
        });

        EmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent with the ACTION_DIAL action and the "tel:" scheme
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

                // Set the phone number to dial
                emailIntent.setData(Uri.parse("mailto:"));
                startActivity(emailIntent);
            }
        });

        AboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),about_us_class.class);
                startActivity(intent);
            }
        });
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profile_images") // You can customize this storage path
                    .child(auth.getCurrentUser().getUid() + ".jpg");

            storageReference.putFile(imageUri)
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

    // Show a prompt before logging out
    private void showLogoutPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform logout
                        auth.signOut();
                        // Navigate to login activity or any other action
                         startActivity(new Intent(getActivity(), loginActivity.class));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}





