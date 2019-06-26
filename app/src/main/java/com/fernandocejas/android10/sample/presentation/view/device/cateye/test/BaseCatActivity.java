package com.fernandocejas.android10.sample.presentation.view.device.cateye.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.MyProgressDialog;
import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;

/**
 * Created by juyang on 16/3/22.
 */
public abstract class BaseCatActivity extends BaseActivity implements IHandlerNotify, IHandlerLikeNotify {

    protected MyHandler handler = new MyHandler(this);
    private IHandlerNotify handlerNotify = this;

    /**
     * 初始化设置，不要在这里写费时的操作
     */
    protected abstract void initSettings();

    /**
     * 初始化界面，不要在这里写费时的操作
     */
    protected abstract void initUi();

    /**
     * 保存设置，不要在这里写费时的操作
     */
    protected abstract void saveSettings();

    /**
     * 释放资源、解锁、删除不用的对象，不要在这里写费时的操作
     */
    protected abstract void freeMe();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ((AndroidApplication) getApplication()).push(this);
        ((AndroidApplication) getApplication()).setCurrentNotify(this);
        initSettings();
        initUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        saveSettings();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ((AndroidApplication) getApplication()).pop();
        freeMe();
        super.onDestroy();
    }

    protected class MyHandler extends android.os.Handler {

        private BaseCatActivity activity;

        public MyHandler(BaseCatActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            activity.handlerNotify.onHandler(msg.what, msg.arg1, msg.arg2, msg.obj);
            super.handleMessage(msg);
        }

    }
}
