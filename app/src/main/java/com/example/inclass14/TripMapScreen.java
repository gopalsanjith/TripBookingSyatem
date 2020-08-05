package com.example.inclass14;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class TripMapScreen extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_map_screen);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        trip = (Trip) getIntent().getExtras().get("TRIP");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLngBounds.Builder latlngBuilder = new LatLngBounds.Builder();

        latlngBuilder.include(trip.createLatLng());
        mMap.addMarker(new MarkerOptions().position(trip.createLatLng()).title(trip.getDescription()));
        for(Place place:trip.getPlaces()){
            latlngBuilder.include(place.createLatLng());
            mMap.addMarker(new MarkerOptions().position(place.createLatLng()).title(place.getDescription()));
        }

        final LatLngBounds latLngBounds = latlngBuilder.build();

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 250));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 250));
            }
        });
    }
}
