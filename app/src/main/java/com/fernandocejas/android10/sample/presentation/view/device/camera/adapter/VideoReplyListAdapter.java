package com.fernandocejas.android10.sample.presentation.view.device.camera.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter1;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.StreamFile;

import java.util.ArrayList;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class VideoReplyListAdapter extends CommAdapter1<StreamFile> {
  private Context mContext;
  OnDownloadVideoClickListener mDownloadVideoClickListener;
  OnVideoPlayClickListener mVideoPlayClickListener;

  public VideoReplyListAdapter(Context context, ArrayList<StreamFile> data, int layoutId) {
    super(context, data, layoutId);
    mContext = context;
  }

  @Override
  public void convert(BaseViewHolder baseViewHolder, StreamFile response, int mPostition) {
    TextView time = baseViewHolder.getView(R.id.tv_time);
    TextView type = baseViewHolder.getView(R.id.tv_type);
    ImageView download = baseViewHolder.getView(R.id.img_video_download);
    RelativeLayout historyVideo = baseViewHolder.getView(R.id.rl_video_history);

    time.setText(response.getFileDate());

    //A：alarm报警录像；M：motion移动侦测；T:定时录像 N：normal手动录像 D:IPC无连接录像 O：一分钟录像 C:抽帧录像
    switch (response.getVideoKind()) {
      case StreamFile.TYPE_ALARM:
        type.setText("报警录像");
        break;
      case StreamFile.TYPE_CHFRAME:
        type.setText("抽帧录像");
        break;
      case StreamFile.TYPE_DISCON:
        type.setText("无连接录像");
        break;
      case StreamFile.TYPE_MOTION:
        type.setText("移动侦测录像");
        break;
      case StreamFile.TYPE_NORMAL:
        type.setText("正常录像");
        break;
      case StreamFile.TYPE_ONE_MIN:
        type.setText("一分钟录像报警");
        break;
      case StreamFile.TYPE_TIME:
        type.setText("定时录像");
        break;
      default:
        type.setText("正常录像");
        break;
    }

    download.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mDownloadVideoClickListener != null) {
          mDownloadVideoClickListener.onDownloadVideoClick(mPostition);
        }
      }
    });

    historyVideo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mVideoPlayClickListener != null) {
          mVideoPlayClickListener.onVideoPlayClick(mPostition);
        }
      }
    });


  }

  public interface OnDownloadVideoClickListener {
    void onDownloadVideoClick(int position);
  }

  public interface OnVideoPlayClickListener {
    void onVideoPlayClick(int position);
  }

  /**
   * 设备详情回调函数
   *
   * @param
   * @return
   */
  public void setOnDownloadVideoClickListener(OnDownloadVideoClickListener downloadVideoClickListener) {
    mDownloadVideoClickListener = downloadVideoClickListener;
  }

  public void setOnVideoPlayClickListener(OnVideoPlayClickListener videoPlayClickListener) {
    mVideoPlayClickListener = videoPlayClickListener;
  }

}
