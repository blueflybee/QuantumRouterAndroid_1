package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityDealWarningInfoBinding;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ExceptionWarnMoreActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/30
 *      desc: 告警信息弹窗
 *      version: 1.0
 * </PRE>
 */

public class DealWarningInfoActivity extends Activity{
    private ActivityDealWarningInfoBinding mBinding;
    private String warningContent = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deal_warning_info);
        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        warningContent = getIntent().getStringExtra("UNLOCK_WARNING_INFO");

        initData();
    }

    private void initData() {

            if(!TextUtils.isEmpty(warningContent)){
                mBinding.tvContent.setText(warningContent);
            }

            findViewById(R.id.btn_ingore).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DealWarningInfoActivity.this.finish();
                }
            });

            findViewById(R.id.btn_detail).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DealWarningInfoActivity.this, ExceptionWarnMoreActivity.class));
                    DealWarningInfoActivity.this.finish();
                }
            });

    }

}
