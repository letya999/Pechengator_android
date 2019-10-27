package com.renewal_studio.pechengator.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.rd.PageIndicatorView;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.DocumentQuote;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragment extends Fragment {

    public LocationFragment() {}

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    PageIndicatorView indicatorView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.visual_desc)
    TextView visual_desc;
    @BindView(R.id.devoted)
    TextView devoted;
    @BindView(R.id.scroll_layout)
    NestedScrollView scrollView;
    @BindView(R.id.scroll_content)
    MotionLayout motionLayout;

    DocumentQuote doc;
    PhotoAdapter adapter;
    boolean course = false;

    @OnClick(R.id.show_map)
    public void showMap(View view) {
        LocationFragmentDirections.ActionLocationFragmentToRouteFragment action =
                LocationFragmentDirections.
                        actionLocationFragmentToRouteFragment();
        action.setLocation(doc);
        Navigation.findNavController(view).navigate(action);
    }

    @OnClick(R.id.btn_history)
    public void clickBtnHistory(View view) {
        if (course) {
            motionLayout.transitionToStart();
            course = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                ValueAnimator realSmoothScrollAnimation =
                        ValueAnimator.ofInt(scrollView.getScrollY(), 0);
                realSmoothScrollAnimation.setDuration(900);
                realSmoothScrollAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int scrollTo = (Integer) animation.getAnimatedValue();
                        scrollView.scrollTo(0, scrollTo);
                    }
                });
                realSmoothScrollAnimation.start();
            } else
                scrollView.smoothScrollTo(0, 0);
        } else {
            motionLayout.transitionToEnd();
            course = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setVisibilityBack(View.VISIBLE);
        ((MainActivity)getActivity()).setName(getString(R.string.location));
        LocationFragmentArgs args = LocationFragmentArgs.fromBundle(getArguments());
        doc = args.getLocation();
        name.setText(doc.getName());
        visual_desc.setText(doc.getVisual_description());
        devoted.setText(doc.getDevoted());
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
            try {
                Glide.with(context).load(itemList.get(pos)).into(image);
            } catch (Exception ex) {
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationFragmentDirections.ActionLocationFragmentToPhotoViewFragment action =
                            LocationFragmentDirections.actionLocationFragmentToPhotoViewFragment();
                    action.setLocation(doc);
                    Navigation.findNavController(itemView).navigate(action);
                }
            });
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
