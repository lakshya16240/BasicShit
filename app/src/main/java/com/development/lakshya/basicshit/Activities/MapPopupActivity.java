package com.development.lakshya.basicshit.Activities;

import android.content.Intent;
import com.appsee.Appsee;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.development.lakshya.basicshit.Adapters.InfoPopupAdapter;
import com.development.lakshya.basicshit.Fragments.Fragment_Info;
import com.development.lakshya.basicshit.Fragments.Fragment_Reviews;
import com.development.lakshya.basicshit.POJO.Place;
import com.development.lakshya.basicshit.R;
import com.development.lakshya.basicshit.Adapters.PhotosAdapter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import me.relex.circleindicator.CircleIndicator;


public class MapPopupActivity extends AppCompatActivity {

    public static final String TAG = "NearbyPlacesInfo";
    private int currentPage = 0;
    Place place = null;
    private int height, width;
    private GeoDataClient mGeoDataClient;
    private PlacePhotoMetadataBuffer photoMetadataBuffer;
    private ViewPager photosViewPager, infoPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private List<Fragment> fragmentList;
    private TabLayout info_tabs;
    private CircleIndicator circleIndicator;
    private TextView tv_placeName, tv_placeAddress;
    private RatingBar ratingBarPlaceRating;
    private AppBarLayout infoPopupAppBar;
    private RecyclerView rv_placePhotos;
    Handler handler;
    Timer swipeTimer;
    Runnable Update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_popup);

        Appsee.start();

        Intent intent = getIntent();
        place = (Place) intent.getSerializableExtra("nearbyPlace");
        Log.d(TAG, "onCreate: " + place.getPlace_id());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        getWindow().setLayout((width),(height));

        fragmentList = new ArrayList<>();
        fragmentList.add(Fragment_Info.newInstance());
        fragmentList.add(Fragment_Reviews.newInstance());

        infoPager = findViewById(R.id.infoPager);
        info_tabs = findViewById(R.id.info_tabs);
        circleIndicator = findViewById(R.id.circleIndicator);
        tv_placeName = findViewById(R.id.tv_placeName);
        tv_placeAddress = findViewById(R.id.tv_placeAddress);
        ratingBarPlaceRating = findViewById(R.id.ratingBar_placeRating_map_popup);
        infoPopupAppBar = findViewById(R.id.infoPopupAppBar);

//        AppBarLayout.LayoutParams layoutParams=new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT,(int)(height*0.33));
//        infoPopupAppBar.setLayoutParams();

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams)infoPopupAppBar.getLayoutParams();
        lp.height = (int)(height*0.33);

        ratingBarPlaceRating.setIsIndicator(true);

        info_tabs.addTab(info_tabs.newTab().setText("Information"));
        info_tabs.addTab(info_tabs.newTab().setText("Reviews"));

        //info_tabs.setupWithViewPager(infoPager,true);

        InfoPopupAdapter infoPopupAdapter = new InfoPopupAdapter(getSupportFragmentManager(),fragmentList);
        infoPager.setAdapter(infoPopupAdapter);

        infoPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(info_tabs));
        info_tabs.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        info_tabs.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
        info_tabs.setBackgroundColor(Color.parseColor("#FE6600"));
        info_tabs.setTabTextColors(Color.parseColor("#808080"), Color.parseColor("#FFFFFF"));
        info_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                infoPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setContentScrimColor(R.attr.colorPrimary);
        collapsingToolbarLayout.setStatusBarScrimColor(R.attr.colorPrimaryDark);

        tv_placeName.setText(place.getPlaceName());
        tv_placeAddress.setText(place.getVicinity());
        if(!place.getRating().equals("-NA-"))
            ratingBarPlaceRating.setRating(Float.parseFloat(place.getRating()));

        LayerDrawable overallStars = (LayerDrawable) ratingBarPlaceRating.getProgressDrawable();
        overallStars.getDrawable(2).setColorFilter(Color.parseColor("#ffd700"), PorterDuff.Mode.SRC_ATOP);
        overallStars.getDrawable(0).setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_ATOP);

        mGeoDataClient = Places.getGeoDataClient(MapPopupActivity.this);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        currentPage = 0;

        getPhotos();

    }

     private void showPhotos(final PlacePhotoMetadata[] placePhotoMetadataArray){

         Log.d(TAG, "showPhotos: ");
         rv_placePhotos = findViewById(R.id.rv_placePhotos);
         rv_placePhotos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        PhotosAdapter photosAdapter = new PhotosAdapter(placePhotoMetadataArray,MapPopupActivity.this, mGeoDataClient);
        rv_placePhotos.setAdapter(photosAdapter);

//        CircleIndicator indicator = findViewById(R.id.circleIndicator);
//        indicator.set

//        if(placePhotoMetadataArray.length>0) {
//            handler = new Handler();
//            Update = new Runnable() {
//                public void run() {
//                    Log.d(TAG, "run: " + currentPage);
//                    if (currentPage == placePhotoMetadataArray.length) {
//                        currentPage = 0;
//                    }
//                    photosViewPager.setCurrentItem(currentPage++, true);
//                }
//            };
//            swipeTimer = new Timer();
//            swipeTimer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    handler.post(Update);
//                }
//            }, 2500, 2500);
//        }
    }

    private void getPhotos() {
        final PlacePhotoMetadataBuffer[] mphotoMetadataBuffer = new PlacePhotoMetadataBuffer[1];
        final String placeId = place.getPlace_id();
        Log.d(TAG, "getPhotos: " + placeId);
        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
        Log.d(TAG, "getPhotos: " + photoMetadataResponse);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                Log.d(TAG, "onComplete: " + photos);
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                mphotoMetadataBuffer[0] = photos.getPhotoMetadata();

                PlacePhotoMetadata[] placePhotoMetadataArray = new PlacePhotoMetadata[mphotoMetadataBuffer[0].getCount()];
                for(int i=0;i<mphotoMetadataBuffer[0].getCount();i++)
                    placePhotoMetadataArray[i] = mphotoMetadataBuffer[0].get(i).freeze();


                mphotoMetadataBuffer[0].release();
                showPhotos(placePhotoMetadataArray);
                Log.d(TAG, "onComplete: " + mphotoMetadataBuffer[0].getCount());



            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(swipeTimer!=null) {
            swipeTimer.cancel();
            swipeTimer.purge();
        }
        if(handler!=null)
            handler.removeCallbacks(Update);

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
