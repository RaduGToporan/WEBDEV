package com.green.marketplace.model;

public class Model {
    private long modelId;
    private String name;
    private double trainedPrice;
    private double untrainedPrice;
    private String tags;

    public long getModelId() {
        return modelId;
    }

    public void setModelId(long modelId) {
        this.modelId = modelId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
