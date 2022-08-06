package com.finalyearproject.daretodonate.Models;

public class BotMessageModel {

    public enum viewType {
        SEND_MESSAGE,
        RECEIVE_MESSAGE
    }

    private viewType viewType;
    private String message;

    public BotMessageModel(viewType viewType, String message) {
        this.viewType = viewType;
        this.message = message;
    }

    public viewType getViewType() {
        return viewType;
    }

    public void setViewType(viewType viewType) {
        this.viewType = viewType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
