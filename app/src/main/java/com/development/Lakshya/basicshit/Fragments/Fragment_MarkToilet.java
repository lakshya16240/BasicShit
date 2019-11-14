package com.development.Lakshya.basicshit.Fragments;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.development.Lakshya.basicshit.Activities.ToiletMarkedPopupActivity;
import com.development.Lakshya.basicshit.POJO.Line;
import com.development.Lakshya.basicshit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import static android.content.Context.LOCATION_SERVICE;

public class Fragment_MarkToilet extends Fragment {

    private GoogleMap googleMap;
    private FirebaseAnalytics mFirebaseAnalytics;
    private String eventName;
    private MapView mapView;
    private Button bv_confirmLocation, bv_confirm_point1, bv_confirm_point2, bv_markAnotherSpot;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationManager locationManager;
    public static final int  MY_LOCATION_PERMISSION_CODE = 100;
    private Marker locationMarker1, locationMarker2;
    private DatabaseReference markToiletDatabase;
    private ImageView iv_map_pin;

    Boolean isLineMode, isPointMode;

    private FloatingActionButton floatingActionButton, fab_line_floating_menu, fab_point_floating_menu, fab_myLocation;
    private Animation fab_open, fab_close, rotate_forward,rotate_backward;
    private boolean isOpen = false;
    private Polyline polyline;

    private TextView tv_mark_a_wall, tv_mark_a_spot, tv_line_floating_menu, tv_point_floating_menu;

    private int count;

    private LatLng center;
    private LatLng position;

    private Task<Location> locationResult;

    private boolean isLocationMarker1 = false, isLocationMarker2 = false;

    private ArrayList<LatLng> locations;
    private ArrayList<Line> walls;

    public static Fragment_MarkToilet newInstance() {
        Fragment_MarkToilet fragment = new Fragment_MarkToilet();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(container == null)
            return null;

        if (isAdded()) {
            mFusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(getActivity());

            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

//            final FrameLayout layout = getActivity().findViewById(R.id.map_fragment);
//            ViewTreeObserver vto = layout.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    } else {
//                        layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                    int width  = layout.getMeasuredWidth();
//                    int height = layout.getMeasuredHeight();
//                    Log.d("check kar rha hun", "onGlobalLayout: " + width +" " + height);
//
//                }
//            });
//
//            final BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.navigation);
//            ViewTreeObserver vto2 = layout.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
//                        bottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    } else {
//                        bottomNavigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    }
//                    int width  = bottomNavigationView.getMeasuredWidth();
//                    int height = bottomNavigationView.getMeasuredHeight();
//                    Log.d("check kar rha hun", "onGlobalLayout2: " + width +" " + height);
//
//                }
//            });


        }
        markToiletDatabase = FirebaseDatabase.getInstance().getReference("Toilets");

        View view = (FrameLayout)inflater.inflate(R.layout.fragment_mark_toilet, container, false);
        mapView = view.findViewById(R.id.markToiletMapView);
        bv_confirmLocation = view.findViewById(R.id.bv_confirmLocation);
        bv_confirm_point1 = view.findViewById(R.id.bv_confirm_point1);
        bv_confirm_point2 = view.findViewById(R.id.bv_confirm_point2);
        bv_markAnotherSpot = view.findViewById(R.id.bv_markAnotherSpot);
        bv_confirm_point1.setEnabled(false);
        bv_confirmLocation.setEnabled(false);
        bv_confirm_point2.setEnabled(false);
        bv_markAnotherSpot.setEnabled(false);

        floatingActionButton = view.findViewById(R.id.fab_mark_settings);
        fab_line_floating_menu = view.findViewById(R.id.fab_line_floating_menu);
        fab_point_floating_menu = view.findViewById(R.id.fab_point_floating_menu);
        fab_myLocation = view.findViewById(R.id.fab_myLocation_markToilet);
        tv_mark_a_spot = view.findViewById(R.id.tv_mark_a_spot);
        tv_mark_a_wall = view.findViewById(R.id.tv_mark_a_wall);
        tv_line_floating_menu = view.findViewById(R.id.tv_line_floating_menu);
        tv_point_floating_menu = view.findViewById(R.id.tv_point_floating_menu);
        fab_line_floating_menu = view.findViewById(R.id.fab_line_floating_menu);
        fab_point_floating_menu = view.findViewById(R.id.fab_point_floating_menu);
        iv_map_pin = view.findViewById(R.id.iv_map_pin);

        fab_open = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_show);
        fab_close = AnimationUtils.loadAnimation(getActivity(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_backward);

        count = 0;

        mapView.onCreate(savedInstanceState);
        loadMap();

        if(isAdded()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

            floatingActionButton.setVisibility(View.INVISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putInt("FloatingActionButtonId",floatingActionButton.getId());
                    eventName = "Floating Action Button Settings";
                    mFirebaseAnalytics.logEvent(eventName,params);
                    showAnimation();
                }
            });



//            fab_line_floating_menu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(locationMarker2!=null)
//                        locationMarker2.remove();
//                    if(locationMarker1!=null)
//                        locationMarker1.remove();
//                    count=0;
//                    bv_confirmLocation.setEnabled(false);
//                    tv_mark_a_spot.setVisibility(View.GONE);
//                    tv_mark_a_wall.setVisibility(View.VISIBLE);
//                    bv_confirmLocation.setVisibility(View.INVISIBLE);
//                    showAnimation();
//                    lineMode();
//                }
//            });

