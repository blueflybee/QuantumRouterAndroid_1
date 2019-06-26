package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterGroupBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.RouterGroupArrayAdapter;
import com.qtec.mapp.model.rsp.GetRouterGroupsResponse;

import java.util.List;

import javax.inject.Inject;


public class RouterGroupActivity extends BaseActivity implements RouterGroupView {

  @Inject
  RouterGroupPresenter mRouterGroupPresenter;
  private ActivityRouterGroupBinding mBinding;

  private int mCurrentCheckPosition = -1;
  private RouterGroupArrayAdapter mAdapter;
  private List<GetRouterGroupsResponse> mGroups;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_router_group);

    initializeInjector();
    initPresenter();
    initView();

    mRouterGroupPresenter.getRouterGroups();

  }

  private void initView() {
    initTitleBar("网关分组");
    changeTitleRightBtnStyle("");
    mTitleBar.setRightAs("完成", v -> {
      if (mCurrentCheckPosition == -1) return;
      GetRouterGroupsResponse item = mAdapter.getItem(mCurrentCheckPosition);
      if (item == null) return;
      mRouterGroupPresenter.modifyRouterGroup(item.getGroupId());
    });

    mBinding.lvGroup.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    mBinding.lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCurrentCheckPosition = position;
        mBinding.lvGroup.setItemChecked(position, true);
      }
    });

    findViewById(R.id.rv_add_new_group).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        showCreateGroupDialog();
      }
    });
  }

  private void showCreateGroupDialog() {
    DialogUtil.showCreateRouterGroupDialog(getContext(), (view, groupName) -> {

      GetRouterGroupsResponse group = new GetRouterGroupsResponse();
      group.setGroupName(groupName);
      if (mGroups.contains(group)) {
        ToastUtils.showShort("分组已存在");
        return;
      }

      mRouterGroupPresenter.addGroup(groupName);

    });
  }

  private String groupName() {
    return getIntent().getStringExtra(Navigator.EXTRA_ROUTER_GROUP);
  }

  private void initPresenter() {
    mRouterGroupPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void showRouterGroups(List<GetRouterGroupsResponse> groups) {
    mGroups = groups;
    if (mAdapter == null) {
      mAdapter = new RouterGroupArrayAdapter(this, R.layout.item_router_group_list, groups);
      mBinding.lvGroup.setAdapter(mAdapter);
    } else {
      mAdapter.clear();
      mAdapter.addAll(groups);
      mAdapter.notifyDataSetChanged();
    }

    for (int i = 0; i < groups.size(); i++) {
      GetRouterGroupsResponse groupsResponse = groups.get(i);
      String groupName = groupsResponse.getGroupName();
      if (TextUtils.isEmpty(groupName)) continue;
      if (!groupName.equals(groupName())) continue;
      mCurrentCheckPosition = i;
      break;
    }
    mBinding.lvGroup.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    mBinding.lvGroup.setItemChecked(mCurrentCheckPosition, true);
    changeTitleRightBtnStyle("enable");
  }

  @Override
  public void showModifyRouterGroupSuccess() {
    ToastUtils.showShort("分组设置成功");
    finish();
  }

  @Override
  public void showCreateRouterGroupSuccess() {
    ToastUtils.showShort("分组创建成功");
    mRouterGroupPresenter.getRouterGroups();
  }
}