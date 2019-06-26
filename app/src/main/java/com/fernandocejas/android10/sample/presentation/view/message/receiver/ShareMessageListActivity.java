package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMessageComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MessageModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.AllQuestionActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.ShareMemberListActivity;
import com.qtec.mapp.model.req.DealInvitationRequest;
import com.qtec.mapp.model.req.DeleteMsgRequest;
import com.qtec.mapp.model.req.GetMsgListRequest;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.mapp.model.rsp.DeleteMsgResponse;
import com.qtec.mapp.model.rsp.GetMsgListResponse;
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
 *      desc: 共享消息列表
 *      version: 1.0
 * </pre>
 */

public class ShareMessageListActivity extends BaseActivity implements IGetMsgListView, IDealInvitationView, IDeleteMsgView/*,ISetMsgReadedView*/ {
  private MessageShareListAdapter mMessageListAdapter;
  private List<GetMsgListResponse<GetMsgListResponse.messageContent>> mMsgList;
  private int mDeleteIndex = 0;
  private PtrClassicFrameLayout mPtrFrame;
  protected boolean isEdit = false;
  protected boolean isSelectAll = false;
  protected int mPageNum = 0;
  private Button mBtnDelete;
  private ListView mListView;
  private List<GetMsgListResponse<GetMsgListResponse.messageContent>> mDeleteMsgList = new ArrayList<>();
  private Boolean isHasNoMoreData = false;

  @Inject
  GetMsgListPresenter mGetMsgListPresenter;
  @Inject
  PostMsgInvitationPresenter mPostMsgInvitationPresenter;
  @Inject
  DeleteMsgPresenter mDeleteMsgPresenter;
/*  @Inject
  SetMsgReadPresenter mSetMsgReadPresenter;*/

  private String[] mMessageUniqueKeys;
  private List<String> mUniqueKeys;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_share_message_list);
    initView();
    initTitleBar("设备中心");
    initializeInjector();
    initPresenter();
   // postMsgReadRequest("0");//消息中心 红点展示
    dealWithRefresh();

    mTitleBar.setRightAs("编辑", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        editBtnClickEvent();
      }
    });

    mBtnDelete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteBtnClickEvent();
      }
    });
  }

  private void initView() {
    mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_layout);
    mBtnDelete = (Button) findViewById(R.id.btn_delete);
    mListView = (ListView) findViewById(R.id.lv_message_list);
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
      Toast.makeText(this, "暂无设备消息，不支持编辑", Toast.LENGTH_SHORT).show();
      return;
    }

    if (isEdit) {
      isEdit = false;
      isSelectAll = false;
      mBtnDelete.setVisibility(View.GONE);
      mTitleBar.getRightBtn().setText("编辑");
      mTitleBar.setLeftAsBackButton();
      setCheckVisibility(false);
    } else {
      isEdit = true;
      mBtnDelete.setVisibility(View.VISIBLE);
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

  public void setCheckVisibility(boolean isCheck) {
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
   * 处理刷新事件
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
        //requestMsgList("0", "8"); //获得消息列表
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
            if(isEdit){
              restoreUiAfterEdit(); //编辑情况下上下拉要恢复UI
            }

            if(isHasNoMoreData){
              Toast.makeText(ShareMessageListActivity.this, "已加载所有数据，请稍候再试", Toast.LENGTH_SHORT).show();
              mPtrFrame.refreshComplete();
              return;
            }else{
            //  requestMsgList("" + (mPageNum * 8), "8");
              requestMsgList("" + mMsgList.size(), "8");
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
            if(isEdit){
              restoreUiAfterEdit();
            }

            if (mMsgList.size() == 0) {
              //部分初始化操作
              mPageNum = 0;
              //setCheckVisibility(false);
              requestMsgList("0", "8");
            } else {
              mMessageListAdapter.notifyDataSetChanged();
              mPtrFrame.refreshComplete();
            }
          }
        }, 100);
      }

      @Override
      public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        return super.checkCanDoLoadMore(frame, mListView, footer);
      }

      @Override
      public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return super.checkCanDoRefresh(frame, mListView, header);
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
    if("0".equals(start)){
      request.setMessageUniqueKey("-1");
    }else {
      if(mMsgList != null && mMsgList.size()>1){
        request.setMessageUniqueKey(mMsgList.get(mMsgList.size()-1).getMessageUniqueKey());
      }
    }
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    request.setMsgType("0");
    request.setStart(start);
    request.setPageSize(pageSize);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetMsgListPresenter.getShareMsgList(encryptInfo);
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
    mDeleteMsgPresenter.setView(this);
    /*mSetMsgReadPresenter.setView(this);*/
  }

  /**
   * 获得消息列表
   *
   * @param
   * @return
   */
  @Override
  public void getMsgList(List<GetMsgListResponse<GetMsgListResponse.messageContent>> response) {
    if(response.size() == 0){
      isHasNoMoreData = true;
    }else {
      isHasNoMoreData = false;
    }

    mMsgList.addAll(response);
    if (mMsgList.size() == 0) {
      mPtrFrame.refreshComplete();
      mListView.setEmptyView(findViewById(R.id.include_empty_view));//无数据显示空视图
      return;
    }

    if (mPageNum == 0) {
      mPageNum = 1;
      mMessageListAdapter = new MessageShareListAdapter(this, mMsgList, R.layout.item_share_msg_list);  //  item_message_list
      mListView.setAdapter(mMessageListAdapter);
      mPtrFrame.refreshComplete();
    } else {
      mPageNum++;
      setCheckVisibility(false);
      //mMessageListAdapter.notifyDataSetChanged();
      mPtrFrame.refreshComplete();
    }

    // 接受邀请
    mMessageListAdapter.setOnAcceptDetailClickListener(new MessageShareListAdapter.OnAcceptDetailClickListener() {
      @Override
      public void onAcceptDetailClick(int position) {
        //处理邀请
        mDeleteIndex = position;

        DealInvitationRequest request = new DealInvitationRequest();
        request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

        request.setHistoryUniqueKey(mMsgList.get(position).getMessageContent().getHistoryUniqueKey());

        request.setHandleType("1");
        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);
        mPostMsgInvitationPresenter.dealInvitation(encryptInfo);
      }
    });

    mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        setCheckVisibility(true);
        mBtnDelete.setVisibility(View.VISIBLE);
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

  /**
   * 处理邀请 结果返回
   *
   * @param
   * @return
   */
  @Override
  public void dealInvitation(DealInvitationResponse response) {
    //Toast.makeText(this, "您接受了邀請", Toast.LENGTH_SHORT).show();
    mMsgList.get(mDeleteIndex).getMessageContent().setHandleType("1");//已接受
    mMessageListAdapter.notifyDataSetChanged();

 /*   ShareMessageListActivity.this.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mMessageListAdapter.setIsAccept(true);
      }
    });*/
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
        if ((mDeleteMsgList.get(i).getMessageUniqueKey().equals(mMsgList.get(j).getMessageUniqueKey()))) {
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
    mBtnDelete.setVisibility(View.GONE);
    mTitleBar.getRightBtn().setText("编辑");
    mTitleBar.setLeftAsBackButton();
    setCheckVisibility(false);
  }

}

