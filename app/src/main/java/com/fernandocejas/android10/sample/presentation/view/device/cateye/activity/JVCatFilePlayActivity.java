package com.fernandocejas.android10.sample.presentation.view.device.cateye.activity;

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCameraReplyBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.NotifyPictureSystemUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;

public class JVCatFilePlayActivity extends CatEyeBaseActivity {

  private static final String TAG = "JVCatFilePlayActivity";

  private ActivityCameraReplyBinding mBinding;
  private int window;
  private String playPath;

  private int currentProgress;
  private int totalProgress;
  private boolean isConnected = false;

  SurfaceHolder playSurfaceHolder;

  private boolean mPaused = false;
  private boolean mLandscape;

  @Override
  protected void initSettings() {
    currentProgress = 0;
    totalProgress = 0;

    window = getIntent().getIntExtra("window", 0);
    playPath = playPath();
  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_reply);
    playSurfaceHolder = mBinding.svSurfaceView.getHolder();
    playSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
      @Override
      public void surfaceCreated(SurfaceHolder holder) {
//        Toast.makeText(JVCatFilePlayActivity.this, "路径:" + playPath, Toast.LENGTH_SHORT).show();
        SovUtil.enableRemotePlay(window, true);
        JniUtil.resumeSurface(window, playSurfaceHolder.getSurface());
        handler.sendEmptyMessageDelayed(111, 500);
      }

      @Override
      public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      }

      @Override
      public void surfaceDestroyed(SurfaceHolder holder) {
      }
    });
    mBinding.rlVideoReply.setVisibility(View.GONE);
    mBinding.tvLinkState.setText("视频连接中...");

    mBinding.rlScreen.setVisibility(View.GONE);
    mBinding.timeNumberLayout.bindRuler(mBinding.timeRuler);
    mBinding.timeRuler.setEnable(false);

    mBinding.seekVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
////        SovUtil.pauseStreamCatRemotePlay(window);
//        int progress = seekBar.getProgress();
//        System.out.println("progress = " + progress);
//        currentProgress = (int) (((float) progress) / 100 * totalProgress);
//        System.out.println("currentProgress = " + currentProgress);
//        SovUtil.streamCatSeekTo(window, progress);
////        SovUtil.goonStreamCatRemotePlay(window);
      }
    });