            fab_point_floating_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle params = new Bundle();
                    params.putInt("FloatingActionButtonId",fab_point_floating_menu.getId());
                    eventName = "Floating Action Button Mark a Spot";
                    mFirebaseAnalytics.logEvent(eventName,params);
                    if(locationMarker2!=null)
                        locationMarker2.remove();
                    if(locationMarker1!=null)
                        locationMarker1.remove();
                    bv_confirmLocation.setEnabled(false);
                    tv_mark_a_spot.setVisibility(View.VISIBLE);
                    tv_mark_a_wall.setVisibility(View.GONE);
                    bv_confirmLocation.setVisibility(View.INVISIBLE);
                    showAnimation();
                    pointMode();
                }
            });

            bv_markAnotherSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleMap.clear();
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(19),700,null);
                    bv_markAnotherSpot.setEnabled(false);
                    bv_markAnotherSpot.setVisibility(View.INVISIBLE);
                    bv_confirmLocation.setEnabled(true);
                    bv_confirmLocation.setVisibility(View.VISIBLE);
                    iv_map_pin.setVisibility(View.VISIBLE);
                }
            });

        }

        return view;
    }

    private void lineMode(){
        isPointMode = false;
        isLineMode = true;

        if(polyline!=null)
            polyline.remove();


        bv_confirm_point2.setVisibility(View.GONE);
        bv_confirm_point2.setEnabled(false);
        bv_confirmLocation.setVisibility(View.GONE);
        bv_confirmLocation.setEnabled(false);
        bv_confirm_point1.setVisibility(View.VISIBLE);
        bv_confirm_point1.setEnabled(true);

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                if(count==0){
//                    locationMarker1 = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_2)));
//                    count++;
//                }
//                else if(count==1){
//                    locationMarker2 = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_2)));
//                    if(polyline!=null)
//                        polyline.remove();
//
//                    polyline = googleMap.addPolyline(new PolylineOptions()
//                                    .add(locationMarker1.getPosition(),locationMarker2.getPosition())
//                                    .color(Color.parseColor("#FF8E6E")));
//
//
//                    count++;
//                }
//                else if(count==2){
//                    locationMarker2.remove();
//                    locationMarker2 = googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_2)));
//                    if(polyline!=null)
//                        polyline.remove();
//                    polyline = googleMap.addPolyline(new PolylineOptions()
//                                    .add(locationMarker1.getPosition(),locationMarker2.getPosition())
//                                    .color(Color.parseColor("#FF8E6E")));
//                }
//                if(count==2){
//                    bv_confirmLocation.setVisibility(View.VISIBLE);
//                    bv_confirmLocation.setEnabled(true);
//                }
//            }
//        });
//
//        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
//                bv_confirmLocation.setEnabled(false);
//                bv_confirmLocation.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
//                if(count==2){
//                    if(polyline!=null)
//                        polyline.remove();
//                    polyline = googleMap.addPolyline(new PolylineOptions()
//                            .add(locationMarker1.getPosition(),locationMarker2.getPosition())
//                            .color(Color.parseColor("#FF8E6E")));
//                }
//
//
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//                bv_confirmLocation.setVisibility(View.VISIBLE);
//                bv_confirmLocation.setEnabled(true);
//
//            }
//        });


        bv_confirm_point1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count++;
                bv_confirm_point1.setVisibility(View.GONE);
                bv_confirm_point1.setEnabled(false);
                bv_confirm_point2.setVisibility(View.VISIBLE);
                bv_confirm_point2.setEnabled(true);

                center = googleMap.getCameraPosition().target;

                final double latitude = center.latitude - 0.508558506846968;
                final double longitude = center.longitude - 0.00000033527613;

                position = new LatLng(latitude,longitude);

                locationMarker1 = googleMap.addMarker(new MarkerOptions()
                        .position(position)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));

                Log.d("lollll", "onClick: " + locationMarker1.getPosition().latitude);
            }
        });

        bv_confirm_point2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                center = googleMap.getCameraPosition().target;
                LatLng position2 = new LatLng(center.latitude - 0.508558506846968, center.longitude - 0.00000033527613);

                locationMarker2 = googleMap.addMarker(new MarkerOptions()
                        .position(position2)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));

                polyline = googleMap.addPolyline(new PolylineOptions()
                        .add(locationMarker1.getPosition(),locationMarker2.getPosition())
                        .color(Color.parseColor("#FF8E6E")));

                bv_confirm_point2.setVisibility(View.GONE);
                bv_confirm_point2.setEnabled(false);
                bv_confirmLocation.setVisibility(View.VISIBLE);
                bv_confirmLocation.setEnabled(true);

            }
        });

        bv_confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==2) {
                    LatLng position1 = new LatLng(locationMarker1.getPosition().latitude, locationMarker1.getPosition().longitude);
                    LatLng position2 = new LatLng(locationMarker2.getPosition().latitude, locationMarker2.getPosition().longitude);
                    Line line = new Line(position1.latitude,position2.latitude,position1.longitude,position2.longitude);
                    markToiletDatabase.push().setValue(line);
                    locationMarker1.remove();
                    locationMarker2.remove();
                    polyline.remove();
                    count=0;
                    bv_confirmLocation.setEnabled(false);
                    bv_confirmLocation.setVisibility(View.INVISIBLE);

                    String address = getCompleteAddressString(locationMarker1.getPosition().latitude,locationMarker1.getPosition().longitude);
                    Intent intent = new Intent(getActivity(), ToiletMarkedPopupActivity.class);
                    intent.putExtra("address",address);
                    startActivity(intent);
                }

            }
        });

    }


    private void pointMode(){

        isPointMode = true;
        isLineMode = false;

        if(polyline!=null)
            polyline.remove();

        bv_confirmLocation.setEnabled(true);

        bv_confirmLocation.setVisibility(View.VISIBLE);



//        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
//            @Override
//            public void onMarkerDragStart(Marker marker) {
////                bv_confirmLocation.setEnabled(false);
////                bv_confirmLocation.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onMarkerDrag(Marker marker) {
//
////                if(count==2){
////                    if(polyline!=null)
////                        polyline.remove();
////                    polyline = googleMap.addPolyline(new PolylineOptions()
////                            .add(locationMarker1.getPosition(),locationMarker2.getPosition())
////                            .color(Color.parseColor("#FF8E6E")));
////                }
//
//
//
//            }
//
//            @Override
//            public void onMarkerDragEnd(Marker marker) {
//
//                locationMarker1 = marker;
//            }
//        });

//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if(locationMarker1!=null)
//                    locationMarker1.remove();
//                locationMarker1 = googleMap.addMarker(new MarkerOptions()
//                                                    .position(latLng)
//                                                    .draggable(true)
//                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_2)));
//
//                bv_confirmLocation.setVisibility(View.VISIBLE);
//                bv_confirmLocation.setEnabled(true);
//            }
//        });

        bv_confirmLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                Bundle params = new Bundle();
                params.putInt("ButtonId",bv_confirmLocation.getId());
                eventName = "Confirm Location Point Mode";
                mFirebaseAnalytics.logEvent(eventName,params);


//                LatLng position = new LatLng(locationMarker1.getPosition().latitude, locationMarker1.getPosition().longitude);
                center = googleMap.getCameraPosition().target;
                LatLng position2 = new LatLng(center.latitude - 0.508558506846968, center.longitude - 0.00000033527613);
                //center.longitude = center.longitude + 0.00000033527611;
                //LatLng position = new LatLng(center.latitude, center.longitude - 0.00000100582838);

                markToiletDatabase.push().setValue(position2);

                toiletDatabaseSnapshot();

                bv_markAnotherSpot.setEnabled(true);
                bv_markAnotherSpot.setVisibility(View.VISIBLE);
                bv_confirmLocation.setEnabled(false);
                bv_confirmLocation.setVisibility(View.INVISIBLE);
                iv_map_pin.setVisibility(View.INVISIBLE);
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10),700,null);

