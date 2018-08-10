package com.development.lakshya.basicshit.Activities;
import android.content.Intent;
import com.appsee.Appsee;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.development.lakshya.basicshit.Fragments.Fragment_AddToilet;
import com.development.lakshya.basicshit.R;
import com.development.lakshya.basicshit.Fragments.Fragment_Home;
import com.development.lakshya.basicshit.Fragments.Fragment_MarkToilet;
import com.development.lakshya.basicshit.Fragments.Fragment_RateToilet;
import com.development.lakshya.basicshit.Helpers.BottomNavigationViewHelper;
import com.google.firebase.analytics.FirebaseAnalytics;

public class OpenActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private BottomNavigationView bottomNavigationView;
    private String fragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);
        bottomNavigationView = findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Appsee.start();

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        Bundle params = new Bundle();
                        params.putInt("FragmentId",item.getItemId());

                        switch (item.getItemId()) {
                            case R.id.nav_home:
                                fragmentName = "HomeFragmentOpened";
                                selectedFragment = Fragment_Home.newInstance();
                                break;
                            case R.id.nav_mark:
                                fragmentName = "MarkToiletOpened";
                                selectedFragment = Fragment_MarkToilet.newInstance();
                                break;
                            case R.id.nav_rate:
                                fragmentName = "RateToiletOpened";
                                selectedFragment = Fragment_RateToilet.newInstance();
                                break;
                            case R.id.nav_add:
                                fragmentName = "AddToiletOpened";
                                selectedFragment = Fragment_AddToilet.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                        transaction.replace(R.id.map_fragment, selectedFragment);
//                        transaction.addToBackStack(null);
                        transaction.commit();

                        mFirebaseAnalytics.logEvent(fragmentName,params);
                        return true;
                    }
                });




        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_fragment, Fragment_Home.newInstance());
        transaction.commit();

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    public void setCurrentItem (int item) {

        Fragment selectedFragment = null;
        switch (item) {
            case R.id.fragmentLocation:
                selectedFragment = Fragment_Home.newInstance();
                break;
            case R.id.fragmentRating:
                selectedFragment = Fragment_RateToilet.newInstance();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.map_fragment, selectedFragment);
//        transaction.addToBackStack(null);
        transaction.commit();

    }

}
