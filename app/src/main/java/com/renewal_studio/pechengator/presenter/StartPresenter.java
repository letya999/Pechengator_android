package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.StartContract;
import com.renewal_studio.pechengator.repository.StartRepository;

public class StartPresenter implements StartContract.Presenter {

    private static final String TAG = "StartPresenter";

    private StartContract.View sView;
    private StartContract.Repository sRepository;

    public StartPresenter(StartContract.View llView){
        this.sView = llView;
        this.sRepository = new StartRepository();
    }

}
