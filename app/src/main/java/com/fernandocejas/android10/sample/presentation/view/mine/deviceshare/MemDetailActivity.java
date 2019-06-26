package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMemDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.req.GetMemRemarkNameRequest;
import com.qtec.mapp.model.rsp.GetMemRemarkNameResponse;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 用户详情
 *      version: 1.0
 * </pre>
 */

public class MemDetailActivity extends BaseActivity implements IModifyRemarkNameView {
  private ActivityMemDetailBinding mBinding;
  private String newRemarkName = "";

  @Inject
  ModifyRemarkNamePresenter modifyRemarkNamePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mem_detail);
    initTitleBar("用户详情");

    initializeInjector();
    initPresenter();

    initView();

    watchEnterNewName();

    mTitleBar.setRightAs("保存", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        modifyRemarkNameRequest();
      }
    });
  }

  private void initView() {
    //mBinding.circleImgHead.setImageResource(R.drawable.circle_icon);
    GlideUtil.loadCircleHeadImage(this, getMemInfo().getSharedUserHeadImage(), mBinding.imgHead, R.drawable.default_avatar);

    mBinding.tvUserName.setText(getMemInfo().getSharedUserName() + "(" + getMemInfo().getSharedUserPhone() + ")");
    mBinding.tvInviteTime.setText(getMemInfo().getShareDate());
    mBinding.etRemarkName.setText(getMemInfo().getSharedUserName());
    mBinding.etRemarkName.setSelection(getMemInfo().getSharedUserName().length());//光标放在最后

  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    modifyRemarkNamePresenter.setView(this);
  }

  private void watchEnterNewName() {
    mBinding.etRemarkName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
              /*  if(TextUtils.isEmpty(s)){
                }else{
                    mBinding.imgClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBinding.etRemarkName.getText().clear();
                        }
                    });
                }*/
      }

      @Override
      public void afterTextChanged(Editable editable) {
        int index = mBinding.etRemarkName.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(editable.charAt(index))) {
            Editable edit = mBinding.etRemarkName.getText();
            if (index == 1) {
              edit.clear();
            } else {
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }

  private void modifyRemarkNameRequest() {
    newRemarkName = getText(mBinding.etRemarkName);

    QtecEncryptInfo<GetMemRemarkNameRequest> encryptInfo = new QtecEncryptInfo<>();
    GetMemRemarkNameRequest data = new GetMemRemarkNameRequest();
    data.setHistoryUniqueKey(getMemInfo().getHistoryUniqueKey());
    data.setRemark(newRemarkName);
    encryptInfo.setData(data);
    modifyRemarkNamePresenter.modifyRemarkName(encryptInfo);
  }

  private GetShareMemListResponse getMemInfo() {
    return (GetShareMemListResponse) getIntent().getSerializableExtra(mNavigator.EXTRA_MEM_DETAIL);
  }

  /**
   * 判断是否是表情
   *
   * @param
   * @return
   */
  private static boolean isEmojiCharacter(char codePoint) {
    return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
  }

  /**
   * 修改备注名 返回
   *
   * @param
   * @return
   */
  @Override
  public void showMemRemarkName(GetMemRemarkNameResponse response) {
    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    finish();
  }

}
