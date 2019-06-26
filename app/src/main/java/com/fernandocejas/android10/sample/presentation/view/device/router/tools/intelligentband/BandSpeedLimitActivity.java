package com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySpeedLimitBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.GetQosInfoResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : 带宽限制
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BandSpeedLimitActivity extends BaseActivity{
  private ActivitySpeedLimitBinding mBinding;
  private String SAVE_FLAG = "11";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_speed_limit);

    initTitleBar("网关限制");

    initData();

    mTitleBar.setRightAs("保存", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 点击了保存 保存到本地
        if((TextUtils.isEmpty(getText(mBinding.etDownloadSpeed)))||(TextUtils.isEmpty(getText(mBinding.etUploadSpeed)))){
          Toast.makeText(BandSpeedLimitActivity.this, "参数不能为空", Toast.LENGTH_SHORT).show();
          return;
        }

        String downloadSpeed = "",uploadSpeed = "";

        if(getText(mBinding.etDownloadSpeed).indexOf(".") != -1){
          //包含.
          downloadSpeed = getText(mBinding.etDownloadSpeed).substring(0,getText(mBinding.etDownloadSpeed).indexOf("."));
          System.out.println("打印 downloadSpeed = " + downloadSpeed);
        }else{
          downloadSpeed = getText(mBinding.etDownloadSpeed);
          System.out.println("打印 downloadSpeed = " + downloadSpeed);
        }

        if(getText(mBinding.etUploadSpeed).indexOf(".") != -1){
          //包含.
          uploadSpeed = getText(mBinding.etUploadSpeed).substring(0,getText(mBinding.etUploadSpeed).indexOf("."));
          System.out.println("打印 uploadSpeed = " + uploadSpeed);
        }else{
          uploadSpeed = getText(mBinding.etUploadSpeed);
          System.out.println("打印 uploadSpeed = " + uploadSpeed);
        }

        //因为string的长度太长的话 会出现Integer.parseInt(downloadSpeed)奔溃的现象 所以作特殊处理
        if(downloadSpeed.length() > 5){
          Toast.makeText(BandSpeedLimitActivity.this, "最大值不能超过1000", Toast.LENGTH_SHORT).show();
          mBinding.etDownloadSpeed.getText().clear();
          return;
        }

        if(uploadSpeed.length() > 5){
          Toast.makeText(BandSpeedLimitActivity.this, "最大值不能超过1000", Toast.LENGTH_SHORT).show();
          mBinding.etUploadSpeed.getText().clear();
          return;
        }

        if((Integer.parseInt(downloadSpeed) > 1000)){
          Toast.makeText(BandSpeedLimitActivity.this, "最大值不能超过1000", Toast.LENGTH_SHORT).show();
          mBinding.etDownloadSpeed.getText().clear();
          return;
        }

        if((Integer.parseInt(uploadSpeed) > 1000)){
          Toast.makeText(BandSpeedLimitActivity.this, "最大值不能超过1000", Toast.LENGTH_SHORT).show();
          mBinding.etUploadSpeed.getText().clear();
          return;
        }

        Intent intent = new Intent();
        intent.putExtra("DownloadSpeed",downloadSpeed);//传string
        intent.putExtra("UploadSpeed",uploadSpeed);
        intent.putExtra("DownloadSpeed_Int",Integer.parseInt(downloadSpeed));//传int
        intent.putExtra("UploadSpeed_Int",Integer.parseInt(uploadSpeed));
        setResult(4,intent);
        finish();

      }
    });
  }


  private void initData() {
    if(getSpeedInfo().getDownload() > 1000*1024){
      mBinding.etDownloadSpeed.setText("0.00Mbps");
    }else {
      mBinding.etDownloadSpeed.setText(String.format("%.2f",Float.parseFloat((getSpeedInfo().getDownload()*1f/1024)+"")) + "Mbps");
    }

    if(getSpeedInfo().getUpload() > 1000*1024){
      mBinding.etUploadSpeed.setText("0.00Mbps");
    }else {
      mBinding.etUploadSpeed.setText(String.format("%.2f",Float.parseFloat((getSpeedInfo().getUpload()*1f/1024)+"")) + "Mbps");
    }

    //光标放在最后
    Selection.setSelection(mBinding.etDownloadSpeed.getText(), mBinding.etDownloadSpeed.getText().length());  //当号码返回时光标放在最后
    Selection.setSelection(mBinding.etUploadSpeed.getText(), mBinding.etUploadSpeed.getText().length());

    mBinding.etDownloadSpeed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBinding.etDownloadSpeed.setFocusable(true);
        mBinding.etDownloadSpeed.setFocusableInTouchMode(true);
        mBinding.etDownloadSpeed.requestFocus();
      }
    });

    mBinding.etUploadSpeed.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBinding.etUploadSpeed.setFocusable(true);
        mBinding.etUploadSpeed.setFocusableInTouchMode(true);
        mBinding.etUploadSpeed.requestFocus();
      }
    });

    mBinding.etDownloadSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
          mBinding.etDownloadSpeed.getText().clear();
        }else {
          //失去焦点
          if(!TextUtils.isEmpty(getText(mBinding.etDownloadSpeed))){
            mBinding.etDownloadSpeed.setText(getText(mBinding.etDownloadSpeed)+".00Mbps");
          }

        }
      }
    });

    mBinding.etUploadSpeed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
          mBinding.etUploadSpeed.getText().clear();
        }else {
          //失去焦点
          if(!TextUtils.isEmpty(getText(mBinding.etUploadSpeed))){
            mBinding.etUploadSpeed.setText(getText(mBinding.etUploadSpeed)+".00Mbps");
          }

        }
      }
    });

    mBinding.rlOther.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        //触摸事件

        mBinding.etDownloadSpeed.setFocusable(false);
        mBinding.etUploadSpeed.setFocusable(false);

        return true;
      }
    });

  }

  private GetQosInfoResponse getSpeedInfo(){
    return (GetQosInfoResponse) getIntent().getSerializableExtra("SPEEDINFO");
  }
}
