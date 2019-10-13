package com.renewal_studio.pechengator.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.LocationContract;

public class LocationFragment extends Fragment implements LocationContract.View{

    private static final String TAG = "LocationFragment";

    public LocationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }
}
