package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityDoorBellRecordListBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.JVCatFilePlayActivity;
import com.qtec.cateye.model.response.DeleteDoorBellRecordResponse;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailResponse;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadResponse;

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
 *      desc:  门铃记录列表
 *      version: 1.0
 * </pre>
 */

public class DoorBellRecordListActivity extends BaseActivity implements GetDoorBellRecordListView, GetDoorBellRecordDetailView, DeleteDoorBellRecordView, SetAllDoorBellRecordIsReadView {
  private DoorBellRecordListAdapter adapter;
  private ActivityDoorBellRecordListBinding mBinding;
  private PtrClassicFrameLayout mPtrFrame;
  private List<GetDoorBellRecordListResponse.MsgList> mAllRecordList, mDeleteMsgList;
  private int mPageNum = 0;//统计上拉加载的次数
  private Boolean isHasNoMoreData = false;
  protected boolean isEdit = false;
  protected boolean isSelectAll = false;
  private List<String> mUniqueKeys;
  private int mSetIsReadIndex = 0;

  @Inject
  GetDoorBellMsgListPresenter mGetDoorBeerMsgListPresenter;
  @Inject
  GetDoorBellMsgDetailPresenter mGetDoorBeerMsgDetailPresenter;
  @Inject
  DeleteDoorBellMsglPresenter mDeleteDoorBeerMsgPresenter;
  @Inject
  SetAllDoorBellMsgReadPresenter mSetAllMsgIsReadPresenter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_door_bell_record_list);
    initView();

    initializeInjector();

    initPresenter();

    dealWithRefresh();

    mTitleBar.setRightAs("编辑", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editBtnClickEvent();
      }
    });

    initEvent();

  }

  private void initEvent() {
    mBinding.btnAllReaded.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSetAllMsgIsReadPresenter.setAllDoorBellMsgIsRead(PrefConstant.getUserUniqueKey("0"), GlobleConstant.getgCatEyeId());
      }
    });

    mBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        mUniqueKeys = new ArrayList<>();

        for (int i = 0; i < adapter.getCount(); i++) {
          if (adapter.checkedMap.get(i)) {
            mUniqueKeys.add(mAllRecordList.get(i).getMessageUniqueKey());
          }
        }

        String[] mMessageUniqueKeys = new String[mUniqueKeys.size()];

        for (int i = 0; i < mUniqueKeys.size(); i++) {
          mMessageUniqueKeys[i] = mUniqueKeys.get(i);
        }

        if (mMessageUniqueKeys.length != 0) {
          //消息删除
          mDeleteDoorBeerMsgPresenter.deleteDoorBeerMsg(mMessageUniqueKeys);
        }


      }
    });
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
    mGetDoorBeerMsgListPresenter.setView(this);
    mGetDoorBeerMsgDetailPresenter.setView(this);
    mDeleteDoorBeerMsgPresenter.setView(this);
    mSetAllMsgIsReadPresenter.setView(this);
  }

  private void initView() {
    initMoveFileTitleBar("门铃记录");
    mPtrFrame = mBinding.ptrLayout;
    mAllRecordList = new ArrayList<>();
    mUniqueKeys = new ArrayList<>();
    mDeleteMsgList = new ArrayList<>();

    mBinding.lvRecordListNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //此接口主要是将红点置为已读
        mSetIsReadIndex = position;
        mGetDoorBeerMsgDetailPresenter.getDoorBeerMsgDetail(mAllRecordList.get(position).getMessageUniqueKey());
      }
    });

    mTitleBar.setLeftAsBackButtonForSamba();
    mTitleBar.setOnSambaBackListener(new TitleBar.OnSambaBackClickListener() {
      @Override
      public void onSambaBackClick() {
        if (isEdit) {
          restoreUiAfterEdit();
        } else {
          finish();
        }
      }
    });
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
        //queryQuestionList("0", "14");
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
            if (isHasNoMoreData) {
              Toast.makeText(DoorBellRecordListActivity.this, "已加载所有数据，请稍候再试", Toast.LENGTH_SHORT).show();
              mPtrFrame.refreshComplete();
              return;
            } else {
              getDoorBeerMsgList(mAllRecordList.get(mAllRecordList.size() - 1).getRecordUniqueKey(), "14");
            }
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

            if (mAllRecordList.size() == 0) {
              mPageNum = 0;
              getDoorBeerMsgList("-1", "10");
            } else {
              adapter.notifyDataSetChanged();
              mPtrFrame.refreshComplete();
            }

          }
        }, 100);
      }

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return super.checkCanDoLoadMore(frame, mBinding.lvRecordListNote, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return super.checkCanDoRefresh(frame, mBinding.lvRecordListNote, header);
      }
    });
  }

  private void getDoorBeerMsgList(String recordUniqueKey, String pageSize) {
    mGetDoorBeerMsgListPresenter.getDoorBeerMsgList(recordUniqueKey, pageSize);
  }

  /**
   * 编辑操作
   *
   * @param
   * @return
   */
  private void editBtnClickEvent() {
    if (mAllRecordList.size() == 0) {
      Toast.makeText(this, "暂无使用记录，不支持编辑", Toast.LENGTH_SHORT).show();
      return;
    }

    isEdit = true;

    mBinding.llIsEdit.setVisibility(View.VISIBLE);
    mTitleBar.getRightBtn().setText("全选");
    mTitleBar.getRightBtn().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isSelectAll) {
          isSelectAll = false;
          mTitleBar.getRightBtn().setText("全选");
          for (int i = 0; i < adapter.getCount(); i++) {
            adapter.checkedMap.put(i, false);
          }
          adapter.notifyDataSetChanged();
        } else {
          isSelectAll = true;
          mTitleBar.getRightBtn().setText("取消");
          for (int i = 0; i < adapter.getCount(); i++) {
            adapter.checkedMap.put(i, true);
          }
          adapter.notifyDataSetChanged();
        }
      }
    });

    setCheckVisibility(true);
  }

  public void setCheckVisibility(boolean isCheck) {

    /*if((mMsgList.size() != 0) && (mMsgList != null)){
      mMessageListAdapter.getPostionsToInsertTitle(mMsgList);  //设置新增数据需要插入title的位置
    }*/

    for (int i = 0; i < adapter.getCount(); i++) {
      if (isCheck) {
        adapter.checkedMap.put(i, false);
        adapter.visibleMap.put(i, CheckBox.VISIBLE);
      } else {
        adapter.checkedMap.put(i, false);
        adapter.visibleMap.put(i, CheckBox.GONE);
      }
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void getDoorBellRecordList(GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>> responses) {

    if (responses.getMsgList().size() == 0) {
      isHasNoMoreData = true;
    } else {
      isHasNoMoreData = false;
    }

    mAllRecordList.addAll(responses.getMsgList());

    if (mAllRecordList.size() == 0) {
      mPtrFrame.refreshComplete();
      mBinding.lvRecordListNote.setEmptyView(findViewById(R.id.include_empty_view));//无数据显示空视图
      return;
    }

    if (mPageNum == 0) {
      mPageNum = 1;
      adapter = new DoorBellRecordListAdapter(this, mAllRecordList, R.layout.item_door_bell_record);
      mBinding.lvRecordListNote.setAdapter(adapter);
      mPtrFrame.refreshComplete();
    } else {
      mPageNum++;
      adapter.getPostionsToInsertTitle(mAllRecordList); //获取新增数据需要插入title的位置
      adapter.notifyDataSetChanged();
      mPtrFrame.refreshComplete();
    }
  }

  @Override
  public void getDoorBeelRecordDetail(GetDoorBellRecordDetailResponse response) {

    mAllRecordList.get(mSetIsReadIndex).setIsRead("1");//设为已读
    adapter.notifyDataSetChanged();

    String videoPath = response.getVideoPath();
    String occurTime = response.getOccurTime();
    DialogUtil.showCatEyeRecordDialog(getContext(), occurTime, videoPath, new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        //回放视频 连接中
        Intent intent = new Intent(getContext(), JVCatFilePlayActivity.class);
        intent.putExtra(Navigator.EXTRA_PLAY_PATH, videoPath);
        intent.putExtra(Navigator.EXTRA_VIDEO_TIME, occurTime);
        startActivity(intent);
      }
    }, null);
  }

  @Override
  public void deleteDoorBeelRecord(DeleteDoorBellRecordResponse response) {
    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();

    mDeleteMsgList.clear();

    for (int i = 0; i < adapter.getCount(); i++) {
      if (adapter.checkedMap.get(i)) {
        mDeleteMsgList.add(mAllRecordList.get(i));
      }
    }

    for (int i = 0; i < mDeleteMsgList.size(); i++) {
      for (int j = 0; j < mAllRecordList.size(); j++) {
        if ((mDeleteMsgList.get(i).getMessageUniqueKey().equals(mAllRecordList.get(j).getMessageUniqueKey()))) {
          mAllRecordList.remove(j);
        }
      }
    }

    restoreUiAfterEdit();

    if (mAllRecordList.size() == 0) {
      mBinding.lvRecordListNote.setEmptyView(findViewById(R.id.include_empty_view));//无数据显示空视图
    }
  }

  @Override
  public void setAllDoorBeelRecordRead(SetAllDoorBellRecordReadResponse response) {

    restoreUiAfterEdit();

    for (int i = 0; i < mAllRecordList.size(); i++) {
      mAllRecordList.get(i).setIsRead("1");
    }
    adapter.notifyDataSetChanged();
  }

  private void restoreUiAfterEdit() {
    isEdit = false;
    isSelectAll = false;
    mDeleteMsgList.clear();
    mBinding.llIsEdit.setVisibility(View.GONE);
    mTitleBar.getRightBtn().setText("编辑");
    setCheckVisibility(false);

    mTitleBar.getRightBtn().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        isEdit = true;

        setCheckVisibility(true);

        mBinding.llIsEdit.setVisibility(View.VISIBLE);

        if (isSelectAll) {
          isSelectAll = false;
          mTitleBar.getRightBtn().setText("全选");
          for (int i = 0; i < adapter.getCount(); i++) {
            adapter.checkedMap.put(i, false);
          }
          adapter.notifyDataSetChanged();
        } else {
          isSelectAll = true;
          mTitleBar.getRightBtn().setText("取消");
          for (int i = 0; i < adapter.getCount(); i++) {
            adapter.checkedMap.put(i, true);
          }
          adapter.notifyDataSetChanged();
        }
      }
    });
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      //如果是根目录的话则退出
      if (isEdit) {
        //编辑状态 恢复编辑前状态
        restoreUiAfterEdit();
      } else {
        finish();
      }
      return true;
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }
}
