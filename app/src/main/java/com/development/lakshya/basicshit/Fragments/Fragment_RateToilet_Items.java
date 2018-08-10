package com.development.lakshya.basicshit.Fragments;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.development.lakshya.basicshit.R;

public class Fragment_RateToilet_Items extends Fragment {


    public static Fragment_RateToilet_Items newInstance() {
        Fragment_RateToilet_Items fragment = new Fragment_RateToilet_Items();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.fragment_rate_toilet_items, container, false);

        return view;
    }
}
