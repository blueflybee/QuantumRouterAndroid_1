package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityExceptionWarnListBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter.ExceptionWarnMoreAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.adapter.LockUseNoteMoreAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.GetUnlockInfoListPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.IGetUnlockInfoListView;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.AllQuestionActivity;
import com.qtec.mapp.model.req.GetUnlockInfoListRequest;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 异常提醒
 *      version: 1.0
 * </pre>
 */

public class ExceptionWarnMoreActivity extends BaseActivity implements IGetUnlockInfoListView {
  private ExceptionWarnMoreAdapter mExceptionWarnListAdapter;
  private ActivityExceptionWarnListBinding mBinding;
  private PtrClassicFrameLayout mPtrFrame;
  private List<GetUnlockInfoListResponse> mAllUnlockInfoList;
  private int mPageNum = 0;
  private Boolean isHasNoMoreData = false;

  @Inject
  GetUnlockInfoListPresenter mGetUnlockInfoListPresenter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_exception_warn_list);
    initTitleBar("异常状态提醒");
    initView();
    initializeInjector();
    initPresenter();
    dealWithRefresh();
  }

  private void initView() {
    mPtrFrame = mBinding.ptrLayout;
    mAllUnlockInfoList = new ArrayList<>();
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
    mGetUnlockInfoListPresenter.setView(this);
  }

  private String getRouterId() {
    return getIntent().getStringExtra(Navigator.EXTR_ROUTER_SERIAL_NUM);
  }

  private String getLockId() {
    return getIntent().getStringExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO);
  }

  /**
   * 处理刷新和加载
   *
   * @param
   * @return
   */
  private void dealWithRefresh() {
    //下拉刷新支持时间
    mPtrFrame.setLastUpdateTimeRelateObject(this);
    mPtrFrame.setDurationToClose(1000);
    mPtrFrame.disableWhenHorizontalMove(true);//解决横向滑动冲突
    //进入Activity自动下拉刷新
    mPtrFrame.postDelayed(new Runnable() {
      @Override
      public void run() {
        //queryExceptionWarnList("0", "8");
        mPtrFrame.autoRefresh();
      }
    }, 300);

    mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
      // 上拉加载的回调方法
      @Override
      public void onLoadMoreBegin(PtrFrameLayout frame) {
        mPtrFrame.postDelayed(new Runnable() {
          @Override
          public void run() {
            if(isHasNoMoreData){
              Toast.makeText(ExceptionWarnMoreActivity.this, "已加载所有数据，请稍候再试", Toast.LENGTH_SHORT).show();
              mPtrFrame.refreshComplete();
              return;
            }else {
              queryExceptionWarnList("" + mAllUnlockInfoList.size(), "8");
            }

            //mPtrFrame.refreshComplete();
          }
        }, 1000);
      }

      // 下拉刷新的回调方法
      @Override
      public void onRefreshBegin(PtrFrameLayout frame) {
        mPtrFrame.postDelayed(new Runnable() {
          @Override
          public void run() {
            // mPtrFrame.autoLoadMore();
            //mPtrFrame.refreshComplete();

            if (mAllUnlockInfoList.size() == 0) {
              mPageNum = 0;
              queryExceptionWarnList("0", "8");
            } else {
              mExceptionWarnListAdapter.notifyDataSetChanged();
              mPtrFrame.refreshComplete();
            }

          }
        }, 100);
      }

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return super.checkCanDoLoadMore(frame, mBinding.lvExceptionWarnNote, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return super.checkCanDoRefresh(frame, mBinding.lvExceptionWarnNote, header);
      }
    });

  }

  /**
   * 异常记录列表
   *
   * @param
   * @return
   */
  private void queryExceptionWarnList(String start, String pageSize) {
    GetUnlockInfoListRequest request = new GetUnlockInfoListRequest();
    request.setDeviceSerialNo(getLockId());
    request.setPageSize(pageSize);
    request.setStart(start);
    request.setType("1");
    if("0".equals(start)){
      request.setRecordUniqueKey("-1");
    }else {
      if(mAllUnlockInfoList != null && mAllUnlockInfoList.size()>1){
        request.setRecordUniqueKey(mAllUnlockInfoList.get(mAllUnlockInfoList.size()-1).getRecordUniqueKey());
      }
    }
   // request.setRouterSerialNo(getRouterId());
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetUnlockInfoListPresenter.getExceptionWarningList(encryptInfo);

  }

  @Override
  public void openLockUseNoteList(List<GetUnlockInfoListResponse> response) {

  }

  @Override
  public void openExceptionWarningList(List<GetUnlockInfoListResponse> exceptionWarnList) {
    if(exceptionWarnList.size() == 0){
      //判断是否为空，为空说明刷新完成，没有更多数据了
      isHasNoMoreData = true;
    }else {
      isHasNoMoreData = false;
    }

    mAllUnlockInfoList.addAll(exceptionWarnList);
    if (mAllUnlockInfoList.size() == 0) {
      mPtrFrame.refreshComplete();
      mBinding.lvExceptionWarnNote.setEmptyView(findViewById(R.id.include_empty_view));//无数据显示空视图
      return;
    }

    if (mPageNum == 0) {
      mPageNum = 1;
      mExceptionWarnListAdapter = new ExceptionWarnMoreAdapter(this, mAllUnlockInfoList, R.layout.item_exception_warn_more);
      mBinding.lvExceptionWarnNote.setAdapter(mExceptionWarnListAdapter);
      mPtrFrame.refreshComplete();
    } else {
      mPageNum++;
      mExceptionWarnListAdapter.getPostionsToInsertTitle(mAllUnlockInfoList);  //设置新增数据需要插入title的位置
      mExceptionWarnListAdapter.notifyDataSetChanged();
      mPtrFrame.refreshComplete();
    }
  }
}
