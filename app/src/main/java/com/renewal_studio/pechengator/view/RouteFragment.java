package com.renewal_studio.pechengator.view;


import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.RouteContract;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteFragment extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,  OnMapReadyCallback , RouteContract.View {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    public RouteFragment() {

    }

    @OnClick(R.id.zoom_plus)
    public void zoomPlus(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
    }

    @OnClick(R.id.zoom_minus)
    public void zoomMinus(View view) {
        mMap.animateCamera(CameraUpdateFactory.zoomOut());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_route, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setName(getString(R.string.show_route));
        ((MainActivity)getActivity()).setVisibilityBack(View.INVISIBLE);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getContext(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(false);
    }
}
