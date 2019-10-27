package com.renewal_studio.pechengator.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.renewal_studio.pechengator.R;
import com.renewal_studio.pechengator.common.DocumentQuote;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoViewFragment extends Fragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    DocumentQuote doc;

    public PhotoViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo_view, container, false);
        ((MainActivity) getActivity()).setName(getString(R.string.photo));
        ButterKnife.bind(this, root);
        PhotoViewFragmentArgs args = PhotoViewFragmentArgs.fromBundle(getArguments());
        doc = args.getLocation();
        viewPager.setAdapter(new SamplePagerAdapter(getContext(), doc.getPhoto()));
        return root;
    }

    static class SamplePagerAdapter extends PagerAdapter {

        List<String> imageUrlsList = new ArrayList<>();
        Context context;

        public SamplePagerAdapter(Context context, List<String> imageUrlsList) {
            this.imageUrlsList = imageUrlsList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageUrlsList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int pos) {
            PhotoView photoView = new PhotoView(container.getContext());
            try {
                Glide.with(context).load(imageUrlsList.get(pos)).into(photoView);
            } catch (Exception ex) {
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
