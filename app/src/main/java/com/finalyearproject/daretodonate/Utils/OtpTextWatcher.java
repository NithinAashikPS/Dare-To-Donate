package com.finalyearproject.daretodonate.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.finalyearproject.daretodonate.Interfaces.OtpListener;

public class OtpTextWatcher implements TextWatcher {

    private EditText etPrev;
    private EditText etNext;
    private OtpListener otpListener;
    private int index;

    public OtpTextWatcher(EditText etPrev, EditText etNext, OtpListener otpListener, int index) {
        this.etPrev = etPrev;
        this.etNext = etNext;
        this.otpListener = otpListener;
        this.index = index;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        otpListener.getOtp(charSequence.toString(), index);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (text.length() == 1)
            etNext.requestFocus();
        else if (text.length() == 0)
            etPrev.requestFocus();
    }

}
