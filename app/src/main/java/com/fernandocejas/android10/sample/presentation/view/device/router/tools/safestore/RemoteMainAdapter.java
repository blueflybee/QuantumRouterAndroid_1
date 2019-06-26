package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.GridViewForExpandableListView;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//自定义Adapter内部类
public class RemoteMainAdapter extends BaseExpandableListAdapter implements CompoundButton.OnCheckedChangeListener {
  private Context mContext;
  private List<FileBean> fileBeans = new ArrayList<>();
  OnDetailClickListener mOnDetailClickListener;
  OnFolderClickListener mOnFolderClickListener;
  String[] titles = {"下载", "重命名", "移动", "删除"};
  //文件夹只支持重命名和删除
  int[] imgs = {R.drawable.nas_filedownload1, R.drawable.nas_filenewname1, R.drawable.nas_filemove1, R.drawable.nas_filedelete1};

  // 文件对应的路径列表
  private static final int ITEMCOUNT = 2; //消息总数

  private final int isSortByTime = 2;
  private final int isSortByName = 1;
  private final int isSortByDefault = 0;
  private int mSortFlag = 0; //默认先排文件夹再文件

  public void add(FileBean bean, int mSortFlag) {
    if (bean == null) return;
    checkedMap.put(fileBeans.size(), false);
    visibleMap.put(fileBeans.size(), CheckBox.GONE);
    fileBeans.add(bean);
    sortFiles(mSortFlag);
    notifyDataSetChanged();
  }

  /**
  * 搜索文件的时候遇到重复的需要剔除
  *
  * @param
  * @return
  */
  public void add1(FileBean bean, int mSortFlag,List<FileBean> mTempList) {
    if (bean == null) return;
    checkedMap.put(fileBeans.size(), false);
    visibleMap.put(fileBeans.size(), CheckBox.GONE);
    Boolean isHasTheSameData = false;
    for (int i = 0; i < fileBeans.size(); i++) {
      if(bean.getName().equals(fileBeans.get(i).getName())){
        isHasTheSameData = true;
        break;
      }
    }

    if(isHasTheSameData) return;

    fileBeans.add(bean);
    mTempList.add(bean);

    sortFiles(mSortFlag);
    notifyDataSetChanged();
  }

  private void sortFiles(int mSortFlag) {
//         对文件/文件夹进行排序:文件夹到文件 默认排序
    try {
      switch (mSortFlag) {
        case isSortByName:
          SambaUtils.FileSortByName(fileBeans);
          break;
        case isSortByTime:
          SambaUtils.FileSortByTime(fileBeans);
          break;
        case isSortByDefault:
          SambaUtils.FileSortDefault(fileBeans);
          break;
        default:
          SambaUtils.FileSortDefault(fileBeans);
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void remove(int groupPosition) {
    if(groupPosition>fileBeans.size())return;
    fileBeans.remove(groupPosition);
    notifyDataSetChanged();
  }

  public static interface IViewType {
    int IVT_FILE = 0;// 文件
    int IVT_FOLDER = 1;// 文件夹
  }

  public Map<Integer, Boolean> checkedMap; // checkbox是否选中
  public Map<Integer, Integer> visibleMap; // checkbox是否可见

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

  public RemoteMainAdapter(Context context) {
    this.mContext = context;

    checkedMap = new HashMap<Integer, Boolean>(); //是否选中
    visibleMap = new HashMap<Integer, Integer>(); //是否可见

    for (int i = 0; i < fileBeans.size(); i++) {
      //数据初始化
      checkedMap.put(i, false);
      visibleMap.put(i, CheckBox.GONE);
    }

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
  public FileBean getGroup(int groupPosition) {
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

    if (convertView == null) {
      LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 初始化列表元素界面
      viewHolder = new GroupViewHolder();

      //文件
      convertView = mLI.inflate(R.layout.item_samba_file, null);
      viewHolder.mFileDate = (TextView) convertView.findViewById(R.id.tv_date_list_childs);
      viewHolder.mFileSize = (TextView) convertView.findViewById(R.id.tv_file_size);

      // 获取列表布局界面元素
      viewHolder.mIV = (ImageView) convertView.findViewById(R.id.image_list_childs);
      viewHolder.mTV = (TextView) convertView.findViewById(R.id.text_list_childs);
      viewHolder.mCB = (CheckBox) convertView.findViewById(R.id.cb_selectFile);
      viewHolder.mRlSpread = (RelativeLayout) convertView.findViewById(R.id.rl_spread);
      viewHolder.mGroupIcon = (ImageView) convertView.findViewById(R.id.img_spread);
      viewHolder.mLlContent = (LinearLayout) convertView.findViewById(R.id.ll_content);
      viewHolder.mLlDropView = (LinearLayout) convertView.findViewById(R.id.ll_edit_samba_file);

      // 将每一行的元素集合设置成标签
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (GroupViewHolder) convertView.getTag();
    }

    viewHolder.mCB.setTag(groupPosition);
    viewHolder.mCB.setOnCheckedChangeListener(this);
    //显示选择的选项
    viewHolder.mCB.setChecked(checkedMap.get(groupPosition));
    //显示可见的选项
    viewHolder.mCB.setVisibility(visibleMap.get(groupPosition));

    if (fileBeans.get(groupPosition).getFileType()) {

      viewHolder.mTV.setText(fileBeans.get(groupPosition).getName());
      //viewHolder.mFolderCount.setText("("+fileBeans.get(position).getCount()+")");
      viewHolder.mFileDate.setText(fileBeans.get(groupPosition).getLastModifyTime());
      viewHolder.mFileSize.setVisibility(View.GONE);

      viewHolder.mLlContent.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (mOnFolderClickListener != null) {
            mOnFolderClickListener.onFolderClick(v, groupPosition, fileBeans.get(groupPosition).getPath(), fileBeans.get(groupPosition).getKey(), fileBeans.get(groupPosition).getFileType());
          }
        }
      });
    } else {
      viewHolder.mTV.setText(fileBeans.get(groupPosition).getName());

      viewHolder.mLlContent.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                    /*return;*/
          if (mOnFolderClickListener != null) {
            mOnFolderClickListener.onFolderClick(v, groupPosition, fileBeans.get(groupPosition).getPath(), fileBeans.get(groupPosition).getKey(), fileBeans.get(groupPosition).getFileType());
          }
        }
      });

