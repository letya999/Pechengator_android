package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.support.DocumentQuote;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inloop.localmessagemanager.LocalMessage;
import eu.inloop.localmessagemanager.LocalMessageCallback;
import eu.inloop.localmessagemanager.LocalMessageManager;

public class LocationFragment extends Fragment implements LocalMessageCallback {

    public LocationFragment() {}

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    PageIndicatorView indicatorView;
    DocumentQuote doc;
    private static final String TAG = "test";
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.visual_desc)
    TextView visual_desc;
    @BindView(R.id.devoted)
    TextView devoted;
    PhotoAdapter adapter;

    @OnClick(R.id.show_map)
    public void showMap(View view) {
        NavController navController = Navigation.
                findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.routeFragment);
    }

    @OnClick(R.id.btn_history)
    public void clickBtnHistory(View view) {
        devoted.setText(doc.getDevoted());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setVisibilityBack(View.VISIBLE);
        ((MainActivity)getActivity()).setName(getString(R.string.location));
        LocalMessageManager.getInstance().addListener(this);
        LocationFragmentArgs args = LocationFragmentArgs.fromBundle(getArguments());
        doc = args.getLocation();
        name.setText(doc.getName());
        visual_desc.setText(doc.getVisual_description());
        adapter = new PhotoAdapter(getContext(),doc.getPhoto());
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        indicatorView.setCount(adapter.getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                indicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        return root;
    }

    public List<String> createList() {
        List<String> list = new ArrayList<String>();
        list.add("");
        return list;
    }

    @Override
    public void handleMessage(@NonNull LocalMessage msg) {
        Log.d(TAG, "handleMessage");
        doc = ((DocumentQuote)msg.getObject());
    }

    public class PhotoAdapter extends PagerAdapter {

        Context context;
        List<String> itemList;

        public PhotoAdapter(Context context, List<String> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int pos) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_photo, collection, false);
            ImageView image = itemView.findViewById(R.id.photo);
            Log.d(TAG, itemList.get(pos));
            try {
                Glide.with(context).load(itemList.get(pos)).into(image);
            } catch (Exception ex) {

            }
            collection.addView(itemView);
            return itemView;
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }
}
