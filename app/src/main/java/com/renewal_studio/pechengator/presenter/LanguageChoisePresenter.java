package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.LanguageChoiseContract;
import com.renewal_studio.pechengator.repository.LanguageChoiseRepository;

public class LanguageChoisePresenter implements LanguageChoiseContract.Presenter {

    private static final String TAG = "LanguageChoisePresenter";

    private LanguageChoiseContract.View lcView;
    private LanguageChoiseContract.Repository lcRepository;

    public LanguageChoisePresenter(LanguageChoiseContract.View lcView){
        this.lcView = lcView;
        this.lcRepository = new LanguageChoiseRepository();
    }

}
