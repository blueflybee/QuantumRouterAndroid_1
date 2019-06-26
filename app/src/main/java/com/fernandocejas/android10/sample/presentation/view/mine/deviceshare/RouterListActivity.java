package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterListBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 设备列表
 *      version: 1.0
 * </pre>
 */

public class RouterListActivity extends BaseActivity implements AdapterView.OnItemClickListener{
    private RouterListAdapter mRouterListAdapter;
    private ActivityRouterListBinding mRouterListBinding;
    private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> mRouterList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRouterListBinding = DataBindingUtil.setContentView(this, R.layout.activity_router_list);
        initTitleBar("设备列表");
        initView();
        initializeInjector();
        initPresenter();
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

    }

    private void initView() {
        // 获取网关列表
        mRouterList = getDevTree();

        if(mRouterList.size() == 0){
            //空视图
            mRouterListBinding.lvRouterList.setEmptyView(findViewById(R.id.include_router_list_empty_view));
            findViewById(R.id.include_router_list_empty_view).findViewById(R.id.btn_empty_add_router).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNavigator.navigateTo(RouterListActivity.this, AddRouterActivity.class,null);
                }
            });

            return;
        }

        mRouterListAdapter = new RouterListAdapter(this,mRouterList,R.layout.item_router_list);
        mRouterListBinding.lvRouterList.setAdapter(mRouterListAdapter);

        mRouterListBinding.lvRouterList.setOnItemClickListener(this);

        /*mRouterListAdapter.setOnMoreClickListener(new RouterListAdapter.OnMoreClickListener() {
            @Override
            public void onMoreClick() {
                SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
                String userUniqueKey = spUtils.getString(PrefConstant.SP_USER_UNIQUE_KEY);

                GetSharedMemListRequest request = new GetSharedMemListRequest();
                mRouterSerialNum = mRouterList.get(mRouterListAdapter.getItemPostion()).getRouterSerialNo();
                request.setRouterSerialNo(mRouterSerialNum);
                request.setUserUniqueKey(userUniqueKey);
                QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
                encryptInfo.setData(request);
                mGetShareMemListPresenter.getShareMemList(encryptInfo);
            }
        });*/
    }

    private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> getDevTree() {
        List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> devTreeResponses = (List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>>)
            getIntent().getSerializableExtra(mNavigator.EXTR_ROUTER_LIST);
        return devTreeResponses;
    }

   @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       Intent intent = new Intent();
       intent.putExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM,mRouterList.get(position).getDeviceSerialNo());
       mNavigator.navigateTo(this,ShareMemberListActivity.class,intent);
    }

}