      try {
        viewHolder.mFileSize.setVisibility(View.VISIBLE);
        viewHolder.mFileSize.setText(fileBeans.get(groupPosition).getSize());
        viewHolder.mFileDate.setText(fileBeans.get(groupPosition).getLastModifyTime());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    viewHolder.mGroupIcon.setVisibility(View.VISIBLE);

    if (isExpanded)
      viewHolder.mGroupIcon.setImageResource(R.drawable.indexic_up);
    else
      viewHolder.mGroupIcon.setImageResource(R.drawable.indexic_down);

    setImage(fileBeans.get(groupPosition).getFileType(), fileBeans.get(groupPosition).getName(), viewHolder.mIV);

    return convertView;
  }

  @Override
  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    ChildViewHolder viewHolder1;

    if (convertView == null) {
      viewHolder1 = new ChildViewHolder();

      LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 初始化列表元素界面
      convertView = mLI.inflate(R.layout.item_samba_expandablelist, null);
      //viewHolder1.llChildContent = (LinearLayout) convertView.findViewById(R.id.ll_edit_samba_file);
      viewHolder1.gridView = (GridViewForExpandableListView) convertView.findViewById(R.id.gv_item_samba_expandview);
      convertView.setTag(viewHolder1);
    } else {
      viewHolder1 = (ChildViewHolder) convertView.getTag();
    }

    List<SingalFileOperateBean> itemBeans = new ArrayList();
    for (int i = 0; i < titles.length; i++) {
      SingalFileOperateBean bean = new SingalFileOperateBean();
      bean.setImg(imgs[i]);
      bean.setTitle(titles[i]);
      itemBeans.add(bean);
    }

    GridViewAdapter itemAdapter = new GridViewAdapter(mContext, itemBeans, fileBeans.get(groupPosition).getFileType());
    viewHolder1.gridView.setAdapter(itemAdapter);
    viewHolder1.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(mContext, "点击了->"+"组号："+groupPosition+"子号："+position, Toast.LENGTH_SHORT).show();*/

        // 文件夹不支持下载和移动操作 置灰不可点击
        if (fileBeans.get(groupPosition).getFileType()) {
          //文件夹
          if (position == 0 || position == 2) {
            return;
          }
        }

        if (mOnDetailClickListener != null) {
          mOnDetailClickListener.onDetailClick(view, groupPosition, position, fileBeans.get(groupPosition).getFileType());
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

  /**
   * 功能: checkbox的单击事件
   *
   * @param:
   * @return:
   */
  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    int row = (Integer) buttonView.getTag();
    checkedMap.put(row, isChecked);
  }

  // 用于存储列表每一行元素的图片和文本
  class GroupViewHolder {
    ImageView mIV;
    TextView mTV;
    TextView mFileDate;
    TextView mFolderCount;
    RelativeLayout mRlSpread;
    LinearLayout mLlContent;
    LinearLayout mLlDropView;
    CheckBox mCB;
    ImageView mGroupIcon;
    TextView mFileSize;
  }

  class ChildViewHolder {
    LinearLayout llChildContent;
    GridViewForExpandableListView gridView;
  }


  public interface OnDetailClickListener {
    void onDetailClick(View v, int groupPosition, int childPosition, Boolean isFolder);
  }

  public interface OnFolderClickListener {
    void onFolderClick(View v, int position, String path, Uri key, Boolean isFolder);
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


  private void setImage(boolean isFile, String fileName, ImageView view) {
    if (isFile) {
      view.setImageBitmap(mFolder);
    } else {
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

}
