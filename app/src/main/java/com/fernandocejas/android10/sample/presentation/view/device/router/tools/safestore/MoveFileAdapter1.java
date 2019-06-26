package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.adapter.BaseViewHolder;
import com.fernandocejas.android10.sample.presentation.view.adapter.CommAdapter;

import java.util.List;

//自定义Adapter内部类
public class MoveFileAdapter1 extends CommAdapter<FileBean> {

    public MoveFileAdapter1(Context context, List<FileBean> fileBeans,int layoutId) {
        super(context, fileBeans, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileBean fileBean) {
       TextView name = baseViewHolder.getView(R.id.tv_move_file_name);
       TextView date = baseViewHolder.getView(R.id.tv_move_file_date);

      //文件夹
      name.setText(fileBean.getName().substring(0,  fileBean.getName().length()));

      date.setText(fileBean.getDate());
    }
}
