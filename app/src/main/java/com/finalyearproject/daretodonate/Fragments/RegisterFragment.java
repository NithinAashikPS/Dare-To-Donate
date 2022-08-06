package com.finalyearproject.daretodonate.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.finalyearproject.daretodonate.Activities.MainActivity;
import com.finalyearproject.daretodonate.Activities.OnBoardingActivity2;
import com.finalyearproject.daretodonate.Activities.SplashActivity;
import com.finalyearproject.daretodonate.Adapters.AppSpinnerAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.AppSpinnerModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Singletons.LiveLocation;
import com.finalyearproject.daretodonate.Singletons.User;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DToast;
import com.finalyearproject.daretodonate.Utils.RegisterFormVerification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterFragment extends Fragment {

    private EditText name;
    private EditText phone;
    private EditText password;
    private EditText dob;
    private Spinner genderSpinner;
    private Spinner bloodSpinner;

    private ArrayList<AppSpinnerModel> appSpinnerModelList;
    private Button registerBtn;
    private LiveLocation liveLocation;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private FrameLayout parentFrameLayout;
    private TextView logInBtn;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);

        name = root.findViewById(R.id.name);
        phone = root.findViewById(R.id.phone);
        password = root.findViewById(R.id.password);
        dob = root.findViewById(R.id.dob);
        genderSpinner = root.findViewById(R.id.gender);
        bloodSpinner = root.findViewById(R.id.blood_type);
        logInBtn = root.findViewById(R.id.log_in_btn);
        registerBtn = root.findViewById(R.id.register_btn);
        parentFrameLayout = requireActivity().findViewById(R.id.main_frame_layout);

        sharedPreferences = requireActivity().getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        liveLocation = LiveLocation.getInstance();
        user = User.getInstance();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSpinner();
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new LoginFragment());
            }
        });

        name.addTextChangedListener(new RegisterFormVerification(view));
        phone.addTextChangedListener(new RegisterFormVerification(view));
        password.addTextChangedListener(new RegisterFormVerification(view));
        dob.addTextChangedListener(new RegisterFormVerification(view));
        genderSpinner.setOnItemSelectedListener(new RegisterFormVerification(view));
        bloodSpinner.setOnItemSelectedListener(new RegisterFormVerification(view));

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = new JSONObject();
                JSONObject header = new JSONObject();
                AppSpinnerModel genderModel = (AppSpinnerModel) genderSpinner.getSelectedItem();
                AppSpinnerModel bloodModel = (AppSpinnerModel) bloodSpinner.getSelectedItem();
                JSONArray location = new JSONArray();
                try {
                    location.put(liveLocation.getLocation().getLatitude());
                    location.put(liveLocation.getLocation().getLongitude());
                    data.put("name", name.getText().toString());
                    header.put("name", name.getText().toString());
                    header.put("phone", phone.getText().toString());
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

                new D2DBackend(requireActivity(), new BackendResponseListener() {
                    @Override
                    public void backendResponse(boolean isError, JSONObject response) {
                        if (!isError) {
                            try {
                                editor.putString("x-user-token", response.getString("x-user-token"));
                                editor.apply();

                                try {
                                    header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new D2DBackend(requireActivity(), new BackendResponseListener() {
                                    @Override
                                    public void backendResponse(boolean isError, JSONObject response) {

                                        user.setUserData(response);
                                        startActivity(new Intent(requireActivity(), MainActivity.class));
                                    }
                                }).getRequest(getResources().getString(R.string.api_user), new JSONObject(), header);

                                requireActivity().finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                new D2DToast().makeText(requireActivity(), response.getString("error"), Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).postRequest(getResources().getString(R.string.api_register), data, header);
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(parentFrameLayout.getId(), fragment);
        transaction.commit();
    }

    private void initSpinner() {
        appSpinnerModelList = new ArrayList<>();
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_gender), "Select Gender"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_male), "Male"));
        appSpinnerModelList.add(new AppSpinnerModel(getResources().getDrawable(R.drawable.ic_female), "Female"));
        genderSpinner.setAdapter(new AppSpinnerAdapter(getContext(), appSpinnerModelList));

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
        bloodSpinner.setAdapter(new AppSpinnerAdapter(getContext(), appSpinnerModelList));
    }
}