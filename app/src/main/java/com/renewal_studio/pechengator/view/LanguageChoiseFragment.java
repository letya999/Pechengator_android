package com.renewal_studio.pechengator.view;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.DatabaseHelper;
import com.renewal_studio.pechengator.contract.LanguageChoiseContract;
import com.renewal_studio.pechengator.support.DocumentQuote;
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LanguageChoiseFragment extends Fragment implements LanguageChoiseContract.View  {

    public void LanguageChoiseFragment() {}

    private static final String TAG = "test";

    @BindView(R.id.progress)
    FrameLayout progress;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    FirebaseFirestore db;
    FirebaseStorage storage;
    DownLoadTask task;
    int i = 0;
    SQLiteDatabase mDb;

    @OnClick(R.id.lang_rus)
    public void onClick(View view) {
        progress.setVisibility(View.VISIBLE);
        DatabaseHelper mDBHelper = new DatabaseHelper(getActivity().getApplicationContext());
        try {
            mDBHelper.updateDataBase();
            mDb = mDBHelper.getWritableDatabase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        task = new DownLoadTask();
        task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getActivity().getApplicationContext());
        View root = inflater.inflate(R.layout.fragment_language_choise, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    class DownLoadTask extends AsyncTask<Void,Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            db.collection("ru").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentQuote doc = document.toObject(DocumentQuote.class);
                        ContentValues content = new ContentValues();
                        content.put("name", doc.getName());
                        content.put("note",doc.getNote());
                        content.put("devoted",doc.getDevoted());
                        content.put("visual_description", doc.getVisual_description());
                        content.put("lat", doc.getLat());
                        content.put("lng", doc.getLng());
                        mDb.insert("DocumentQuote", null, content);
                        downLoadPhotoUrl(doc);
                    }
                }
            });
            return null;
        }

        public void downLoadPhotoUrl(DocumentQuote doc) {
            for(String url : doc.getPhoto()) {
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
                                    content.put("name",doc.getName());
                                    mDb.insert("Photo", null, content);
                                    publishProgress(++i);
                                }
                            });
                        }
                    }
                });
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==386) {
                progress.setVisibility(View.INVISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavController navController = Navigation.
                                findNavController(getActivity(), R.id.splash_nav_host_fragment);
                        navController.navigate(R.id.startFragment);
                    }
                }, 1000);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mDb.close();
    }

}
