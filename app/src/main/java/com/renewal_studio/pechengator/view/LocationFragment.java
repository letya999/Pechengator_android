package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.asksira.loopingviewpager.LoopingViewPager;
import com.rd.PageIndicatorView;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.contract.LocationContract;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends Fragment implements LocationContract.View{

    public LocationFragment() {}

    @BindView(R.id.viewpager)
    LoopingViewPager viewPager;
    @BindView(R.id.indicator)
    PageIndicatorView indicatorView;

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
        viewPager.setAdapter(new PhotoAdapter(getContext(), items, true));
        indicatorView.setCount(viewPager.getIndicatorCount());
        viewPager.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
                indicatorView.setProgress(selectingPosition, progress);
                viewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {}
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.resumeAutoScroll();
    }

    @Override
    public void onPause() {
        viewPager.pauseAutoScroll();
        super.onPause();
    }

    public class PhotoAdapter extends LoopingPagerAdapter<Integer> {

        private static final int VIEW_TYPE_NORMAL = 100;
        Context context;

        public PhotoAdapter(Context context, ArrayList<Integer> itemList, boolean isInfinite) {
            super(context, itemList, isInfinite);
            this.context = context;
        }

        @Override
        protected int getItemViewType(int listPosition) {
            return VIEW_TYPE_NORMAL;
        }

        @Override
        protected View inflateView(int viewType, ViewGroup container, int listPosition) {
            return LayoutInflater.from(context).inflate(R.layout.item_photo, container, false);
        }

        @Override
        protected void bindView(View view, int pos, int viewType) {
            ImageView imageView = view.findViewById(R.id.photo);
            imageView.setBackgroundColor(context.getResources().getColor(getBackgroundColor(pos)));
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
    }
}
