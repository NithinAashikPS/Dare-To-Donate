package com.finalyearproject.daretodonate.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.finalyearproject.daretodonate.Activities.MainActivity;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Interfaces.OtpListener;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DToast;
import com.finalyearproject.daretodonate.Utils.OtpTextWatcher;
import com.finalyearproject.daretodonate.Utils.ResetPasswordFormVerification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class ResetPasswordFragment extends Fragment implements OtpListener {

    private Button resetPasswordBtn;
    private LayoutInflater otpLayoutInflater;
    private View otpDialogueView;
    private AlertDialog otpDialogue;

    private EditText otp1, otp2, otp3, otp4;
    private String[] otp;

    private ImageView resetPasswordBackBtn;
    private FrameLayout parentFrameLayout;

    private EditText phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_reset_password, container, false);

        resetPasswordBtn = root.findViewById(R.id.reset_password_btn);
        resetPasswordBackBtn = root.findViewById(R.id.reset_password_back_btn);
        phone = root.findViewById(R.id.phone);
        parentFrameLayout = requireActivity().findViewById(R.id.main_frame_layout);
        otp = new String[4];
        initOtpDialogue();

        return root;
    }

    private void initOtpDialogue() {
        otpLayoutInflater = LayoutInflater.from(getContext());
        otpDialogueView = otpLayoutInflater.inflate(R.layout.otp_dialogue, null);
        otpDialogue = new AlertDialog.Builder(requireContext()).create();
        otpDialogue.setView(otpDialogueView);
        otpDialogue.setCancelable(false);
        otpDialogueView.findViewById(R.id.close_otp_dialogue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otpDialogue.dismiss();
            }
        });
        otp1 = otpDialogueView.findViewById(R.id.otp1);
        otp2 = otpDialogueView.findViewById(R.id.otp2);
        otp3 = otpDialogueView.findViewById(R.id.otp3);
        otp4 = otpDialogueView.findViewById(R.id.otp4);

        otp1.addTextChangedListener(new OtpTextWatcher(otp1, otp2, this, 0));
        otp2.addTextChangedListener(new OtpTextWatcher(otp1, otp3, this, 1));
        otp3.addTextChangedListener(new OtpTextWatcher(otp2, otp4, this, 2));
        otp4.addTextChangedListener(new OtpTextWatcher(otp3, otp4, this, 3));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        phone.addTextChangedListener(new ResetPasswordFormVerification(view));
        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                otpDialogue.show();
                JSONObject data = new JSONObject();
                JSONObject header = new JSONObject();
                try {
                    data.put("phone", phone.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new D2DBackend(requireActivity(), new BackendResponseListener() {
                    @Override
                    public void backendResponse(boolean isError, JSONObject response) {
                        if (!isError) {
                            try {
                                new D2DToast().makeText(requireActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
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
                }).postRequest(getResources().getString(R.string.api_reset_password), data, header);
            }
        });
        resetPasswordBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new LoginFragment());
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(parentFrameLayout.getId(), fragment);
        transaction.commit();
    }

    @Override
    public void getOtp(String otp, int index) {
        this.otp[index] = otp;
        if (!Arrays.asList(this.otp).subList(0, 4).contains(null) && !Arrays.asList(this.otp).subList(0, 4).contains(""))
            Log.i("ASDFG", String.valueOf(this.otp.length));
    }
}