package com.finalyearproject.daretodonate.Models;

import android.graphics.drawable.Drawable;

public class AppSpinnerModel {

    private Drawable spinnerIcon;
    private String spinnerText;

    public AppSpinnerModel(Drawable spinnerIcon, String spinnerText) {
        this.spinnerIcon = spinnerIcon;
        this.spinnerText = spinnerText;
    }

    public Drawable getSpinnerIcon() {
        return spinnerIcon;
    }

    public void setSpinnerIcon(Drawable spinnerIcon) {
        this.spinnerIcon = spinnerIcon;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public void setSpinnerText(String spinnerText) {
        this.spinnerText = spinnerText;
    }
}