//                Marker marker = googleMap.addMarker(new MarkerOptions()
//                        .position(center)
//                        .draggable(true)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_2)));
//
//                marker.setPosition(position2);

//                googleMap.addMarker(new MarkerOptions()
//                        .position(googleMap.getProjection().getVisibleRegion().latLngBounds.getCenter())
//                        .draggable(true)
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_2)));



//                Log.d("lollll", "onClick: " + position.longitude + " " + center.longitude );
//                Log.d("lollll", "onClick: " + position.latitude + " " + center.latitude );
                //locationMarker1.remove();
//                bv_confirmLocation.setEnabled(false);
//                bv_confirmLocation.setVisibility(View.INVISIBLE);

                Bundle args = new Bundle();
                args.putParcelable("from_position", center);

                String address = getCompleteAddressString(center.latitude,center.longitude);
                String uri = "http://maps.google.com/?q=" + center.latitude+","+center.longitude;
                String location = address + "\n" + uri;
                final Intent intent = new Intent(getActivity(), ToiletMarkedPopupActivity.class);
                intent.putExtra("location",args);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startActivity(intent);
                    }
                }, 1000);


            }
        });

    }

    private void showAnimation(){

        if(isOpen){
            floatingActionButton.startAnimation(rotate_backward);
            //fab_line_floating_menu.startAnimation(fab_close);
            fab_point_floating_menu.startAnimation(fab_close);
            //tv_line_floating_menu.startAnimation(fab_close);
            tv_point_floating_menu.startAnimation(fab_close);
            //fab_line_floating_menu.setClickable(false);
            fab_point_floating_menu.setClickable(false);
            isOpen = false;
        }
        else{
            floatingActionButton.startAnimation(rotate_forward);
            //fab_line_floating_menu.startAnimation(fab_open);
            fab_point_floating_menu.startAnimation(fab_open);
            //tv_line_floating_menu.startAnimation(fab_open);
            tv_point_floating_menu.startAnimation(fab_open);
            //fab_line_floating_menu.setClickable(true);
            fab_point_floating_menu.setClickable(true);
            isOpen = true;
        }

    }


    private void loadMap(){

        mapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if (isAdded()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_PERMISSION_CODE);
                        return;
                    } else {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        //final Task<Location> locationResult;
                        locationResult = mFusedLocationProviderClient.getLastLocation();

                        fab_myLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Bundle params = new Bundle();
                                params.putInt("FloatingActionButtonId",fab_myLocation.getId());
                                eventName = "Floating Action Button My Location";
                                mFirebaseAnalytics.logEvent(eventName,params);
                                setCameraToMyLocation(locationResult,15);
                            }
                        });


                        setCameraToMyLocation(locationResult,15);

                    }

                    pointMode();
                }


            }
        });
    }

    private void toiletDatabaseSnapshot(){

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

//        for(int i=0;i<walls.size();i++){
//            Marker marker1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(walls.get(i).getLatitude1(),walls.get(i).getLongitude1()))
//                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_location_orange4)));
//            Marker marker2 = googleMap.addMarker(new MarkerOptions().position(new LatLng(walls.get(i).getLatitude2(),walls.get(i).getLongitude2()))
//                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_location_orange4)));
//
//            Polyline wall = googleMap.addPolyline(new PolylineOptions()
//                    .add(marker1.getPosition(),marker2.getPosition())
//                    .color(R.color.colorAccent));
//
//        }
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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

    private void setCameraToMyLocation(Task<Location> locationResult, final float zoomLevel){

        locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {

                    Location location = task.getResult();

                    if(location!=null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(zoomLevel).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        center = googleMap.getCameraPosition().target;
                    }
                    else{
                        Toast.makeText(getActivity(), "Unable to Locate You", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_LOCATION_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    loadMap();
                }
            }
            else {
                Toast.makeText(getActivity(), "location permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
