package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMessageListBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMessageComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MessageModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.req.DeleteMsgRequest;
import com.qtec.mapp.model.req.GetMsgListRequest;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.mapp.model.rsp.DeleteMsgResponse;
import com.qtec.mapp.model.rsp.GetMsgDetailResponse;
import com.qtec.mapp.model.rsp.GetMsgOtherListResponse;
import com.qtec.mapp.model.rsp.SetMsgReadResponse;
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
 *      desc: 其他消息列表
 *      version: 1.0
 * </pre>
 */

public class OtherMessageListActivity extends BaseActivity implements IGetMsgDetailView, IGetMsgOtherListView, IDealInvitationView, IDeleteMsgView/* ,ISetMsgReadedView*/{
  private MessageListAdapter mMessageListAdapter;
  private ActivityMessageListBinding mMessageBinding;
  private List<GetMsgOtherListResponse> mMsgList;
  private int mDeleteIndex = 0;
  private int mPageNum = 0;//统计上拉加载的次数
  private PtrClassicFrameLayout mPtrFrame;
  protected boolean isEdit = false;
  protected boolean isSelectAll = false;
  private String[] mMessageUniqueKeys;
  private List<GetMsgOtherListResponse> mDeleteMsgList = new ArrayList<>();
  private List<String> mUniqueKeys;
  private Boolean isHasNoMoreData = false;

