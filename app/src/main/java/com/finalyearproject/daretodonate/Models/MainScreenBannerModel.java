package com.finalyearproject.daretodonate.Models;

public class MainScreenBannerModel {

    private String imageUrl;
    private String youtubeUrl;

    public MainScreenBannerModel(String imageUrl, String youtubeUrl) {
        this.imageUrl = imageUrl;
        this.youtubeUrl = youtubeUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }
}
