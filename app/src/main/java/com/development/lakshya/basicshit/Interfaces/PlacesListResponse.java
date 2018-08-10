package com.development.lakshya.basicshit.Interfaces;
import com.development.lakshya.basicshit.POJO.Place;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;

public interface PlacesListResponse {

    void getNearbyPlacesList(List<Place> placeList, HashMap<Place,Marker> placesMarkers);
}
