package com.development.lakshya.basicshit.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.development.lakshya.basicshit.Adapters.NearbyToiletsAdapter;
import com.development.lakshya.basicshit.AsyncTasks.GetNearbyPlacesData;
import com.development.lakshya.basicshit.Interfaces.GoogleMapResponse;
import com.development.lakshya.basicshit.Interfaces.PlacesListResponse;
import com.development.lakshya.basicshit.POJO.Place;
import com.development.lakshya.basicshit.Activities.MapPopupActivity;
import com.development.lakshya.basicshit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
//import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;



public class Fragment_Home extends Fragment implements LocationListener {

    MapView rateMapView;

    private GoogleMap googleMap;
    public static final int MY_LOCATION_PERMISSION_CODE = 100;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private ArrayList<Place> nearbyPlacesList = null;
    private FloatingActionButton fab_myLocation;

    private GoogleMapResponse googleMapResponse;

    private FloatingActionButton floatingActionButton;
    private Dialog dialog;
    private FirebaseAnalytics mFirebaseAnalytics;
    private int placeNumber = 0;
    private String eventName;
    private UltraViewPager ultraViewPager;
    private PagerAdapter adapter;
    LocationManager locationManager;
    private HashMap<Place,Marker> placeMarkers;
    private ProgressBar progressBarHome;

    private Button bv_findToilets;


    private int count ;


    public static Fragment_Home newInstance() {
        Fragment_Home fragment = new Fragment_Home();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null)
            return null;

        count=0;

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rateMapView = view.findViewById(R.id.rateMapView);
        fab_myLocation = view.findViewById(R.id.fab_myLocation_home);
        ultraViewPager = view.findViewById(R.id.ultra_viewpager);
        progressBarHome = view.findViewById(R.id.progressBarHome);
        bv_findToilets = view.findViewById(R.id.bv_findToilets);
        ultraViewPager.getBackground().setAlpha(0);

        if (isAdded()) {


            ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            ultraViewPager.setMultiScreen(0.6f);
            ultraViewPager.setItemRatio(0.6f);
            ultraViewPager.setRatio(2.0f);
            ultraViewPager.setMaxHeight(200);
            ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
            ultraViewPager.setAutoMeasureHeight(true);
            ultraViewPager.setInfiniteLoop(true);
            //rv_slideUp_places.setLayoutManager(new LinearLayoutManager(getActivity()));
            rateMapView.onCreate(savedInstanceState);

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            mFusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(getActivity());

            locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            mGeoDataClient = Places.getGeoDataClient(getActivity());

            mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity());

            loadMap();
        }

        return view;


    }

    private void loadMap(){

        rateMapView.getMapAsync(new OnMapReadyCallback() {


            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                if (isAdded()) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_PERMISSION_CODE);
                        return;
                    }
                    else {
                        bv_findToilets.setVisibility(View.VISIBLE);
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

                        final Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                        fab_myLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle params = new Bundle();
                                params.putInt("FloatingActionButtonId",fab_myLocation.getId());
                                eventName = "Floating Action Button My Location";
                                mFirebaseAnalytics.logEvent(eventName,params);
                                setCameraToMyLocation(locationResult);
                            }
                        });


                        setCameraToMyLocation(locationResult);

                    }

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Bundle params = new Bundle();
                            params.putInt("Marker Clicked", R.id.rateMapView);
                            eventName = "Marker Clicked";
                            mFirebaseAnalytics.logEvent(eventName,params);
                            Double latitude = marker.getPosition().latitude;
                            Double longitude = marker.getPosition().longitude;

                            Place place = findPlaceObject(latitude,longitude);


                            Intent intent = new Intent(getActivity(),MapPopupActivity.class);
                            intent.putExtra("nearbyPlace", place);
                            startActivity(intent);
                            return false;
                        }
                    });

                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            // Inflate the layouts for the info window, title and snippet.
                            View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);

                            TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                            title.setText(marker.getTitle());

                            TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                            snippet.setText(marker.getSnippet());

                            return infoWindow;
                        }
                    });

                    ultraViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            Log.d("utlraViewPager", "onPageScrolled: ");
                        }

                        @Override
                        public void onPageSelected(int position) {
                            Place place2 = nearbyPlacesList.get(placeNumber);
                            Marker marker2 = placeMarkers.get(place2);
                            marker2.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            Place place = nearbyPlacesList.get(ultraViewPager.getCurrentItem());
                            Marker marker1 = placeMarkers.get(place);
                            marker1.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.toilet_location_orange4));
                            LatLng latLng = new LatLng(marker1.getPosition().latitude,marker1.getPosition().longitude);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            placeNumber = ultraViewPager.getCurrentItem();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                            Log.d("utlraViewPager", "onPageScrollStateChanged: ");
                        }
                    });

                }
            }
        });

    }

    private void setCameraToMyLocation(Task<Location> locationResult){

        locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {

                    final Location location = task.getResult();

                    if(location!=null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(),
                                location.getLongitude());

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(17).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        bv_findToilets.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                bv_findToilets.setVisibility(View.INVISIBLE);
                                if(count==0)
                                    loadNearByPlaces(location.getLatitude(), location.getLongitude());
                            }
                        });

                    }
                    else{
                        Toast.makeText(getActivity(), "Unable to Locate You", Toast.LENGTH_LONG).show();
                    }


                }
            }
        });
    }

    private void loadNearByPlaces(double latitude, double longitude) {
        progressBarHome.setVisibility(View.VISIBLE);
        count=1;
        String type = "cafe";
        StringBuilder googlePlacesUrl =
                new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(latitude).append(",").append(longitude);
        googlePlacesUrl.append("&radius=").append(500);
        googlePlacesUrl.append("&types=").append(type);
        googlePlacesUrl.append("&key=" + "AIzaSyC4B4LRJJ2-677FKYLmyqtQ50LtUI2Id-U");

        String url = googlePlacesUrl.toString();
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = googleMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);

        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(new PlacesListResponse() {
            @Override
            public void getNearbyPlacesList(List<Place> placeList, HashMap<Place,Marker> placeMarkerHashMap) {
                nearbyPlacesList = (ArrayList<Place>) placeList;
                placeMarkers = placeMarkerHashMap;
                Log.d("PlacesList", "getNearbyPlacesList: " + placeList.size());
                adapter = new NearbyToiletsAdapter(nearbyPlacesList,getActivity());
                ultraViewPager.setVisibility(View.VISIBLE);
                ultraViewPager.setAdapter(adapter);
                progressBarHome.setVisibility(View.INVISIBLE);
            }
        });
        getNearbyPlacesData.execute(DataTransfer);

    }


    public Place findPlaceObject(Double latitude, Double longitude){

        for(int i=0;i<nearbyPlacesList.size();i++){

            Double lat = Double.parseDouble(nearbyPlacesList.get(i).getLatitude());
            Double lng = Double.parseDouble(nearbyPlacesList.get(i).getLongitude());

            if(lat.equals(latitude)
                    && lng.equals(longitude)){

                return nearbyPlacesList.get(i);
            }

        }

        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,15);
        googleMap.animateCamera(update);
        if(isAdded())
            loadNearByPlaces(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
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
        rateMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        rateMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rateMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        rateMapView.onLowMemory();
    }


}
