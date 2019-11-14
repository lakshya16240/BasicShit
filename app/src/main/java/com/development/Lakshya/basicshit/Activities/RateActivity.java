package com.development.Lakshya.basicshit.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.development.Lakshya.basicshit.Fragments.Fragment_RateToilet_Items;
import com.development.Lakshya.basicshit.Fragments.Fragment_RateToilet_Overall;
import com.development.Lakshya.basicshit.Fragments.Fragment_RateToilet_Type;
import com.development.Lakshya.basicshit.Fragments.Fragment_RateToilet_Usage;
import com.development.Lakshya.basicshit.R;

public class RateActivity extends AppCompatActivity {

    private Fragment selectedFragment;
    private int count=1;
    private ImageView iv_back_to_MapPopup;
    private ImageView iv_navigate_backward_disabled, iv_navigate_forward_disabled, iv_navigate_forward_enabled, iv_navigate_backward_enabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

//        iv_navigate_backward_disabled = findViewById(R.id.iv_navigate_backward_disabled);
//        iv_navigate_forward_disabled = findViewById(R.id.iv_navigate_forward_disabled);
//        iv_navigate_forward_enabled = findViewById(R.id.iv_navigate_forward_enabled);
//        iv_navigate_backward_enabled = findViewById(R.id.iv_navigate_backward_enabled);
//        iv_back_to_MapPopup = findViewById(R.id.iv_back_to_MapPopup);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        selectedFragment = Fragment_RateToilet_Usage.newInstance();
//        transaction.replace(R.id.ratingCards, selectedFragment);
//        transaction.commit();
//
//        iv_navigate_forward_enabled.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count++;
//                if(count == 4) {
//                    iv_navigate_forward_enabled.setVisibility(View.GONE);
//                    iv_navigate_forward_disabled.setVisibility(View.VISIBLE);
//                }
//                if(count==2) {
//                    iv_navigate_backward_disabled.setVisibility(View.GONE);
//                    iv_navigate_backward_enabled.setVisibility(View.VISIBLE);
//                }
//
//                if(count==2)
//                    selectedFragment = Fragment_RateToilet_Type.newInstance();
//                else if(count==3)
//                    selectedFragment = Fragment_RateToilet_Items.newInstance();
//                else
//                    selectedFragment = Fragment_RateToilet_Overall.newInstance();
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.ratingCards, selectedFragment);
//                transaction.commit();
//
//            }
//        });
//
//        iv_navigate_backward_enabled.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                count--;
//                if(count==1){
//                    iv_navigate_backward_enabled.setVisibility(View.GONE);
//                    iv_navigate_forward_disabled.setVisibility(View.VISIBLE);
//                }
//                if(count==3){
//                    iv_navigate_forward_disabled.setVisibility(View.GONE);
//                    iv_navigate_forward_enabled.setVisibility(View.VISIBLE);
//                }
//
//                if(count==2)
//                    selectedFragment = Fragment_RateToilet_Type.newInstance();
//                else if(count==3)
//                    selectedFragment = Fragment_RateToilet_Items.newInstance();
//                else
//                    selectedFragment = Fragment_RateToilet_Usage.newInstance();
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.ratingCards, selectedFragment);
//                transaction.commit();
//
//            }
//        });


    }
}
