package com.finalyearproject.daretodonate.Models;

import android.graphics.drawable.Drawable;

public class OnBoardingScreenModel {

    private Drawable onBoardingImage;
    private String onBoardingTitle;
    private String onBoardingDescription;

    public OnBoardingScreenModel(Drawable onBoardingImage, String onBoardingTitle, String onBoardingDescription) {
        this.onBoardingImage = onBoardingImage;
        this.onBoardingTitle = onBoardingTitle;
        this.onBoardingDescription = onBoardingDescription;
    }

    public Drawable getOnBoardingImage() {
        return onBoardingImage;
    }

    public void setOnBoardingImage(Drawable onBoardingImage) {
        this.onBoardingImage = onBoardingImage;
    }

    public String getOnBoardingTitle() {
        return onBoardingTitle;
    }

    public void setOnBoardingTitle(String onBoardingTitle) {
        this.onBoardingTitle = onBoardingTitle;
    }

    public String getOnBoardingDescription() {
        return onBoardingDescription;
    }

    public void setOnBoardingDescription(String onBoardingDescription) {
        this.onBoardingDescription = onBoardingDescription;
    }
}
