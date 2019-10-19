package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.StartContract;
import com.renewal_studio.pechengator.repository.StartRepository;
import com.renewal_studio.pechengator.view.StartFragment;

public class StartPresenter implements StartContract.Presenter {

    private static final String TAG = "StartPresenter";

    private StartFragment sView;
    private StartRepository sRepository;

    public StartPresenter(StartFragment llView){
        this.sView = llView;
        this.sRepository = new StartRepository();
    }

}
