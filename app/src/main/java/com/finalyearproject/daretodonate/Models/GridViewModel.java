package com.finalyearproject.daretodonate.Models;

public class GridViewModel {

    private int image;
    private String name;
    private Class moveToActivity;

    public GridViewModel(int image, String name, Class moveToActivity) {
        this.image = image;
        this.name = name;
        this.moveToActivity = moveToActivity;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getMoveToActivity() {
        return moveToActivity;
    }

    public void setMoveToActivity(Class moveToActivity) {
        this.moveToActivity = moveToActivity;
    }
}
