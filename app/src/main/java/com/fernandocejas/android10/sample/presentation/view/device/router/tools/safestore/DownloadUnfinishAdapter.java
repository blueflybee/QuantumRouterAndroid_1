package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DownloadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.CustomCircleProgress;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//自定义Adapter内部类
public class DownloadUnfinishAdapter extends BaseAdapter {
  private Context mContext;
  private List<FileUploadBean> fileBeans = new ArrayList<>();
  OnUploadCircleProgress mOnUploadCircleProgress;
  public Map<Integer, Boolean> checkedMap; // 保存checkbox是否被选中的状态
  public Map<Integer, Integer> visibleMap; // 保存checkbox是否显示的状态
  private TextView mLoadingTitle;
  private Button mBtnDownload;

  // 各种格式的文件的图标
  private Bitmap mImage;
  private Bitmap mAudio;
  private Bitmap mRar;
  private Bitmap mVideo;
  private Bitmap mFolder;
  private Bitmap mApk;
  private Bitmap mOthers;
  private Bitmap mTxt;
  private Bitmap mWeb;
  private Bitmap mWord;
  private Bitmap mPPT;
  private Bitmap mExcel;
  private Bitmap mPdf;

  public DownloadUnfinishAdapter(Context context, TextView mLoadingTitle,Button mBtnDownload) {
    this.mContext = context;
    this.mLoadingTitle = mLoadingTitle;
    this.mBtnDownload = mBtnDownload;

    checkedMap = new HashMap<Integer, Boolean>();
    visibleMap = new HashMap<Integer, Integer>();
    for (int i = 0; i < fileBeans.size(); i++) {
      checkedMap.put(i, false);//checked未被选中
      visibleMap.put(i, CheckBox.GONE); //checked不显示
    }

    /**
     * 视图模式对应的icon
     */
    // 初始化图片资源
    // 图片文件对应的icon
    if (mContext != null) {
      mImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_pic);
      // 音频文件对应的icon
      mAudio = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_music);
      // 视频文件对应的icon
      mVideo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_video);
      // 可执行文件对应的icon
      mApk = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_apk);
      // 文本文档对应的icon
      mTxt = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_txt);
      // 其他类型文件对应的icon
      mOthers = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_usual);
      // 文件夹对应的icon
      mFolder = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_document);
      // zip文件对应的icon
      mRar = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_zip);
      // 网页文件对应的icon
      mWeb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_html);
      // Word对应的icon
      mWord = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_word);
      // PPT对应的icon
      mPPT = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_ppt);
      // Excel对应的icon
      mExcel = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_excel);
      // pdf对应的icon
      mPdf = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nas_pdf);
    }

  }

  // 获得文件的总数
  public int getCount() {
    return fileBeans.size();
  }

  // 获得当前位置对应的文件名
  public FileUploadBean getItem(int position) {
    return fileBeans.get(position);
  }

  // 获得当前的位置
  public long getItemId(int position) {
    return position;
  }

  @SuppressWarnings("WrongConstant")
  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;

    if (convertView == null) {
      LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 初始化列表元素界面
      viewHolder = new ViewHolder();

      //文件
      convertView = mLI.inflate(R.layout.item_download_unfinish, null);
      viewHolder.mSize = (TextView) convertView.findViewById(R.id.tv_transit_size);
      viewHolder.mSpeed = (TextView) convertView.findViewById(R.id.tv_transit_speed);
      viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_select_smb_file);
      viewHolder.mCustomCircleProgress = (CustomCircleProgress) convertView.findViewById(R.id.ccp_circleProgress);

      // 获取列表布局界面元素
      viewHolder.mHeadImg = (ImageView) convertView.findViewById(R.id.img_transit_headIcon);
      viewHolder.mName = (TextView) convertView.findViewById(R.id.tv_transit_name);
      // 将每一行的元素集合设置成标签
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    CheckBox mCB = viewHolder.mCheckBox;

    try {
      mCB.setTag(position);
      if (visibleMap.containsKey(position)) {
        mCB.setVisibility(visibleMap.get(position));
      }
      if (checkedMap.containsKey(position)) {
        mCB.setChecked(checkedMap.get(position));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        checkedMap.put((Integer) buttonView.getTag(), isChecked);
      }
    });

    try {
      // fileSize 只更新一次   complete speed 不断更新

      long completeSize = fileBeans.get(position).getFinishedSize();
      long fileSize = fileBeans.get(position).getSumSize();

      if (fileBeans.get(position).getState() == Downloader.DOWNLOADING) {
        viewHolder.mCustomCircleProgress.setStatus(CustomCircleProgress.Status.Starting);

        if (fileBeans.get(position).getLoadingSpeed() != null) {
          if (!fileBeans.get(position).getLoadingSpeed().startsWith(".")) {
            //过滤掉.00开头的网速
            viewHolder.mSpeed.setText(fileBeans.get(position).getLoadingSpeed() + "/S");
          } else {
            viewHolder.mSpeed.setText("下载中…");
          }
        } else {
          viewHolder.mSpeed.setText("下载中…");
        }

      } else if (fileBeans.get(position).getState() == Downloader.WAITING) {
        viewHolder.mCustomCircleProgress.setStatus(CustomCircleProgress.Status.Starting);
        viewHolder.mSpeed.setText("等待中…");
      } else if (fileBeans.get(position).getState() == Downloader.FAILED) {
        viewHolder.mCustomCircleProgress.setStatus(CustomCircleProgress.Status.End);
        viewHolder.mSpeed.setText("下载失败");
      } else {
        viewHolder.mCustomCircleProgress.setStatus(CustomCircleProgress.Status.End);
        viewHolder.mSpeed.setText("暂停中…");
      }

      viewHolder.mCustomCircleProgress.setMax((int) fileSize);
      viewHolder.mCustomCircleProgress.setProgress((int) completeSize);

      if (completeSize == 0) {
        viewHolder.mSize.setText("0K/" + SambaUtils.bytes2kb(fileSize));
      } else {
        viewHolder.mSize.setText(SambaUtils.bytes2kb(completeSize) + "/" + SambaUtils.bytes2kb(fileSize));
      }

      fileBeans.get(position).setCustomCircleProgress(viewHolder.mCustomCircleProgress);

      viewHolder.mName.setText(fileBeans.get(position).getName());
      setImage(fileBeans.get(position).getName(), viewHolder.mHeadImg);


    } catch (Exception e) {
      e.printStackTrace();
    }

    //设置点击事件
    viewHolder.mCustomCircleProgress.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // 回调接口
        if (mOnUploadCircleProgress != null) {
          mOnUploadCircleProgress.onUploadCircleProgress(v, position);
        }

      }
    });

    return convertView;
  }

  public void add(FileUploadBean fileBean) {
    if (fileBean == null) return;
    checkedMap.put(fileBeans.size(), false);
    visibleMap.put(fileBeans.size(), CheckBox.GONE);
    fileBeans.add(fileBean);
    notifyDataSetChanged();
  }

  public void remove(int position) {
    fileBeans.remove(position);
    notifyDataSetChanged();
  }

  public void remove(String url) {
    System.out.println("url = [" + url + "]");
    if (TextUtils.isEmpty(url)) return;
    for (int i = 0; i < fileBeans.size(); i++) {
      FileUploadBean bean = fileBeans.get(i);
      if (!url.equals(bean.getDownloadsKey())) continue;
      fileBeans.remove(i);
      mLoadingTitle.setText("正在下载(" + (fileBeans.size()) + ")");
      break;
    }

    if(fileBeans.size() == 0){
      mBtnDownload.setVisibility(View.GONE);
    }

    notifyDataSetChanged();
  }

  public void updateFailed(String url) {
    if (TextUtils.isEmpty(url)) return;
    for (int i = 0; i < fileBeans.size(); i++) {
      FileUploadBean bean = fileBeans.get(i);
      if (url.equals(bean.getDownloadsKey())) {
        bean.setState(Downloader.FAILED);
        notifyDataSetChanged();
        break;
      }
    }
  }

  public FileUploadBean getNext(String url) {
    if (TextUtils.isEmpty(url)) return null;
    for (int i = 0; i < fileBeans.size(); i++) {
      FileUploadBean bean = fileBeans.get(i);
      if (url.equals(bean.getDownloadsKey())) {
        return bean;
      }
    }
    return null;
  }

  public void removeContainData(FileUploadBean fileUploadBean, Map<String, Downloader> downloaders, DatabaseUtil mDBUtil) {
    if (fileUploadBean == null) return;
    if (fileBeans.contains(fileUploadBean)) {
      fileBeans.remove(fileUploadBean);
      notifyDataSetChanged();

      String url = fileUploadBean.getDownloadsKey();

      if (downloaders.get(url) != null) {
        downloaders.get(url).delete(url, mContext);
        downloaders.get(url).reset();
        downloaders.remove(url);
/*
       mDBUtil.deleteKey(url);//下载完就删除key
*/

        DownloadCacheManager.delete(mContext, url);
      }

    }
  }

  public void updateInfo(int position, Boolean isStop) {
    if (position > fileBeans.size()) return;
    if (isStop) {
      fileBeans.get(position).getCustomCircleProgress().setStatus(CustomCircleProgress.Status.Starting);
      fileBeans.get(position).setState(Downloader.WAITING);
      notifyDataSetChanged();
    } else {
      fileBeans.get(position).getCustomCircleProgress().setStatus(CustomCircleProgress.Status.End);
      fileBeans.get(position).setState(Downloader.PAUSE);
      notifyDataSetChanged();
    }

  }

  public void updateDownloadBtnState(Boolean isAllDownload) {
    if(isAllDownload){
      mBtnDownload.setText("全部暂停下载");
    }else {
      mBtnDownload.setText("全部开始下载");
    }
  }

  // 用于存储列表每一行元素的图片和文本
  class ViewHolder {
    ImageView mHeadImg;
    TextView mName;
    TextView mSize;
    TextView mSpeed;
    CheckBox mCheckBox;
    CustomCircleProgress mCustomCircleProgress;
  }

  public interface OnUploadCircleProgress {
    void onUploadCircleProgress(View v, int position);
  }

  public void setOnUploadCircleProgress(OnUploadCircleProgress mOnUploadCircleProgress) {
    this.mOnUploadCircleProgress = mOnUploadCircleProgress;
  }

  public CustomCircleProgress getCustomCircusProgress(int index) {
    return fileBeans.get(index).getCustomCircleProgress();
  }

  private void setImage(String fileName, ImageView view) {

    if (fileName.lastIndexOf(".") == -1)
      view.setImageBitmap(mOthers);
    else {
      String fileEnds = fileName.substring(
          fileName.lastIndexOf(".") + 1, fileName.length())
          .toLowerCase();// 取出文件后缀名并转成小写
      if (fileEnds.equals("m4a") || fileEnds.equals("mp3")
          || fileEnds.equals("mid") || fileEnds.equals("xmf")
          || fileEnds.equals("ogg") || fileEnds.equals("wav")) {
        view.setImageBitmap(mAudio);
      } else if (fileEnds.equals("3gp") || fileEnds.equals("mp4")
          || fileEnds.equals("avi") || fileEnds.equals("rmvb")) {
        view.setImageBitmap(mVideo);
      } else if (fileEnds.equals("jpg") || fileEnds.equals("gif")
          || fileEnds.equals("png") || fileEnds.equals("jpeg")
          || fileEnds.equals("bmp")) {
        view.setImageBitmap(mImage);
      } else if (fileEnds.equals("apk")) {
        view.setImageBitmap(mApk);
      } else if (fileEnds.equals("txt") || fileEnds.equals("xml")) {
        view.setImageBitmap(mTxt);
      } else if (fileEnds.equals("doc") || fileEnds.equals("docx")) {
        view.setImageBitmap(mWord);
      } else if (fileEnds.equals("ppt") || fileEnds.equals("pptx")
          || fileEnds.equals("pps")) {
        view.setImageBitmap(mPPT);
      } else if (fileEnds.equals("xls") || fileEnds.equals("xlsx")) {
        view.setImageBitmap(mExcel);
      } else if (fileEnds.equals("pdf")) {
        view.setImageBitmap(mPdf);
      } else if (fileEnds.equals("zip") || fileEnds.equals("rar")) {
        view.setImageBitmap(mRar);
      } else if (fileEnds.equals("html") || fileEnds.equals("htm")
          || fileEnds.equals("mht")) {
        view.setImageBitmap(mWeb);
      } else {
        view.setImageBitmap(mOthers);
      }

    }
  }
}
