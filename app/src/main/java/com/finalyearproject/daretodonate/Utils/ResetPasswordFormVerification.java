package com.finalyearproject.daretodonate.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.finalyearproject.daretodonate.R;

public class ResetPasswordFormVerification implements TextWatcher {

    private EditText phone;
    private Button resetPasswordBtn;

    public ResetPasswordFormVerification(View root) {
        this.phone = root.findViewById(R.id.phone);
        this.resetPasswordBtn = root.findViewById(R.id.reset_password_btn);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        checkForm();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void checkForm() {
        if (!phone.getText().toString().isEmpty()) {
            resetPasswordBtn.setEnabled(true);
            resetPasswordBtn.setBackgroundResource(R.drawable.button_background);
        } else {
            resetPasswordBtn.setEnabled(false);
            resetPasswordBtn.setBackgroundResource(R.drawable.button_background_disabled);
        }
    }

}
