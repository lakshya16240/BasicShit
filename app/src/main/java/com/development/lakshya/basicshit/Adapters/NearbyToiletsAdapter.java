package com.development.lakshya.basicshit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.development.lakshya.basicshit.Activities.MapPopupActivity;
import com.development.lakshya.basicshit.POJO.Place;
import com.development.lakshya.basicshit.R;

import java.util.ArrayList;

public class NearbyToiletsAdapter extends PagerAdapter {
    private ArrayList<Place> nearbyToiletsList = null;
    private Context context;

    public NearbyToiletsAdapter(ArrayList<Place> nearbyToiletsList, Context context) {
        this.nearbyToiletsList = nearbyToiletsList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return nearbyToiletsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d("NearbyToiletsAdapter", "instantiateItem: " + position);
        CardView cardView = (CardView) LayoutInflater.from(container.getContext()).inflate(R.layout.item_nearby_toilets, null);
        final Place place = nearbyToiletsList.get(position);
        TextView textView = cardView.findViewById(R.id.tv_slide_placeName);
        RatingBar ratingBar = cardView.findViewById(R.id.ratingBar_slide_placeRating);
        if(!place.getRating().equals("-NA-"))
            ratingBar.setRating(Float.parseFloat(place.getRating()));
        textView.setText(place.getPlaceName());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MapPopupActivity.class);
                intent.putExtra("nearbyPlace", place);
//                context.startActivity(intent);
            }
        });

        container.addView(cardView);
        return cardView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        CardView view = (CardView) object;
        container.removeView(view);
    }
}
