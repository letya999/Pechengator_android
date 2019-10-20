package com.renewal_studio.pechengator.contract;

import com.renewal_studio.pechengator.support.DocumentQuote;

import java.util.ArrayList;

public interface ListLocationContract {

    interface View{
    }

    interface Presenter{

    }

    interface Repository{
        public ArrayList<DocumentQuote> LoadCollectionData(String language);
        public DocumentQuote LoadDocumentData(String language, String document);
    }
}