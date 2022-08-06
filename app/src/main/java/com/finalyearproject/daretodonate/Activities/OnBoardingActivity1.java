package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.finalyearproject.daretodonate.Adapters.OnBoardingScreenAdapter;
import com.finalyearproject.daretodonate.Models.OnBoardingScreenModel;
import com.finalyearproject.daretodonate.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity1 extends AppCompatActivity {

    private List<OnBoardingScreenModel> onBoardingScreenModelList;
    private OnBoardingScreenAdapter onBoardingScreenAdapter;

    private ViewPager onBoardingPager;
    private TabLayout onBoardingIndicator;

    private TextView onBoardingNext;
    private TextView onBoardingSkip;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding1);

        sharedPreferences = this.getSharedPreferences(getResources().getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        onBoardingPager = findViewById(R.id.on_boarding_view_pager);
        onBoardingIndicator = findViewById(R.id.on_boarding_indicator);

        onBoardingNext = findViewById(R.id.on_boarding_next);
        onBoardingSkip = findViewById(R.id.on_boarding_skip);

        onBoardingScreenModelList = new ArrayList<>();
        onBoardingScreenModelList.add(new OnBoardingScreenModel(getResources().getDrawable(R.drawable.ic_on_boarding_1), getResources().getString(R.string.on_boarding_title_1), getResources().getString(R.string.on_boarding_description_1)));
        onBoardingScreenModelList.add(new OnBoardingScreenModel(getResources().getDrawable(R.drawable.ic_on_boarding_2), getResources().getString(R.string.on_boarding_title_2), getResources().getString(R.string.on_boarding_description_2)));

        onBoardingScreenAdapter = new OnBoardingScreenAdapter(this, onBoardingScreenModelList);
        onBoardingPager.setAdapter(onBoardingScreenAdapter);
        onBoardingIndicator.setupWithViewPager(onBoardingPager, true);

        onBoardingNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBoardingPager.getCurrentItem() < onBoardingScreenModelList.size()-1)
                    onBoardingPager.setCurrentItem(onBoardingPager.getCurrentItem()+1);
                else {
                    editor.putBoolean("onBoard", true);
                    editor.apply();
                    startActivity(new Intent(OnBoardingActivity1.this, OnBoardingActivity2.class));
                    finish();
                }
            }
        });

        onBoardingSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean("onBoard", true);
                editor.apply();
                startActivity(new Intent(OnBoardingActivity1.this, OnBoardingActivity2.class));
                finish();
            }
        });
    }
}