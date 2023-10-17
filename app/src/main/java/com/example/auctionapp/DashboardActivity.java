package com.example.auctionapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Retrieve User Info and User List
        UserArray.UpdateUserListFromDatabase();
        UserArray.getCurrentUser();
//        UserArray.AddToDatabaseWinSoldItems();

        // Set the initial fragment
        replaceFragment(new HomeFragment());

        // Set navigation item selected listener
        binding.navigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.chat) {
                replaceFragment(new ChatFragment());
                return true;
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
                return true;
            } else if (itemId == R.id.search) {
                replaceFragment(new SearchFragment());
                return true;
            }
            return false;
        });
    }

    // Method to replace the current fragment
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}
