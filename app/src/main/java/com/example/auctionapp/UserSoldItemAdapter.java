package com.example.auctionapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserSoldItemAdapter extends RecyclerView.Adapter<UserSoldItemAdapter.ItemViewHolder> {
    private List<Item> itemList;

    public UserSoldItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.won_item_list_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemDescription;
        private TextView itemPrice;
        private TextView sellerName;
        private TextView sellerEmail;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemDescription = itemView.findViewById(R.id.item_description);
            itemPrice = itemView.findViewById(R.id.item_price);
            sellerName = itemView.findViewById(R.id.sellerName);
            sellerEmail = itemView.findViewById(R.id.sellerEmail);

            ConstraintLayout wonCardView = itemView.findViewById(R.id.wonCardView);
            wonCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String SellerEmail = sellerEmail.getText().toString();

                    if(!SellerEmail.equals("")){
                        Toast.makeText(view.getContext(), "No Buyer", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"+SellerEmail));
                    view.getContext().startActivity(emailIntent);

                }
            });
        }

        public void bind(Item item) {
            itemName.setText(item.getName());
            itemDescription.setText(item.getDescription());
            itemPrice.setText(String.valueOf(item.getCurrentPrice()));
            sellerName.setText(item.getCurrentWinnerName());
            sellerEmail.setText(item.getCurrentWinnerEmail());
        }
    }
}
