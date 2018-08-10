package com.development.lakshya.basicshit.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.development.lakshya.basicshit.Activities.RateActivity;
import com.development.lakshya.basicshit.R;

public class Fragment_Info extends Fragment {

    private Button bv_rateToilet;


    public static Fragment_Info newInstance() {
        Fragment_Info fragment = new Fragment_Info();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.fragment_info, container, false);
        Log.d("FRAGMENT INFO", "onCreateView: ");



            bv_rateToilet = view.findViewById(R.id.bv_rateToilet);
            bv_rateToilet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RateActivity.class);
                    startActivity(intent);
                }
            });


        return view;
    }
}
