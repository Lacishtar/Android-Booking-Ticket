package com.tdtu.starrail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.tdtu.starrail.R;

import java.util.ArrayList;

public class AdapterVpgDetailMovie extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> mImageUrls;

    public AdapterVpgDetailMovie(Context context, ArrayList<String> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.layout_viewpager_trangchu, container, false);

        ImageView imageView = view.findViewById(R.id.img_viewpager);
        Glide.with(mContext)
                .load("file:///android_asset/" + mImageUrls.get(position))
                .centerCrop()
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
