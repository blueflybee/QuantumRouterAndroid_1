package com.fernandocejas.android10.sample.presentation.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fernandocejas.android10.sample.presentation.R;

import java.util.List;

/**
 * Created by etiennelawlor on 8/20/15.
 */
public class FullScreenImageGalleryAdapter extends PagerAdapter {

    // region Member Variables
    private List<String> mImages;
    // endregion

    // region Constructors
    public FullScreenImageGalleryAdapter(List<String> images) {
        mImages = images;
    }
    // endregion

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_fullscreen_image, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll);

        String image = mImages.get(position);

        if(!TextUtils.isEmpty(image)){
//            Picasso.with(imageView.getContext()).invalidate(image);
//            Picasso.with(imageView.getContext()).load(image).into(imageView, null);
        } else {
            imageView.setImageDrawable(null);
        }


        container.addView(view, 0);

        return view;
    }

    @Override
    public int getCount() {
        return mImages.size();
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
