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

package com.fernandocejas.android10.sample.presentation.view.device.cateye.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCatEyeMainBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.NotifyPictureSystemUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.CatEyeSettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.DoorBellRecordListActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.QueryCatLockPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.QueryCatLockView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UploadDevicePwdPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UploadDevicePwdView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.RemoteLockOperationPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.RemoteLockOperationView;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

/**
 * CatEyeMainActivity
 */
public class CatEyeMainActivity extends BaseCatActivity implements QueryCatLockView, RemoteLockOperationView, UploadDevicePwdView {
  private ActivityCatEyeMainBinding mBinding;
  SurfaceHolder surfaceHolder;//
  private boolean isConnected = false;//是否已连接
  private int window = 0;
  private TextView linkState;//视频连接状态
  private String group = "";
  private int ystNo = 0;
  private static final String TAG = "CatEyeMainActivity";
  private Boolean isStreamLayoutVisiablle = false;

  //是否正在监听
  private boolean enableSound = false;
  //是否正在录像
  private boolean recording = false;
  //是否正在对讲
  private boolean doubleCalling = false;
  //当前码流 1：超清  2：高清  3：流畅
  private int currentMobileQuality = 0;
  private Boolean isPortrait = true; // 判断横竖屏

  private QueryCatLockResponse mQueryCatLockResponse;

  @Inject
  QueryCatLockPresenter mQueryCatLockPresenter;

  @Inject
  RemoteLockOperationPresenter mRemoteLockOperationPresenter;

  @Inject
  UploadDevicePwdPresenter uploadPwdPresenter;

  @Override
  protected void initSettings() {
    group = CommonUtil.getGroup(GlobleConstant.getgCatEyeId());// "C200474135"
    ystNo = CommonUtil.getYST(GlobleConstant.getgCatEyeId());
  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cat_eye_main);

    surfaceHolder = ((SurfaceView) findViewById(R.id.sv_surfaceView)).getHolder();
    linkState = ((TextView) findViewById(R.id.tv_linkState));

