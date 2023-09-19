package com.example.auctionapp;

import android.widget.Toast;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class Item {
    private String name;
    private int pictureResource = 0; // Store the image as a resource ID
    private String description;
    private double currentPrice;
    private long startTime; // End time in milliseconds
    private long remainingTime; // Remaining time in milliseconds

    public Item(String name, String description, double currentPrice, long startTime) {
        this.name = name;
        this.pictureResource = pictureResource;
        this.description = description;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.remainingTime = (startTime + 100000) - System.currentTimeMillis();
        this.pictureResource = 0;
    }

    // Getter and Setter methods for the attributes

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPictureResource() {
        return pictureResource;
    }

    public void setPictureResource(int pictureResource) {
        this.pictureResource = pictureResource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getRemainingTime(){
        return remainingTime+"";
    }

    public void updateRemainingTime(){
//        remainingTime = (startTime + 100000) - System.currentTimeMillis();
        remainingTime = remainingTime - 1;
    }
}
