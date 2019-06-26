package com.fernandocejas.android10.sample.presentation.view.photo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.ImageGalleryUtils;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.adapter.FullScreenImageGalleryAdapter;
import com.orhanobut.logger.Logger;
import java.util.ArrayList;
import java.util.List;

public class FullScreenImageGalleryActivity extends BaseActivity {
    // region Member Variables
    private FullScreenImageGalleryAdapter mFullScreenImageGalleryAdapter;
    private List<String> mImages;
    private int mPosition;

    private ViewPager mViewPager;
    // endregion

    // region Listeners
    private ViewPager.OnPageChangeListener mViewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image_gallery);

        bindViews();


        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mImages = extras.getStringArrayList("images");
                mPosition = extras.getInt("position");
            }
        }

        setUpViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mViewPager.removeOnPageChangeListener(mViewPagerOnPageChangeListener);
    }
    // endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // region Helper Methods
    private void bindViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp);
    }

    private void setUpViewPager(){
        ArrayList<String> images = new ArrayList<>();

        int width = ImageGalleryUtils.getScreenWidth(this);
        int height = ImageGalleryUtils.getScreenHeight(this);

        for(String image : mImages){
            String imageUrl = ImageGalleryUtils.getFormattedImageUrl(image, width, height);
            images.add(image);
        }

      Logger.d(images + "");
        mFullScreenImageGalleryAdapter = new FullScreenImageGalleryAdapter(images);
        mViewPager.setAdapter(mFullScreenImageGalleryAdapter);
        mViewPager.setOnPageChangeListener(mViewPagerOnPageChangeListener);
        mViewPager.setCurrentItem(mPosition);

    }

    // endregion
}
