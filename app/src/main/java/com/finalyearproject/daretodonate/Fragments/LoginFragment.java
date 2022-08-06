package com.finalyearproject.daretodonate.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalyearproject.daretodonate.Activities.MainActivity;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DToast;
import com.finalyearproject.daretodonate.Utils.LoginFormVerification;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private TextView registerNowBtn;
    private TextView forgotPasswordBtn;
    private FrameLayout parentFrameLayout;

    private EditText phone;
    private EditText password;
    private Button loginBtn;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        registerNowBtn = root.findViewById(R.id.register_now_btn);
        forgotPasswordBtn = root.findViewById(R.id.forgot_password_btn);
        parentFrameLayout = requireActivity().findViewById(R.id.main_frame_layout);

        phone = root.findViewById(R.id.phone);
        password = root.findViewById(R.id.password);
        loginBtn = root.findViewById(R.id.login_btn);

        sharedPreferences = requireActivity().getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new RegisterFragment());
            }
        });
        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ResetPasswordFragment());
            }
        });
        phone.addTextChangedListener(new LoginFormVerification(view));
        password.addTextChangedListener(new LoginFormVerification(view));
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = new JSONObject();
                JSONObject header = new JSONObject();
                try {
                    data.put("phone", phone.getText().toString());
                    data.put("password", password.getText().toString());
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
                                requireActivity().startActivity(new Intent(requireActivity(), MainActivity.class));
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
                }).postRequest(getResources().getString(R.string.api_login), data, header);
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(parentFrameLayout.getId(), fragment);
        transaction.commit();
    }
}