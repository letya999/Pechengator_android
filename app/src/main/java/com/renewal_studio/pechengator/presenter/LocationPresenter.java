package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.LocationContract;
import com.renewal_studio.pechengator.repository.LocationRepository;

public class LocationPresenter implements LocationContract.Presenter {

    private static final String TAG = "LocationPresenter";

    private LocationFragment lView;
    private LocationRepository lRepository;

    public LocationPresenter(LocationFragment llView){
        this.lView = llView;
        this.lRepository = new LocationRepository();
    }

}
