package com.finalyearproject.daretodonate.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.easywaylocation.GetLocationDetail;
import com.example.easywaylocation.LocationData;
import com.finalyearproject.daretodonate.Activities.OnBoardingActivity2;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Interfaces.UserDetailsUpdate;
import com.finalyearproject.daretodonate.Models.UserModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Services.LiveLocationService;
import com.finalyearproject.daretodonate.Singletons.ForegroundStatus;
import com.finalyearproject.daretodonate.Singletons.LiveLocation;
import com.finalyearproject.daretodonate.Singletons.User;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DDialogue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment implements LocationData.AddressCallBack {

    private ImageView profileImage;
    private TextView userName;
    private TextView userLocation;
    private TextView userBlood;
    private TextView donatedCount;
    private TextView requestedCount;
    private Switch userAvailable;
    private Button directRequest;

    private User user;
    private JSONObject header;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private D2DDialogue d2DDialogue;

    private TextView dialogueMessage;
    private FloatingActionButton dialogueButton;
    private Button signOut;

    private Intent liveLocationServiceIntent;
    private ForegroundStatus foregroundStatus;
    private JSONObject data;

    private GetLocationDetail getLocationDetail;
    private LiveLocation liveLocation;
    private String location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = root.findViewById(R.id.profile_image);
        userName = root.findViewById(R.id.user_name);
        userLocation = root.findViewById(R.id.user_location);
        userBlood = root.findViewById(R.id.user_blood);
        donatedCount = root.findViewById(R.id.donated_count);
        requestedCount = root.findViewById(R.id.requested_count);
        userAvailable = root.findViewById(R.id.user_available);
        directRequest = root.findViewById(R.id.direct_request_button);
        signOut = root.findViewById(R.id.sign_out);

        sharedPreferences = requireActivity().getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        foregroundStatus = ForegroundStatus.getInstance();
        liveLocation = LiveLocation.getInstance();

        getLocationDetail = new GetLocationDetail(this, requireContext());
        user = User.getInstance();
        data = new JSONObject();
        header = new JSONObject();
        d2DDialogue = new D2DDialogue();

        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (user.getUserModel().getName() == null) {
            new D2DBackend(requireActivity(), new BackendResponseListener() {
                @Override
                public void backendResponse(boolean isError, JSONObject response) {
                    user.setUserData(response);
                }
            }).getRequest(getResources().getString(R.string.api_user), new JSONObject(), header);
        }
        userAvailable.setChecked(sharedPreferences.getBoolean("available", true));
        user.setUserDetailsUpdate(new UserDetailsUpdate() {
            @Override
            public void newData(UserModel userModel) {
                JSONObject address = userModel.getAddress();

                try {
                    location = String.format("%s, %s", address.getString("district"), address.getString("state"));
                    getLocationDetail.getAddress(userModel.getLocation().getDouble(0), userModel.getLocation().getDouble(1), getResources().getString(R.string.google_maps_api));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userModel.getGender().equals("Male"))
                                profileImage.setImageResource(R.drawable.ic_user_male);
                            else
                                profileImage.setImageResource(R.drawable.ic_user_female);

                            userName.setText(userModel.getName());
                            userLocation.setText(location);
                            userBlood.setText(userModel.getBloodType());
                            donatedCount.setText(String.valueOf(userModel.getDonated().length()));
                            requestedCount.setText(String.valueOf(userModel.getRequested().length()));

                            if (userModel.isGotBlood()) {
                                directRequest.setEnabled(true);
                                directRequest.setText(userModel.getButtonText());
                                directRequest.setBackgroundResource(R.drawable.button_background);
                            } else {
                                directRequest.setEnabled(false);
                                directRequest.setBackgroundResource(R.drawable.button_background_disabled);
                            }

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        directRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.getUserModel().getButtonText().equals("Request")) {
                    d2DDialogue.makeText(requireActivity(), R.layout.request_successful_dialogue, false);
                    dialogueMessage = (TextView) d2DDialogue.getViewById(R.id.dialogue_message);
                    dialogueButton = (FloatingActionButton) d2DDialogue.getViewById(R.id.close_dialogue);
                    dialogueButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d2DDialogue.dismiss();
                        }
                    });
                    d2DDialogue.show();

                    new D2DBackend(requireActivity(), new BackendResponseListener() {
                        @Override
                        public void backendResponse(boolean isError, JSONObject response) {
                            if (!isError) {
                                user.setUserData(response);
                                d2DDialogue.getViewById(R.id.dialogue_image).setVisibility(View.VISIBLE);
                                dialogueMessage.setText("Blood is successfully requested.");
                                dialogueButton.setVisibility(View.VISIBLE);
                                d2DDialogue.getViewById(R.id.dialogue_progress).setVisibility(View.GONE);
                            }
                        }
                    }).postRequest(getResources().getString(R.string.api_bloodBank_directRequest), new JSONObject(), header);
                }

                if (user.getUserModel().getButtonText().equals("Received")) {
                    new D2DBackend(requireActivity(), new BackendResponseListener() {
                        @Override
                        public void backendResponse(boolean isError, JSONObject response) {
                            if (!isError) {
                                user.setUserData(response);
                            }
                        }
                    }).putRequest(getString(R.string.api_bloodReceived), new JSONObject(), header);
                }
            }
        });

        userAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                try {
                    data.put("fcmRegToken", sharedPreferences.getString("fcmRegToken", ""));
                    data.put("available", b);
                    data.put("pincode", liveLocation.getPinCode());
                    header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new D2DBackend(foregroundStatus.getActivity(), new BackendResponseListener() {
                    @Override
                    public void backendResponse(boolean isError, JSONObject response) {
                        if (!isError) {
                            user.setUserData(response);

                            liveLocationServiceIntent = new Intent(foregroundStatus.getActivity(), LiveLocationService.class);
                            if (b)
                                ContextCompat.startForegroundService(foregroundStatus.getActivity(), liveLocationServiceIntent);
                            else
                                foregroundStatus.getActivity().stopService(liveLocationServiceIntent);

                            userAvailable.setChecked(b);
                            editor.putBoolean("available", b);
                            editor.apply();
                        }
                    }
                }).putRequest(getResources().getString(R.string.api_update), data, header);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("x-user-token", "");
                editor.apply();
                requireActivity().startActivity(new Intent(requireActivity(), OnBoardingActivity2.class));
                requireActivity().finish();
            }
        });

    }

    @Override
    public void locationData(LocationData locationData) {
        liveLocation.setPinCode(locationData.getPincode());
    }
}