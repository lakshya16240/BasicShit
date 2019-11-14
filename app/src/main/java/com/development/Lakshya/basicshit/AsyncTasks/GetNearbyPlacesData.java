package com.development.Lakshya.basicshit.AsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.development.Lakshya.basicshit.Helpers.DataParser;
import com.development.Lakshya.basicshit.Helpers.DownloadUrl;
import com.development.Lakshya.basicshit.Interfaces.PlacesListResponse;
import com.development.Lakshya.basicshit.POJO.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    private List<Place> nearbyPlacesList = null;
    private PlacesListResponse placesListResponse = null;
    private HashMap<Place,Marker> placeMarkers = null;

    public GetNearbyPlacesData(PlacesListResponse placesListResponse) {
        this.placesListResponse = placesListResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        placesListResponse.getNearbyPlacesList(nearbyPlacesList,placeMarkers);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void ShowNearbyPlaces(List<Place> nearbyPlacesList) {
        placeMarkers = new HashMap<>();
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute","Entered into showing locations");
            MarkerOptions markerOptions = new MarkerOptions();
            Place googlePlace = nearbyPlacesList.get(i);
            Log.d("ShowNearbyPlaces", "ShowNearbyPlaces: " + nearbyPlacesList.size());
            Log.d("ShowNearbyPlaces", "ShowNearbyPlaces: " + googlePlace);
            double lat = Double.parseDouble(googlePlace.getLatitude());
            double lng = Double.parseDouble(googlePlace.getLongitude());
            String placeName = googlePlace.getPlaceName();
            String vicinity = googlePlace.getVicinity();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Marker marker = mMap.addMarker(markerOptions);
            //googlePlace.setMarker(marker);
            placeMarkers.put(googlePlace,marker);

        }
    }


}
