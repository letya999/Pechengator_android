package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.RouteContract;
import com.renewal_studio.pechengator.repository.RouteRepository;

public class RoutePresenter implements RouteContract.Presenter {

    private static final String TAG = "RoutePresenter";

    private RouteFragment  rView;
    private RouteRepository rRepository;

    public RoutePresenter(RouteFragment llView){
        this.rView = llView;
        this.rRepository = new RouteRepository();
    }

}
