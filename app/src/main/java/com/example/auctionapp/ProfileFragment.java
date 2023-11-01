//package com.example.auctionapp;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.telecom.Call;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import com.example.auctionapp.R;
//import com.example.auctionapp.databinding.FragmentProfileBinding;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.FileDownloadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
//
//import java.io.File;
//
//public class ProfileFragment extends Fragment {
//
//    private TextView profileNameTextView;
//    private TextView profileEmailTextView;
//
//    private FirebaseAuth auth;
//    private Button logoutButton,EmailButton,CallButton,AboutButton,SoldItemButton;
//
//    private ImageView profileImageView;
//    private Button selectImageButton;
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private Uri imageUri;
//    String fileKey ;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);
//        profileNameTextView = view.findViewById(R.id.profileName);
//        profileEmailTextView = view.findViewById(R.id.profileEmail);
//        logoutButton = view.findViewById(R.id.logoutButton);
//        EmailButton = view.findViewById(R.id.EmailButton);
//        AboutButton = view.findViewById(R.id.Aboutbutton);
//        CallButton = view.findViewById(R.id.CallButton);
//        SoldItemButton = view.findViewById(R.id.SoldItemButton);
//
//        UserArray.RetrieveFromDatabaseSoldItemOfUser();
//
//        auth = FirebaseAuth.getInstance();
//        String userName = auth.getCurrentUser().getDisplayName();
//        String userEmail = auth.getCurrentUser().getEmail();
//
//        profileNameTextView.setText(userName);
//        profileEmailTextView.setText(userEmail);
//
//
//        profileImageView = view.findViewById(R.id.profileImageView);
//        selectImageButton = view.findViewById(R.id.selectImageButton);
//
//
//         fileKey = auth.getCurrentUser().getUid() + ".jpg" ;
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference("profile_images");
//        final StorageReference fileRef = storageRef.child(fileKey);
//
//        // Check if the local file exists
//        File localFile = new File(getContext().getFilesDir(), fileKey);
//
//        if (localFile.exists()) {
//            // If the local file exists, load it using Picasso or Glide
//            Picasso.get().load(localFile).into(profileImageView);
//        } else {
//            // If the local file does not exist, download from the downloadUri
//            downloadImageToLocal(localFile);
//
//            // Set an empty image while the download is in progress
//            profileImageView.setImageResource(R.drawable.pb);
//        }
//
//        selectImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openImageChooser();
//            }
//        });
//
//        return view;
//    }
//
//    private void downloadImageToLocal(final File localFile) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference("profile_images");
//        final StorageReference fileRef = storageRef.child(fileKey);
//
//        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Load the image using Picasso or Glide
//                Picasso.get().load(uri).into(profileImageView);
//
//                // Download the image to local storage for future use
//                fileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                        // File downloaded successfully to local storage
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                // Handle the error if the file download fails
//                Toast.makeText(getActivity().getApplicationContext(), "ImageFailed To Load", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void openImageChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            imageUri = data.getData();
//            profileImageView.setImageURI(imageUri);
//            uploadImageToFirebase();
//        }
//    }
//
//
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showLogoutPrompt();
//            }
//        });
//
//        CallButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create an intent with the ACTION_DIAL action and the "tel:" scheme
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//
//                // Set the phone number to dial
//                 callIntent.setData(Uri.parse("tel:1234567890"));
//                 startActivity(callIntent);
//            }
//        });
//
//        EmailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create an intent with the ACTION_DIAL action and the "tel:" scheme
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//
//                // Set the phone number to dial
//                emailIntent.setData(Uri.parse("mailto:"));
//                startActivity(emailIntent);
//            }
//        });
//
//        AboutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),about_us_class.class);
//                startActivity(intent);
//            }
//        });
//
//        SoldItemButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),soldItemClass.class);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void uploadImageToFirebase() {
//
//         fileKey = auth.getCurrentUser().getUid() + ".jpg" ;
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference("profile_images");
//        final StorageReference fileRef = storageRef.child(fileKey);
//        File localFile = new File(getContext().getFilesDir(), fileKey);
//
//        if (localFile.exists()) {
//            // If the local file exists, delete it
//            localFile.delete();
//        }
//
//        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getActivity().getApplicationContext(), "Ager image delete", Toast.LENGTH_SHORT).show();
//                saveData();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(getActivity().getApplicationContext(), "Ager image  delete hoy na", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//    }
//
//    private void saveData() {
//        if (imageUri != null) {
//
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
//                    .child("profile_images") // You can customize this storage path
//                    .child(fileKey);
//
//            storageReference.putFile(imageUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            // Handle image upload failure
//                        }
//                    });
//        }
//    }
//
//    // Show a prompt before logging out
//    private void showLogoutPrompt() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//        builder.setTitle("Logout")
//                .setMessage("Are you sure you want to logout?")
//                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Perform logout
//                        auth.signOut();
//                        // Navigate to login activity or any other action
//                         startActivity(new Intent(getActivity(), loginActivity.class));
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
//}

package com.example.auctionapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
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
import android.widget.Toast;

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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ProfileFragment extends Fragment {

    private TextView profileNameTextView;
    private TextView profileEmailTextView;
    private DatabaseReference databaseReference;
    //    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ProfileImage");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfileImage");
    private FirebaseAuth auth;
    private Button logoutButton,EmailButton,CallButton,AboutButton,SoldItemButton;

    private ImageView profileImageView;
    private Button selectImageButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    String fileKey ;

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
        SoldItemButton = view.findViewById(R.id.SoldItemButton);

        UserArray.RetrieveFromDatabaseSoldItemOfUser();

        auth = FirebaseAuth.getInstance();
        String userName = auth.getCurrentUser().getDisplayName();
        String userEmail = auth.getCurrentUser().getEmail();

        profileNameTextView.setText(userName);
        profileEmailTextView.setText(userEmail);


        profileImageView = view.findViewById(R.id.profileImageView);
        selectImageButton = view.findViewById(R.id.selectImageButton);


        fileKey = auth.getCurrentUser().getUid() + ".jpg" ;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("profile_images");
        final StorageReference fileRef = storageRef.child(fileKey);

        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "ImageFailed To Load", Toast.LENGTH_SHORT).show();
            }
        });

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
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:faiyaz.res@gmail.com"));

                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    // Handle the case where no email client is available on the device
                    Log.e("email error","no email client found");
                }
            }
        });


        AboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),about_us_class.class);
                startActivity(intent);
            }
        });

        SoldItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),soldItemClass.class);
                startActivity(intent);
            }
        });
    }

    private void uploadImageToFirebase() {

        fileKey = auth.getCurrentUser().getUid() + ".jpg" ;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("profile_images");
        final StorageReference fileRef = storageRef.child(fileKey);

        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getActivity().getApplicationContext(), "Ager image delete", Toast.LENGTH_SHORT).show();
                saveData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
//                Toast.makeText(getActivity().getApplicationContext(), "Ager image  delete hoy na", Toast.LENGTH_SHORT).show();
                saveData();
            }
        });



    }

    private void saveData() {
        if (imageUri != null) {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profile_images") // You can customize this storage path
                    .child(fileKey);

            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
