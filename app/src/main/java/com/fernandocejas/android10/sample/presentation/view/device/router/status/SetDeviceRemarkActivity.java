package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetDeviceNameBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetDeviceRemarkBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ModifyLockNamePresenter;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 修改设备备注
 *      version: 1.0
 * </pre>
 */

public class SetDeviceRemarkActivity extends BaseActivity {
    // implements IModifyLockNameView
    private ActivitySetDeviceRemarkBinding mBinding;
    private String newLockName = "";
    @Inject
    ModifyLockNamePresenter modifyLockNamePresenter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_device_remark);

        initTitleBar("设备备注");
        /*initializeInjector();
        initPresenter();
*/
        watchEnterNewName();

        mTitleBar.setRightAs("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改设备名称
                modifyLockName();
            }
        });

    }

    private void watchEnterNewName() {
        mBinding.etNewDeviceRemark.addTextChangedListener(new TextWatcher() {
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
                            mBinding.etNewDeviceRemark.getText().clear();
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int index = mBinding.etNewDeviceRemark.getSelectionStart() - 1;
                if (index > 0) {
                    if (isEmojiCharacter(editable.charAt(index))) {
                        Editable edit = mBinding.etNewDeviceRemark.getText();
                        edit.delete(index, index + 1);
                    }
                }
            }
        });
    }

    private void initializeInjector() {
       /* DaggerRouterComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .routerModule(new RouterModule())
            .build()
            .inject(this);*/
    }
/*
    private void initPresenter() {
        modifyLockNamePresenter.setView(this);
    }*/

    private String getLockId() {
        return getIntent().getStringExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO);
    }

    private void modifyLockName() {

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
/*
    @Override
    public void showModifySuccess(IntelDevInfoModifyResponse response) {
        Intent intent = new Intent();
        intent.putExtra("NEW_LOCK_NAME",newLockName);
        this.setResult(mNavigator.MODIFY_LOCK_NAME, intent);
        this.finish();
    }*/
}
