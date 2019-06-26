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
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCameraMainBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.NotifyPictureSystemUtil;
import com.fernandocejas.android10.sample.presentation.view.device.camera.util.CameraPopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UploadDevicePwdPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UploadDevicePwdView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.StreamFile;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

/**
 * 摄像机主页
 *
 * @param
 * @return
 */
public class CameraMainActivity extends BaseCatActivity implements View.OnClickListener,UploadDevicePwdView {
  private ActivityCameraMainBinding mBinding;
  SurfaceHolder surfaceHolder;//
  private boolean isConnected = false;//是否已连接
  private int window = 0;
  private TextView linkState;//视频连接状态
  private String group = "";
  private int ystNo = 0;

  private int osSpeedCmd = 0x01;

  private int ptz_speed = 0;  //云台转到速度 云台速度速度范围3-255，速度设置后转动一下云台就能保存，不用单独保存速度

  private static final String TAG = "CameraMainActivity";
  private Boolean isStreamLayoutVisiablle = false;
  private CameraPopupWindow mPopWin;

  private ArrayList<StreamFile> videoList;

  private int year;
  private int month;
  private int day;

  //是否正在监听
  private boolean enableSound = false;
  //是否正在录像
  private boolean recording = false;
  //是否正在对讲
  private boolean doubleCalling = false;
  //当前码流 1：超清  2：高清  3：流畅
  private int currentMobileQuality = 0;
  private Boolean isPortrait = true; // 判断横竖屏
  private Boolean isMoveVisiable = false;
  private int randPwd = 000000;

  @Inject
  UploadDevicePwdPresenter uploadPwdPresenter;

  @Override
  protected void initSettings() {

    group = CommonUtil.getGroup(GlobleConstant.getgCameraId());// "C200474135"
    ystNo = CommonUtil.getYST(GlobleConstant.getgCameraId());
    System.out.println("group = " + group);
    System.out.println("ystNo = " + ystNo);

    System.out.println("pwd = "+GlobleConstant.getgCameraPwd());


  }

  private void initPresenter() {
    uploadPwdPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_main);

    mBinding.rlLeftArrow.setOnClickListener(this);
    mBinding.rlLeftArrow.setOnTouchListener(new CameraMainActivity.LongClickListener());
    mBinding.rlUpArrow.setOnClickListener(this);
    mBinding.rlUpArrow.setOnTouchListener(new CameraMainActivity.LongClickListener());
    mBinding.rlRightArrow.setOnClickListener(this);
    mBinding.rlRightArrow.setOnTouchListener(new CameraMainActivity.LongClickListener());
    mBinding.rlDownArrow.setOnClickListener(this);
    mBinding.rlDownArrow.setOnTouchListener(new CameraMainActivity.LongClickListener());

    surfaceHolder = ((SurfaceView) findViewById(R.id.sv_surfaceView)).getHolder();
    linkState = ((TextView) findViewById(R.id.tv_linkState));

    videoList = new ArrayList<>();

