package com.renewal_studio.pechengator.view;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.DocumentQuote;
import com.renewal_studio.pechengator.common.LatLngInterpolator;
import com.renewal_studio.pechengator.common.MarkerAnimation;
import com.renewal_studio.pechengator.contract.RouteContract;
import com.renewal_studio.pechengator.utils.MapUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.renewal_studio.pechengator.utils.Constant.SAVE_CHOICE;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_FROM_LAT;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_FROM_LNG;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_FROM_TEXT;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_TO_LAT;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_TO_LNG;
import static com.renewal_studio.pechengator.utils.Constant.SAVE_TO_TEXT;

public class RouteFragment extends Fragment implements OnMapReadyCallback, RouteContract.View {

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationClient;
    Location mCurrentLocation;
    Marker fromMarker = null;
    Marker currentLocationMarker;
    Marker toMarker = null;
    @BindView(R.id.search_from)
    EditText search_from;
    @BindView(R.id.search_to)
    EditText search_to;
    @BindView(R.id.types_transport)
    BottomNavigationView types_transport;
    @BindView(R.id.motion_layout)
    MotionLayout motion_layout;
    Snackbar snackbar;
    private LocationCallback locationCallback = null;
    private int width;
    private int trans = 0;
    private String to_text, from_text;
    private double to_lat, to_lng, from_lat, from_lng;
    private SharedPreferences pref;
    private Polyline route;
    private TravelMode mode = TravelMode.DRIVING;

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

