package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.repository.ListLocationRepository;

public class ListLocationPresenter implements ListLocationContract.Presenter {

    private static final String TAG = "ListLocationPresenter";

    private ListLocationContract.View llView;
    private ListLocationContract.Repository llRepository;

    public ListLocationPresenter(ListLocationContract.View llView){
        this.llView = llView;
        this.llRepository = new ListLocationRepository();
    }

}