    surfaceHolder.addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
        if (isConnected) {
          JniUtil.resumeSurface(window, surfaceHolder.getSurface());
        } else {
          linkState.setText("连接中...");

          System.out.println("group = " + group);
          System.out.println("ystNo = " + ystNo);
          System.out.println("user = admin");
          String pwd = GlobleConstant.getgCatPwd();
          System.out.println("pwd = " + pwd);
          int result = SovUtil.connect(group, ystNo, "admin", pwd, false, surfaceHolder.getSurface());
          if (result > 0) {
            linkState.setText("连接成功，等待加载");
          } else {
            linkState.setText("连接失败，请检查网络连接并重试(" + result + ")");
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

    mQueryCatLockPresenter.queryCatLock(GlobleConstant.getgCatEyeId());
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mQueryCatLockPresenter.setView(this);
    mRemoteLockOperationPresenter.setView(this);
    uploadPwdPresenter.setView(this);
  }

  @Override
  protected void saveSettings() {
  }

  @Override
  protected void freeMe() {
  }

  /**
   * 开启声音
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

    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    try {
      mBinding.imgPic.setBackground(getResources().getDrawable(R.drawable.ic_pic_pressed));

      Boolean isCaptureSuccessed = JniUtil.capture(window);

      if (isCaptureSuccessed) {
        Toast.makeText(CatEyeMainActivity.this, "截图成功", Toast.LENGTH_SHORT).show();
        mBinding.imgPic.setBackground(getResources().getDrawable(R.drawable.ic_pic_normal));

        //通知系统图库有相片更新
        NotifyPictureSystemUtil.sendNotifyToPictureSystem(this, AppConsts.CAPTURE_PATH);

      } else {
        Toast.makeText(CatEyeMainActivity.this, "截图失败", Toast.LENGTH_SHORT).show();
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

  public void onCatMsgClick(View view) {

    if (!isConnected) {
      Toast.makeText(this, "请等待视频连接成功后再执行此操作", Toast.LENGTH_SHORT).show();
      return;
    }

    startActivity(new Intent(CatEyeMainActivity.this, DoorBellRecordListActivity.class));
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
    Intent intent = new Intent(this, CatEyeSettingActivity.class);
    intent.putExtra("isConnected", isConnected);
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
            linkState.setText("连接异常断开，请重新连接");
            break;
          }
          //其他都是连接失败
          default: {
            linkState.setText("连接失败，请检查网络连接并重试(" + arg2 + ")");
            break;
          }

        }

        break;
      }
      case JVNetConst.CALL_NORMAL_DATA: {
        linkState.setText("正在加载中...");
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
        if (TextUtils.isEmpty(GlobleConstant.getgCatPwd())) {
          //生成一个6位的随机码 上报服务器
          int randPwd = (int) ((Math.random() * 9 + 1) * 100000);
          GlobleConstant.setgCatPwd(randPwd + "");
          //上报服务器
          uploadPwdPresenter.uploadDevicePwd(GlobleConstant.getgCatEyeId(), GlobleConstant.getgCatPwd());
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
                int ptz_speed = allParamObject.containsKey(AppConsts.TAG_PTZ_SPEED) ?
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
                  if (isPortrait) {
                    mBinding.imgStartVoiceTalk.setBackground(getResources().getDrawable(R.drawable.ic_end_intercom));
                  } else {
                    findViewById(R.id.img_start_voice_talk_land).setBackground(getResources().getDrawable(R.drawable.ic_end_intercom));
                  }

                  Toast.makeText(this, "开启对讲", Toast.LENGTH_SHORT).show();
                } else if (result == 0) {//失败
                  Log.e("CATCAT", "开启对讲--失败");
                  Toast.makeText(this, "当前设备已和另一个手机开启对讲，请稍候再试", Toast.LENGTH_SHORT).show();

                  if (isPortrait) {
                    mBinding.imgStartVoiceTalk.setBackground(getResources().getDrawable(R.drawable.ic_begin_intercom));
                  } else {
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

                    if (isPortrait) {
                      mBinding.imgStartVoiceTalk.setBackground(getResources().getDrawable(R.drawable.ic_begin_intercom));
                    } else {
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

    if (isPortrait) {
      //竖屏
      SovUtil.disconnect(window);
      finish();
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  private void backEvent() {
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

    if (enableSound) {
      //此时声音已开启
    } else {
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
          DialogUtil.showProgress(CatEyeMainActivity.this);
        }
      });

      mBinding.tvStreamStateCommon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //标清
          SovUtil.streamCatChangeStream(window, 2);
          DialogUtil.showProgress(CatEyeMainActivity.this);
        }
      });

      mBinding.tvStreamStateLow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //流畅
          SovUtil.streamCatChangeStream(window, 3);
          DialogUtil.showProgress(CatEyeMainActivity.this);
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

  //判断远程开门
  @Override
  public void queryCayLock(QueryCatLockResponse response) {
    //有网关 门锁 并且都有秘钥才展示开门按钮
    Boolean isLockHasKey = false;
    this.mQueryCatLockResponse = response;

    if (TextUtils.isEmpty(response.getDeviceSerialNo())) {
      isLockHasKey = false;
    } else {
      if (KeystoreRepertory.getInstance().has(response.getDeviceSerialNo() + "_" + PrefConstant.getUserUniqueKey("1"))) {
        //网关是否有密钥  //  0是网关，1是门锁，2是猫眼
        isLockHasKey = true;
      } else {
        isLockHasKey = false;
      }
    }

    if (isLockHasKey) {
      mBinding.rlOpenRemoteDoor.setVisibility(View.VISIBLE);

      mBinding.rlOpenRemoteDoor.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //远程开门
          // TODO: 2018/1/19 待实现……
          remoteUnlock();
        }
      });
    } else {
      mBinding.rlOpenRemoteDoor.setVisibility(View.GONE);
    }
  }

  private void remoteUnlock() {
    if (mQueryCatLockResponse == null) return;
    DialogUtil.showLoginPasswordDialog(getContext(), "请输入登录密码用于远程开锁", (view, pwd) -> {
      String userPwd = PrefConstant.getUserPwd();
      String inputPwd = EncryptUtils.encryptMD5ToString(pwd).toLowerCase();
      if (!inputPwd.equals(userPwd)) {
        ToastUtils.showShort("密码错误");
        return;
      }
      showLoading();
      BlePkg pkg = new BlePkg();
      pkg.setHead("aa");
      pkg.setLength("0c");
      pkg.setKeyId(BleMapper.NO_ENCRYPTION);
      pkg.setUserId(PrefConstant.getUserIdInHexString());
      pkg.setSeq("00");
      BleBody body = new BleBody();
      body.setCmd(BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE);
      body.setPayload("");
      pkg.setBody(body);
      mRemoteLockOperationPresenter.remoteOpt(mQueryCatLockResponse.getRouterSerialNo(), mQueryCatLockResponse.getDeviceSerialNo(), pkg);
    });
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);

    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      // 横屏
      isPortrait = false;
      mBinding.view.setVisibility(View.GONE);
      mBinding.rlVoiceTalk.setVisibility(View.GONE);
      mBinding.imgStartVoiceTalkLand.setVisibility(View.VISIBLE);

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
      mBinding.rlVoiceTalk.setVisibility(View.VISIBLE);
      mBinding.imgStartVoiceTalkLand.setVisibility(View.GONE);

      RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 95);
      lp1.addRule(RelativeLayout.ABOVE, R.id.rl_voice_talk);
      lp1.setMargins(0, 0, 0, 130);
      mBinding.rlOperate.setLayoutParams(lp1);

      RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT); // 400/2
      lp2.addRule(RelativeLayout.BELOW, R.id.title_bar);
      lp2.addRule(RelativeLayout.ABOVE, R.id.rl_operate);
      mBinding.rlScreenSurface.setLayoutParams(lp2);


    }
  }

  /**
   * 标题栏 返回键
   *
   * @param
   * @return
   */
  public void onTitleBarBackClick(View view) {

    backEvent();

    if (isPortrait) {
      //竖屏
      SovUtil.disconnect(window);
      finish();
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  }

  //远程门锁操作回调
  @Override
  public void showRemoteLockOptFailed(String cmdType) {
    switch (cmdType) {
      case BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE:
      case BleMapper.BLE_CMD_UNLOCK_REMOTE:
        hideLoading();
        ToastUtils.showShort("开门失败，请重试");
        break;
      default:
        break;
    }
  }

  @Override
  public void showRemoteLockOptSuccess(BlePkg blePkg, String cmdType) {

    switch (cmdType) {

      case BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE:
        String payload = blePkg.getBody().getPayload();
        if (TextUtils.isEmpty(payload) || payload.length() < 2) return;
        String rspCode = payload.substring(0, 2);
        if (checkBleRsp(rspCode)) return;
        String randomCode = payload.substring(2);
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("00");
        pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UNLOCK);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_UNLOCK_REMOTE);
        body.setPayload(randomCode);
        pkg.setBody(body);
        mRemoteLockOperationPresenter.remoteOpt(mQueryCatLockResponse.getRouterSerialNo(), mQueryCatLockResponse.getDeviceSerialNo(), pkg);
        break;

      case BleMapper.BLE_CMD_UNLOCK_REMOTE:
        if (checkBleRsp(blePkg.getBody().getPayload())) return;
        ToastUtils.showShort("已开门");
        hideLoading();
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            ToastUtils.showShort("已上锁");
          }
        }, 5000);
        break;

      default:
        break;
    }
  }

  @Override
  public void onRefreshComplete() {
  }

  @Override
  public void onRefreshStart() {
  }

  @Override
  public void onRefreshError() {
  }

  @Override
  public void uploadDevicePwdSuccessed() {
    System.out.println("设备密码: 上传成功 猫眼 = " + GlobleConstant.getgCatPwd());
  }
}


