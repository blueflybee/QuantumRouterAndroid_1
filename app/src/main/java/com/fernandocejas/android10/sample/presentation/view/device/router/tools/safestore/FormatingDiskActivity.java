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
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFormatingDiskBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.router.model.rsp.QueryDiskStateResponse;

import javax.inject.Inject;

/**
 * 正在格式化
 *
 * @param
 * @return
 */
public class FormatingDiskActivity extends BaseActivity implements IFormatDiskView, IQueryDiskStateView {
  private ActivityFormatingDiskBinding mBinding;
  private Handler mHandler;
  private Runnable mRunnbale;
  private int progress = 0;
  private Boolean isFirstFormat = true;//标志是否第一次格式化

  @Inject
  QueryDiskStatePresenter mQueryDiskStatePresenter;
  @Inject
  FormatDiskPresenter mFormatPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_formating_disk);
    initializeInjector();
    initPresenter();
    initView();
    //保护措施 格式化之前再次查询状态
    queryDiskState();
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mFormatPresenter.setView(this);
    mQueryDiskStatePresenter.setView(this);
  }

  private void initView() {
    mBinding.rlCloseFormat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
       /* Intent intent = new Intent();
        intent.putExtra("DiskData", "1");
        setResult(REQUEST_FORMATING_CODE, intent);
        finish();*/
        mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
      }
    });
  }


  /**
   * 查询磁盘状态
   *
   * @param
   * @return
   */
  private void queryDiskState() {
    mQueryDiskStatePresenter.queryDiskState();
  }

  /**
   * 格式化 磁盘
   *
   * @param
   * @return
   */
  private void formatDisk() {
    mFormatPresenter.formatDisk();
  }


  public void injectFinish(View view) {
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
  }

  public void onKeyInjectProgress(int progress) {
    mBinding.progressLoadKey.setProgress(progress);
    mBinding.tvProgress.setText(progress + "");
  }

  @Override
  public void formatDisk(QueryDiskStateResponse response) {
    //启动定时器去查询磁盘状态
    mBinding.tvState.setText("磁盘正在格式化，请稍候");
    startTimesQueryDiskState();
  }

  @Override
  public void queryDiskState(QueryDiskStateResponse response) {
    if (response.getStatus_errorcode() == 3005) {
      //格式化失败

      if (response.getProcess_statuscode() == 0) {
        //可以使用
        mBinding.tvState.setText("磁盘格式成功");
        removeHandlerTimer();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            //1s之后弹回刚才的界面
          /*  Intent intent = new Intent();
            intent.putExtra("DiskData", "0");//0代表格式化失败  1 成功
            setResult(REQUEST_FORMATING_CODE, intent);
            finish();*/
            mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
          }
        }, 1000);
      } else {
        if (isFirstFormat) {
          isFirstFormat = false;
          formatDisk();
        } else {
          mBinding.tvState.setText("磁盘格式失败");
          removeHandlerTimer();
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              //1s之后弹回刚才的界面
              /*Intent intent = new Intent();
              intent.putExtra("DiskData", "0");//0代表格式化失败  1 成功
              setResult(REQUEST_FORMATING_CODE, intent);
              finish();*/
              mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
            }
          }, 1000);
        }
      }
    } else if (response.getStatus_errorcode() == 3004 || response.getStatus_errorcode() == 3003) {
      //挂载失败
      if (isFirstFormat) {
        isFirstFormat = false;
        formatDisk();
      } else {
        Toast.makeText(this, "磁盘出现异常，请重试", Toast.LENGTH_SHORT).show();
        mBinding.tvState.setText("磁盘格式失败");
        removeHandlerTimer();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            //1s之后弹回刚才的界面
          /*  Intent intent = new Intent();
            intent.putExtra("DiskData", "0");//0代表格式化失败  1 成功
            setResult(REQUEST_FORMATING_CODE, intent);
            finish();*/

            mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
          }
        }, 1000);
      }
    } else {
      if (response.getProcess_statuscode() == 0) {
        //格式化成功
        mBinding.tvState.setText("磁盘格式完成");
        mBinding.tvProgress.setText(100 + "");
        mBinding.progressLoadKey.setProgress(100);
        mBinding.rlDiskProgress.setBackground(getResources().getDrawable(R.drawable.ef_formato));

        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            /*Intent intent = new Intent();
            intent.putExtra("DiskData", "1");
            setResult(REQUEST_FORMATING_CODE, intent);
            finish();*/
            mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
          }
        }, 500);
      } else {
        if (progress <= 100) {
          onKeyInjectProgress(progress++);
        }

        startTimesQueryDiskState();
      }
    }
  }

  @Override
  public void queryDiskStateFailed() {
    /*Toast.makeText(this, "磁盘状态查询失败，请重试", Toast.LENGTH_SHORT).show();*/
    Toast.makeText(this, "未检测到磁盘信息，请重试", Toast.LENGTH_SHORT).show();
    //延时2s
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
      }
    }, 2000);
  }

  @Override
  protected void onDestroy() {
    removeHandlerTimer();
    super.onDestroy();
  }

  @Override
  protected void onPause() {
    removeHandlerTimer();
    super.onPause();
  }

  /**
   * 移除定时器
   *
   * @param
   * @return
   */
  private void removeHandlerTimer() {
    if (mHandler != null) {
      if (mRunnbale != null) {
        mHandler.removeCallbacks(mRunnbale);
      }
    }
  }

  private void startTimesQueryDiskState() {
    mHandler = new Handler();

    mRunnbale = new Runnable() {
      @Override
      public void run() {
        queryDiskState();
        if (progress <= 100) {
          onKeyInjectProgress(progress++);
        }
      }
    };

    mHandler.postDelayed(mRunnbale, 1900);//2s查一次
  }

  @Override
  public void onBackPressed() {
 /*   Intent intent = new Intent();
    intent.putExtra("DiskData", "1");
    setResult(REQUEST_FORMATING_CODE, intent);
    finish();*/
    mNavigator.navigateExistAndClearTop(FormatingDiskActivity.this,RemoteMainActivity.class);
    super.onBackPressed();
  }
}
