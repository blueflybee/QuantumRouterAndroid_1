package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//自定义Adapter内部类
public class UploadFinishedAdapter extends BaseAdapter {
  private Context mContext;
  private List<FileUploadBean> fileBeans;
  public Map<Integer, Boolean> checkedMap; // 保存checkbox是否被选中的状态
  public Map<Integer, Integer> visibleMap; // 保存checkbox是否显示的状态
  private TextView mFinishTitle;

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

  public UploadFinishedAdapter(Context context, List<FileUploadBean> fileBeans, TextView mFinishTitle) {
    this.mContext = context;
    this.fileBeans = fileBeans;
    this.mFinishTitle = mFinishTitle;

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
  public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder viewHolder = null;
    final FileUploadBean entity = fileBeans.get(position);

    if (convertView == null) {
      LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // 初始化列表元素界面
      viewHolder = new ViewHolder();

      //文件
      convertView = mLI.inflate(R.layout.item_upload_finished, null);
      viewHolder.mDate = (TextView) convertView.findViewById(R.id.tv_transit_date);

      // 获取列表布局界面元素
      viewHolder.mHeadImg = (ImageView) convertView.findViewById(R.id.img_transit_headIcon);
      viewHolder.mName = (TextView) convertView.findViewById(R.id.tv_transit_name);
      viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_select_smb_file);
      // 将每一行的元素集合设置成标签
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.mName.setText(fileBeans.get(position).getName());

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
      viewHolder.mDate.setText(fileBeans.get(position).getDate());
    } catch (Exception e) {
      e.printStackTrace();
    }

    setImage(fileBeans.get(position).getName(), viewHolder.mHeadImg);

    /*    try {
            // TODO 2017 8 9 此处是否需要auth
           // mFile = new SmbFile(fileBeans.get(position).toString());
            //String fileName = mFile.getName();

                viewHolder.mName.setText(fileBeans.get(position).getName());

                try {
                    viewHolder.mSize.setText(fileBeans.get(position).getSize());
                }catch (Exception e){
                    e.printStackTrace();
                }
            //setImage(mFile.isDirectory(), fileName, viewHolder.mHeadImg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }*/
    return convertView;
  }

  public void remove(int i) {
    fileBeans.remove(i);
    notifyDataSetChanged();
  }

  public void removeContainData(FileUploadBean fileUploadBean, DatabaseUtil mDBUtil1) {
    if (fileUploadBean == null) return;
    if (fileBeans.contains(fileUploadBean)) {
      fileBeans.remove(fileUploadBean);
      notifyDataSetChanged();

      if (mDBUtil1 != null) {
        mDBUtil1.deleteUploadUrls(fileUploadBean.getPath());
      }
    }
  }

  public void add(FileUploadBean bean) {
    if (bean == null) return;
    Boolean isHasSameData = false;

    for (int i = 0; i < fileBeans.size(); i++) {
      if (fileBeans.get(i).getName().equals(bean.getName())) {
        isHasSameData = true;
        fileBeans.get(i).setDate(bean.getDate());
        break;
      }
    }

    if (isHasSameData) {
      notifyDataSetChanged();
    } else {
      checkedMap.put(fileBeans.size(), false);
      visibleMap.put(fileBeans.size(), CheckBox.GONE);
      fileBeans.add(bean);
      mFinishTitle.setText("上传成功(" + fileBeans.size() + ")");
      notifyDataSetChanged();
    }
  }

  // 用于存储列表每一行元素的图片和文本
  class ViewHolder {
    ImageView mHeadImg;
    TextView mName;
    TextView mDate;
    CheckBox mCheckBox;
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
