package com.renewal_studio.pechengator.repository;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.support.DocumentQuote;
import java.util.ArrayList;

public class ListLocationRepository implements ListLocationContract.Repository {

    private static final String TAG = "ListLocationRepository";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // For first add of locations. Language like collections name(ru, nor, eng, chi)
    @Override
    public ArrayList<DocumentQuote> LoadCollectionData(String language){
        ArrayList<DocumentQuote> collection = new ArrayList<DocumentQuote>();
        db.collection(language)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                collection.add(document.toObject(DocumentQuote.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return collection;
    }

    @Override
    public DocumentQuote LoadDocumentData(String language, String document){
        DocumentQuote findDocument = null;
        db.collection(language)
                .whereEqualTo(document, true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DocumentQuote findDocument = document.toObject(DocumentQuote.class);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return findDocument;
    }
}
