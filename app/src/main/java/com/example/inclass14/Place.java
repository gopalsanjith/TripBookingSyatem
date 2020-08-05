package com.example.inclass14;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable {
    String description,place_id, icon_id;
    String latitide, longitude;

    public String getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(String icon_id) {
        this.icon_id = icon_id;
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
        return "Place{" +
                "description='" + description + '\'' +
                ", place_id='" + place_id + '\'' +
                ", icon_id='" + icon_id + '\'' +
                ", latitide='" + latitide + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Place place = (Place)obj;
        return this.place_id == place.place_id;
    }

    public LatLng createLatLng(){
        return new LatLng(Double.parseDouble(this.latitide),Double.parseDouble(this.longitude));
    }
}
