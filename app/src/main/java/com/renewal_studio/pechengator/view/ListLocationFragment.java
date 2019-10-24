package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.GeoPoint;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.DatabaseHelper;
import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.support.DocumentQuote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inloop.localmessagemanager.LocalMessageManager;

public class ListLocationFragment extends Fragment implements ListLocationContract.View {

    private ListLocationContract.Presenter llPresenter;
    @BindView(R.id.list_location)
    RecyclerView list_locations;
    ListLocationAdapter adapter;
    SQLiteDatabase mDb;
    ArrayList<DocumentQuote> collection = new ArrayList<DocumentQuote>();
    UpLoadTask task;
    int i = 0;
    private static final String TAG = "test";

    public void ListLocationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_location, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setName(getString(R.string.list_locations));
        ((MainActivity)getActivity()).setVisibilityBack(View.INVISIBLE);
        DatabaseHelper mDBHelper = new DatabaseHelper(getActivity().getApplicationContext());
        try {
            mDBHelper.updateDataBase();
            mDb = mDBHelper.getWritableDatabase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        task = new UpLoadTask();
        task.execute();
        list_locations.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        return root;
    }

    public List<DocumentQuote> createList() {
        List<DocumentQuote> list = new ArrayList<DocumentQuote>();
        list.add(new DocumentQuote("", "", "",
                "", new ArrayList<String>(Arrays.asList("")), new GeoPoint(0, 0)));
        return list;
    }

    class UpLoadTask extends AsyncTask<Void,Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = mDb.query("DocumentQuote",null,
                    null, null, null, null, null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                DocumentQuote doc = new DocumentQuote();
                doc.setName(cursor.getString(cursor.getColumnIndex("name")));
                doc.setNote(cursor.getString(cursor.getColumnIndex("note")));
                doc.setDevoted(cursor.getString(cursor.getColumnIndex("devoted")));
                doc.setVisual_description(cursor.getString(cursor.getColumnIndex("visual_description")));
                doc.setLat(cursor.getDouble(cursor.getColumnIndex("lat")));
                doc.setLng(cursor.getDouble(cursor.getColumnIndex("lng")));
                doc.setPhoto(upLoadPhotoUrl(cursor.getString(cursor.getColumnIndex("name"))));
                collection.add(doc);
                publishProgress(++i);
                cursor.moveToNext();
            }
            cursor.close();
            return null;
        }

        public List<String> upLoadPhotoUrl(String name) {
            List<String> urls = new ArrayList<String>();
            Cursor cursor = mDb.query("Photo",null,
                    "name = ?",  new String[]{name}, null, null, null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                urls.add(cursor.getString(cursor.getColumnIndex("url")));
                cursor.moveToNext();
            }
            cursor.close();
            return urls;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            adapter = new ListLocationAdapter(getContext(), collection);
            list_locations.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mDb.close();
    }

    public class ListLocationAdapter extends RecyclerView.Adapter<ListLocationAdapter.ItemViewHolder> {
        private List<DocumentQuote> locations;
        private Context context;

        public ListLocationAdapter(Context context, List<DocumentQuote> locations){
            this.context = context;
            this.locations = locations;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.name.setText(locations.get(position).getName());
            holder.desc.setText(locations.get(position).getVisual_description());
            try {
                Glide.with(getContext()).load(locations.get(position).getPhoto().get(0)).into(holder.image);
            } catch (Exception ex) {}
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.name)
            TextView name;
            @BindView(R.id.desc)
            TextView desc;
            @BindView(R.id.distance)
            TextView distance;
            @BindView(R.id.image)
            ImageView image;

            public ItemViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "click");
                        LocalMessageManager.getInstance().send(R.id.msg_event, locations.get(getAdapterPosition()));

                        ListLocationFragmentDirections.ActionListLocationFragmentToLocationFragment2 action =
                                ListLocationFragmentDirections.
                                        actionListLocationFragmentToLocationFragment2(locations.get(getAdapterPosition()));
                        Navigation.findNavController(view).navigate(action);
                    }
                });
            }
        }
    }
}
