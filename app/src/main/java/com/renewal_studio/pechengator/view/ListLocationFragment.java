package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.DatabaseHelper;
import com.renewal_studio.pechengator.common.DocumentQuote;
import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.utils.MapUtil;
import com.renewal_studio.pechengator.utils.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListLocationFragment extends Fragment implements ListLocationContract.View {

    private ListLocationContract.Presenter llPresenter;
    @BindView(R.id.list_location)
    RecyclerView list_locations;
    @BindView(R.id.searchTextView)
    EditText searh;
    @BindView(R.id.search)
    ImageButton btnSearh;
    ListLocationAdapter mAdapter;
    SQLiteDatabase mDb;
    ArrayList<DocumentQuote> dictionaryWords = new ArrayList<DocumentQuote>();
    List<DocumentQuote> filteredList = new ArrayList<DocumentQuote>();
    UpLoadTask task;
    int i = 0;
    String searhText = "";
    boolean click_list = false;
    boolean click_empty = false;
    private FusedLocationProviderClient fusedLocationClient;

    @OnClick(R.id.search)
    public void clearInput(View view) {
        if (click_empty) {
            searh.setText(null);
            searhText = null;
            searh.clearFocus();
            mAdapter.notifyDataSetChanged();
            click_empty = false;
        }
    }

    public void ListLocationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_location, container, false);
        ButterKnife.bind(this, root);
        ListLocationFragmentArgs args = ListLocationFragmentArgs.fromBundle(getArguments());
        click_list = args.getListClick();
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
        mAdapter = new ListLocationAdapter(getContext(), createList());
        list_locations.setAdapter(mAdapter);
        searh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    mAdapter.getFilter().filter(s.toString());
                    searhText = s.toString();
                    btnSearh.setImageDrawable(getResources().getDrawable(R.drawable.close));
                    click_empty = true;
                } else mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MapUtil.isGooglePlayServicesAvailable(getContext())) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
    }

    public List<DocumentQuote> createList() {
        List<DocumentQuote> list = new ArrayList<DocumentQuote>();
        list.add(new DocumentQuote("", "", "",
                "", new ArrayList<String>(Arrays.asList("")), new GeoPoint(0, 0)));
        return list;
    }

    public void onDestroy() {
        super.onDestroy();
        mDb.close();
    }

    public class ListLocationAdapter extends RecyclerView.Adapter<ListLocationAdapter.ItemViewHolder> implements Filterable {

        private List<DocumentQuote> mValues;
        private Context context;
        private CustomFilter mFilter;

        public ListLocationAdapter(Context context, List<DocumentQuote> mValues) {
            this.context = context;
            this.mValues = mValues;
            mFilter = new CustomFilter(ListLocationAdapter.this);
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int pos) {
            holder.name.setText(mValues.get(pos).getName());
            holder.desc.setText(mValues.get(pos).getVisual_description());
            try {
                Glide.with(getContext()).load(mValues.get(pos).getPhoto().get(0)).into(holder.image);
            } catch (Exception ex) {
            }
            if (!searh.getText().toString().isEmpty()) {
                TextUtils utils = new TextUtils(getContext());
                holder.name.setText(utils.highlightText(searhText, mValues.get(pos).getName()));
                holder.desc.setText(utils.highlightText(searhText, mValues.get(pos).getVisual_description()));
            } else {
                holder.name.setText(mValues.get(pos).getName());
                holder.desc.setText(mValues.get(pos).getVisual_description());
            }
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double distance = SphericalUtil.computeDistanceBetween(
                                        new LatLng(location.getLatitude(), location.getLongitude()),
                                        new LatLng(mValues.get(pos).getLat(), mValues.get(pos).getLat()));
                                int km = (int) distance / 1000;
                                int m = (int) distance % 1000;
                                holder.distance.setText(km + " " + getString(R.string.km) + " " + m + " " + getString(R.string.m));
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }


        public class ItemViewHolder extends RecyclerView.ViewHolder {
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
                        if (click_list) {
                            ListLocationFragmentDirections.ActionListLocationFragmentToRouteFragment action =
                                    ListLocationFragmentDirections.actionListLocationFragmentToRouteFragment();
                            action.setLocation(mValues.get(getAdapterPosition()));
                            Navigation.findNavController(view).navigate(action);
                        } else {
                            ListLocationFragmentDirections.ActionListLocationFragmentToLocationFragment2 action =
                                    ListLocationFragmentDirections.
                                            actionListLocationFragmentToLocationFragment2(mValues.get(getAdapterPosition()));
                            Navigation.findNavController(view).navigate(action);
                        }

                    }
                });
            }
        }

        public class CustomFilter extends Filter {

            private ListLocationAdapter mAdapter;

            private CustomFilter(ListLocationAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredList.addAll(dictionaryWords);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final DocumentQuote mWords : dictionaryWords) {
                        if (mWords.getName().toLowerCase().contains(filterPattern) || mWords.getVisual_description().toLowerCase().contains(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mAdapter.mValues = (List<DocumentQuote>) results.values;
                this.mAdapter.notifyDataSetChanged();
            }
        }
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
                dictionaryWords.add(doc);
                filteredList.add(doc);
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
            mAdapter = new ListLocationAdapter(getContext(), filteredList);
            list_locations.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
