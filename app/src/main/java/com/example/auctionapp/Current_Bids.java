package com.example.auctionapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Current_Bids extends Fragment {
    public static UserBidItemAdapter adapter;
    public static ListView userCurrentBidItemRecyclerView;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_search, container, false);


        UserBidItemsCurrent.RetrieveUserCurrentBidItemFromDatabase();
        adapter = new UserBidItemAdapter(getActivity(),UserBidItemsCurrent.UserCurrentBidItemList);
        userCurrentBidItemRecyclerView = rootView.findViewById(R.id.userCurrentBidItemRecyclerView);
        userCurrentBidItemRecyclerView.setAdapter(adapter);

        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UserBidItemsCurrent.RetrieveUserCurrentBidItemFromDatabase();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        startRefresh();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopRefresh();
    }

    void startRefresh() {
//        Toast.makeText(getActivity(),"cholse", Toast.LENGTH_SHORT).show();
        BidItemRefreshClass.refresh(rootView, getActivity());
    }

    void stopRefresh() {
        BidItemRefreshClass.stopRefresh();
    }
}

class BidItemRefreshClass {
    private static boolean refreshStatus = false;
    private static Thread backgroundThread;

    public static void refresh(View rootView, Activity activity) {
        itemArray.ItemUpdateTimeRunnable();
        if (!refreshStatus) {
            refreshStatus = true;
            backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (refreshStatus) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UserBidItemsCurrent.RetrieveUserCurrentBidItemFromDatabase();
                                if(UserBidItemsCurrent.UserCurrentBidItemList.size() == 0)return;

                                int firstVisiblePosition = Current_Bids.userCurrentBidItemRecyclerView.getFirstVisiblePosition();
                                int lastVisiblePosition = Current_Bids.userCurrentBidItemRecyclerView.getLastVisiblePosition();

                                // Iterate through visible items and update their data
                                for (int i = firstVisiblePosition; i <= lastVisiblePosition; i++) {

                                    //if item removed realtime
//                                    if(i >= UserBidItemsCurrent.UserCurrentBidItemList.size() || UserBidItemsCurrent.UserCurrentBidItemList.size() == 0){
//                                        break;
//                                    }
                                    Item item = Current_Bids.adapter.getItem(i);
//                                    item.RetrieveItemPriceFromDatabase();

//                                    Item refreshedItem = UserBidItemsCurrent.RefreshUserCurrentBidItemInfoFromItemListDatabase(item);
//                                    UserBidItemsCurrent.UserCurrentBidItemList.set(i,refreshedItem);
                                    UserBidItemsCurrent.updateItemToCurrentBidItemListDatabase(item);
                                }

                                //last Item flicking error
                                Item item = Current_Bids.adapter.getItem(UserBidItemsCurrent.UserCurrentBidItemList.size()-1);
                                UserBidItemsCurrent.updateItemToCurrentBidItemListDatabase(item);

                                // Notify the adapter that the data has changed for visible items
                                Current_Bids.adapter.notifyDataSetChanged();
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            backgroundThread.start();
        }
    }

    public static void stopRefresh() {
        refreshStatus = false;
        if (backgroundThread != null) {
            backgroundThread.interrupt();
        }
    }
}
