package com.simple.wildfishingnote.bean;

public class FishWeight {

    private String weight;
    private String weightName;

    
    public String getWeight() {
        return weight;
    }


    public void setWeight(String weight) {
        this.weight = weight;
    }


    public String getWeightName() {
        return weightName;
    }


    public void setWeightName(String weightName) {
        this.weightName = weightName;
    }


    public String toString(){
        return this.weightName;
    }
}
