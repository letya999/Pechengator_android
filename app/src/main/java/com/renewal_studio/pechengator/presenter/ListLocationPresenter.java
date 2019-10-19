package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.repository.ListLocationRepository;
import com.renewal_studio.pechengator.support.DocumentQuote;

import java.util.ArrayList;

public class ListLocationPresenter implements ListLocationContract.Presenter {

    private static final String TAG = "ListLocationPresenter";

    private String language;
    private ArrayList<DocumentQuote> documentQuotes = new ArrayList<DocumentQuote>();

    private ListLocationContract.View llView;
    private ListLocationContract.Repository llRepository;

    public ListLocationPresenter(ListLocationContract.View llView){
        this.llView = llView;
        this.llRepository = new ListLocationRepository();
    }

    public void OnCreate(String language){
        this.language = language;
        documentQuotes = llRepository.LoadCollectionData(language);
        /*
        Set it on View
         */
    }

    public void OnFinderClicked(){
        /*
        View update
         */
    }

    public void OnBackButtonClicked(){
        /*
        View update
         */
    }

    public void OnLocationChoised(){
        /*
        load Location layout
         */
    }
}
