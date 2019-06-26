package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityShareMenberListBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.component.InputInviterNumPopupWindow;
import com.qtec.mapp.model.req.GetSharedMemListRequest;
import com.qtec.mapp.model.req.PostInvitationRequest;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;
import com.qtec.mapp.model.rsp.PostInvitationResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 设备共享成员列表
 *      version: 1.0
 * </pre>
 */

public class ShareMemberListActivity extends BaseActivity implements IPostInvitationView, IGetSharedMemListView {
  private List<GetShareMemListResponse> mShareMems;
  private ShareMemListAdapter mMemListAdapter;
  private ActivityShareMenberListBinding mMenberListBinding;
  private InputInviterNumPopupWindow mInputNumPopWin;
  @Inject
  PostInvitationPresenter mPostInvitation;
  @Inject
  GetSharedMemListPresenter mGetShareMemListPresenter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMenberListBinding = DataBindingUtil.setContentView(this, R.layout.activity_share_menber_list);
    initTitleBar("设备共享");
    initView();
    initializeInjector();
    initPresenter();
    queryShareMemList();

    mTitleBar.setRightAs("添加", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        inviteMemCLick();
      }
    });
  }

  private void initView() {
    mShareMems = new ArrayList<>();
  }

  /**
  * 邀请逻辑处理
  *
  * @param
  * @return
  */
  private void inviteMemCLick(){
    //解决Edittext无法再PopupWindow复制粘贴问题,用Activtiy代替PopupWindow
    startActivityForResult(new Intent(this, InputPhoneNumActivity.class),11);

    /*弹出PopupWindow
    mInputNumPopWin = new InputInviterNumPopupWindow(ShareMemberListActivity.this);
    mInputNumPopWin.setFocusable(true);
    mInputNumPopWin.showAtLocation(mInputNumPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
    */
  }

  /**
   * 获取共享成员
   *
   * @param
   * @return
   */
  private void queryShareMemList() {
    SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
    String userUniqueKey = spUtils.getString(PrefConstant.SP_USER_UNIQUE_KEY);

    GetSharedMemListRequest request = new GetSharedMemListRequest();
    request.setRouterSerialNo(getRouterSerialNum());
    request.setUserUniqueKey(userUniqueKey);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mGetShareMemListPresenter.getShareMemList(encryptInfo);
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
    mPostInvitation.setView(this);
    mGetShareMemListPresenter.setView(this);
  }

  private String getRouterSerialNum() {
    return getIntent().getStringExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if ((data == null)&&(requestCode != 2)) {
      return;
    }

    if(requestCode == 11){
      //选择通讯录回来
      String num = data.getStringExtra("PHONENUM");
      System.out.println("通讯录 sharemember = " + num);
      //邀请逻辑处理
      SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
      String userPhone = spUtils.getString(PrefConstant.SP_USER_PHONE);

      PostInvitationRequest request = new PostInvitationRequest();
      request.setDeviceSerialNo(getRouterSerialNum());
      request.setSharedPhone(num);
      request.setSharePhone(userPhone);
      QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
      encryptInfo.setData(request);
      mPostInvitation.postInvitation(encryptInfo);
    }

    if(requestCode == 2){
      queryShareMemList();
    }
  }

  /**
   * 邀请 返回
   *
   * @param
   * @return
   */
  @Override
  public void postInvitation(PostInvitationResponse response) {
    SnackbarUtils.showShort(mMenberListBinding.getRoot(), "发送成功，请用户登录App查看", Color.WHITE, getResources().getColor(R.color.green_48b04b));
    queryShareMemList();
  }

  @Override
  public void onError(String message) {
    //super.onError(message);
    if(!TextUtils.isEmpty(message)){
      //SnackbarUtils.showShort(mMenberListBinding.getRoot(), "该用户已经在共享列表中，无需再次添加", Color.WHITE, getResources().getColor(R.color.red_ff504c));
      SnackbarUtils.showShort(mMenberListBinding.getRoot(), ""+message, Color.WHITE, getResources().getColor(R.color.red_ff504c));
    }
  }

  @Override
  public void getSharedMemList(List<GetShareMemListResponse> response) {
    // 获取分享成员
    mShareMems.clear();

    mShareMems.addAll(response);

    if (mShareMems.size() == 0) {
      mMenberListBinding.lvShareMemberList.setEmptyView(findViewById(R.id.include_share_mem_empty_view));
      findViewById(R.id.include_share_mem_empty_view).findViewById(R.id.btn_empty_shareMemList).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          inviteMemCLick();
        }
      });
      return;
    }

    mMemListAdapter = new ShareMemListAdapter(this, mShareMems, R.layout.item_share_menber_list);
    mMenberListBinding.lvShareMemberList.setAdapter(mMemListAdapter);

    mMenberListBinding.lvShareMemberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ShareMemberListActivity.this, MemDetailActivity.class);
        intent.putExtra(mNavigator.EXTRA_MEM_DETAIL, mShareMems.get(position));
        startActivityForResult(intent,2);
      }
    });
  }

}

