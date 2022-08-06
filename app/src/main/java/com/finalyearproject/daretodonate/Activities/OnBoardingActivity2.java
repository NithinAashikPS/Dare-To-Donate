package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.finalyearproject.daretodonate.R;

public class OnBoardingActivity2 extends AppCompatActivity {

    private Button appLoginBtn;
    private Button appRegisterBtn;

    private Intent onBoarding3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding2);

        appLoginBtn = findViewById(R.id.app_login_btn);
        appRegisterBtn = findViewById(R.id.app_register_btn);

        onBoarding3 = new Intent(OnBoardingActivity2.this, OnBoardingActivity3.class);

        appLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoarding3.putExtra("PAGE_TYPE", 0);
                startActivity(onBoarding3);
            }
        });
        appRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBoarding3.putExtra("PAGE_TYPE", 1);
                startActivity(onBoarding3);
            }
        });
    }
}