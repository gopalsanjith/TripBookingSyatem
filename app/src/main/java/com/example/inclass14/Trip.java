package com.example.inclass14;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Trip implements Serializable {
    String description,place_id,tripName;
    String latitide, longitude;
    ArrayList<Place> places = new ArrayList<>();

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getLatitide() {
        return latitide;
    }

    public void setLatitide(String latitide) {
        this.latitide = latitide;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "description='" + description + '\'' +
                ", place_id='" + place_id + '\'' +
                ", tripName='" + tripName + '\'' +
                ", latitide='" + latitide + '\'' +
                ", longitude='" + longitude + '\'' +
                ", places=" + places +
                '}';
    }

    public LatLng createLatLng(){
        return new LatLng(Double.parseDouble(this.latitide),Double.parseDouble(this.longitude));
    }
}
