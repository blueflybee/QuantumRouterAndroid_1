package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fernandocejas.android10.sample.presentation.R;

/**
 * @author shaojun
 * @name AutoCompleteUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-5
 */
public class GlideUtil {
  public static final String IMG_OSS_HANDLE_POSTFIX = "?x-oss-process=image/resize,m_fixed,h_200,w_200";

  public static void loadCircleHeadImage(Context context, String headImgUrl, ImageView imageView) {
    Glide.with(context).load(headImgUrl)
        .asBitmap().centerCrop()
        .placeholder(R.drawable.circle_icon)
        .into(new BitmapImageViewTarget(imageView) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(circularBitmapDrawable);
          }
        });
  }

  public static void loadCircleHeadImage(Context context, String headImgUrl, ImageView imageView, int holderImgId) {
    Glide.with(context).load(headImgUrl)
        .asBitmap().centerCrop()
        .placeholder(holderImgId)
        .into(new BitmapImageViewTarget(imageView) {
          @Override
          protected void setResource(Bitmap resource) {
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
            circularBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(circularBitmapDrawable);
          }
        });
  }

  public static void loadImage(Context context, String imgUrl, int placeHolder, ImageView imageView) {
    Glide.with(context)
        .load(imgUrl)
        .asBitmap()
        .centerCrop()
        .placeholder(placeHolder)
        .into(imageView);
  }
}
