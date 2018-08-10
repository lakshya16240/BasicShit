package com.development.lakshya.basicshit.POJO;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

public class Place implements Serializable {

    private String placeName = "-NA-";
    private String vicinity = "-NA-";
    private String place_id = "";
    private String latitude = "";
    private String longitude = "";
    private String reference = "";
    private String rating;
    //private Marker marker;

    public Place(String placeName, String vicinity, String place_id, String latitude, String longitude, String reference, String rating) {
        this.placeName = placeName;
        this.vicinity = vicinity;
        this.place_id = place_id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reference = reference;
        this.rating = rating;
    }

//    public Marker getMarker() {
//        return marker;
//    }
//
//    public void setMarker(Marker marker) {
//        this.marker = marker;
//    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getReference() {
        return reference;
    }
}