//    int orientation = getResources().getConfiguration().orientation;
//    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//      mLandscape = true;
//    } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//      mLandscape = false;
//    }

  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void onResume() {
    if (isConnected) {
      JniUtil.resumeSurface(window, playSurfaceHolder.getSurface());
    }
    super.onResume();
  }

  @Override
  protected void onPause() {
    if (isConnected) {
      JniUtil.pauseSurface(window);
    }
    super.onPause();
  }


  @Override
  protected void freeMe() {
    SovUtil.stopStreamRemotePlay(window);
    SovUtil.enableRemotePlay(window, false);
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    if (null == obj) {
      Log.e(TAG, "what=" + what + ";arg1=" + arg1 + ";arg2=" + arg2 + ";obj=null");
    } else {
      Log.e(TAG, "what=" + what + ";arg1=" + arg1 + ";arg2=" + arg2 + ";obj=" + obj.toString());
    }

    switch (what) {
      //连接结果,视频异常断开时关闭此界面
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        if (arg2 != JVNetConst.CCONNECTTYPE_CONNOK) {
          this.finish();
        }
        break;
      }
      case 111: {
        SovUtil.startStreamRemotePlay(window, playPath);
        break;
      }
      //猫眼流媒体远程回放数据回调0xA7
      case JVNetConst.CALL_PLAY_DATA: {
//                MyLog.v(JVLogConst.LOG_STREAM_CAT, getLocalClassName() + "--callback:CALL_CATEYE_PLAY:what:" + what + ";arg1:" + arg1 + ";arg2:" + arg2 + ";obj:" + obj.toString());
//                what:218;arg1:0;arg2:3;obj:{"audio_bit":16,"audio_channel":1,"audio_sample_rate":8000,"audio_type":2,"count":2399,"fps":20.0,"height":540,"total":2399,"width":960}
        switch (arg2) {
          case JVNetConst.JVN_DATA_O: {
            try {
              org.json.JSONObject jObj = new org.json.JSONObject(obj.toString());
              if (null != jObj) {
                totalProgress = jObj.optInt("total");
                mBinding.seekVideo.setMax(totalProgress);
              }

            } catch (org.json.JSONException e) {
              e.printStackTrace();
            }
            break;
          }
          case JVNetConst.JVN_DATA_I:
          case JVNetConst.JVN_DATA_B:
          case JVNetConst.JVN_DATA_P: {
            currentProgress++;
            Log.e(TAG, "totalProgress=" + totalProgress + ";currentProgress=" + currentProgress);
            mBinding.seekVideo.setProgress(currentProgress);
            break;
          }
          //远程回放结束回调
          case JVNetConst.REMOTE_PLAY_CMD_OVER: {
//            Toast.makeText(JVCatFilePlayActivity.this, "远程回放结束", Toast.LENGTH_LONG).show();
//            this.finish();
            break;
          }
          //远程回放出错
          case JVNetConst.REMOTE_PLAY_CMD_PLAYERROR: {
            Toast.makeText(JVCatFilePlayActivity.this, "远程回放出错", Toast.LENGTH_LONG).show();
            this.finish();
            break;
          }
          //远程回放超时
          case JVNetConst.REMOTE_PLAY_CMD_PLTIMEOUT: {
            Toast.makeText(JVCatFilePlayActivity.this, "远程回放超时", Toast.LENGTH_LONG).show();
//            this.finish();
            break;
          }

        }
        break;
      }
      //出图
      case JVNetConst.CALL_NEW_PICTURE: {
        isConnected = true;
        mBinding.tvLinkState.setText("");
        break;
      }
      default: {
        break;
      }
    }
  }

  public void onTitleBarBackClick(View view) {
    onBack();
    finish();
  }

  @Override
  public void onBackPressed() {
    onBack();
    super.onBackPressed();
  }

  private void onBack() {
    //    if (doubleCalling) {
//      stopDoubleCall();
//      Toast.makeText(this, "关闭对讲", Toast.LENGTH_SHORT).show();
//    }
    if (mRecording) {
      mRecording = false;
      JniUtil.stopRecord(window);
    }
    if (enableSound) {
      enableSound = false;
      JniUtil.closeSound(window);
    }

//    SovUtil.disconnect(window);
  }

  /************************************** 基本功能 ***********************************************/

  //是否正在监听
  private boolean enableSound = false;

  //是否正在录像
  private boolean mRecording = false;

  //声音按钮
  public void onVoiceClick(View view) {
    if (enableSound) {
      enableSound = false;
      Toast.makeText(this, "关闭声音", Toast.LENGTH_SHORT).show();
      JniUtil.closeSound(window);
      mBinding.imgVoice.setBackground(getResources().getDrawable(R.drawable.ic_voice_normal));
    } else {
      enableSound = true;
      Toast.makeText(this, "开启声音", Toast.LENGTH_SHORT).show();
      JniUtil.openSound(window);
      mBinding.imgVoice.setBackground(getResources().getDrawable(R.drawable.ic_voice_pressed));
    }
  }

  public void onVideoReplyClick(View view) {
    if (mPaused) {
      mPaused = false;
      mBinding.imgVideoReply.setBackground(getResources().getDrawable(R.drawable.ic_replay_stop));
      // 继续播放视频
      SovUtil.goonStreamCatRemotePlay(window);
    } else {
      mPaused = true;
      // 暂停视频
      SovUtil.pauseStreamCatRemotePlay(window);
      mBinding.imgVideoReply.setBackground(getResources().getDrawable(R.drawable.ic_sx_replay));
    }
  }

  public void onVideoClick(View view) {
    if (mRecording) {
      JniUtil.stopRecord(window);
      NotifyPictureSystemUtil.sendNotifyToPictureSystem(this, AppConsts.VIDEO_PATH);
      mBinding.imgVideo.setBackground(getResources().getDrawable(R.drawable.ic_video_normal));
      Toast.makeText(this, "停止录像", Toast.LENGTH_SHORT).show();
      mRecording = false;
    } else {
      JniUtil.startRecord(window);
      mBinding.imgVideo.setBackground(getResources().getDrawable(R.drawable.ic_video_pressed));
      Toast.makeText(this, "开启录像", Toast.LENGTH_SHORT).show();
      mRecording = true;
    }
  }

  private String playPath() {
    return getIntent().getStringExtra(Navigator.EXTRA_PLAY_PATH);
  }

  public void onPicClick(View view) {
    JniUtil.capture(window);
    Toast.makeText(this, "抓拍成功", Toast.LENGTH_SHORT).show();
  }

  public void onScreenAlmostClick(View view) {
//    System.out.println("mLandscape = " + mLandscape);

  }
}
