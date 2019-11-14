package com.development.Lakshya.basicshit.Fragments;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.Lakshya.basicshit.R;

public class Fragment_RateToilet_Type extends Fragment {


    public static Fragment_RateToilet_Type newInstance() {
        Fragment_RateToilet_Type fragment = new Fragment_RateToilet_Type();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.fragment_rate_toilet_type, container, false);

        return view;
    }
}
