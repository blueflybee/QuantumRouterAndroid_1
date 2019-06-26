package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.mine.utils.ProxyTools;
import com.hss01248.photoouter.PhotoCallback;
import com.hss01248.photoouter.PhotoUtil;

import java.util.List;


public class PhotoTestActivity extends Activity implements View.OnClickListener {

  //    Button camera;
//    Button pick1Crop;
//    Button pic9;
//    Button pick1;
//    LinearLayout activityMain;
  PhotoCallback callback;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_test);
//        ButterKnife.bind(this);
    // PhotoUtil.init(getApplicationContext(),R.color.colorPrimaryDark,R.color.colorPrimary);
    findViewById(R.id.camera).setOnClickListener(this);
    findViewById(R.id.pick1_crop).setOnClickListener(this);
    findViewById(R.id.pic9).setOnClickListener(this);
    findViewById(R.id.pick1).setOnClickListener(this);


    callback = ProxyTools.getShowMethodInfoProxy(new PhotoCallback() {
      @Override
      public void onFail(String msg, Throwable r, int requestCode) {
        System.out.println("MainActivity.onFail");
        System.out.println("msg = " + msg);

      }

      @Override
      public void onSuccessSingle(String originalPath, String compressedPath, int requestCode) {
        System.out.println("MainActivity.onSuccessSingle");
        System.out.println("compressedPath = " + compressedPath);

      }

      @Override
      public void onSuccessMulti(List<String> originalPaths, List<String> compressedPaths, int requestCode) {
        System.out.println("MainActivity.onSuccessMulti");
        System.out.println("compressedPaths = " + compressedPaths);

      }

      @Override
      public void onCancel(int requestCode) {
        System.out.println("MainActivity.onCancel");

      }
    });
  }

  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.camera:
//        PhotoUtil.cropAvatar(true)
//            .start(this, 23, callback);
        PhotoUtil.begin()
            .setFromCamera(true)
            .setNeedCropWhenOne(true)
            .setNeedCompress(true)
            .setMaxSelectCount(1)
            .setCropMuskOval()
            .setSelectGif()
                        /*.setFromCamera(false)
                        .setMaxSelectCount(5)
                        .setNeedCropWhenOne(false)
                        .setNeedCompress(true)
                        .setCropRatio(16,9)*/
            .start(this, 23, callback);
        break;
      case R.id.pick1_crop:
        PhotoUtil.begin()
            .setFromCamera(false)
            .setMaxSelectCount(1)
            .setNeedCropWhenOne(true)
            .setCropRatio(16, 9)
            .start(this, 77, callback);
        break;
      case R.id.pic9:
        PhotoUtil.multiSelect(9)
            .start(this, 55, callback);
        break;
      case R.id.pick1:
        PhotoUtil.begin()
            .setNeedCropWhenOne(true)
            .setNeedCompress(true)
            .setMaxSelectCount(1)
            .setCropMuskOval()
            .setSelectGif()
                        /*.setFromCamera(false)
                        .setMaxSelectCount(5)
                        .setNeedCropWhenOne(false)
                        .setNeedCompress(true)
                        .setCropRatio(16,9)*/
            .start(this, 33, callback);
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    PhotoUtil.onActivityResult(this, requestCode, resultCode, data);
  }
}
