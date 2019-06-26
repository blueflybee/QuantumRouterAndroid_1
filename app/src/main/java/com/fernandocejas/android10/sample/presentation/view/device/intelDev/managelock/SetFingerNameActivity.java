package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetFingerNameBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.DeleteFingerPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.IDeleteFingerView;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.IModifyFingerNameView;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.ModifyFingerNamePresenter;
import com.qtec.router.model.rsp.DeleteFingerResponse;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 设置指纹名称
 *      version: 1.0
 * </pre>
 */

public class SetFingerNameActivity extends BaseActivity implements IModifyFingerNameView, View.OnClickListener, IDeleteFingerView {
  private ActivitySetFingerNameBinding mBinding;
  private String isEdit = "";
  private String newFingerName = "";
  @Inject
  DeleteFingerPresenter mDeleteFingerPresenter;

  @Inject
  ModifyFingerNamePresenter mModifyFingerName;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_finger_name);

    isEdit = getIntent().getStringExtra(mNavigator.EXTRA_FINGER_EDIT_FLAG);

    if (isEdit.equals("1")) {
      initTitleBar("编辑指纹");
    } else {
      initTitleBar("设置指纹名称");
    }

    initializeInjector();
    initPresenter();
    initData();
    watchEnterNewName();

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        newFingerName = getText(mBinding.etNewFingerName);
        if(TextUtils.isEmpty(newFingerName)){
          Toast.makeText(getContext(), "请输入新名称", Toast.LENGTH_SHORT).show();
          return;
        }

        mModifyFingerName.postModifyFingerName(getRouterId(), getLockId(), getFingerId(), newFingerName);
      }
    });

  }

  private void watchEnterNewName() {
    mBinding.etNewFingerName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(TextUtils.isEmpty(s)){
          mBinding.imgClear.setVisibility(View.GONE);
        }else{
          mBinding.imgClear.setVisibility(View.VISIBLE);
          mBinding.imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mBinding.etNewFingerName.getText().clear();
            }
          });
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {
        int index = mBinding.etNewFingerName.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(editable.charAt(index))) {
            Editable edit = mBinding.etNewFingerName.getText();
            if(index == 1){
              edit.clear();// 解决第一个是表情而无法清除的情况
            }else{
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }

  private String getLockId() {
    return getIntent().getStringExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO);
  }

  private String getRouterId() {
    return getIntent().getStringExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM);
  }

  private String getFingerId() {
    return getIntent().getStringExtra(mNavigator.EXTRA_FINGER_ID);
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mModifyFingerName.setView(this);
    mDeleteFingerPresenter.setView(this);
  }

  private void initData() {
    mBinding.btnRightBigFinger.setOnClickListener(this);
    mBinding.btnLeftBigFinger.setOnClickListener(this);
    mBinding.btnMiddleFinger.setOnClickListener(this);
    mBinding.btnRingFinger.setOnClickListener(this);
    mBinding.btnSecondFinger.setOnClickListener(this);

    if (isEdit.equals("1")) {
      //处于编辑状态
      mBinding.btnDeleteFinger.setVisibility(View.VISIBLE);
      mBinding.btnDeleteFinger.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* new AlertDialog.Builder(SetFingerNameActivity.this)
                .setMessage("是否确定真的删除指纹")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    // 删除指纹请求
                    deleteFingerRequest();
                  }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                  }
                }).create().show();*/

          DialogUtil.showOkCancelAlertDialogWithMessage(getContext(), "删除指纹", "您确定要删除指纹吗？", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // 删除指纹请求
              deleteFingerRequest();
            }
          }, null);

        }
      });
    } else {
      mBinding.btnDeleteFinger.setVisibility(View.GONE);
    }
  }

  /**
   * 修改指纹名称
   *
   * @param
   * @return
   */
  @Override
  public void modifyFingerName(ModifyFingerNameResponse response) {
   /* Intent intent = new Intent();
    intent.putExtra("NEW_FINGER_NAME", mBinding.etNewFingerName.getText().toString().trim());
    intent.putExtra("FINGER_ID", getFingerId());
    mBinding.etNewFingerName.getText().clear();
    if ("1".equals(isEdit)) {
      intent.putExtra("DELETE_FINGER", 0);
      this.setResult(mNavigator.EDIT_FINGER_NAME, intent);
    } else {
      this.setResult(mNavigator.ADD_FINGER_NAME, intent);
    }*/
    Intent intent = new Intent(this, LockMainActivity.class);
    intent.putExtra("addFingerBack",5);
    startActivity(intent);

    this.finish();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_rightBigFinger:
        mBinding.etNewFingerName.setText(mBinding.btnRightBigFinger.getText());
        break;

      case R.id.btn_leftBigFinger:
        mBinding.etNewFingerName.setText(mBinding.btnLeftBigFinger.getText());
        break;

      case R.id.btn_middleFinger:
        mBinding.etNewFingerName.setText(mBinding.btnMiddleFinger.getText());
        break;

      case R.id.btn_secondFinger:
        mBinding.etNewFingerName.setText(mBinding.btnSecondFinger.getText());
        break;

      case R.id.btn_ringFinger:
        mBinding.etNewFingerName.setText(mBinding.btnRingFinger.getText());
        break;

      default:
        break;
    }
  }

  /**
   * 删除指纹请求
   *
   * @param
   * @return
   */
  private void deleteFingerRequest() {
    mDeleteFingerPresenter.postDeleteFingerInfo(getRouterId(), getLockId(), getFingerId());
  }

  /**
   * 删除指纹
   *
   * @param
   * @return
   */
  @Override
  public void deleteFingerInfo(DeleteFingerResponse response) {
    /*Toast.makeText(this, "指纹删除成功", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent();
    intent.putExtra("FINGER_ID", getFingerId());
    intent.putExtra("DELETE_FINGER", "1");
    this.setResult(mNavigator.EDIT_FINGER_NAME, intent);*/
    this.finish();
  }

  /**
   * 判断是否是表情
   *
   * @param
   * @return
   */
  private static boolean isEmojiCharacter(char codePoint) {
    return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
  }
}
