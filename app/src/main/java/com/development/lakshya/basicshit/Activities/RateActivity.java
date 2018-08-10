package com.development.lakshya.basicshit.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.development.lakshya.basicshit.Fragments.Fragment_RateToilet_Usage;
import com.development.lakshya.basicshit.R;

public class RateActivity extends AppCompatActivity {

    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        selectedFragment = Fragment_RateToilet_Usage.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ratingCards, selectedFragment);
        transaction.commit();
    }
}
