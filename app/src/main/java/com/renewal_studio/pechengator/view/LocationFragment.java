package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.rd.PageIndicatorView;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.LocationContract;
import com.renewal_studio.pechengator.support.DocumentQuote;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inloop.localmessagemanager.LocalMessage;
import eu.inloop.localmessagemanager.LocalMessageCallback;

public class LocationFragment extends Fragment implements LocationContract.View, LocalMessageCallback {

    public LocationFragment() {}

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.indicator)
    PageIndicatorView indicatorView;
    @BindView(R.id.scroll_text)
    NestedScrollView scrollView;
    DocumentQuote location;
    private boolean click = false;
    @BindView(R.id.scroll_content)
    MotionLayout scroll_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, root);
        ((MainActivity)getActivity()).setVisibilityBack(View.VISIBLE);
        ((MainActivity)getActivity()).setName(getString(R.string.location));
        ArrayList<Integer> items = new ArrayList<>();
        items.add(0, 1);
        items.add(1, 2);
        items.add(2, 3);
        viewPager.setAdapter(new PhotoAdapter(getContext(), items));
        viewPager.getAdapter().notifyDataSetChanged();
        indicatorView.setCount(viewPager.getAdapter().getCount());
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
        scroll_content.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {


            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }

            @Override
            public boolean allowsTransition(MotionScene.Transition transition) {
                return false;
            }
        });
        return root;
    }

    @Override
    public void handleMessage(@NonNull LocalMessage msg) {
        switch (msg.getId()) {
            case R.id.msg_event : {
                location = ((DocumentQuote)msg.getObject());
            }
        }
    }

    public class PhotoAdapter extends PagerAdapter {

        Context context;
        ArrayList<Integer> itemList;

        public PhotoAdapter(Context context, ArrayList<Integer> itemList) {
            this.itemList = itemList;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int pos) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_photo, collection, false);
            ImageView imageView = itemView.findViewById(R.id.photo);
            imageView.setBackgroundColor(context.getResources().getColor(getBackgroundColor(pos)));
            collection.addView(itemView);
            return itemView;
        }

        private int getBackgroundColor (int number) {
            switch (number) {
                case 0:
                    return android.R.color.holo_orange_light;
                case 1:
                    return android.R.color.holo_blue_light;
                case 2:
                    return android.R.color.holo_green_light;
                default:
                    return android.R.color.holo_red_light;
            }
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
