package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.LocationContract;
import com.renewal_studio.pechengator.repository.LocationRepository;

public class LocationPresenter implements LocationContract.Presenter {

    private static final String TAG = "LocationPresenter";

    private LocationContract.View lView;
    private LocationContract.Repository lRepository;

    public LocationPresenter(LocationContract.View llView){
        this.lView = llView;
        this.lRepository = new LocationRepository();
    }

}
