/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTransmitFileBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TransmitMainActivity extends BaseActivity {
  private ActivityTransmitFileBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_transmit_file);

    initView();
  }

  private void initView() {
    initMoveFileTitleBar("传输列表");
    mBinding.vpTransmitFile.setAdapter(new TransmitMainActivity.MainLockPagerAdapter(getSupportFragmentManager()));
    mBinding.vpTransmitFile.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tlTransmitTab));
    mBinding.tlTransmitTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.vpTransmitFile));
    mBinding.vpTransmitFile.setCurrentItem(0);

    mTitleBar.setOnSambaBackListener(new TitleBar.OnSambaBackClickListener() {
      @Override
      public void onSambaBackClick() {
        System.out.println("TransmitMainActivity onSambaBackClick");
        SambaUtils.mUserPause = true;
        makeAllTaskWaiting();
        finish();
      }
    });
  }

  private class MainLockPagerAdapter extends FragmentPagerAdapter {

    public MainLockPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return DownloadFragment.newInstance();
        case 1:
          return UploadFragment.newInstance();
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tlTransmitTab.getTabCount();
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    SambaUtils.mUserPause = true;
    System.out.println("TransmitMainActivity onBackPressed");
    makeAllTaskWaiting();
  }

  private void makeAllTaskWaiting(){

    Map<String, Downloader> uploaders = AndroidApplication.getUploaders();

    Map<String, Downloader> downloaders = AndroidApplication.getDownloaders();

    List<String> mUploadPaths = new ArrayList<>();
    List<String> mDownloadPaths = new ArrayList<>();


    if (uploaders.size() > 0) {
      Iterator it = uploaders.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        try {
          String key;
          key = (String) it.next();
          mUploadPaths.add(key);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    if (downloaders.size() > 0) {
      Iterator it = downloaders.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        try {
          String key;
          key = (String) it.next();
          mDownloadPaths.add(key);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    for (int i = 0; i < mUploadPaths.size(); i++) {
      String urlstr = mUploadPaths.get(i);

      if (uploaders.get(urlstr).getState() == Downloader.PAUSE) continue;

      SambaUtils.waitingDownload(uploaders.get(urlstr).getUrlstr(), uploaders);

      if (uploaders.get(urlstr) != null) {
        System.out.println("上传操作中 暂停 urlstr = " + urlstr);
        uploaders.get(urlstr).setState(Downloader.WAITING);
      }
    }

    for (int i = 0; i < mDownloadPaths.size(); i++) {
      String urlstr = mDownloadPaths.get(i);

      if (downloaders.get(urlstr).getState() == Downloader.PAUSE) continue;

      SambaUtils.waitingDownload(downloaders.get(urlstr).getUrlstr(), downloaders);

      if (downloaders.get(urlstr) != null) {
        System.out.println("上传操作中 暂停 urlstr = " + urlstr);
        downloaders.get(urlstr).setState(Downloader.WAITING);
      }
    }

    SambaUtils.mUploadingIndex = 0;

    SambaUtils.mDownloadingIndex = 0;

  }
}
