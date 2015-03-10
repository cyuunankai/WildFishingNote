package com.simple.wildfishingnote.bean;

public class FishCount {

    private String count;
    private String countName;
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getCountName() {
        return countName;
    }
    public void setCountName(String countName) {
        this.countName = countName;
    }
    public String toString(){
        return this.countName;
    }
}
