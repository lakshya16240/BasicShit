package com.development.Lakshya.basicshit.Activities;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsee.Appsee;

import com.development.Lakshya.basicshit.POJO.Line;
import com.development.Lakshya.basicshit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class ToiletMarkedPopupActivity extends AppCompatActivity {

    private DatabaseReference markToiletDatabase;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private FirebaseAnalytics mFirebaseAnalytics;

    private LatLng center;

    private SupportMapFragment mapFragment;

    private ArrayList<LatLng> locations;
    private ArrayList<Line> walls;

    public static final int  MY_LOCATION_PERMISSION_CODE = 100;

    private int height, width;
    private Button bv_share;
    private TextView tv_visitWebPage;
    private TextView tv_share;
    private ImageView iv_thanks, close_popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toilet_marked_popup);

        Appsee.start();

        mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(this);
        markToiletDatabase = FirebaseDatabase.getInstance().getReference("Toilets");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapViewPopup);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        Bundle bundle = getIntent().getParcelableExtra("location");
        center = bundle.getParcelable("from_position");
        Log.d("Toilet Marked", "onCreate: " + center);

        String address = getCompleteAddressString(center.latitude,center.longitude);
        String uri = "http://maps.google.com/?q=" + center.latitude+","+center.longitude;
        final String location = address + "\n" + uri;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        bv_share = findViewById(R.id.bv_share);
        //tv_share = findViewById(R.id.tv_share);
        tv_visitWebPage = findViewById(R.id.tv_visitWebPage);
        iv_thanks = findViewById(R.id.iv_thanks);
        close_popup = findViewById(R.id.close_popup);

        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;

        Spanned html = Html.fromHtml("Visit " +
                "<a href='basicshit.org'>basicshit.org</a>" + " to contribute towards building more and better toilets for a cleaner India");


        tv_visitWebPage.setMovementMethod(LinkMovementMethod.getInstance());
        tv_visitWebPage.setText(html);
        tv_visitWebPage.setLinksClickable(true);
        Linkify.addLinks(tv_visitWebPage, Linkify.WEB_URLS);

//        BetterLinkMovementMethod.linkify(Linkify.ALL, this)
//                .setOnLinkClickListener(new BetterLinkMovementMethod.OnLinkClickListener() {
//                    @Override
//                    public boolean onClick(TextView textView, String url) {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("basicshit.org"));
//                        startActivity(browserIntent);
//                        return true;
//                    }
//                });

        //tv_thanks.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float)(width*0.024*0.7));
        //tv_share.setTextSize(TypedValue.COMPLEX_UNIT_SP,(float)(width*0.024*0.7));

        //getWindow().setLayout((int)((width)*0.8),(int)((height)*0.6));

        //bv_share.setTextSize(TypedValue.COMPLEX_UNIT_SP, (float)(width*0.0168*0.7));

//        ConstraintLayout parentConstraintLayout = (ConstraintLayout) bv_share.getParent();
//        ConstraintSet constraintSet = new ConstraintSet();
//        constraintSet.clone(parentConstraintLayout);
//        constraintSet.connect(bv_share.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, convertToPixels((float) (height*0.0057*0.7)));
//        constraintSet.connect(bv_share.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, convertToPixels((float) (height*0.0057*0.7)));
//        constraintSet.connect(bv_share.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, convertToPixels((float) (height*0.0057*0.7)));
//        constraintSet.connect(bv_share.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, convertToPixels((float) (height*0.0173*0.7)));
//        constraintSet.setVerticalBias(bv_share.getId(), (float)0.9);
//        constraintSet.constrainHeight(bv_share.getId(),convertToPixels((float) (height*0.035*0.7)));
//        constraintSet.constrainWidth(bv_share.getId(),convertToPixels((float) (height*0.05*0.7)));
//        //bv_share.setLayoutParams(new ConstraintLayout.LayoutParams(convertToPixels((float) (height*0.065*0.7)), convertToPixels((float) (width*0.061*0.7))));
//        constraintSet.applyTo(parentConstraintLayout);
//        parentConstraintLayout.invalidate();
//
        iv_thanks.requestLayout();
        iv_thanks.getLayoutParams().height = convertToPixels((float) (height*0.0655*0.7));

        // Apply the new width for ImageView programmatically
        iv_thanks.getLayoutParams().width = convertToPixels((float) (width*0.1*0.7));
        iv_thanks.setScaleType(ImageView.ScaleType.FIT_XY);

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Hi! I have just marked a spot at:\n "
                        + location + "\n\n" +
                        "for a toilet using the BasicShit App.\n" +
                        "You can do it too! Let's make India clean!\n" +
                        "If there are people openly pissing in your area, mark that spot! \n" +
                        "BasicShit will build a public toilet there\n" +
                        "\n" +
                        "Download the BasicShit App now! \n" +
                        "basicshit.org/#app";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, "Share"));
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loadMap();

    }

    private int convertToPixels(float value){
        Resources r = getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return px;
    }

    private void loadMap(){

        Log.d("testing", "loadMap: ");
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                if (ActivityCompat.checkSelfPermission(ToiletMarkedPopupActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("testing", "onMapReady: ");
                    ActivityCompat.requestPermissions(ToiletMarkedPopupActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_CODE);
                    return;
                } else {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    toiletDatabaseSnapshot();
                }
            }
        });
    }

    private void toiletDatabaseSnapshot(){
        Log.d("testing", "toiletDatabaseSnapshot: ");

        markToiletDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                markToiletsOnMap((Map<String,Object>)dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void markToiletsOnMap(Map<String,Object> toilets){
        Log.d("testing", "markToiletsOnMap: ");
        locations = new ArrayList<>();
        walls = new ArrayList<>();

        for (Map.Entry<String, Object> entry : toilets.entrySet()){

            //Get user map
            Map singleToilet = (Map) entry.getValue();
            //Get phone field and append to list
            if(singleToilet.get("latitude") != null){
                locations.add(new LatLng(Double.parseDouble(singleToilet.get("latitude").toString()),
                        Double.parseDouble(singleToilet.get("longitude").toString())));
            }
            else{
                walls.add(new Line(Double.parseDouble(singleToilet.get("latitude1").toString()),
                        Double.parseDouble(singleToilet.get("latitude2").toString()),
                        Double.parseDouble(singleToilet.get("longitude1").toString()),
                        Double.parseDouble(singleToilet.get("longitude2").toString())));
            }
            Log.d("locations", "markToiletsOnMap: " + singleToilet.get("latitude"));
        }

        for(int i=0;i<locations.size();i++){
            googleMap.addMarker(new MarkerOptions().position(locations.get(i))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_location_orange4)));
        }

        for(int i=0;i<walls.size();i++){
            Marker marker1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(walls.get(i).getLatitude1(),walls.get(i).getLongitude1()))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_location_orange4)));
            Marker marker2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(walls.get(i).getLatitude2(),walls.get(i).getLongitude2()))
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_location_orange4)));

            Polyline wall = googleMap.addPolyline(new PolylineOptions()
                    .add(marker1.getPosition(),marker2.getPosition())
                    .color(R.color.colorAccent));

        }

        Marker temp = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_locations2)).position(center));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(12).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        temp.setTitle("Your Marked Location");
        temp.showInfoWindow();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(ToiletMarkedPopupActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current address", strReturnedAddress.toString());
            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Cannot get Address!");
        }
        return strAdd;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_LOCATION_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    loadMap();
                }
            }
            else {
                Toast.makeText(this, "location permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }

}
