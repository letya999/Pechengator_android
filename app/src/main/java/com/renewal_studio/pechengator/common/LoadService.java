package com.renewal_studio.pechengator.common;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

import static com.renewal_studio.pechengator.utils.Constant.BROADCAST_ACTION;
import static com.renewal_studio.pechengator.utils.Constant.DEBUG_TAG;

public class LoadService extends Service {

    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
    SQLiteDatabase mDb;
    FirebaseFirestore db;
    int i = 0;
    FirebaseStorage storage;

    public LoadService() {
    }

    public void onCreate() {
        super.onCreate();
        Log.d(DEBUG_TAG, "onCreate");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String language = intent.getStringExtra(PARAM_IN_MSG);
        Log.d(DEBUG_TAG, "onStartCommand");
        someTask(language);
        return super.onStartCommand(intent, flags, startId);
    }

    void someTask(String lang) {
        FirebaseApp.initializeApp(getApplicationContext());
        DatabaseHelper mDBHelper = new DatabaseHelper(getApplicationContext());
        try {
            mDBHelper.updateDataBase();
            mDb = mDBHelper.getWritableDatabase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb.delete("Photo", null, null);
        mDb.delete("DocumentQuote", null, null);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        Log.d(DEBUG_TAG, "someTask");
        db.collection(lang).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    DocumentQuote doc = document.toObject(DocumentQuote.class);
                    ContentValues content = new ContentValues();
                    content.put("name", doc.getName());
                    content.put("note", doc.getNote());
                    content.put("devoted", doc.getDevoted());
                    content.put("visual_description", doc.getVisual_description());
                    content.put("lat", doc.getLat());
                    content.put("lng", doc.getLng());
                    mDb.insert("DocumentQuote", null, content);
                    downLoadPhotoUrl(doc);
                }
            }
        });
    }

    public void downLoadPhotoUrl(DocumentQuote doc) {
        for (String url : doc.getPhoto()) {
            StorageReference gsReference = storage.getReferenceFromUrl(url);
            gsReference.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {

                @Override
                public void onComplete(@NonNull Task<ListResult> task) {
                    for (StorageReference item : task.getResult().getItems()) {
                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ContentValues content = new ContentValues();
                                content.put("url", uri.toString());
                                content.put("name", doc.getName());
                                mDb.insert("Photo", null, content);
                                publishProgress(++i);
                            }
                        });
                    }
                }
            });
        }
    }

    public void publishProgress(int val) {
        if (val == 386) {
            Log.d(DEBUG_TAG, "publishProgress");
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(BROADCAST_ACTION);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(PARAM_OUT_MSG, val);
            sendBroadcast(broadcastIntent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