    @OnClick(R.id.geolocation)
    public void setFromMyLocation(View view) {
        LocationRequest locationRequest = LocationRequest.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        101);
                return;
            }
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setFrom(getString(R.string.current_location), new LatLng(location.getLatitude(), location.getLongitude()));
                    startLocationUpdates();
                }
            }
        });
    }

    @OnClick(R.id.search_from)
    public void clickFrom(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt(SAVE_CHOICE, 1);
        ed.commit();
        RouteFragmentDirections.ActionRouteFragmentToListLocationFragment action =
                RouteFragmentDirections.
                        actionRouteFragmentToListLocationFragment();
        action.setListClick(true);
        Navigation.findNavController(view).navigate(action);
    }

    @OnClick(R.id.search_to)
    public void clickTo(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.setFocusableInTouchMode(false);
        SharedPreferences.Editor ed = pref.edit();
        ed.putInt(SAVE_CHOICE, 2);
        ed.commit();
        RouteFragmentDirections.ActionRouteFragmentToListLocationFragment action =
                RouteFragmentDirections.
                        actionRouteFragmentToListLocationFragment();
        action.setListClick(true);
        Navigation.findNavController(view).navigate(action);
    }

    public void setTo(String text, LatLng loc) {
        search_to.setText(text);
        to_text = text;
        to_lat = loc.latitude;
        to_lng = loc.longitude;
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(SAVE_TO_TEXT, to_text);
        ed.putFloat(SAVE_TO_LAT, (float) to_lat);
        ed.putFloat(SAVE_TO_LNG, (float) to_lng);
        ed.commit();
    }

    public void setFrom(String text, LatLng loc) {
        search_from.setText(text);
        from_text = text;
        from_lat = loc.latitude;
        from_lng = loc.longitude;
        SharedPreferences.Editor ed = pref.edit();
        ed.putString(SAVE_FROM_TEXT, from_text);
        ed.putFloat(SAVE_FROM_LAT, (float) from_lat);
        ed.putFloat(SAVE_FROM_LNG, (float) from_lng);
        ed.commit();
    }

    @OnClick({R.id.create_route})
    public void createRoute(View view) {
        showRoute(new LatLng(from_lat, from_lng), new LatLng(to_lat, to_lng));
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick({R.id.show_route})
    public void showRoute(View view) {
        motion_layout.transitionToStart();
    }

    @OnClick({R.id.my_location})
    public void showLocation(View view) {
        startLocationUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_route, container, false);
        ButterKnife.bind(this, root);
        pref = getActivity().getPreferences(Context.MODE_PRIVATE);
        trans = pref.getInt(SAVE_CHOICE, 0);
        search_from.setInputType(InputType.TYPE_NULL);
        search_to.setInputType(InputType.TYPE_NULL);
        ((MainActivity) getActivity()).setName(getString(R.string.show_route));
        ((MainActivity) getActivity()).setVisibilityBack(View.INVISIBLE);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        width = getResources().getDisplayMetrics().widthPixels;
        types_transport.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.driving:
                        mode = TravelMode.DRIVING;
                        return true;
                    case R.id.transit:
                        mode = TravelMode.TRANSIT;
                        return true;
                    case R.id.walking:
                        mode = TravelMode.WALKING;
                        return true;
                }
                return false;
            }
        });
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        from_lat = pref.getFloat(SAVE_FROM_LAT, 0);
        if (from_lat != 0) {
            from_lng = pref.getFloat(SAVE_FROM_LNG, 0);
            from_text = pref.getString(SAVE_FROM_TEXT, "");
            setFrom(from_text, new LatLng(from_lat, from_lng));
        }
        to_lat = pref.getFloat(SAVE_TO_LAT, 0);
        if (to_lat != 0) {
            to_lng = pref.getFloat(SAVE_TO_LNG, 0);
            to_text = pref.getString(SAVE_TO_TEXT, "");
            setTo(to_text, new LatLng(to_lat, to_lng));
        }
        RouteFragmentArgs args = RouteFragmentArgs.fromBundle(getArguments());
        DocumentQuote doc = args.getLocation();
        if (doc != null) {
            if (trans == 0) {
                setTo(doc.getName(), new LatLng(doc.getLat(), doc.getLng()));
            } else if (trans == 1) {
                setFrom(doc.getName(), new LatLng(doc.getLat(), doc.getLng()));
            } else if (trans == 2) {
                setTo(doc.getName(), new LatLng(doc.getLat(), doc.getLng()));
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MapUtil.isGooglePlayServicesAvailable(getContext())) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationCallback != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    public com.google.maps.model.LatLng decodeLatLng(LatLng loc) {
        return new com.google.maps.model.LatLng(loc.latitude, loc.longitude);
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1500);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        101);
                return;
            }
        }
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult.getLastLocation() == null)
                    return;
                for (Location location : locationResult.getLocations()) {
                    mCurrentLocation = location;
                    showMarker(mCurrentLocation);
                }

            }
        };
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(latLng)
                .zoom(mMap.getCameraPosition().zoom)
                .bearing(location.getBearing())
                .build()));
    }

    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions fromOption = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory
                .fromResource(R.drawable.my_geo)).zIndex(1.0f);
        if (currentLocationMarker == null) {
            currentLocationMarker = mMap.addMarker(fromOption);
        } else
            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());
    }

    public void showRoute(LatLng origin, LatLng destination) {
        try {
            if (fromMarker != null)
                fromMarker.remove();
            if (toMarker != null)
                toMarker.remove();
            if (route != null)
                route.remove();
            if (from_lat != 0 || to_lat != 0) {
                MarkerOptions fromOption = new MarkerOptions().position(new LatLng(from_lat, from_lng)).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.marker_a));
                fromMarker = mMap.addMarker(fromOption);
                MarkerOptions toOption = new MarkerOptions().position(new LatLng(to_lat, to_lng)).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.marker_b));
                toMarker = mMap.addMarker(toOption);
                GeoApiContext geoApiContext = new GeoApiContext.Builder()
                        .apiKey(this.getResources().getString(R.string.google_maps_key))
                        .build();
                DirectionsResult result = null;
                result = DirectionsApi.newRequest(geoApiContext)
                        .mode(mode)
                        .origin(decodeLatLng(origin))
                        .destination(decodeLatLng(destination)).await();
                if (result.routes.length > 0) {
                    List<com.google.maps.model.LatLng> path = result.routes[0].overviewPolyline.decodePath();
                    PolylineOptions options = new PolylineOptions();
                    LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                    for (int i = 0; i < path.size(); i++) {
                        options.add(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
                        latLngBuilder.include(new com.google.android.gms.maps.model.LatLng(path.get(i).lat, path.get(i).lng));
                    }
                    options.width(12f).color(getResources().getColor(R.color.colorAccent));
                    route = mMap.addPolyline(options);
                    LatLngBounds latLngBounds = latLngBuilder.build();
                    CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, width, width, 25);
                    mMap.animateCamera(track);
                    motion_layout.transitionToEnd();
                } else
                    showSnackbar();
            } else
                showSnackbar();
            if (from_lat == to_lat && from_lng == to_lng
                    && from_lat != 0 && to_lat != 0)
                showSnackbar();
        } catch (Exception e) {
            showSnackbar();
            e.printStackTrace();
        }
    }

    public void showSnackbar() {
        snackbar = Snackbar.make(getView(), "Unsupported input", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.text_white_color));
        TextView snackTextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackTextView.setTextColor(getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

}
