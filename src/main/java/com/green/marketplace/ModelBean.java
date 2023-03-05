package com.green.marketplace;

import java.io.Serializable;
import java.util.ArrayList;

//java bean
public class ModelBean implements Serializable {

    private int id;
    private String name;
    private int trainedPrice;
    private int untrainedPrice;
    private String unprocessedtags;
    private ArrayList<String> tags;
    private boolean available;


    public ModelBean(int id, String name, int trainedPrice, int untrainedPrice, String unprocessedtags, ArrayList<String> tags, boolean available) {
        this.id = id;
        this.name = name;
        this.trainedPrice = trainedPrice;
        this.untrainedPrice = untrainedPrice;
        this.unprocessedtags = unprocessedtags;
        this.tags = tags;
        this.available = available;
    }
    

    public ModelBean(){}
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTrainedPrice() {
        return trainedPrice;
    }
    public void setTrainedPrice(int trainedPrice) {
        this.trainedPrice = trainedPrice;
    }
    public int getUntrainedPrice() {
        return untrainedPrice;
    }
    public void setUntrainedPrice(int untrainedPrice) {
        this.untrainedPrice = untrainedPrice;
    }
    public String getUnprocessedtags() {
        return unprocessedtags;
    }
    public void setUnprocessedtags(String unprocessedtags) {
        this.unprocessedtags = unprocessedtags;
    }
    public ArrayList<String> getTags() {
        return tags;
    }
    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }

}

