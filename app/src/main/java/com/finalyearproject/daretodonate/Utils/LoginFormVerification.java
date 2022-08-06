package com.finalyearproject.daretodonate.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.finalyearproject.daretodonate.R;

public class LoginFormVerification implements TextWatcher {

    private EditText phone;
    private EditText password;
    private Button loginBtn;

    public LoginFormVerification(View root) {
        this.phone = root.findViewById(R.id.phone);
        this.password = root.findViewById(R.id.password);
        this.loginBtn = root.findViewById(R.id.login_btn);
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
            if (!password.getText().toString().isEmpty()) {
                loginBtn.setEnabled(true);
                loginBtn.setBackgroundResource(R.drawable.button_background);
            } else {
                loginBtn.setEnabled(false);
                loginBtn.setBackgroundResource(R.drawable.button_background_disabled);
            }
        } else {
            loginBtn.setEnabled(false);
            loginBtn.setBackgroundResource(R.drawable.button_background_disabled);
        }
    }
}
