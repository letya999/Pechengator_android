package com.renewal_studio.pechengator.contract;

import com.renewal_studio.pechengator.common.DocumentQuote;

import java.util.ArrayList;

public interface ListLocationContract {

    interface View{
    }

    interface Presenter{

    }

    interface Repository{
        ArrayList<DocumentQuote> LoadCollectionData(String language);

        DocumentQuote LoadDocumentData(String language, String document);
    }
}