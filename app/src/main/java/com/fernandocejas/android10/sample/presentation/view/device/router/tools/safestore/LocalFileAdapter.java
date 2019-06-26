package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//自定义Adapter内部类
public class LocalFileAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
    private Context mContext;
    // 文件名列表
    private List<FileBean> fileBeans;
    // 文件对应的路径列表
    private Button mBtnUpload;

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

    public LocalFileAdapter(Context context, List<FileBean> fileBeans,Button upload) {
        this.mContext = context;
        mBtnUpload = upload;
        this.fileBeans = fileBeans;

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

    // 获得文件的总数
    public int getCount() {
        return fileBeans.size();
    }

    // 获得当前位置对应的文件名
    public FileBean getItem(int position) {
        return fileBeans.get(position);
    }

    // 获得当前的位置
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("WrongConstant")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)   {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 初始化列表元素界面
            convertView = mLI.inflate(R.layout.item_samba_file, null);
            // 获取列表布局界面元素
            viewHolder.mIV = (ImageView) convertView.findViewById(R.id.image_list_childs);
            viewHolder.mTV = (TextView) convertView.findViewById(R.id.text_list_childs);
            viewHolder.mCB = (CheckBox) convertView.findViewById(R.id.cb_selectFile);
            viewHolder.mFileDate = (TextView) convertView.findViewById(R.id.tv_date_list_childs);
            // 将每一行的元素集合设置成标签
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mCB.setTag(position);
        viewHolder.mCB.setOnCheckedChangeListener(this);

        try {
            if (fileBeans.get(position).getFileType()) {
                //文件夹
                viewHolder.mCB.setVisibility(View.GONE);

                //viewHolder.mTV.setText( fileBeans.get(position).getName().substring(0,  fileBeans.get(position).getName().length() - 1));
                viewHolder.mTV.setText( fileBeans.get(position).getName());
                //viewHolder.mFolderCount.setText("("+fileBeans.get(position).getCount()+")");
                viewHolder.mFileDate.setText(fileBeans.get(position).getLastModifyTime());
            } else {
                viewHolder.mTV.setText(fileBeans.get(position).getName());
                //显示选择的选项
                viewHolder.mCB.setChecked(checkedMap.get(position));
                //显示可见的选项
                viewHolder.mCB.setVisibility(visibleMap.get(position));

                try {
                    viewHolder.mFileDate.setText(fileBeans.get(position).getLastModifyTime()+"  "+fileBeans.get(position).getSize());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            setImage(fileBeans.get(position).getFileType(), fileBeans.get(position).getName(), viewHolder.mIV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    /**
     * 功能: checkbox的单击事件
     * @param:
     * @return:
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int row = (Integer) buttonView.getTag();
        int count = 0;
        checkedMap.put(row,isChecked);
        mBtnUpload.setVisibility(View.VISIBLE);
        for (int i = 0; i < fileBeans.size(); i++) {
            //统计文件的个数
            if(!fileBeans.get(i).getFileType()){
                if(checkedMap.get(i)){
                    count++;
                }
            }
        }
        mBtnUpload.setText("上传("+count+")");

    }

    // 用于存储列表每一行元素的图片和文本
    class ViewHolder {
        ImageView mIV;
        TextView mTV;
        CheckBox mCB;
        TextView mFileDate;
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
