package com.finalyearproject.daretodonate.Fragments;

import static android.content.Context.MODE_PRIVATE;

import static com.finalyearproject.daretodonate.Adapters.DonationRequestAdapter.getBloodGroupImage;
import static com.finalyearproject.daretodonate.Fragments.DonationFragment.setWhiteNavigationBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.finalyearproject.daretodonate.Activities.BloodBankActivity;
import com.finalyearproject.daretodonate.Activities.BloodCampActivity;
import com.finalyearproject.daretodonate.Activities.ChatActivity;
import com.finalyearproject.daretodonate.Activities.MyDonationActivity;
import com.finalyearproject.daretodonate.Activities.NotificationsActivity;
import com.finalyearproject.daretodonate.Activities.RequestBloodActivity;
import com.finalyearproject.daretodonate.Activities.SplashActivity;
import com.finalyearproject.daretodonate.Adapters.GridViewAdapter;
import com.finalyearproject.daretodonate.Adapters.MainScreenBannerAdapter;
import com.finalyearproject.daretodonate.Interfaces.BackendResponseListener;
import com.finalyearproject.daretodonate.Models.GridViewModel;
import com.finalyearproject.daretodonate.Models.MainScreenBannerModel;
import com.finalyearproject.daretodonate.R;
import com.finalyearproject.daretodonate.Utils.D2DBackend;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private List<MainScreenBannerModel> mainScreenBannerModelList;
    private MainScreenBannerAdapter mainScreenBannerAdapter;
    private ViewPager mainViewPager;
    private TabLayout mainViewPagerIndicator;

    private GridView mainGridView;
    private ArrayList<GridViewModel> gridViewModelArrayList;
    private GridViewAdapter gridViewAdapter;

    private JSONObject header;
    private SharedPreferences sharedPreferences;
    private View acceptedRequestView;
    private JSONObject data;
    private JSONObject user;
    private JSONObject address;

    private TextView name;
    private TextView location;
    private ImageView gender;
    private ImageView bloodType;

    private BottomSheetDialog bottomSheetDialog;
    private View bottomSheetView;
    private Uri uri;
    private Intent intent;

    private ImageView bottomSheetProfileImage;
    private TextView bottomSheetUserName;
    private TextView bottomSheetUserLocation;
    private Button bottomSheetCallButton;
    private Button bottomSheetNavigateButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mainViewPager = root.findViewById(R.id.main_view_pager);
        mainViewPagerIndicator = root.findViewById(R.id.main_view_pager_indicator);
        acceptedRequestView = root.findViewById(R.id.accepted_request);

        name = root.findViewById(R.id.name);
        location = root.findViewById(R.id.location);
        bloodType = root.findViewById(R.id.blood_type);
        gender = root.findViewById(R.id.gender);

        mainGridView = root.findViewById(R.id.main_grid_view);
        gridViewModelArrayList = new ArrayList<>();

        header = new JSONObject();
        sharedPreferences = requireActivity().getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        try {
            header.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bottomSheetDialog = new BottomSheetDialog(
                requireActivity(),
                R.style.BottomSheetDialogTheme
        );
        setWhiteNavigationBar(bottomSheetDialog);
        bottomSheetView = LayoutInflater.from(requireContext())
                .inflate(
                        R.layout.accepted_request_layout,
                        requireActivity().findViewById(R.id.bottom_sheet_container)
                );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetProfileImage = bottomSheetDialog.findViewById(R.id.profile_image);
        bottomSheetUserName = bottomSheetDialog.findViewById(R.id.user_name);
        bottomSheetUserLocation = bottomSheetDialog.findViewById(R.id.user_location);
        bottomSheetCallButton = bottomSheetDialog.findViewById(R.id.call_button);
        bottomSheetNavigateButton = bottomSheetDialog.findViewById(R.id.navigate_button);

        gridViewModelArrayList.add(new GridViewModel(R.drawable.ic_assistant, "Assistant", ChatActivity.class));
        gridViewModelArrayList.add(new GridViewModel(R.drawable.ic_donates, "Donates", MyDonationActivity.class));
        gridViewModelArrayList.add(new GridViewModel(R.drawable.ic_campaign, "Camps", BloodCampActivity.class));
        gridViewModelArrayList.add(new GridViewModel(R.drawable.ic_hospital, "Blood Banks", BloodBankActivity.class));
        gridViewModelArrayList.add(new GridViewModel(R.drawable.ic_notification, "Notifications", NotificationsActivity.class));
        gridViewModelArrayList.add(new GridViewModel(R.drawable.ic_request, "Request", RequestBloodActivity.class));

        mainScreenBannerModelList = new ArrayList<>();
        mainScreenBannerModelList.add(new MainScreenBannerModel("https://i.ytimg.com/vi/-1O4jlEhsxU/maxresdefault.jpg", "https://youtu.be/-1O4jlEhsxU"));
        mainScreenBannerModelList.add(new MainScreenBannerModel("https://i.ytimg.com/vi/oXhsvlUx9fc/maxresdefault.jpg", "https://youtu.be/oXhsvlUx9fc"));
        mainScreenBannerModelList.add(new MainScreenBannerModel("https://i.ytimg.com/vi/ezafVzfJw60/maxresdefault.jpg", "https://youtu.be/ezafVzfJw60"));
        mainScreenBannerModelList.add(new MainScreenBannerModel("https://i.ytimg.com/vi/XveDeDPP3TY/maxresdefault.jpg", "https://youtu.be/XveDeDPP3TY"));

        mainScreenBannerAdapter = new MainScreenBannerAdapter(requireContext(), mainScreenBannerModelList);
        mainViewPager.setAdapter(mainScreenBannerAdapter);
        mainViewPagerIndicator.setupWithViewPager(mainViewPager);

        gridViewAdapter = new GridViewAdapter(requireContext(), gridViewModelArrayList);
        mainGridView.setAdapter(gridViewAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new D2DBackend(requireActivity(), new BackendResponseListener() {
            @Override
            public void backendResponse(boolean isError, JSONObject response) {
                try {
                    data = response.getJSONArray("data").getJSONObject(0);
                    user = data.getJSONArray("user").getJSONObject(0);
                    address = user.getJSONObject("address");
                    if (data.length() != 0) {
                        acceptedRequestView.setVisibility(View.VISIBLE);
                        name.setText(user.getString("name"));
                        bottomSheetUserName.setText(user.getString("name"));
                        location.setText(String.format("%s, %s", address.getString("district"), address.getString("state")));
                        bottomSheetUserLocation.setText(String.format("%s, %s", address.getString("district"), address.getString("state")));
                        if (user.getString("gender").equals("Male")) {
                            gender.setImageResource(R.drawable.ic_user_male);
                            bottomSheetProfileImage.setImageResource(R.drawable.ic_user_male);
                        }
                        else {
                            gender.setImageResource(R.drawable.ic_user_female);
                            bottomSheetProfileImage.setImageResource(R.drawable.ic_user_female);
                        }
                        bloodType.setImageResource(getBloodGroupImage(user.getString("bloodType")));
                        Log.i("jhgjhgjh", String.valueOf(user));
                        uri = Uri.parse(String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%s,%s", user.getJSONArray("location").get(0), user.getJSONArray("location").get(1)));

                        bottomSheetCallButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    requireActivity().startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getString("phone"))));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        bottomSheetNavigateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    intent = new Intent(Intent.ACTION_VIEW, uri);
                                    intent.setPackage("com.google.android.apps.maps");
                                    requireActivity().startActivity(intent);
                                } catch (Exception e) {
                                    intent = new Intent(Intent.ACTION_VIEW, uri);
                                    requireActivity().startActivity(intent);
                                }
                            }
                        });
                    } else {
                        acceptedRequestView.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).getRequest(getString(R.string.api_acceptedRequests), new JSONObject(), header);

        acceptedRequestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.show();
            }
        });
    }
}