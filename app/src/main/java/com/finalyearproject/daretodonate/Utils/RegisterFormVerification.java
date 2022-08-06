package com.finalyearproject.daretodonate.Utils;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.finalyearproject.daretodonate.Models.AppSpinnerModel;
import com.finalyearproject.daretodonate.R;

public class RegisterFormVerification implements TextWatcher, AdapterView.OnItemSelectedListener {

    private EditText name;
    private EditText phone;
    private EditText password;
    private EditText dob;
    private Spinner genderSpinner;
    private Spinner bloodSpinner;
    private Button registerBtn;

    public RegisterFormVerification(Activity activity) {
        this.name = activity.findViewById(R.id.name);
        this.phone = activity.findViewById(R.id.phone);
        this.password = activity.findViewById(R.id.password);
        this.dob = activity.findViewById(R.id.dob);
        this.genderSpinner = activity.findViewById(R.id.gender);
        this.bloodSpinner = activity.findViewById(R.id.blood_type);
        registerBtn = activity.findViewById(R.id.register_btn);
    }

    public RegisterFormVerification(View root) {
        this.name = root.findViewById(R.id.name);
        this.phone = root.findViewById(R.id.phone);
        this.password = root.findViewById(R.id.password);
        this.dob = root.findViewById(R.id.dob);
        this.genderSpinner = root.findViewById(R.id.gender);
        this.bloodSpinner = root.findViewById(R.id.blood_type);
        registerBtn = root.findViewById(R.id.register_btn);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        checkForm();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    private void checkForm() {
        if (!name.getText().toString().isEmpty()) {
            if (!phone.getText().toString().isEmpty()) {
                if (!password.getText().toString().isEmpty()) {
                    if (!dob.getText().toString().isEmpty()) {
                        AppSpinnerModel genderModel = (AppSpinnerModel) genderSpinner.getSelectedItem();
                        if (!genderModel.getSpinnerText().equals("Select Gender")) {
                            AppSpinnerModel bloodModel = (AppSpinnerModel) bloodSpinner.getSelectedItem();
                            if (!bloodModel.getSpinnerText().equals("Select Blood")) {
                                registerBtn.setEnabled(true);
                                registerBtn.setBackgroundResource(R.drawable.button_background);
                            } else {
                                registerBtn.setEnabled(false);
                                registerBtn.setBackgroundResource(R.drawable.button_background_disabled);
                            }
                        } else {
                            registerBtn.setEnabled(false);
                            registerBtn.setBackgroundResource(R.drawable.button_background_disabled);
                        }
                    } else {
                        registerBtn.setEnabled(false);
                        registerBtn.setBackgroundResource(R.drawable.button_background_disabled);
                    }
                } else {
                    registerBtn.setEnabled(false);
                    registerBtn.setBackgroundResource(R.drawable.button_background_disabled);
                }
            } else {
                registerBtn.setEnabled(false);
                registerBtn.setBackgroundResource(R.drawable.button_background_disabled);
            }
        } else {
            registerBtn.setEnabled(false);
            registerBtn.setBackgroundResource(R.drawable.button_background_disabled);
        }
    }

}