    surfaceHolder.addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        if (isConnected) {
          JniUtil.resumeSurface(window, surfaceHolder.getSurface());
        } else {
          linkState.setText(R.string.connecting);
          int result = SovUtil.connect(group, ystNo, "admin", GlobleConstant.getgCameraPwd(), false, surfaceHolder.getSurface());
          if (result > 0) {
            linkState.setText("调用连接成功");
          } else {
            linkState.setText("连接失败,连接错误值=" + result);
          }
        }
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {

      }
    });

    initializeInjector();

    initPresenter();

  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

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
        NotifyPictureSystemUtil.sendNotifyToPictureSystem(this,AppConsts.VIDEO_PATH);
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

      isCaptureSuccessed  = JniUtil.capture(window);

      if (isCaptureSuccessed) {
        Toast.makeText(this, "截图成功", Toast.LENGTH_SHORT).show();
        mBinding.imgPic.setBackground(getResources().getDrawable(R.drawable.ic_pic_normal));

        //通知系统图库有相片更新
        NotifyPictureSystemUtil.sendNotifyToPictureSystem(this,AppConsts.CAPTURE_PATH);

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

  /**
   * 对讲功能
   *
   * @param
   * @return
   */
  public void onCatStartVoiceClick(View view) {
    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    mBinding.imgStartVoiceTalk.setEnabled(false);
    mBinding.imgStartVoiceTalk.setClickable(false);

    DialogUtil.showProgress(this);

    if (doubleCalling) {
      stopDoubleCall();
    } else {
      startDoubleCall();
    }
  }

  public void onCatSettingClick(View view) {
    //因为有解绑入口在设置模块，故不作限制
    Intent intent = new Intent(this,CameraSettingActivity.class);
    intent.putExtra("isConnected",isConnected);
    startActivity(intent);
  }

  @Override
  protected void onResume() {
    SovUtil.enableStreamCatVideoData(window, true);
    super.onResume();
  }

  @Override
  protected void onPause() {
    if (isConnected) {
      SovUtil.enableStreamCatVideoData(window, false);
      JniUtil.pauseSurface(window);
    }
    super.onPause();
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    DialogUtil.hideProgress();
    System.out.println("catEye: onHandler what=" + what);
    switch (what) {
      //连接结果
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        switch (arg2) {
          //连接成功
          case JVNetConst.CCONNECTTYPE_CONNOK: {
            linkState.setText("连接成功，正在接收数据");

            break;
          }
          //连接异常断开
          case JVNetConst.CCONNECTTYPE_DISCONNE: {
            isConnected = false;
            SovUtil.disconnect(window);
            linkState.setText("连接异常断开,错误码:arg1=" + arg1 + ";arg2=" + arg2);
            break;
          }
          //其他都是连接失败
          default: {
            linkState.setText(getResources().getString(R.string.connect_failed) + ",错误码=" + arg2);
            break;
          }

        }

        break;
      }
      case JVNetConst.CALL_NORMAL_DATA:{
        linkState.setText("正在缓冲中...");
        break;
      }
      //视频连接出图
      case JVNetConst.CALL_NEW_PICTURE: {
        isConnected = true;
        linkState.setVisibility(View.GONE);

        //获取所有配置信息
        SovUtil.getStreamCatDataAll(window);
        DialogUtil.hideProgress();

        //视频连接成功后判断是否有密码 没有的则生成一个6位随机码
        if(TextUtils.isEmpty(GlobleConstant.getgCameraPwd())){
          //生成一个6位的随机码 上报服务器
          randPwd = (int)((Math.random()*9+1)*100000);
          GlobleConstant.setgCameraPwd(randPwd+"");
          //上报服务器
          uploadPwdPresenter.uploadDevicePwd(GlobleConstant.getgCameraId(),GlobleConstant.getgCameraPwd());
        }

        break;
      }
      //设置协议回调
      case JVNetConst.CALL_CATEYE_SENDDATA: {
        try {
          JSONObject object = new JSONObject(obj.toString());
          int nType = object.getInt("nPacketType");
          int nCmd = object.getInt("nCmd");
          Log.e(TAG, "nPacketType=" + nType + "；nCmd=" + nCmd + "；obj=" + obj.toString());
          switch (nCmd) {
            //配置信息全的回调
            case JVNetConst.SRC_PARAM_ALL: {
              try {
                String jsonStrAll = obj.toString();
                String dataStr = object.getString(AppConsts.TAG_DATA);
                Log.e("CloudSet", "jsonStrAll=" + jsonStrAll);
                com.alibaba.fastjson.JSONObject allParamObject = com.alibaba.fastjson.JSONObject.parseObject(dataStr);

                int nChatMode = allParamObject.containsKey(AppConsts.TAG_STREAM_CAT_CHATMODE) ?
                    allParamObject.getInteger(AppConsts.TAG_STREAM_CAT_CHATMODE) : 2;
                int ptzSupport = allParamObject.containsKey(AppConsts.TAG_PTZ_SUPPORT) ?
                    allParamObject.getInteger(AppConsts.TAG_PTZ_SUPPORT) : 1;
                ptz_speed = allParamObject.containsKey(AppConsts.TAG_PTZ_SPEED) ?
                    allParamObject.getInteger(AppConsts.TAG_PTZ_SPEED) : 0;

                Log.e("CloudSet", "ptzSupport=" + ptzSupport);

                // 获取当前码流 1：高清 2：标清 3：流畅
                currentMobileQuality = allParamObject.getInteger(AppConsts.TAG_STREAM_CAT_MOBILEQUALITY);

                refreshVideoStreamUI(currentMobileQuality, false);

              } catch (Exception e) {
                e.printStackTrace();
              }
              break;
            }

            //检索视频文件
            case JVNetConst.SRC_REMOTE_CHECK: {
              com.alibaba.fastjson.JSONObject root = JSON.parseObject(obj.toString());
              int nPacketType = root.getInteger(AppConsts.TAG_NPACKETTYPE);
              String dat = root.getString(AppConsts.TAG_DATA);
              switch (nPacketType) {
                case JVNetConst.SRC_EX_CHECK_VIDEO:
                  //视频回放数据
                  videoList.clear();
                  videoList.addAll(SovUtil.getStreamIPCFileList(dat, getTimeNow()));
                  String acBuffStr = "";
                  if(videoList != null && videoList.size() != 0){
                    //默认获取第一个数据
                    StreamFile videoFile = videoList.get(0);
                    acBuffStr = videoFile.getFilePath();
                    Log.v(TAG, "onItemClickListener--acBuffStr:" + acBuffStr);
                  }

                  //回放视频 连接中
                  Intent intent = new Intent(CameraMainActivity.this, CameraReplyActivity.class);
                  intent.putExtra("acBuffStr", acBuffStr);
                  intent.putExtra("VideoTime",getTimeNow());
                  startActivity(intent);

                  break;
              }
              break;
            }

            //1.报警设置相关回调
            case JVNetConst.SRC_INTELLIGENCE: {

              switch (nType) {

                case JVNetConst.SRC_EX_INTELLIGENCE_MDETECT_SENS://移动侦测灵敏度

                  int result = object.getInt("result");

                  if (result == 1) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    //获取所有配置信息
                    SovUtil.getStreamCatDataAll(window);
                  } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                  }

                  System.out.println("result111 = " + result);
                  break;

                default:
                  break;
              }
              break;
            }

            //码流切换回调
            case JVNetConst.SRC_REMOTE_CMD: {
              int result = object.getInt("result");
              if (1 == result) {
                if (nType == JVNetConst.SRC_EX_CMD_GET_STORAGE) {
                } else {
                  isStreamLayoutVisiablle = false;
                  mBinding.llStreamState.setVisibility(View.GONE);
                  Toast.makeText(this, "码流切换成功", Toast.LENGTH_SHORT).show();
                }

              }
              break;
            }

            //对讲回调
            case JVNetConst.SRC_CHAT: {
              int result = object.getInt("result");

              mBinding.imgStartVoiceTalk.setEnabled(true);
              mBinding.imgStartVoiceTalk.setClickable(true);

              if (nType == JVNetConst.SRC_EX_START_CHAT) {//开启对讲
                if (result == 1) {//成功
                  //Toast.makeText(this, "主控同意对讲了！", Toast.LENGTH_SHORT).show();
                  Log.e("CATCAT", "开启对讲--成功：主控同意对讲了！开始发送音频");
                  JniUtil.startRecordSendAudio(window);
//                                    JniUtil.resumeAudio(connectIndex);
                  doubleCalling = true;
                  if(isPortrait){
                    mBinding.imgStartVoiceTalk.setBackground(getResources().getDrawable(R.drawable.ic_end_intercom));
                  }else {
                    findViewById(R.id.img_start_voice_talk_land).setBackground(getResources().getDrawable(R.drawable.ic_end_intercom));
                  }

                  Toast.makeText(this, "开启对讲", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {//失败
                  Log.e("CATCAT", "开启对讲--失败");
                  Toast.makeText(this, "当前设备已和另一个手机开启对讲，请稍候再试", Toast.LENGTH_SHORT).show();

                  if(isPortrait){
                    mBinding.imgStartVoiceTalk.setBackground(getResources().getDrawable(R.drawable.ic_begin_intercom));
                  }else {
                    findViewById(R.id.img_start_voice_talk_land).setBackground(getResources().getDrawable(R.drawable.ic_begin_intercom));
                  }

                }
              } else if (nType == JVNetConst.SRC_EX_STOP_CHAT) {//结束对讲

                if (result == 1) {//成功
                  Log.e("CATCAT", "关闭对讲--成功");
                  //Toast.makeText(this, "收到对讲结束回调！", Toast.LENGTH_SHORT).show();

                  if (doubleCalling) {//正在对讲，停止对讲
                    Log.e("CATCAT", "正在对讲_结束对讲");
                    Toast.makeText(this, "结束对讲", Toast.LENGTH_SHORT).show();

                    if(isPortrait){
                      mBinding.imgStartVoiceTalk.setBackground(getResources().getDrawable(R.drawable.ic_begin_intercom));
                    }else {
                      findViewById(R.id.img_start_voice_talk_land).setBackground(getResources().getDrawable(R.drawable.ic_begin_intercom));
                    }

                  } else {//设备已在其他客户端开启对讲
                    Log.e("CATCAT", "设备已在其他客户端开启对讲");
                    JniUtil.closeSound(window);
                    Toast.makeText(this, "当前设备已和另一个手机开启对讲，请稍候再试", Toast.LENGTH_SHORT).show();
                  }
                  doubleCalling = false;
                } else if (result == 0) {//失败
                  Log.e("CATCAT", "关闭对讲--失败");
                  Toast.makeText(this, "结束对讲 失败", Toast.LENGTH_SHORT).show();
                }
              }
              break;
            }
            default: {
              break;
            }
          }


        } catch (JSONException e) {
          e.printStackTrace();
        }
        break;
      }


      default:
        break;
    }
  }

  @Override
  public void onBackPressed() {

    backEvent();

    if(isPortrait){
      //竖屏
      SovUtil.disconnect(window);
      finish();
    }else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  private void backEvent(){
    if (doubleCalling) {
      stopDoubleCall();
      Toast.makeText(this, "关闭对讲", Toast.LENGTH_SHORT).show();
    }
    if (recording) {
      JniUtil.stopRecord(window);
      Toast.makeText(this, "停止录像", Toast.LENGTH_SHORT).show();
      recording = false;
    }
    if (enableSound) {
      enableSound = false;
      Toast.makeText(this, "关闭声音", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  /************************************** 基本功能 ***********************************************/

  //开启对讲
  public void startDoubleCall() {
    SovUtil.startStreamVoiceCall(window);
    JniUtil.openSound(window);

  }

  //关闭对讲
  public void stopDoubleCall() {
    SovUtil.stopStreamVoiceCall(window);
    JniUtil.stopRecordSendAudio(window);

    if(enableSound){
      //此时声音已开启
    }else {
      JniUtil.closeSound(window);
    }
  }

  /**
   * 码流切换
   *
   * @param
   * @return
   */
  public void onStreamClick(View view) {
    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    if (isStreamLayoutVisiablle) {
      isStreamLayoutVisiablle = false;
      mBinding.llStreamState.setVisibility(View.GONE);

    } else {
      isStreamLayoutVisiablle = true;

      mBinding.llStreamState.setVisibility(View.VISIBLE);
      refreshVideoStreamUI(currentMobileQuality, true);

      mBinding.tvStreamStateSuper.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //超清
          SovUtil.streamCatChangeStream(window, 1);
          DialogUtil.showProgress(CameraMainActivity.this);
        }
      });

      mBinding.tvStreamStateCommon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //标清
          SovUtil.streamCatChangeStream(window, 2);
          DialogUtil.showProgress(CameraMainActivity.this);
        }
      });

      mBinding.tvStreamStateLow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //流畅
          SovUtil.streamCatChangeStream(window, 3);
          DialogUtil.showProgress(CameraMainActivity.this);
        }
      });

    }
  }

  private void refreshVideoStreamUI(int flag, Boolean isClick) {
    // 获取当前码流 1：高清 2：标清 3：流畅
    if (isClick) {
      if (flag == 1) {
        mBinding.tvStreamStateSuper.setTextColor(getResources().getColor(R.color.blue_2196f3));
        mBinding.tvStreamStateCommon.setTextColor(getResources().getColor(R.color.white));
        mBinding.tvStreamStateLow.setTextColor(getResources().getColor(R.color.white));
      } else if (flag == 2) {
        mBinding.tvStreamStateSuper.setTextColor(getResources().getColor(R.color.white));
        mBinding.tvStreamStateCommon.setTextColor(getResources().getColor(R.color.blue_2196f3));
        mBinding.tvStreamStateLow.setTextColor(getResources().getColor(R.color.white));
      } else if (flag == 3) {
        mBinding.tvStreamStateSuper.setTextColor(getResources().getColor(R.color.white));
        mBinding.tvStreamStateCommon.setTextColor(getResources().getColor(R.color.white));
        mBinding.tvStreamStateLow.setTextColor(getResources().getColor(R.color.blue_2196f3));
      }

    } else {
      if (flag == 1) {
        mBinding.imgVideoStream.setBackground(getResources().getDrawable(R.drawable.ic_achaoqing));
      } else if (flag == 2) {
        mBinding.imgVideoStream.setBackground(getResources().getDrawable(R.drawable.ic_agaoqing));
      } else if (flag == 3) {
        mBinding.imgVideoStream.setBackground(getResources().getDrawable(R.drawable.ic_aliuchang));
      }
    }

  }

  @Override
  protected void onDestroy() {
    JniUtil.pauseSurface(window);
    super.onDestroy();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
      // 横屏
      isPortrait = false;
      mBinding.view.setVisibility(View.GONE);
      mBinding.rlVoiceTalk.setVisibility(View.GONE);
      mBinding.imgStartVoiceTalkLand.setVisibility(View.VISIBLE);

      RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,95); // 88/2
      lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
      mBinding.rlOperate.setLayoutParams(lp);

      RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
      lp1.addRule(RelativeLayout.BELOW,R.id.title_bar);
      lp1.addRule(RelativeLayout.ABOVE,R.id.rl_operate);
      mBinding.rlScreenSurface.setLayoutParams(lp1);

    }else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
      // 竖屏
      isPortrait = true;
      mBinding.view.setVisibility(View.VISIBLE);
      mBinding.rlVoiceTalk.setVisibility(View.VISIBLE);
      mBinding.imgStartVoiceTalkLand.setVisibility(View.GONE);

      RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,95);
      lp1.addRule(RelativeLayout.ABOVE,R.id.rl_voice_talk);
      lp1.setMargins(0,0,0,130);
      mBinding.rlOperate.setLayoutParams(lp1);

      RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT); // 400/2
      lp2.addRule(RelativeLayout.BELOW,R.id.title_bar);
      lp2.addRule(RelativeLayout.ABOVE,R.id.rl_operate);
      mBinding.rlScreenSurface.setLayoutParams(lp2);

    }
  }

  /**
  * 移动
  *
  * @param
  * @return
  */
  public void onCameraMoveClick(View view) {

    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    if(isMoveVisiable){
      isMoveVisiable = false;
      mBinding.rlMovePositon.setVisibility(View.GONE);
      mBinding.imgStartVoiceTalk.setVisibility(View.VISIBLE);

      mBinding.tvCameraMove.setText("语音");

      Drawable drawable = getResources().getDrawable(R.drawable.ic_sx_voice);
      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
      mBinding.tvCameraMove.setCompoundDrawables(null, drawable, null, null);
    }else {
      isMoveVisiable = true;
      mBinding.imgStartVoiceTalk.setVisibility(View.GONE);
      mBinding.rlMovePositon.setVisibility(View.VISIBLE);

      mBinding.tvCameraMove.setText("位移");

      Drawable drawable = getResources().getDrawable(R.drawable.ic_sx_move);
      drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
      mBinding.tvCameraMove.setCompoundDrawables(null, drawable, null, null);
    }
  }

  /**
  * 视频回放
  *
  * @param
  * @return
  */
  public void onVideoReplyClick(View view) {
    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    searchRemoteData();

  }

  @Override
  public void onClick(View v) {

  }

  public void onMoveCenterClick(View view) {
    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    mPopWin = new CameraPopupWindow(this,"2",ptz_speed);
    mPopWin.setFocusable(true);
    mPopWin.showAtLocation(mPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

    mPopWin.setOnPositiveClickListener(new CameraPopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int progress,View dialogPos) {

        if (isConnected) {
          boolean isSuccess = SovUtil.setPtz(window, osSpeedCmd,
              String.format(AppConsts.FORMAT_PTZ_SPEED, progress));

          if(isSuccess){

            ptz_speed = progress;

            SovUtil.setPtz(window, JVNetConst.SRC_PTZ_STOP,
                String.format(AppConsts.FORMAT_PTZ_SPEED, ptz_speed));

            Toast.makeText(CameraMainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
          }
        }
      }
    });

  }

  /**
  * 标题栏 返回键
  *
  * @param
  * @return
  */
  public void onTitleBarBackClick(View view) {

    backEvent();

    if(isPortrait){
      //竖屏
      SovUtil.disconnect(window);
      finish();
    }else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  @Override
  public void uploadDevicePwdSuccessed() {
    System.out.println("设备密码: 上传成功 摄像头 = "+GlobleConstant.getgCameraPwd());
  }

  @Override
  public void onStop() {
    super.onStop();
  }

  /**
   * 长按--云台事件
   */
  protected  class LongClickListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      int action = event.getAction();
      int cmd = 0;
      switch (v.getId()) {
        case R.id.rl_upArrow: // up
          cmd = JVNetConst.SRC_PTZ_UP;
          break;
        case R.id.rl_downArrow: // down
          cmd = JVNetConst.SRC_PTZ_DOWN;
          break;
        case R.id.rl_leftArrow: // left
          cmd = JVNetConst.SRC_PTZ_LEFT;
          break;
        case R.id.rl_rightArrow:// right
          cmd = JVNetConst.SRC_PTZ_RIGHT;
          break;
      }

      osSpeedCmd = cmd;

      try {
        if (action == MotionEvent.ACTION_DOWN) {
          if (isConnected) {
            SovUtil.setPtz(window, cmd,
                String.format(AppConsts.FORMAT_PTZ_SPEED, ptz_speed));
          }
        } else if (action == MotionEvent.ACTION_UP) {

          if (isConnected) {
            SovUtil.setPtz(window, JVNetConst.SRC_PTZ_STOP,
                String.format(AppConsts.FORMAT_PTZ_SPEED, ptz_speed));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      return false;
    }
  }

  /**
   * 检索远程回放数据
   */
  public void searchRemoteData() {
    String temStr = getTimeNow();
    String[] temArray = temStr.split("-");
    year = Integer.parseInt(temArray[0]);
    month = Integer.parseInt(temArray[1]);
    day = Integer.parseInt(temArray[2]);

    SovUtil.checkStreamRemoteVideo(window, year, month, day);
  }

  private String getTimeNow(){
    // "yyyy-MM-dd HH:mm:ss:SS"
    return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
  }

}


