package com.green.marketplace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//java bean
public class ModelBean implements Serializable {

    private int id;
    private String name;
    private double trainedPrice;
    private double untrainedPrice;
    private String unprocessedtags;
    private List<String> tags;
    private boolean available;
    private String description;


    public ModelBean(int id, String name, double trainedPrice, double untrainedPrice, String unprocessedtags, ArrayList<String> tags, boolean available, String description) {
        this.id = id;
        this.name = name;
        this.trainedPrice = trainedPrice / 100;
        this.untrainedPrice = untrainedPrice / 100;
        this.unprocessedtags = unprocessedtags;
        this.tags = tags;
        this.available = available;
        this.description = description;
    }


    public ModelBean() {
    }

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

    public double getTrainedPrice() {
        return trainedPrice;
    }

    public void setTrainedPrice(double trainedPrice) {
        this.trainedPrice = trainedPrice;
    }

    public double getUntrainedPrice() {
        return untrainedPrice;
    }

    public void setUntrainedPrice(double untrainedPrice) {
        this.untrainedPrice = untrainedPrice;
    }

    public String getUnprocessedtags() {
        return unprocessedtags;
    }

    public void setUnprocessedtags(String unprocessedtags) {
        this.unprocessedtags = unprocessedtags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

