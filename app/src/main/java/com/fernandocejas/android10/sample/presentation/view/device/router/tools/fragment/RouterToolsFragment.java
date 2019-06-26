package com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;

import com.fernandocejas.android10.sample.presentation.databinding.FragmentRouterToolsBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.fragment.NetCheckPopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.router.RouterMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.adapter.ApplyAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.adapter.MineMenuAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.callback.MenuDragCallback;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.constant.MenuConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.ApplyMenu;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.MenuManager;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.util.ACache;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.RouterToolsAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.AntiFritNetMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.SetQuestionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure.BandSpeedMeasureActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare.ChildCareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection.SafeInspectionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi.GuestWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband.IntelligentBandActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.RemoteMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation.*;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention.SpecialAttentionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch.WifiTimeSwitchActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.WirelessRelayActivity;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.GetAntiFritNetStatusResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class RouterToolsFragment extends BaseFragment implements AntiFritNetStatusView, IGetAntiQuestionView, IGetSignalModeView {
  private Boolean isRouterDirect = false;
  private FragmentRouterToolsBinding mBinding;
  private NetCheckPopupWindow mNetCheckPopWin;

  @Inject
  AntiFritNetStatusPresenter mAntiFritNetStatusPresenter;
  @Inject
  GetAntiQuestionPresenter mAntiQuestionPresenter;
  @Inject
  GetSignalModePresenter mGetSignalModePresenter;

  private MineMenuAdapter mMineAdapter;
  private ApplyAdapter mAllAdapter;

  private MenuManager mMenuManager = new MenuManager();

  private List<ApplyMenu> mSavedMineMenus = new ArrayList<>();
  private List<ApplyMenu> mSavedAllMenus = new ArrayList<>();

  private List<ApplyMenu> mTempMineMenus = new ArrayList<>();
  private List<ApplyMenu> mTempAllMenus = new ArrayList<>();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static RouterToolsFragment newInstance() {
    return new RouterToolsFragment();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_router_tools, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initData();
    initView();
    initPresenter();
  }

  private void initData() {
    ArrayList<ApplyMenu> mineMenus = mMenuManager.loadMenus(MenuConstant.APPLY_MINE, getMineMenuKey());
    for (ApplyMenu mineMenu : mineMenus) {
      mSavedMineMenus.add(mineMenu.clone());
      mTempMineMenus.add(mineMenu.clone());
    }

    ArrayList<ApplyMenu> allMenus = mMenuManager.loadMenus(MenuConstant.APPLY_MORE, getAllMenuKey());
    for (ApplyMenu allMenu : allMenus) {
      mSavedAllMenus.add(allMenu.clone());
      mTempAllMenus.add(allMenu.clone());
    }

    mineMenus.clear();
    allMenus.clear();
  }

  private void queryAntiFritNetStatus() {
    mAntiFritNetStatusPresenter.queryAntiFritNetStatus(GlobleConstant.getgDeviceId());
  }

  private void getQuestionRequest() {
    mAntiQuestionPresenter.getAntiFritQuestion(GlobleConstant.getgDeviceId());
  }

  private void initPresenter() {
    mAntiFritNetStatusPresenter.setView(this);
    mAntiQuestionPresenter.setView(this);
    mGetSignalModePresenter.setView(this);
  }

  private void initView() {
    initEditView();
    initMineView();
    initAllView();
  }

  private void initEditView() {
    mBinding.tvEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mMineAdapter.setEditable(true);
        mAllAdapter.setEditable(true);
        mBinding.tvEdit.setVisibility(View.GONE);
        mBinding.tvFinish.setVisibility(View.VISIBLE);
        mBinding.tvCancle.setVisibility(View.VISIBLE);
      }
    });

    mBinding.tvCancle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mTempMineMenus.clear();
        for (ApplyMenu mineMenu : mSavedMineMenus) {
          mTempMineMenus.add(mineMenu.clone());
        }
        mTempAllMenus.clear();
        for (ApplyMenu allMenu : mSavedAllMenus) {
          mTempAllMenus.add(allMenu.clone());
        }
        mMineAdapter.addData(mTempMineMenus);
        mAllAdapter.addData(mTempAllMenus);

        mMineAdapter.setEditable(false);
        mAllAdapter.setEditable(false);
        mBinding.tvCancle.setVisibility(View.GONE);
        mBinding.tvFinish.setVisibility(View.GONE);
        mBinding.tvEdit.setVisibility(View.VISIBLE);
      }
    });
    mBinding.tvFinish.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mMineAdapter.setEditable(false);
        mAllAdapter.setEditable(false);
        mBinding.tvCancle.setVisibility(View.GONE);
        mBinding.tvFinish.setVisibility(View.GONE);
        mBinding.tvEdit.setVisibility(View.VISIBLE);
        save(mMineAdapter.getAdapterData(), mAllAdapter.getAdapterData());
      }
    });
  }

  private void querySignalModeRequest() {
    mGetSignalModePresenter.getSignalMode(GlobleConstant.getgDeviceId());
  }

  @Override
  public void getAntiFritnetStatus(GetAntiFritNetStatusResponse response) {

    if (response.getEnable() == 0) {
      //关闭并且没有问题才去配置问题
      getQuestionRequest();
    } else {
      Intent intent6 = new Intent();
      intent6.putExtra("FragmentPosition", 0);
      mNavigator.navigateTo(getContext(), AntiFritNetMainActivity.class, intent6);
    }

  }

  @Override
  public void getAntiFritQuestion(GetAntiFritQuestionResponse response) {

    if (TextUtils.isEmpty(response.getAnswer()) && TextUtils.isEmpty(response.getQuestion())) {
      Intent intent0 = new Intent();
      intent0.putExtra("FLAG", "0");//首次设置
      mNavigator.navigateTo(getContext(), SetQuestionActivity.class, intent0);
    } else {
      //有问题就不用设置问题
      Intent intent7 = new Intent();
      intent7.putExtra("FragmentPosition", 0);
      mNavigator.navigateTo(getContext(), AntiFritNetMainActivity.class, intent7);
    }

  }

  @Override
  public void getSignalMode(GetSignalRegulationResponse response) {
    Intent intent1 = new Intent();
    intent1.putExtra("MODE", response.getMode());
    mNavigator.navigateTo(getContext(), SignalRegulationActivity.class, intent1);
  }

  private String getRouterName() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_NAME);
  }

  private String getRouterModel() {
    return getActivity().getIntent().getStringExtra(Navigator.EXTRA_ROUTER_MODEL);
  }


  private void initAllView() {
    if (mAllAdapter == null) {
      /**设置全部数据适配器*/
      mBinding.recyvToolsAll.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
      mBinding.recyvToolsAll.setItemAnimator(new DefaultItemAnimator());
      mBinding.recyvToolsAll.setAdapter(mAllAdapter);
      mAllAdapter = new ApplyAdapter(getContext(), mMenuManager.loadMenus(MenuConstant.APPLY_MORE, getAllMenuKey()));
      mBinding.recyvToolsAll.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
      mBinding.recyvToolsAll.setItemAnimator(new DefaultItemAnimator());
      mBinding.recyvToolsAll.setAdapter(mAllAdapter);
    }
    mAllAdapter.notifyDataSetChanged();
    mAllAdapter.setOnItemClickListenerEdit(new ApplyAdapter.OnItemClickListenerEdit() {
      @Override
      public void onItemAdd(View view, int position) {
        List<ApplyMenu> mineMenus = mMineAdapter.getAdapterData();
        if (mineMenus.size() >= 7) {
          ToastUtils.showShort("不能再多啦");
        } else {
          List<ApplyMenu> allMenus = mAllAdapter.getAdapterData();
          ApplyMenu allMenu = allMenus.get(position);
          mineMenus.add(new ApplyMenu(allMenu.getName(), allMenu.getId(), allMenu.getType(), allMenu.getUrl(),
              allMenu.getIndex(), allMenu.getFixed(), allMenu.getImgRes(), 0));
          mMineAdapter.notifyDataSetChanged();
          allMenu.setState(0);
          mAllAdapter.notifyDataSetChanged();
        }
      }

      @Override
      public void onItemDel(View view, int position) {
        if (mMineAdapter.getAdapterData().size() <= 3) {
          ToastUtils.showShort("不能再少啦");
        } else {
          List<ApplyMenu> mineMenus = mMineAdapter.getAdapterData();
          List<ApplyMenu> allMenus = mAllAdapter.getAdapterData();
          ApplyMenu menu = allMenus.get(position);
          menu.setState(1);
          mAllAdapter.notifyDataSetChanged();
          for (int i = 0; i < mineMenus.size(); i++) {
            if (menu.getName().equals(mineMenus.get(i).getName())) {
              mineMenus.remove(i);
              break;
            }
          }
          mMineAdapter.notifyDataSetChanged();
        }
      }
    });
    mAllAdapter.setOnItemClickListener(new ApplyAdapter.OnItemClickListener() {

      @Override
      public void onItemClick(View view, ApplyMenu menu) {
        goMenu(menu);
      }
    });
  }

  private void initMineView() {
    ArrayList<ApplyMenu> mineMenus = mMenuManager.loadMenus(MenuConstant.APPLY_MINE, getMineMenuKey());
    if (mMineAdapter == null) {
      /**设置头部数据适配器*/
      mMineAdapter = new MineMenuAdapter(getContext(), mineMenus);
      mBinding.recyvTools.setLayoutManager(new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false));
      //DefaultItemAnimator()默认的RecyclerView动画实现类，如果产品需求没有特别复杂的动画要求，可以使用
      mBinding.recyvTools.setItemAnimator(new DefaultItemAnimator());//设置Item增加、移除动画
      mBinding.recyvTools.setAdapter(mMineAdapter);
      //加载数据完毕后 设置监听，拖拽移动事件 和 点击事件  把ItemDragHelperCallback设置给RecyclerView使用
      MenuDragCallback callback = new MenuDragCallback(mMineAdapter);
      //是一个工具类，可实现侧滑删除和拖拽移动,完成后数据的刷新（UI更新）由重写的ItemDragHelperCallback完成
      ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
      //调用ItemTouchHelper的attachToRecyclerView方法建立联系
      touchHelper.attachToRecyclerView(mBinding.recyvTools);
      mMineAdapter.setMenuDragCallback(callback);
    }
    mMineAdapter.addData(mineMenus);

    mMineAdapter.setOnItemClickListenerEdit(new MineMenuAdapter.OnItemClickListenerEdit() {
      @Override
      public void onItemClick(View view, int position) {
        List<ApplyMenu> mineMenus = mMineAdapter.getAdapterData();
        List<ApplyMenu> allMenus = mAllAdapter.getAdapterData();
        if (mineMenus.size() <= 3) {
          ToastUtils.showShort("不能再少啦");
          return;
        }
        ApplyMenu menu = mineMenus.get(position);
        for (int i = 0; i < allMenus.size(); i++) {
          if (menu.getName().equals(allMenus.get(i).getName())) {
            allMenus.get(i).setState(1);
          }
        }
        mAllAdapter.notifyDataSetChanged();
        mineMenus.remove(position);
        mMineAdapter.notifyDataSetChanged();
      }
    });
    mMineAdapter.setOnItemClickListener(new MineMenuAdapter.OnItemClickListener() {

      @Override
      public void onItemClick(View view, ApplyMenu menu) {
        goMenu(menu);
      }
    });
  }

  private void save(List<ApplyMenu> mineApplyMenus, List<ApplyMenu> allApplyMenus) {
    ACache cache = ACache.get(getContext().getApplicationContext());
    cache.put(getMineMenuKey(), (ArrayList<ApplyMenu>) mineApplyMenus);
    cache.put(getAllMenuKey(), (ArrayList<ApplyMenu>) allApplyMenus);
  }

  @NonNull
  private String getAllMenuKey() {
    return MenuConstant.APPLY_MORE + "_" + GlobleConstant.getgDeviceId() + "_" + PrefConstant.getUserUniqueKey("0");
  }

  @NonNull
  private String getMineMenuKey() {
    return MenuConstant.APPLY_MINE + "_" + GlobleConstant.getgDeviceId() + "_" + PrefConstant.getUserUniqueKey("0");
  }


  private void goMenu(ApplyMenu menu) {
    switch (menu.getId()) {
      case "100001":
        getRouterMainActivity().scrollToPage(0);
        break;

      case "100002":
        querySignalModeRequest();
        break;

      case "100003":
        Intent intent2 = new Intent();
        mNavigator.navigateTo(getContext(), IntelligentBandActivity.class, intent2);
        break;

      case "100004":
        Intent intent6 = new Intent();
        mNavigator.navigateTo(getContext(), SafeInspectionActivity.class, intent6);
        break;

      case "100005":
        Intent intent5 = new Intent();
        mNavigator.navigateTo(getContext(), WirelessRelayActivity.class, intent5);
        break;

      case "100006":
        //防蹭网
        queryAntiFritNetStatus();
        break;

      case "100007":
        Intent intent7 = new Intent();
        mNavigator.navigateTo(getContext(), BandSpeedMeasureActivity.class, intent7);
        break;

      case "100008":
        Intent intent3 = new Intent();
        mNavigator.navigateTo(getContext(), SpecialAttentionActivity.class, intent3);
        break;

      case "100009":
        mNavigator.navigateTo(getContext(), WifiTimeSwitchActivity.class);
        break;

      case "1000010":
        Intent intent11 = new Intent();
        mNavigator.navigateTo(getContext(), GuestWifiActivity.class, intent11);
        break;

      case "1000011":
        Intent intent12 = new Intent();
        mNavigator.navigateTo(getContext(), ChildCareActivity.class, intent12);
        break;

      case "1000012":
        //非直连不允许进入
        isRouterNetConnectDirect();
        if (isRouterDirect) {
          GlobleConstant.isSambaExtranetAccess = false;
          System.out.println("GlobleConstant.isSambaExtranetAccess routertools1 = " + GlobleConstant.isSambaExtranetAccess+" gloableId = "+GlobleConstant.getgDeviceId()+" PreconstaneId = "+PrefConstant.ROUTER_ID_CONNECTED_DIRECT);
          mNavigator.navigateTo(getContext(), RemoteMainActivity.class);
        } else {
  /*      mNetCheckPopWin = new NetCheckPopupWindow(getActivity());
        mNetCheckPopWin.showAtLocation(mNetCheckPopWin.getOuterLayout(), Gravity.CENTER,0,0);*/
          GlobleConstant.isSambaExtranetAccess = true;
          System.out.println("GlobleConstant.isSambaExtranetAccess routertools2 = " + GlobleConstant.isSambaExtranetAccess+" gloableId = "+GlobleConstant.getgDeviceId()+" PreconstaneId = "+PrefConstant.ROUTER_ID_CONNECTED_DIRECT);
          mNavigator.navigateTo(getContext(), RemoteMainActivity.class);
        }
        break;
    }
  }


  public void isRouterNetConnectDirect() {

    try {
      if (TextUtils.isEmpty(GlobleConstant.getgDeviceId())) {
        System.out.println("链路测试：中转网络");
        isRouterDirect = false;
      } else {
        if ((PrefConstant.ROUTER_ID_CONNECTED_DIRECT).equals(GlobleConstant.getgDeviceId())) {
          System.out.println("链路测试：直连网络");
          isRouterDirect = true;
        } else {
          System.out.println("链路测试：中转网络");
          isRouterDirect = false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private RouterMainActivity getRouterMainActivity() {
    return ((RouterMainActivity) getActivity());
  }
}