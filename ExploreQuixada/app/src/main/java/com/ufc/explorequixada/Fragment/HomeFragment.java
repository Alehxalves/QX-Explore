package com.ufc.explorequixada.Fragment;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.type.LatLng;
//import com.ufc.explorequixada.Manifest;
import com.ufc.explorequixada.R;
public class HomeFragment extends Fragment  {

    Location currentLocation;
    FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    FrameLayout map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //map = map.findViewById(R.id.map_fragment);

        //fusedClient = LocationServices.getFusedLocationProviderClient(getActivity());
        //getLocation();

    }
//
//    private void getLocation() {
//        if(ActivityCompat.checkSelfPermission(
//                getActivity(), Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(
//                        getActivity(), Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION}, REQUEST_CODE);
//            return;
//        }
//
//        Task<Location> task = fusedClient.getLastLocation();
//
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location != null)
//                    currentLocation = location;
//                SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
//                assert supportMapFragment != null;
//                supportMapFragment.getMapAsync(HomeFragment.this);
//            }
//        });
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//       // LatLng latlng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//    }
}