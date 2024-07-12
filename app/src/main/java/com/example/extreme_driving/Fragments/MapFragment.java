package com.example.extreme_driving.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.extreme_driving.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    private static final LatLng INITIAL_LOCATION = new LatLng(32.1129923, 34.8182147);
    private static final String INITIAL_LOCATION_TITLE = "Initial Location";
    private static final float INITIAL_ZOOM_LEVEL = 15.0f;
    private GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map_FRAME_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(googleMap -> {
                MapFragment.this.googleMap = googleMap;
                initializeMapWithDefaultLocation();
            });
        }
    }

    private void initializeMapWithDefaultLocation() {
        if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(INITIAL_LOCATION).title(INITIAL_LOCATION_TITLE));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INITIAL_LOCATION, INITIAL_ZOOM_LEVEL));
        }
    }

    public void zoom(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng targetLocation = new LatLng(latitude, longitude);
            googleMap.clear(); // Clear existing markers
            googleMap.addMarker(new MarkerOptions().position(targetLocation).title("Target Location"));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(targetLocation)
                    .zoom(INITIAL_ZOOM_LEVEL)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}