package com.development.Lakshya.basicshit.Helpers;
import android.util.Log;

import com.development.Lakshya.basicshit.POJO.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public List<Place> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse " + jsonData);
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<Place> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<Place> placesList = new ArrayList<>();
        Place place = null;
        Log.d("Places", "getPlaces " + placesCount);

        for (int i = 0; i < placesCount; i++) {
            try {
                place = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(place);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private Place getPlace(JSONObject googlePlaceJson) {
//        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        Place place = null;
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String place_id = "";
        String latitude = "";
        String longitude = "";
        String reference = "";
        String rating = "-NA-";

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("rating")) {
                rating = googlePlaceJson.getString("rating");
            }
            place_id = googlePlaceJson.getString("place_id");
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            //Log.d("getPlaces", "getPlace: " + latitude + " " + longitude);
            reference = googlePlaceJson.getString("reference");
            place = new Place(placeName,vicinity,place_id,latitude,longitude,reference,rating);
//            googlePlaceMap.put("place_name", placeName);
//            googlePlaceMap.put("place_id",place_id);
//            googlePlaceMap.put("vicinity", vicinity);
//            googlePlaceMap.put("lat", latitude);
//            googlePlaceMap.put("lng", longitude);
//            googlePlaceMap.put("reference", reference);
            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return place;
    }
}