  @Inject
  GetMsgOtherPresenter mGetMsgListPresenter;
  @Inject
  PostMsgInvitationPresenter mPostMsgInvitationPresenter;
  @Inject
  DealInvitationPresenter mDealInvitationPresenter;
  @Inject
  DeleteMsgPresenter mDeleteMsgPresenter;
 /* @Inject
  SetMsgReadPresenter mSetMsgReadPresenter;*/

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMessageBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_list);
    initView();
    initTitleBar("其他通知");
    initializeInjector();
    initPresenter();
    dealWithRefresh();

    mTitleBar.setRightAs("编辑", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editBtnClickEvent();
      }
    });

    mMessageBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteBtnClickEvent();
      }
    });
  }

  private void initView() {
    mPtrFrame = mMessageBinding.ptrLayout;
    mMsgList = new ArrayList<>();
  }

  /**
   * 编辑操作
   *
   * @param
   * @return
   */
  private void editBtnClickEvent() {
    if(mMsgList.size() == 0){
      Toast.makeText(this, "暂无通知，不支持编辑", Toast.LENGTH_SHORT).show();
      return;
    }

    if (isEdit) {
      isEdit = false;
      isSelectAll = false;
      mMessageBinding.btnDelete.setVisibility(View.GONE);
      mTitleBar.getRightBtn().setText("编辑");
      mTitleBar.setLeftAsBackButton();
      setCheckVisibility(false);
    } else {
      isEdit = true;
      mMessageBinding.btnDelete.setVisibility(View.VISIBLE);
      mTitleBar.getRightBtn().setText("取消");
      mTitleBar.setLeftAs("全选", new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (isSelectAll) {
            isSelectAll = false;
            for (int i = 0; i < mMessageListAdapter.getCount(); i++) {
              mMessageListAdapter.checkedMap.put(i, false);
            }
            mMessageListAdapter.notifyDataSetChanged();
          } else {
            isSelectAll = true;
            for (int i = 0; i < mMessageListAdapter.getCount(); i++) {
              mMessageListAdapter.checkedMap.put(i, true);
            }
            mMessageListAdapter.notifyDataSetChanged();
          }
        }
      });
      setCheckVisibility(true);
    }
  }


  /**
   * 删除操作
   *
   * @param
   * @return
   */
  private void deleteBtnClickEvent() {
    mUniqueKeys = new ArrayList<>();
    for (int i = 0; i < mMessageListAdapter.getCount(); i++) {
      if (mMessageListAdapter.checkedMap.get(i)) {
        mUniqueKeys.add(mMsgList.get(i).getMessageUniqueKey());
      }
    }
    mMessageUniqueKeys = new String[mUniqueKeys.size()];
    for (int i = 0; i < mUniqueKeys.size(); i++) {
      mMessageUniqueKeys[i] = mUniqueKeys.get(i);
    }
    if (mMessageUniqueKeys.length != 0) {
      //消息删除
      DeleteMsgRequest request = new DeleteMsgRequest();
      request.setMessageUniqueKeys(mMessageUniqueKeys);
      QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
      encryptInfo.setData(request);
      mDeleteMsgPresenter.deleteMsg(encryptInfo);
    }
  }

  /**
   * 处理刷新事件
   *
   * @param
   * @return
   */
  private void dealWithRefresh() {
    //下拉刷新支持时间
    mPtrFrame.setLastUpdateTimeRelateObject(this);
    mPtrFrame.setDurationToClose(200);
    mPtrFrame.disableWhenHorizontalMove(true);//解决横向滑动冲突
    //进入Activity自动下拉刷新
    mPtrFrame.postDelayed(new Runnable() {
      @Override
      public void run() {
        //requestMsgList("0", "12"); //获得消息列表
        mPtrFrame.autoRefresh();
      }
    }, 300);

    mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
      @Override
      public void onLoadMoreBegin(PtrFrameLayout frame) {
        mPtrFrame.postDelayed(new Runnable() {
          @Override
          public void run() {
            if(isEdit){
              restoreUiAfterEdit();
            }

            if(isHasNoMoreData){
              Toast.makeText(OtherMessageListActivity.this, "已加载所有数据，请稍候再试", Toast.LENGTH_SHORT).show();
              mPtrFrame.refreshComplete();
              return;
            }else {
              requestMsgList("" + mMsgList.size(), "12");// 每次请求12条记录
            }
          }
        }, 1000);
      }

      //下拉刷新
      @Override
      public void onRefreshBegin(PtrFrameLayout frame) {
        mPtrFrame.postDelayed(new Runnable() {
          @Override
          public void run() {

            if(isEdit){
              restoreUiAfterEdit();
            }

            if (mMsgList.size() == 0) {
              //部分初始化操作
              mPageNum = 0;
              //setCheckVisibility(false);
              requestMsgList("0", "12");
            } else {
              mMessageListAdapter.notifyDataSetChanged();
              mPtrFrame.refreshComplete();
            }
          }
        }, 100);
      }

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return super.checkCanDoLoadMore(frame, mMessageBinding.lvMessageList, footer);
        //return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return super.checkCanDoRefresh(frame, mMessageBinding.lvMessageList, header);
        //return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
      }
    });
  }

  /**
   * 获取消息列表
   *
   * @param
   * @return
   */
  private void requestMsgList(String start, String pageSize) {
    GetMsgListRequest request = new GetMsgListRequest();
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    if("0".equals(start)){
      request.setMessageUniqueKey("-1");
    }else {
      if(mMsgList != null && mMsgList.size()>1){
        request.setMessageUniqueKey(mMsgList.get(mMsgList.size()-1).getMessageUniqueKey());
      }
    }
    request.setMsgType("1");
    request.setStart(start);
    request.setPageSize(pageSize);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetMsgListPresenter.getMsgList(encryptInfo);
  }

  private void initializeInjector() {
    DaggerMessageComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .messageModule(new MessageModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mPostMsgInvitationPresenter.setView(this);
    mGetMsgListPresenter.setView(this);
    mDealInvitationPresenter.setView(this);
    mDeleteMsgPresenter.setView(this);
    /*mSetMsgReadPresenter.setView(this);*/
  }

  /**
   * 消息详情
   *
   * @param
   * @return
   */
  @Override
  public void getMsgDetail(GetMsgDetailResponse response) {
       /* MessagePopupWindow mMsgPopWin = new MessagePopupWindow(OtherMessageListActivity.this, response);
        mMsgPopWin.setFocusable(true);
        mMsgPopWin.showAtLocation(mMsgPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

        mMsgPopWin.setOnPositiveClickListener(new MessagePopupWindow.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {
                //处理邀请
                SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
                String userUniqueKey = spUtils.getString(PrefConstant.SP_USER_UNIQUE_KEY);

                DealInvitationRequest request = new DealInvitationRequest();
                request.setUserUniqueKey(userUniqueKey);
                request.setHandleType("123");
                request.setHistoryUniqueKey("213231");
                QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
                encryptInfo.setData(request);
                mDealInvitationPresenter.dealInvitation(encryptInfo);
            }
        });*/
  }

  /**
   * 获得消息列表
   *
   * @param
   * @return
   */
  @Override
  public void getMsgOtherList(List<GetMsgOtherListResponse> response) {
    if(response.size() == 0){
      isHasNoMoreData = true;
    }else {
      isHasNoMoreData = false;
    }

    mMsgList.addAll(response);
    if (mMsgList.size() == 0) {
      mPtrFrame.refreshComplete();
      mMessageBinding.lvMessageList.setEmptyView(findViewById(R.id.include_empty_view));//无数据显示空视图
      return;
    }

    if (mPageNum == 0) {
      mPageNum = 1;
      mMessageListAdapter = new MessageListAdapter(this, mMsgList, R.layout.item_message_list);
      mMessageBinding.lvMessageList.setAdapter(mMessageListAdapter);
      mPtrFrame.refreshComplete();
    } else {
      mPageNum++;
      setCheckVisibility(false);
      //mMessageListAdapter.notifyDataSetChanged();
      mPtrFrame.refreshComplete();
    }


    mMessageBinding.lvMessageList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        setCheckVisibility(true);
        mMessageBinding.btnDelete.setVisibility(View.VISIBLE);
        mTitleBar.getRightBtn().setText("取消");
        mTitleBar.setLeftAs("全选", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (isSelectAll) {
              isSelectAll = false;
              for (int i = 0; i < mMessageListAdapter.getCount(); i++) {
                mMessageListAdapter.checkedMap.put(i, false);
              }
              mMessageListAdapter.notifyDataSetChanged();
            } else {
              isSelectAll = true;
              for (int i = 0; i < mMessageListAdapter.getCount(); i++) {
                mMessageListAdapter.checkedMap.put(i, true);
              }
              mMessageListAdapter.notifyDataSetChanged();
            }
          }
        });
        return true;
      }
    });
  }

  public void setCheckVisibility(boolean isCheck) {

    /*if((mMsgList.size() != 0) && (mMsgList != null)){
      mMessageListAdapter.getPostionsToInsertTitle(mMsgList);  //设置新增数据需要插入title的位置
    }*/

    for (int i = 0; i < mMessageListAdapter.getCount(); i++) {
      if (isCheck) {
        mMessageListAdapter.checkedMap.put(i, false);
        mMessageListAdapter.visibleMap.put(i, CheckBox.VISIBLE);
      } else {
        mMessageListAdapter.checkedMap.put(i, false);
        mMessageListAdapter.visibleMap.put(i, CheckBox.GONE);
      }
    }
    mMessageListAdapter.notifyDataSetChanged();
  }

  /**
   * 处理邀请 结果返回
   *
   * @param
   * @return
   */
  @Override
  public void dealInvitation(DealInvitationResponse response) {
    /*Toast.makeText(this, "您处理了邀请", Toast.LENGTH_SHORT).show();*/
  }

  /**
   * 消息删除
   *
   * @param
   * @return
   */
  @Override
  public void deleteMsg(DeleteMsgResponse response) {
    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();

    for (int i = 0; i < mMsgList.size(); i++) {
      if (mMessageListAdapter.checkedMap.get(i)) {
        mDeleteMsgList.add(mMsgList.get(i));
      }
    }

    for (int i = 0; i < mDeleteMsgList.size(); i++) {
      for (int j = 0; j < mMsgList.size(); j++) {
        if ((mDeleteMsgList.get(i)).equals(mMsgList.get(j))) {
          mMsgList.remove(j);
        }
      }
    }

    restoreUiAfterEdit();
  }

  /**
   * 恢复编辑前的ui
   *
   * @param
   * @return
   */
  private void restoreUiAfterEdit(){
    isEdit = false;
    isSelectAll = false;
    mDeleteMsgList.clear();
    mMessageBinding.btnDelete.setVisibility(View.GONE);
    mTitleBar.getRightBtn().setText("编辑");
    mTitleBar.setLeftAsBackButton();
    setCheckVisibility(false);
  }

}

