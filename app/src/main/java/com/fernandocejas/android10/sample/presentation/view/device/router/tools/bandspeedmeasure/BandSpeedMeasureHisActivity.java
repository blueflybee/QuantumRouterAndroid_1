package com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySpeedHistoryBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.BandSpeedHistoryAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 宽带测速 历史记录 存在本地
 *      version: 1.0
 * </pre>
 */

public class BandSpeedMeasureHisActivity extends BaseActivity{
  private ActivitySpeedHistoryBinding mBinding;
  private List<BandSpeedHisBean> speedHisList;
  private BandSpeedHistoryAdapter adapter;
  private Boolean isFirstHasData = true;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_speed_history);
    initTitleBar("宽带测速历史记录");
    initView();
    initData();
  }

  private void initData() {
    //从数据库查询数据

    speedHisList = new DatabaseUtil(this).queryAllBandSpeedHistory();

    if(speedHisList.size() != 0){

      if(isFirstHasData){
        isFirstHasData = false;
        adapter = new BandSpeedHistoryAdapter(this, speedHisList, R.layout.item_speed_history);
        mBinding.lvSpeedHistory.setAdapter(adapter);
      }else{
        adapter.getPostionsToInsertTitle(speedHisList);  //设置新增数据需要插入title的位置
        adapter.notifyDataSetChanged();
      }

    }else{
      mBinding.lvSpeedHistory.setEmptyView(findViewById(R.id.include_empty_view_speedHis));
      findViewById(R.id.include_empty_view_speedHis).findViewById(R.id.btn_empty_speed).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          finish();
        }
      });
    }
  }

  private void initView() {
    speedHisList = new ArrayList<>();
  }

}
