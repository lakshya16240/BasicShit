package com.development.Lakshya.basicshit.Fragments;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.development.Lakshya.basicshit.Helpers.CheckableImageView;
import com.development.Lakshya.basicshit.R;

import me.relex.circleindicator.CircleIndicator;

public class Fragment_RateToilet extends Fragment{

    private CheckableImageView male_rating_card, female_rating_card, child_rating_card,elderly_rating_card;
    private CircleIndicator circleIndicatorRateToilet;

    public static Fragment_RateToilet newInstance() {
        Fragment_RateToilet fragment = new Fragment_RateToilet();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container == null)
            return null;

        View view = inflater.inflate(R.layout.fragment_rate_toilet,container,false);
//
//        male_rating_card = view.findViewById(R.id.male_rating_card);
//        female_rating_card = view.findViewById(R.id.female_rating_card);
//        child_rating_card = view.findViewById(R.id.child_rating_card);
//        elderly_rating_card = view.findViewById(R.id.elderly_rating_card);
//        circleIndicatorRateToilet = view.findViewById(R.id.circleIndicatorRateToilet);
//
//        circleIndicatorRateToilet.
//
//        male_rating_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("RateToilet", "onClick: male");
//                male_rating_card.toggle();
//            }
//        });
//        female_rating_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("RateToilet", "onClick: female");
//                male_rating_card.toggle();
//            }
//        });
//        child_rating_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("RateToilet", "onClick: child");
//                male_rating_card.toggle();
//            }
//        });
//        elderly_rating_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("RateToilet", "onClick: elderly");
//                male_rating_card.toggle();
//            }
//        });

//        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
//        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.parseColor("#ffd700"), PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

        return view;

    }

}
