package com.fernandocejas.android10.sample.presentation.view.mine.aboutus;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUserAgreementBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 获取隐私协议
 *      version: 1.0
 * </pre>
 */

public class GetSecretAgreementActivity extends BaseActivity {
    private ActivityUserAgreementBinding mBinding;
    private WebView mWebView;
    private String url = "https://appdoc-3caretec.oss-cn-hangzhou.aliyuncs.com/userPrivacyStatement.htm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_agreement);
        mNavigator = new Navigator();
        initTitleBar("隐私协议");

        initData();
    }

    private void initData() {
       /* if(TextUtils.isEmpty(getAgreementContent())){
            mUserAgreementBinding.setAgreementContent("暂时没有内容哦!");
        }else{
            mUserAgreementBinding.setAgreementContent(getAgreementContent());
        }*/
        mWebView = (WebView) findViewById(R.id.webView);
        /**
         * WebViewClient主要帮助WebView处理各种通知、请求事件的，
         * WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度，具体可重写的接口大家可自行查阅SDK文档
         */
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        mWebView.loadUrl(url);
    }

   /* private String getAgreementContent(){
        return getIntent().getStringExtra(mNavigator.EXTR_SERECT_AGREEMENT_CONTENT);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防内存泄漏
        if(mWebView != null){
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

}
