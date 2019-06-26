package com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModeChooseBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetCustomQuestionBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.CustomQuestionListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.ModeChooseAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WaitAuthBean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class ModeChooseActivity extends BaseActivity implements AdapterView.OnItemClickListener{
  private ActivityModeChooseBinding mBinding;
  private ModeChooseAdapter adapter;
  private List<WaitAuthBean> waitAuthList;
  private String[] content = {"自动模式","游戏优先","网页优先","视频优先"};
  private int checkedItemPosition = 0;

  public static ModeChooseActivity newInstance() {
    ModeChooseActivity fragment = new ModeChooseActivity();
    return fragment;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mode_choose);

    initView();
  }

  private void initView() {
    initTitleBar("应用优先级");

    waitAuthList = new ArrayList<>();

    for (int i = 0; i < content.length; i++) {
      WaitAuthBean bean = new WaitAuthBean();
      bean.setName(content[i]);
      waitAuthList.add(bean);
    }

    getQuestionIndex(getMode());

    adapter = new ModeChooseAdapter(this,waitAuthList,R.layout.item_custom_question,checkedItemPosition);
    mBinding.lvModeChoose.setAdapter(adapter);
    mBinding.lvModeChoose.setOnItemClickListener(this);
  }

  private void getQuestionIndex(String question) {
    switch (question) {
      case "自动模式":
        checkedItemPosition = 0;
        break;

      case "游戏优先":
        checkedItemPosition = 1;
        break;

      case "网页优先":
        checkedItemPosition = 2;
        break;

      case "视频优先":
        checkedItemPosition = 3;
        break;

      default:
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvModeChoose.getCheckedItemPosition();

    ((TextView)view.findViewById(R.id.tv_content)).setTextColor(getResources().getColor(R.color.blue_2196f3));
    ((ImageView)view.findViewById(R.id.img_chooseLogo)).setVisibility(View.VISIBLE);

    adapter.initOtherItem(checkedItemPosition);

    Intent intent = new Intent();
    intent.putExtra("CHOOSEDMODE",content[checkedItemPosition]);
    setResult(2,intent);
  }

  private String getMode(){
    return getIntent().getStringExtra("MODE");
  }

}