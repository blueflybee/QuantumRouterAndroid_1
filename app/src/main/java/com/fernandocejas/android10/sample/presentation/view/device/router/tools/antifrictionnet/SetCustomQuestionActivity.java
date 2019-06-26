package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetCustomQuestionBinding;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentWaitingAuthBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.CustomQuestionListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.WaitAuthListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WaitAuthBean;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;

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
public class SetCustomQuestionActivity extends BaseActivity implements AdapterView.OnItemClickListener {
  private ActivitySetCustomQuestionBinding mBinding;
  private CustomQuestionListAdapter adapter;
  private List<WaitAuthBean> waitAuthList;
  private String[] content = {"我的手机号码后六位？", "我的微信号？", "我的qq号码？", "自定义"};
  private int checkedItemPosition = 0;

  public static SetCustomQuestionActivity newInstance() {
    SetCustomQuestionActivity fragment = new SetCustomQuestionActivity();
    return fragment;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_custom_question);

    initView();
  }

  private String getQuestion() {
    return getIntent().getStringExtra("Question");
  }

  private void initView() {
    initTitleBar("防蹭网");

    waitAuthList = new ArrayList<>();

    for (int i = 0; i < content.length; i++) {
      WaitAuthBean bean = new WaitAuthBean();
      bean.setName(content[i]);
      waitAuthList.add(bean);
    }
    System.out.println("Anti问题 = " + getQuestion());
    getQuestionIndex(getQuestion());

    adapter = new CustomQuestionListAdapter(this, waitAuthList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvCustonQuestion.setAdapter(adapter);
    mBinding.lvCustonQuestion.setOnItemClickListener(this);
  }

  private void getQuestionIndex(String question) {
    switch (question) {
      case "我的手机号码后六位？":
        checkedItemPosition = 0;
        break;

      case "我的微信号？":
        checkedItemPosition = 1;
        break;

      case "我的qq号码？":
        checkedItemPosition = 2;
        break;

      case "自定义":
        checkedItemPosition = 3;
        break;

      default:
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvCustonQuestion.getCheckedItemPosition();

    ((TextView) view.findViewById(R.id.tv_content)).setTextColor(getResources().getColor(R.color.blue_2196f3));
    ((ImageView) view.findViewById(R.id.img_chooseLogo)).setVisibility(View.VISIBLE);

    adapter.initOtherItem(checkedItemPosition);

    Intent intent = new Intent();
    intent.putExtra("CHOOSEDQUESTION", content[checkedItemPosition]);
    setResult(2, intent);
    finish();
  }

}