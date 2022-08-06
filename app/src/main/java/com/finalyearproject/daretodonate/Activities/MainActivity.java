package com.finalyearproject.daretodonate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.finalyearproject.daretodonate.Fragments.DonationFragment;
import com.finalyearproject.daretodonate.Fragments.HomeFragment;
import com.finalyearproject.daretodonate.Fragments.MapFragment;
import com.finalyearproject.daretodonate.Fragments.OrdersFragment;
import com.finalyearproject.daretodonate.Fragments.ProfileFragment;
import com.finalyearproject.daretodonate.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.mapbox.mapboxsdk.Mapbox;

public class MainActivity extends AppCompatActivity {

    private BottomAppBar bottomAppBar;
    private MaterialShapeDrawable materialShapeDrawable;

    private static BottomNavigationView bottomNavigationView;
    private FragmentTransaction transaction;
    private FloatingActionButton bloodBankLocation;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomNavigationView = findViewById(R.id.navigation_view);
        bloodBankLocation = findViewById(R.id.blood_bank_location);

        try {
            if (getIntent().getAction().equals("Notification")) {
                transaction = getSupportFragmentManager().beginTransaction();
                setFragment(new DonationFragment(), R.id.donation_requests, transaction, true);
                notificationManager.cancel(2);
            }
        } catch (Exception e) {
            transaction = getSupportFragmentManager().beginTransaction();
            setFragment(new HomeFragment(), R.id.home, transaction, true);
        }
        bloodBankLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                transaction = getSupportFragmentManager().beginTransaction();
                setFragment(new MapFragment(), R.id.blood_bank_map, transaction, true);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        transaction = getSupportFragmentManager().beginTransaction();
                        setFragment(new HomeFragment(), item.getItemId(), transaction, false);
                        break;
                    case R.id.nearby_orders:
                        transaction = getSupportFragmentManager().beginTransaction();
                        setFragment(new OrdersFragment(), item.getItemId(), transaction, false);
                        break;
                    case R.id.blood_bank_map:
                        transaction = getSupportFragmentManager().beginTransaction();
                        setFragment(new MapFragment(), item.getItemId(), transaction, false);
                        break;
                    case R.id.donation_requests:
                        transaction = getSupportFragmentManager().beginTransaction();
                        setFragment(new DonationFragment(), item.getItemId(), transaction, false);
                        break;
                    case R.id.profile:
                        transaction = getSupportFragmentManager().beginTransaction();
                        setFragment(new ProfileFragment(), item.getItemId(), transaction, false);
                        break;
                }
                return true;
            }
        });

//        materialShapeDrawable = (MaterialShapeDrawable) bottomAppBar.getBackground();
//        materialShapeDrawable.setShapeAppearanceModel(
//                materialShapeDrawable.getShapeAppearanceModel()
//                        .toBuilder()
//                        .setAllCorners(CornerFamily.ROUNDED, getResources().getDimension(R.dimen._5sdp))
//                        .build());
    }

    public static void setFragment(Fragment fragment, int id, FragmentTransaction transaction, boolean flag) {
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.commit();
        if (flag)
            bottomNavigationView.setSelectedItemId(id);
    }
}