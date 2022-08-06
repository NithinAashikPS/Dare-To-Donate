package com.finalyearproject.daretodonate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.finalyearproject.daretodonate.Fragments.LoginFragment;
import com.finalyearproject.daretodonate.Fragments.RegisterFragment;
import com.finalyearproject.daretodonate.Fragments.ResetPasswordFragment;
import com.finalyearproject.daretodonate.R;

public class OnBoardingActivity3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding3);

        switch (getIntent().getExtras().getInt("PAGE_TYPE")) {
            case 0:
                setFragment(new LoginFragment());
                break;
            case 1:
                setFragment(new RegisterFragment());
                break;
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, fragment);
        transaction.commit();
    }
}