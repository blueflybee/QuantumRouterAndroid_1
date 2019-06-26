package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;

import java.util.List;

//自定义Adapter内部类
public class DownloadFinishedAdapter1 extends BaseExpandableListAdapter {
  private Context mContext;
  private List<FileUploadBean> fileBeans;
  OnDetailClickListener mOnDetailClickListener;
  OnFolderClickListener mOnFolderClickListener;

  // 文件对应的路径列表
  private static final int ITEMCOUNT = 2; //消息总数

  public static interface IViewType {
    int IVT_FILE = 0;// 文件
    int IVT_FOLDER = 1;// 文件夹
  }

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

  public DownloadFinishedAdapter1(Context context, List<FileUploadBean> fileBeans) {
    this.mContext = context;
    this.fileBeans = fileBeans;

    /**
     * 视图模式对应的icon
     */
    // 初始化图片资源
    // 图片文件对应的icon
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

  @Override
  public int getGroupCount() {
    return fileBeans.size();
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return 1;
  }

  @Override
  public Object getGroup(int groupPosition) {
    return fileBeans.get(groupPosition);
  }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return "";
  }

  @Override
  public long getGroupId(int groupPosition) {
    return groupPosition;
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return childPosition;
  }

  @Override
  public boolean hasStableIds() {
    return true;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
    GroupViewHolder viewHolder = null;
    FileUploadBean entity = fileBeans.get(groupPosition);
        /*boolean isFoler = entity.getFileType();*/

    if (convertView == null) {
      LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 初始化列表元素界面
      viewHolder = new GroupViewHolder();

      //文件
      convertView = mLI.inflate(R.layout.item_finished_download1, null);
      viewHolder.mFileDate = (TextView) convertView.findViewById(R.id.tv_date_list_childs);

      // 获取列表布局界面元素
      viewHolder.mHeadIcon = (ImageView) convertView.findViewById(R.id.image_list_childs);
      viewHolder.mName = (TextView) convertView.findViewById(R.id.text_list_childs);
      viewHolder.mRlSpread = (RelativeLayout) convertView.findViewById(R.id.rl_spread);
      viewHolder.mGroupIcon = (ImageView) convertView.findViewById(R.id.img_spread);
      viewHolder.mLlContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
      viewHolder.mLlDropView = (LinearLayout) convertView.findViewById(R.id.ll_edit_samba_file);

      // 将每一行的元素集合设置成标签
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (GroupViewHolder) convertView.getTag();
    }

    if (isExpanded)
      viewHolder.mGroupIcon.setImageResource(R.drawable.indexic_up);
    else
      viewHolder.mGroupIcon.setImageResource(R.drawable.indexic_down);

    viewHolder.mName.setText(fileBeans.get(groupPosition).getName());

    viewHolder.mLlContent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnFolderClickListener != null) {
          mOnFolderClickListener.onFolderClick(v, groupPosition);
        }
      }
    });

    viewHolder.mFileDate.setText(fileBeans.get(groupPosition).getDate());

    setImage(fileBeans.get(groupPosition).getName(), viewHolder.mHeadIcon);

    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    ChildViewHolder viewHolder1;

    if (convertView == null) {
      viewHolder1 = new ChildViewHolder();

      LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 初始化列表元素界面
      convertView = mLI.inflate(R.layout.item_edit_samba_file, null);
      viewHolder1.llChildContent = (LinearLayout) convertView.findViewById(R.id.ll_edit_samba_file);
      convertView.setTag(viewHolder1);
    } else {
      viewHolder1 = (ChildViewHolder) convertView.getTag();
    }

    viewHolder1.llChildContent.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnDetailClickListener != null) {
          mOnDetailClickListener.onDetailClick(v);
        }
      }
    });

    return convertView;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return true;
  }

  /**
   * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
   */
/*    public int getItemViewType(int position) {
        FileBean bean = fileBeans.get(position);

        if(bean.getFileType()){
            return IViewType.IVT_FOLDER; //文件夹
        }else{
            return IViewType.IVT_FILE;  //文件
        }
    }*/

  // 用于存储列表每一行元素的图片和文本
  class GroupViewHolder {
    ImageView mHeadIcon;
    TextView mName;
    TextView mFileDate;
    TextView mFolderCount;
    RelativeLayout mRlSpread;
    LinearLayout mLlContent;
    LinearLayout mLlDropView;
    ImageView mGroupIcon;
  }

  class ChildViewHolder {
    LinearLayout llChildContent;
  }


  public interface OnDetailClickListener {
    void onDetailClick(View v);
  }

  public interface OnFolderClickListener {
    void onFolderClick(View v, int position);
  }

  /**
   * 详情下拉 回调
   *
   * @param
   * @return
   */
  public void setOnDetailListener(OnDetailClickListener mOnDetailClickListener) {
    this.mOnDetailClickListener = mOnDetailClickListener;
  }

  public void setOnFolderListener(OnFolderClickListener mOnFolderClickListener) {
    this.mOnFolderClickListener = mOnFolderClickListener;
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
