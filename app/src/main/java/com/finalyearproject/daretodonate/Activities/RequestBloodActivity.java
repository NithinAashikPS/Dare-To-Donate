package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.finalyearproject.daretodonate.Adapters.AppSpinnerAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.AppSpinnerModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Singletons.LiveLocation;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DDialogue;
import com.finalyearproject.daretodonate.Utils.RegisterFormVerification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class RequestBloodActivity extends AppCompatActivity {


    private EditText name;
    private EditText phone;
    private EditText password;
    private EditText dob;
    private Spinner genderSpinner;
    private Spinner bloodSpinner;

    private Button requestBloodBtn;
    private LiveLocation liveLocation;
    private SharedPreferences sharedPreferences;

    private ArrayList<AppSpinnerModel> appSpinnerModelList;
    private D2DDialogue d2DDialogue;
    private TextView dialogueMessage;
    private FloatingActionButton dialogueButton;
    private JSONObject header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        dob = findViewById(R.id.dob);
        genderSpinner = findViewById(R.id.gender);
        bloodSpinner = findViewById(R.id.blood_type);
        requestBloodBtn = findViewById(R.id.register_btn);
        liveLocation = LiveLocation.getInstance();
        d2DDialogue = new D2DDialogue();
        header = new JSONObject();
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);

        initSpinner();

        name.addTextChangedListener(new RegisterFormVerification(this));
        phone.addTextChangedListener(new RegisterFormVerification(this));
        password.addTextChangedListener(new RegisterFormVerification(this));
        dob.addTextChangedListener(new RegisterFormVerification(this));
        genderSpinner.setOnItemSelectedListener(new RegisterFormVerification(this));
        bloodSpinner.setOnItemSelectedListener(new RegisterFormVerification(this));

        password.setText(String.valueOf(generatePassword(8)));
        password.setEnabled(false);

        requestBloodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestBloodBtn.setEnabled(false);
                requestBloodBtn.setBackgroundResource(R.drawable.button_background_disabled);
                d2DDialogue.makeText(RequestBloodActivity.this, R.layout.request_successful_dialogue, false);
                dialogueMessage = (TextView) d2DDialogue.getViewById(R.id.dialogue_message);
                dialogueButton = (FloatingActionButton) d2DDialogue.getViewById(R.id.close_dialogue);
                dialogueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d2DDialogue.dismiss();
                        RequestBloodActivity.this.onBackPressed();
                    }
                });
                d2DDialogue.show();

                JSONObject data = new JSONObject();
                AppSpinnerModel genderModel = (AppSpinnerModel) genderSpinner.getSelectedItem();
                AppSpinnerModel bloodModel = (AppSpinnerModel) bloodSpinner.getSelectedItem();
                JSONArray location = new JSONArray();
                try {
                    location.put(liveLocation.getLocation().getLongitude());
                    location.put(liveLocation.getLocation().getLatitude());
                    data.put("name", name.getText().toString());
                    data.put("phone", phone.getText().toString());
                    data.put("password", password.getText().toString());
                    data.put("dob", dob.getText().toString());
                    data.put("gender", genderModel.getSpinnerText());
                    data.put("bloodType", bloodModel.getSpinnerText());
                    data.put("location", location);
                    data.put("pincode", liveLocation.getPinCode());
                    data.put("fcmRegToken", sharedPreferences.getString("fcmRegToken", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new D2DBackend(RequestBloodActivity.this, new BackendResponseListener() {
                    @Override
                    public void backendResponse(boolean isError, JSONObject response) {
                        if (!isError) {
                            try {
                                header.put("x-user-token", response.getString("x-user-token"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new D2DBackend(RequestBloodActivity.this, new BackendResponseListener() {
                                @Override
                                public void backendResponse(boolean isError, JSONObject response) {
                                    if (!isError) {
                                        d2DDialogue.getViewById(R.id.dialogue_image).setVisibility(View.VISIBLE);
                                        dialogueMessage.setText("Blood is successfully requested.");
                                        dialogueButton.setVisibility(View.VISIBLE);
                                        d2DDialogue.getViewById(R.id.dialogue_progress).setVisibility(View.GONE);
                                    }
                                }
                            }).postRequest(getString(R.string.api_bloodBank_directRequest), new JSONObject(), header);
                        }
                    }
                }).postRequest(getResources().getString(R.string.api_register), data, new JSONObject());
            }
        });

    }

    private void initSpinner() {
        appSpinnerModelList = new ArrayList<>();
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_gender), "Select Gender"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_male), "Male"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_female), "Female"));
        genderSpinner.setAdapter(new AppSpinnerAdapter(this, appSpinnerModelList));

        appSpinnerModelList = new ArrayList<>();
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood), "Select Blood"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_a_p), "A+ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_a_n), "A-ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_b_p), "B+ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_b_n), "B-ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_o_p), "O+ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_o_n), "O-ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_ab_p), "AB+ve"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_blood_ab_n), "AB-ve"));
        bloodSpinner.setAdapter(new AppSpinnerAdapter(this, appSpinnerModelList));
    }

    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }
}