package com.finalyearproject.daretodonate.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;

public class D2DDialogue {

    private Dialog dialog;

    public D2DDialogue makeText(Activity activity, int layout, boolean cancelable) {

        dialog = new Dialog(activity);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.setCancelable(cancelable);

        return this;
    }

    public D2DDialogue show() {
        dialog.show();
        return this;
    }

    public D2DDialogue dismiss() {
        dialog.dismiss();
        return this;
    }

    public D2DDialogue update() {
        dialog.notify();
        return this;
    }

    public View getViewById(int id) {
        return dialog.findViewById(id);
    }
}
