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
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityVideoReplyBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.NotifyPictureSystemUtil;
import com.fernandocejas.android10.sample.presentation.view.device.camera.adapter.VideoReplyListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.camera.util.VideoReplyPopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.StreamFile;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;

import java.util.ArrayList;

/**
 * 视频回放
 *
 * @param
 * @return
 */
public class VideoReplyActivity extends BaseCatActivity {
  private ActivityVideoReplyBinding mBinding;
  private VideoReplyListAdapter adapter;
  private ArrayList<StreamFile> videoList;
  private VideoReplyPopupWindow mPopWin;
  private int REQUEST_VIDEO_HISTORY_PLAY = 0x33;
  private int year,month,day,hasDownLoadSize,downLoadFileSize,window;// 已下载文件大小 下载文件大小
  public static final int FLAG_DOWNLOAD_FILE = 0x01111;//录像下载

  @Override
  protected void initSettings() {
  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_reply);

    initView();

    searchRemoteData();
  }

  @Override
  protected void saveSettings() {
  }

  @Override
  protected void freeMe() {
  }

  /**
   * 检索远程回放数据
   */
  public void searchRemoteData() {
    String temStr = getTimeChoose();
    String[] temArray = temStr.split("-");
    year = Integer.parseInt(temArray[0]);
    month = Integer.parseInt(temArray[1]);
    day = Integer.parseInt(temArray[2]);

    DialogUtil.showProgress(this);
    SovUtil.checkStreamRemoteVideo(window, year, month, day);
  }

  private void initView() {
    initTitleBar("录像回放");
    videoList = new ArrayList<>();

    adapter = new VideoReplyListAdapter(this, videoList, R.layout.item_video_reply);
    mBinding.lvVideoList.setAdapter(adapter);

    //下载视频
    adapter.setOnDownloadVideoClickListener(new VideoReplyListAdapter.OnDownloadVideoClickListener() {
      @Override
      public void onDownloadVideoClick(int position) {
        if (isDownloading()) {
          showPopWin();
        } else {
          VideoReplyActivity.this.onNotify(VideoReplyActivity.FLAG_DOWNLOAD_FILE,
              position, 0, null);
          DialogUtil.showProgress(VideoReplyActivity.this);
        }
      }
    });

    //选择视频 播放
    adapter.setOnVideoPlayClickListener(new VideoReplyListAdapter.OnVideoPlayClickListener() {
      @Override
      public void onVideoPlayClick(int position) {

        StreamFile videoFile = videoList.get(position);
        String acBuffStr = videoFile.getFilePath();
        //回放视频 连接中
        Intent intent = new Intent();
        intent.putExtra("acBuffStr", acBuffStr);
        /*intent.putExtra("VideoTime", "");*/
        setResult(REQUEST_VIDEO_HISTORY_PLAY,intent);
        finish();

      }
    });
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    DialogUtil.hideProgress();
    switch (what) {
      //连接结果,视频异常断开时关闭此界面
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        if (arg2 != JVNetConst.CCONNECTTYPE_CONNOK) {
          this.finish();
        }
        break;
      }
      //流媒体协议
      case JVNetConst.CALL_CATEYE_SENDDATA: {
        try {
          com.alibaba.fastjson.JSONObject root = JSON.parseObject(obj.toString());
          int nCmd = root.getInteger(AppConsts.TAG_NCMD);
          int nPacketType = root.getInteger(AppConsts.TAG_NPACKETTYPE);
          int result = root.getInteger(AppConsts.TAG_RESULT);
          String reason = root.getString(AppConsts.TAG_REASON);
          String dat = root.getString(AppConsts.TAG_DATA);
          switch (nCmd) {
            //检索视频文件
            case JVNetConst.SRC_REMOTE_CHECK: {
              switch (nPacketType) {
                case JVNetConst.SRC_EX_CHECK_VIDEO:
                  videoList.clear();
                  videoList.addAll(SovUtil.getStreamIPCFileList(dat, getTimeChoose()));
                  if (null != videoList && 0 != videoList.size()) {
                    mBinding.tvEmpty.setVisibility(View.GONE);
                    mBinding.lvVideoList.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                  } else {
                    /*Toast.makeText(VideoReplyActivity.this, R.string.video_nodata_failed, Toast.LENGTH_SHORT).show();*/
                    //暂时没有数据
                    mBinding.tvEmpty.setVisibility(View.VISIBLE);
                    mBinding.lvVideoList.setVisibility(View.GONE);
                  }
                  break;
              }
              break;
            }
            case JVNetConst.SRC_REMOTE_DOWNLOAD: {
              break;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      }
      // 下载进度
      case JVNetConst.CALL_DOWNLOAD: {
        switch (arg2) {
          //文件头回调
          case JVNetConst.DT_FILE_HEAD: {
            //file_length
            try {
              com.alibaba.fastjson.JSONObject data = JSON.parseObject(obj.toString());
              downLoadFileSize = data.getInteger("file_length");

              System.out.println("开始下载-文件总大小：" + downLoadFileSize);
              updateDownloadProgressBar(downLoadFileSize, hasDownLoadSize);

            } catch (Exception e) {
              e.printStackTrace();
            }

            if (downLoadFileSize <= 0) {
              downLoadFileSize = 0;
              Toast.makeText(VideoReplyActivity.this, "下载失败：文件大小获取失败", Toast.LENGTH_LONG).show();
              hidePopWin();
            }

            break;
          }
          //文件数据回调
          case JVNetConst.DT_FILE_DATA: {

            if (downLoadFileSize == 0) {
              Toast.makeText(VideoReplyActivity.this, "下载失败", Toast.LENGTH_LONG).show();
              SovUtil.cancelStreamDownload(window);
              break;
            } else {
              try {
                com.alibaba.fastjson.JSONObject data = JSON.parseObject(obj.toString());
                int dataLen = data.getInteger("data_len");
                hasDownLoadSize += dataLen;
                System.out.println("下载中-文件总大小：" + downLoadFileSize + "；已下载：" + hasDownLoadSize + "；进度：" + (int) ((float) hasDownLoadSize / downLoadFileSize * 100) + "%");
                updateDownloadProgressBar(downLoadFileSize, hasDownLoadSize);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }

            break;
          }
          case JVNetConst.DT_FILE_TAIL: {
            com.alibaba.fastjson.JSONObject data = JSON.parseObject(obj.toString());
            int result = data.getInteger("result");

            if (result == 1) {
              Toast.makeText(VideoReplyActivity.this, "下载完成", Toast.LENGTH_LONG).show();
              //通知系统图库有视频更新
              NotifyPictureSystemUtil.sendNotifyToPictureSystem(this, AppConsts.DOWNLOAD_PATH);
            } else {
              Toast.makeText(VideoReplyActivity.this, "下载失败", Toast.LENGTH_LONG).show();
            }

            hidePopWin();

            break;
          }
        }
        break;
      }
      //下载按钮事件：录像下载
      case FLAG_DOWNLOAD_FILE: {
        StreamFile videoFile = videoList.get(arg1);
        String acBuffStr = videoFile.getFilePath();

        String fileName = System.currentTimeMillis()
            + AppConsts.VIDEO_MP4_KIND;
        String downFileFullName = AppConsts.DOWNLOAD_PATH + fileName;
        JniUtil.setDownloadFileName(window, downFileFullName);

        downLoadFileSize = 0;
        hasDownLoadSize = 0;
        SovUtil.startStreamDownload(window, acBuffStr);
        //下载弹窗
        showPopWin();

        break;
      }
      default: {
        break;
      }
    }

  }

  private String getTimeChoose() {
    return getIntent().getStringExtra("TimeChoose");
  }

  private void hidePopWin() {
    if (mPopWin != null) {
      mPopWin.dismiss();
    }
  }

  private void showPopWin() {
    mPopWin = new VideoReplyPopupWindow(VideoReplyActivity.this, downLoadFileSize, hasDownLoadSize);
    mPopWin.setFocusable(true);
    mPopWin.showAtLocation(mPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
  }

  /**
   * 更新进度
   *
   * @param
   * @return
   */
  private void updateDownloadProgressBar(int fileSize, int hasDownloadSize) {
    if (mPopWin != null) {
      mPopWin.updateDownloadProgress(fileSize, hasDownloadSize);
    }
  }

  /**
   * 判断是否正在下载
   *
   * @param
   * @return
   */
  private Boolean isDownloading() {
    if (downLoadFileSize > 0) {
      if (hasDownLoadSize < downLoadFileSize) {
        return true;
      }
    }
    return false;
  }

}
