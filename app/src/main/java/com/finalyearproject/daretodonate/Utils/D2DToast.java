package com.finalyearproject.daretodonate.Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.finalyearproject.daretodonate.R;

public class D2DToast {

    private LayoutInflater inflater;
    private View layout;
    private TextView textView;
    private Toast toast;

    public D2DToast makeText(Activity activity, String text, int duration) {

        inflater = activity.getLayoutInflater();
        layout = inflater.inflate(R.layout.d2d_toast, (ViewGroup) activity.findViewById(R.id.toast));

        textView = layout.findViewById(R.id.message_toast);
        textView.setText(text);

        toast = new Toast(activity.getApplicationContext());
        toast.setDuration(duration);
        toast.setView(layout);
        return this;
    }
    public D2DToast show() {

        toast.show();
        return this;
    }
}
