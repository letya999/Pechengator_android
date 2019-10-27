package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.common.DocumentQuote;
import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.contract.LocationContract;
import com.renewal_studio.pechengator.repository.ListLocationRepository;

public class LocationPresenter implements LocationContract.Presenter {

    private static final String TAG = "LocationPresenter";

    private String language;
    private String documentName;
    private DocumentQuote documentQuote = new DocumentQuote();

    private LocationContract.View lView;
    private ListLocationContract.Repository lRepository;

    public LocationPresenter(LocationContract.View llView){
        this.lView = llView;
        this.lRepository = new ListLocationRepository();
    }

    public void OnCreate(String language, String documentName){
        this.language = language;
        this.documentName = documentName;
        documentQuote = lRepository.LoadDocumentData(language , documentName);
        /*
        Set it on View
         */
    }

    public void OnMoreInformationClicked(){
        /*
        View update
         */
    }

    public void OnBackButtonClicked(){
        /*
        View update
         */
    }

    public void OnMapFinderClicked(){
        /*
        load Location layout
         */
    }

}
