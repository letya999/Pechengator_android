package com.renewal_studio.pechengator.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.LocationContract;
import butterknife.ButterKnife;

public class LocationFragment extends Fragment implements LocationContract.View{

    public LocationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setVisibilityBack(View.VISIBLE);
        ((MainActivity)getActivity()).setName(getString(R.string.location));
        return root;
    }
}
