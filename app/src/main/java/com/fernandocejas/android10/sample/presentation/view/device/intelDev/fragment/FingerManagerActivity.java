package com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentFingerManagerBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.DeleteFingerPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.IDeleteFingerView;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.IQueryFingerView;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.QueryFingerInfoPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.SetFingerNameActivity;
import com.qtec.router.model.rsp.DeleteFingerResponse;
import com.qtec.router.model.rsp.QueryFingerInfoResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc: 指纹管理
 *      version: 1.0
 * </pre>
 */
public class FingerManagerActivity extends BaseActivity implements View.OnClickListener, IQueryFingerView,IDeleteFingerView {

  private FragmentFingerManagerBinding mBinding;
  private View mDeleteView;

  @Inject
  DeleteFingerPresenter mDeleteFingerPresenter;
  @Inject
  QueryFingerInfoPresenter mQueryFingerInfo;

  private List<TextView> mViewLists = null;
  private QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>> mFingerInfoList;// FingerInfoBean

  public static FingerManagerActivity newInstance() {
    FingerManagerActivity fragment = new FingerManagerActivity();
    return fragment;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.fragment_finger_manager);

    initView();
    initializeInjector();
    initPresenter();
    queryFingerInfo();
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
    mDeleteFingerPresenter.setView(this);
    mQueryFingerInfo.setView(this);
  }

  private void initView() {
    mBinding.tvAddFinger.setOnClickListener(this);

    mViewLists = new ArrayList<TextView>();
    mFingerInfoList = new QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>();

  }

  private String getLockId() {
    return getIntent().getStringExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO);
  }

  private String getRouterId() {
    return getIntent().getStringExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM);
  }

  private void initEvent() {
    for (int i = 0; i < mViewLists.size(); i++) {
      mViewLists.get(i).setTag(i);
      mViewLists.get(i).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getContext(), SetFingerNameActivity.class);
          int index = (int) view.getTag();
          intent.putExtra(mNavigator.EXTRA_FINGER_EDIT_FLAG, "1"); //1代表 指纹处于编辑状态，其他非编辑
          intent.putExtra(mNavigator.EXTRA_FINGER_ID, mFingerInfoList.getFingerprintlist().get(index).getFpid());
          intent.putExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO, getLockId());
          intent.putExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM, getRouterId());
          startActivityForResult(intent, mNavigator.EDIT_FINGER_NAME);
        }
      });

      mViewLists.get(i).setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          //删除指纹
          int index = (int) view.getTag();
          mDeleteView = view;
          deleteFingerRequest(index);
          return true;
        }
      });
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_addFinger:
        Intent intent = new Intent();
        intent.putExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO, getLockId());
        intent.putExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM, getRouterId());
        mNavigator.navigateTo(getContext(),AddFingerActivity.class,intent);
        break;

      default:
        break;
    }
  }

  /**
   * 指纹获取
   *
   * @param
   * @return
   */
  private void queryFingerInfo() {
    mQueryFingerInfo.queryFingerInfo(getRouterId(), getLockId());
  }



  /**
   * 删除指纹请求
   *
   * @param
   * @return
   */
  private void deleteFingerRequest(int position) {
    mDeleteFingerPresenter.postDeleteFingerInfo(getRouterId(), getLockId(), mFingerInfoList.getFingerprintlist().get(position).getFpid());
  }

  /**
   * 添加指纹
   *
   * @param
   * @return
   *//*
  @Override
  public void getFingerInfo(AddFingerResponse response) {
    //response.setFpid(response.getFpid());
   *//* FingerInfoBean fingerBean = new FingerInfoBean();
    fingerBean.setFingerID(response.getFpid());*//*
    QueryFingerInfoResponse.FingerInfo fingerBean = new QueryFingerInfoResponse.FingerInfo();
    fingerBean.setFpid(response.getFpid());
    mFingerInfoList.getFingerprintlist().add(fingerBean);

    Intent intent = new Intent(getContext(), SetFingerNameActivity.class);
    intent.putExtra(mNavigator.EXTRA_FINGER_EDIT_FLAG, "0"); //1代表 指纹处于编辑状态，其他非编辑

    intent.putExtra(mNavigator.EXTRA_FINGER_ID, response.getFpid());

    intent.putExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO, getLockId());
    intent.putExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM, getRouterId());

    startActivityForResult(intent, mNavigator.ADD_FINGER_NAME);
  }*/

  /**
   * 删除指纹
   *
   * @param
   * @return
   */
  @Override
  public void deleteFingerInfo(DeleteFingerResponse response) {
    //Toast.makeText(getContext(), "指纹删除成功", Toast.LENGTH_SHORT).show();
    mBinding.flFindgerList.removeView(mDeleteView);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //super.onActivityResult(requestCode, resultCode, data);
    mBinding.flFindgerList.removeAllViews();
    mFingerInfoList.getFingerprintlist().clear();
    mViewLists.clear();
    queryFingerInfo();
    /*if (!(data == null)) {
      //主要是考虑按返回键没有setResult的问题
      if (requestCode == mNavigator.ADD_FINGER_NAME) {
        String newFingerName = data.getStringExtra("NEW_FINGER_NAME");
        String fingerID = data.getStringExtra("FINGER_ID");

        for (int i = 0; i < mFingerInfoList.size(); i++) {
          if (fingerID.equals(mFingerInfoList.get(i).getFpid())) {
            if (TextUtils.isEmpty(newFingerName)) {
              if (TextUtils.isEmpty(mFingerInfoList.get(i).getFpname())) {
                newFingerName = "未命名";
              } else {
                newFingerName = mFingerInfoList.get(i).getFpname();
              }
            }
            mFingerInfoList.get(i).setFpname(newFingerName);
          }
        }
        addSingleTextView(newFingerName);
        initEvent();

      } else if (requestCode == mNavigator.EDIT_FINGER_NAME) {
        String deleteFlag = data.getStringExtra("DELETE_FINGER");
        String fingerID = data.getStringExtra("FINGER_ID");
        if ("1".equals(deleteFlag)) {
          //删除指纹
          for (int i = 0; i < mFingerInfoList.size(); i++) {
            if (fingerID.equals(mFingerInfoList.get(i).getFpid())) {
              mBinding.flFindgerList.removeView(mViewLists.get(i));
            }
          }
        } else {
          String newFingerName = data.getStringExtra("NEW_FINGER_NAME");
          for (int i = 0; i < mFingerInfoList.size(); i++) {
            if (fingerID.equals(mFingerInfoList.get(i).getFpid())) {
              mFingerInfoList.get(i).setFpname(newFingerName);
              mViewLists.get(i).setText(newFingerName);
            }
          }
        }
      }
    }*/

  }

  /**
   * 添加流式布局的Textview
   *
   * @param
   * @return
   */
  private void addAllTextView() {
    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.leftMargin = 5;
    lp.rightMargin = 5;
    lp.topMargin = 5;
    lp.bottomMargin = 5;
    for (int i = 0; i < mFingerInfoList.getFingerprintlist().size(); i++) {
      TextView view = new TextView(getContext());
      view.setText(mFingerInfoList.getFingerprintlist().get(i).getFpname());
      view.setTextColor(getResources().getColor(R.color.blue_1976d2));
      view.setTextSize(15);
      view.setGravity(Gravity.CENTER);
      view.setSingleLine(true);
      view.setEllipsize(TextUtils.TruncateAt.END);
      view.setBackgroundDrawable(getResources().getDrawable(R.drawable.fingerprint_button));
      //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_background));
      //mFlowLayout.addView(view, lp);
      mViewLists.add(view);
      mBinding.flFindgerList.addView(view, lp);
    }
  }

  /**
   * 添加单个textview
   *
   * @param
   * @return
   */
  private void addSingleTextView(String newName) {
    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.leftMargin = 5;
    lp.rightMargin = 5;
    lp.topMargin = 5;
    lp.bottomMargin = 5;
    TextView view = new TextView(getContext());
    view.setText(newName);
    view.setTextColor(Color.WHITE);
    view.setSingleLine(true);
    view.setEllipsize(TextUtils.TruncateAt.END);
    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_background));
    //mFlowLayout.addView(view, lp);
    mViewLists.add(view);
    mBinding.flFindgerList.addView(view, lp);
  }

  @Override
  public void showFingerInfo(QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>> response) {
    //mFingerInfoList.addAll(response);
    mFingerInfoList.setFingerprintlist(response.getFingerprintlist());

    if(mFingerInfoList.getFingerprintlist().size() != 0){
      mBinding.flFindgerList.setVisibility(View.VISIBLE);
      mBinding.tvNoFinger.setVisibility(View.GONE);
      addAllTextView();
      initEvent();
    }else{
      //没有指纹
      mBinding.tvNoFinger.setVisibility(View.VISIBLE);
      mBinding.flFindgerList.setVisibility(View.GONE);
    }

  }
}