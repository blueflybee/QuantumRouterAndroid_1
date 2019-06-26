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

package com.fernandocejas.android10.sample.presentation.view.device.camera.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCameraReplyBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.NotifyPictureSystemUtil;
import com.fernandocejas.android10.sample.presentation.view.device.camera.util.VideoPlayScaleView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 摄像机 回放 主页
 *
 * @param
 * @return
 */
public class CameraReplyActivity extends BaseCatActivity {
  private ActivityCameraReplyBinding mBinding;
  SurfaceHolder surfaceHolder;//
  private boolean isConnected = false;//是否已连接
  private int window = 0;
  private TextView linkState;//视频连接状态
  private String group = "";
  private int ystNo = 0;
  private Boolean isPaused = false;// 是否已暂停

  private int REQUEST_VIDEO_HISTORY_PLAY = 0x33;
  private String mCurrentUrl = "";//当前url
  private static final String TAG = "CatEyeMainActivity";

  private int result = 0;
  private int totalProgress;// 总进度
  private int currentProgress = 0;// 当前进度
  private int seekProgress;// 手动进度

  //是否正在监听
  private boolean enableSound = false;
  //是否正在录像
  private boolean recording = false;
  private Boolean isPortrait = true; // 判断横竖屏
  private Handler mHandler = new Handler();

  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      reconnectEvent();
    }
  };

  @Override
  protected void initSettings() {

    group = CommonUtil.getGroup(GlobleConstant.getgCameraId());// "C200474135"
    ystNo = CommonUtil.getYST(GlobleConstant.getgCameraId());

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_reply);

    surfaceHolder = ((SurfaceView) findViewById(R.id.sv_surfaceView)).getHolder();
    linkState = ((TextView) findViewById(R.id.tv_linkState));

    mCurrentUrl = getAcBuffStr();


    surfaceHolder.addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        connectVideo(mCurrentUrl);
        JniUtil.resumeSurface(window, holder.getSurface());
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
        JniUtil.pauseSurface(window);
      }
    });
    mBinding.timeNumberLayout.bindRuler(mBinding.timeRuler);

    mBinding.timeRuler.setEnable(AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType()) ? true : false);

    //拖到seekbar滚动条
    mBinding.seekVideo.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

  }

  private void connectVideo(String url){
    linkState.setText(R.string.connecting);
    boolean enable = SovUtil.enableRemotePlay(window, true);
    if (enable) {
      if (null != url && !"".equalsIgnoreCase(url)) {
        System.out.println("connect url = " + url);
        SovUtil.startStreamRemotePlay(window, url);
      }
    }
  }

  /**
   * 重连机制
   *
   * @param
   * @return
   */
  private void reconnectEvent() {
    if (!isConnected) {
      isConnected = false;
      SovUtil.disconnect(window);
      System.out.println("catEye: 启动重连……");
      result = SovUtil.connect(group, ystNo, "admin", GlobleConstant.getgCatPwd(), false, surfaceHolder.getSurface());
//
//      if (result > 0) {
//        linkState.setText("");
//        //3s之后如果刷不出视频
//        if(mHandler != null){
//          if(mRunnable != null){
//            mHandler.postDelayed(mRunnable,2000);
//          }
//        }
//      } else {
//        reconnectEvent();
//      }
    }
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {
    SovUtil.enableRemotePlay(window, false);
  }

  /**
   * 开启声音
   *
   * @param
   * @return
   */
  public void onVoiceClick(View view) {
    Boolean isVoiceStarted = false;
    Boolean isVoiceClosed = false;

    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    if (enableSound) {
      enableSound = false;
      isVoiceClosed = JniUtil.closeSound(window);
      if (isVoiceClosed) {
        Toast.makeText(this, "关闭声音", Toast.LENGTH_SHORT).show();
        mBinding.imgVoice.setBackground(getResources().getDrawable(R.drawable.ic_voice_normal));
      }
    } else {
      enableSound = true;
      isVoiceStarted = JniUtil.openSound(window);
      if (isVoiceStarted) {
        Toast.makeText(this, "开启声音", Toast.LENGTH_SHORT).show();
        mBinding.imgVoice.setBackground(getResources().getDrawable(R.drawable.ic_voice_pressed));
      }
    }
  }

  /**
   * 录制视频
   *
   * @param
   * @return
   */
  public void onVideoClick(View view) {

    Boolean isRecordStart = false;
    Boolean isRecordEnd = false;

    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    if (recording) {
      isRecordEnd = JniUtil.stopRecord(window);

      if (isRecordEnd) {
        Toast.makeText(this, "停止录像 成功", Toast.LENGTH_SHORT).show();
        mBinding.imgVideo.setBackground(getResources().getDrawable(R.drawable.ic_video_normal));

        //通知系统图库有视频更新
        NotifyPictureSystemUtil.sendNotifyToPictureSystem(this, AppConsts.VIDEO_PATH);
      } else {
        Toast.makeText(this, "停止录像 失败", Toast.LENGTH_SHORT).show();
        mBinding.imgVideo.setBackground(getResources().getDrawable(R.drawable.ic_video_normal));
      }

      recording = false;
    } else {
      isRecordStart = JniUtil.startRecord(window);

      if (isRecordStart) {
        Toast.makeText(this, "开启录像 成功", Toast.LENGTH_SHORT).show();
        mBinding.imgVideo.setBackground(getResources().getDrawable(R.drawable.ic_video_pressed));
      } else {
        Toast.makeText(this, "开启录像 失败", Toast.LENGTH_SHORT).show();
        mBinding.imgVideo.setBackground(getResources().getDrawable(R.drawable.ic_video_normal));
      }
      recording = true;
    }
  }

  /**
   * 截图
   *
   * @param
   * @return
   */
  public void onPicClick(View view) {
    Boolean isCaptureSuccessed = false;
    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    try {
      mBinding.imgPic.setBackground(getResources().getDrawable(R.drawable.ic_pic_pressed));

      isCaptureSuccessed = JniUtil.capture(window);

      if (isCaptureSuccessed) {
        Toast.makeText(this, "截图成功", Toast.LENGTH_SHORT).show();
        mBinding.imgPic.setBackground(getResources().getDrawable(R.drawable.ic_pic_normal));

        //通知系统图库有相片更新
        NotifyPictureSystemUtil.sendNotifyToPictureSystem(this, AppConsts.CAPTURE_PATH);

      } else {
        Toast.makeText(this, "截图失败", Toast.LENGTH_SHORT).show();
        mBinding.imgPic.setBackground(getResources().getDrawable(R.drawable.ic_pic_normal));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 屏幕切换
   *
   * @param
   * @return
   */
  public void onScreenAlmostClick(View view) {

    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    //横竖屏切换
    if (isPortrait) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

  }

  public void onCatSettingClick(View view) {
    //因为有解绑入口在设置模块，故不作限制
    Intent intent = new Intent(this, CameraSettingActivity.class);
    intent.putExtra("isConnected", isConnected);
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    SovUtil.enableStreamCatVideoData(window, true);
    System.out.println("catEye: enableStreamCatVideoData = true");
    super.onResume();
  }

  @Override
  protected void onPause() {
    if (isConnected) {
      SovUtil.enableStreamCatVideoData(window, false);
      System.out.println("catEye: enableStreamCatVideoData = false");
      JniUtil.pauseSurface(window);
    }
    SovUtil.disconnect(window);
    isConnected = false;

    removeHandler();

    super.onPause();
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    DialogUtil.hideProgress();

    Log.e(TAG,"onHandler,what"+what+";arg1="+arg1+";arg2="+arg2+";obj="+obj);
    switch (what) {
      //连接结果,视频异常断开时关闭此界面
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        if (arg2 != JVNetConst.CCONNECTTYPE_CONNOK) {
          this.finish();
        }
        break;
      }
      //出图
      case JVNetConst.CALL_NEW_PICTURE: {
        linkState.setVisibility(View.GONE);
        break;
      }
      // 远程回放数据
      case JVNetConst.CALL_PLAY_DATA: {
        switch (arg2) {
          case JVNetConst.JVN_DATA_O: {
            if (0 == totalProgress) {
              try {
                JSONObject jobj;
                jobj = new JSONObject(obj.toString());
                if (null != jobj) {
                  totalProgress = jobj.optInt("total");
                  mBinding.seekVideo.setMax(totalProgress);
                }
              } catch (JSONException e) {
                e.printStackTrace();
              }
            }
            break;
          }
          case JVNetConst.JVN_DATA_I:
          case JVNetConst.JVN_DATA_B:
          case JVNetConst.JVN_DATA_P: {
            currentProgress++;
            mBinding.seekVideo.setProgress(currentProgress);
            break;
          }
          //远程回放结束回调
          case JVNetConst.REMOTE_PLAY_CMD_OVER: {
            Toast.makeText(CameraReplyActivity.this,"远程回放结束",Toast.LENGTH_LONG).show();
            backMethod();
            break;
          }
          //远程回放出错
          case JVNetConst.REMOTE_PLAY_CMD_PLAYERROR: {
            Toast.makeText(CameraReplyActivity.this,"远程回放出错",Toast.LENGTH_LONG).show();
            backMethod();
            break;
          }
          //远程回放超时
          case JVNetConst.REMOTE_PLAY_CMD_PLTIMEOUT: {
            Toast.makeText(CameraReplyActivity.this,"远程回放超时",Toast.LENGTH_LONG).show();
            backMethod();
            break;
          }

        }

        break;
      }

      default:
        break;
    }

  }

  @Override
  public void onBackPressed() {

    SovUtil.stopStreamRemotePlay(window);
    SovUtil.enableRemotePlay(window,false);

    if (recording) {
      JniUtil.stopRecord(window);
      Toast.makeText(this, "停止录像", Toast.LENGTH_SHORT).show();
      recording = false;
    }
    if (enableSound) {
      enableSound = false;
      Toast.makeText(this, "关闭声音", Toast.LENGTH_SHORT).show();
    }

    if (isPortrait) {
      //竖屏
      SovUtil.disconnect(window);
      finish();
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  /************************************** 基本功能 ***********************************************/

  @Override
  protected void onDestroy() {
    removeHandler();
    super.onDestroy();
  }

  /**
   * 检测屏幕切换
   *
   * @param
   * @return
   */

  private void removeHandler() {
    if (mHandler != null) {
      if (mRunnable != null) {
        mHandler.removeCallbacks(mRunnable);
      }
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      // 横屏
      isPortrait = false;
      mBinding.view.setVisibility(View.GONE);

      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 95); // 88/2
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
      mBinding.rlOperate.setLayoutParams(lp);

      RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
      lp1.addRule(RelativeLayout.BELOW, R.id.title_bar);
      lp1.addRule(RelativeLayout.ABOVE, R.id.rl_operate);
      mBinding.rlScreenSurface.setLayoutParams(lp1);

    } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      // 竖屏
      isPortrait = true;
      mBinding.view.setVisibility(View.VISIBLE);

      RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,95);
      lp1.addRule(RelativeLayout.ABOVE,R.id.rl_reply_time_choose);
      lp1.setMargins(0,0,0,130);
      mBinding.rlOperate.setLayoutParams(lp1);

      RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT); // 400/2
      lp2.addRule(RelativeLayout.BELOW, R.id.title_bar);
      lp2.addRule(RelativeLayout.ABOVE, R.id.rl_operate);
      mBinding.rlScreenSurface.setLayoutParams(lp2);

    }
  }

  /**
   * 视频回放
   *
   * @param
   * @return
   */
  public void onVideoReplyClick(View view) {

    if (isPaused) {
      isPaused = false;
      mBinding.imgVideoReply.setBackground(getResources().getDrawable(R.drawable.ic_replay_stop));
      // 继续播放视频
      SovUtil.goonStreamCatRemotePlay(window);
    } else {
      isPaused = true;
      // 暂停视频
      SovUtil.pauseStreamCatRemotePlay(window);
      mBinding.imgVideoReply.setBackground(getResources().getDrawable(R.drawable.ic_sx_replay));
    }

  }

  /**
   * 标题栏 返回键
   *
   * @param
   * @return
   */
  public void onTitleBarBackClick(View view) {
    if (isPortrait) {
      finish();
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  /**
   * 视频回放历史
   *
   * @param
   * @return
   */
  public void onVideoHistoryClick(View view) {

    Intent intent = new Intent(CameraReplyActivity.this, VideoReplyActivity.class);
    intent.putExtra("TimeChoose", "2018-03-28");
    startActivityForResult(intent,REQUEST_VIDEO_HISTORY_PLAY);

  }

  private String getVideoTime() {
    String chooseTime = getIntent().getStringExtra("VideoTime");
    if(TextUtils.isEmpty(chooseTime)){
      chooseTime = getTimeNow();
    }
    return chooseTime;
  }

  private String getAcBuffStr() {
    return getIntent().getStringExtra("acBuffStr");
  }

  public void backMethod(){
    SovUtil.stopStreamRemotePlay(window);
    SovUtil.enableRemotePlay(window,false);
    this.finish();
  }

  /**
   * 远程回放进度条拖动事件
   */
  SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
      seekProgress = arg1;
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
      currentProgress = seekProgress;
      SovUtil.streamSeekTo(window, seekProgress);
    }
  };

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(data == null){
      return;
    }

    if(requestCode == REQUEST_VIDEO_HISTORY_PLAY){
      mCurrentUrl = data.getStringExtra("acBuffStr");
    }

  }

  private String getTimeNow(){
    // "yyyy-MM-dd HH:mm:ss:SS"
    return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
  }
}


