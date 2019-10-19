package com.renewal_studio.pechengator.presenter;

import com.renewal_studio.pechengator.contract.LanguageChoiseContract;
import com.renewal_studio.pechengator.repository.LanguageChoiseRepository;
import com.renewal_studio.pechengator.view.LanguageChoiseFragment;

public class LanguageChoisePresenter implements LanguageChoiseContract.Presenter {

    private static final String TAG = "LanguageChoisePresenter";

    private LanguageChoiseFragment lcView;
    private LanguageChoiseRepository lcRepository;

    public LanguageChoisePresenter(LanguageChoiseFragment lcView){
        this.lcView = lcView;
        this.lcRepository = new LanguageChoiseRepository();
    }

}
