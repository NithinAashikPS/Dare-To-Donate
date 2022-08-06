package com.finalyearproject.daretodonate.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.finalyearproject.daretodonate.Activities.MainActivity.setFragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.finalyearproject.daretodonate.Adapters.DonationRequestAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Interfaces.ClickOnBottomSheet;
import com.finalyearproject.daretodonate.Models.DonationRequestModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.finalyearproject.daretodonate.Utils.D2DDialogue;
import com.finalyearproject.daretodonate.Utils.D2DToast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DonationFragment extends Fragment {

    private RecyclerView donationRequestRecyclerview;
    private List<DonationRequestModel> donationRequestModelList;
    private DonationRequestAdapter donationRequestAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;

    private JSONObject header;
    private SharedPreferences sharedPreferences;
    private FragmentTransaction transaction;

    private JSONArray data;
    private JSONObject user;
    private JSONObject userData;
    private JSONObject address;

    private JSONObject acceptData;
    private D2DDialogue d2DDialogue;
    private FloatingActionButton dialogueButton;
    private TextView dialogueMessage;

    private ImageView bottomSheetProfileImage;
    private TextView bottomSheetUserName;
    private TextView bottomSheetUserLocation;
    private Button bottomSheetAcceptButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_donation, container, false);

        d2DDialogue = new D2DDialogue();
        header = new JSONObject();
        acceptData = new JSONObject();
        sharedPreferences = requireActivity().getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        donationRequestRecyclerview = root.findViewById(R.id.donation_request_recyclerview);
        donationRequestModelList = new ArrayList<>();
        bottomSheetDialog = new BottomSheetDialog(
                requireActivity(),
                R.style.BottomSheetDialogTheme
        );
        setWhiteNavigationBar(bottomSheetDialog);
        bottomSheetView = LayoutInflater.from(requireContext())
                .inflate(
                        R.layout.bottom_sheet_layout,
                        requireActivity().findViewById(R.id.bottom_sheet_container)
                );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetProfileImage = bottomSheetDialog.findViewById(R.id.profile_image);
        bottomSheetUserName = bottomSheetDialog.findViewById(R.id.user_name);
        bottomSheetUserLocation = bottomSheetDialog.findViewById(R.id.user_location);
        bottomSheetAcceptButton = bottomSheetDialog.findViewById(R.id.accept_button);
        donationRequestAdapter = new DonationRequestAdapter(donationRequestModelList, new ClickOnBottomSheet() {
            @Override
            public void onClick(JSONObject data, String patientId) {
                try {
                    if (data.getString("gender").equals("Male"))
                        bottomSheetProfileImage.setImageResource(R.drawable.ic_user_male);
                    else
                        bottomSheetProfileImage.setImageResource(R.drawable.ic_user_female);
                    bottomSheetUserName.setText(data.getString("name"));
                    address = data.getJSONObject("address");
                    bottomSheetUserLocation.setText(String.format("%s, %s", address.getString("district"), address.getString("state")));
                    bottomSheetAcceptButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomSheetDialog.dismiss();
                            d2DDialogue.makeText(requireActivity(), R.layout.request_successful_dialogue, false);
                            dialogueMessage = (TextView) d2DDialogue.getViewById(R.id.dialogue_message);
                            dialogueButton = (FloatingActionButton) d2DDialogue.getViewById(R.id.close_dialogue);
                            dialogueMessage.setText("Accepting Blood Request");
                            dialogueButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    d2DDialogue.dismiss();
                                    transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    setFragment(new HomeFragment(), R.id.home, transaction, true);
                                }
                            });
                            d2DDialogue.show();
                            try {
                                acceptData.put("patient", patientId);
                                new D2DBackend(requireActivity(), new BackendResponseListener() {
                                    @Override
                                    public void backendResponse(boolean isError, JSONObject response) {
                                        if (!isError) {
                                            d2DDialogue.getViewById(R.id.dialogue_progress).setVisibility(View.GONE);
                                            dialogueMessage.setText("Blood Request Accepted");
                                            dialogueButton.setVisibility(View.VISIBLE);
                                        } else {
                                            d2DDialogue.dismiss();
                                            try {
                                                new D2DToast().makeText(requireActivity(), response.getString("error"), Toast.LENGTH_LONG).show();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).putRequest(getString(R.string.api_acceptRequest), acceptData, header);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                bottomSheetDialog.show();
            }
        });
        donationRequestRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        donationRequestRecyclerview.setAdapter(donationRequestAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new D2DBackend(requireActivity(), new BackendResponseListener() {
            @Override
            public void backendResponse(boolean isError, JSONObject response) {
                try {
                    data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        userData = data.getJSONObject(i);
                        user = userData.getJSONArray("user").getJSONObject(0);
                        donationRequestModelList.add(new DonationRequestModel(
                                userData.getString("_id"),
                                user,
                                userData.getBoolean("gotBlood"),
                                userData.getJSONArray("location"),
                                userData.getJSONArray("receiveFrom")
                        ));
                    }
                    donationRequestAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).getRequest(getString(R.string.api_donationRequest), new JSONObject(), header);


    }

    public static void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }
}