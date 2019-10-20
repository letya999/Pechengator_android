package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.ListLocationContract;
import com.renewal_studio.pechengator.data.Location;
import com.renewal_studio.pechengator.presenter.ListLocationPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListLocationFragment extends Fragment implements ListLocationContract.View {

    private static final String TAG = "ListLocationFragment";
    private ListLocationContract.Presenter llPresenter;

    public void ListLocationFragment() {}

    @BindView(R.id.list_location)
    RecyclerView list_locations;


    @OnClick(R.id.searchTextView)
    public void click(View view) {
        NavController navController = Navigation.
                findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.locationFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_location, container, false);
        ButterKnife.bind(this, root);
        list_locations.setAdapter(new ListLocationAdapter(getContext(), new ArrayList<Location>()));
        list_locations.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        return root;
    }

    public class ListLocationAdapter extends RecyclerView.Adapter<ListLocationAdapter.ItemViewHolder> {
        private List<Location> locations;
        private Context context;

        public ListLocationAdapter(Context context, List<Location> locations){
            this.context = context;
            this.locations = locations;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ItemViewHolder holder = new ItemViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.name.setText(locations.get(position).getLocationName());
            holder.desc.setText(locations.get(position).getLocationDiscription());
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
            }
        }
    }

}
