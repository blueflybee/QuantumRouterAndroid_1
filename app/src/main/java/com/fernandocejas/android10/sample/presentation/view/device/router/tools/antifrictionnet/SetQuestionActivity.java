package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityQuestionSettingBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;
import com.qtec.router.model.rsp.SetAntiNetQuestionResponse;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SetQuestionActivity extends BaseActivity implements View.OnClickListener, IEnableAntiFritNetView, IGetAntiFritQuestionView, ISetAntiNetQuestionView {
  private ActivityQuestionSettingBinding mBinding;

  @Inject
  EnableAntiFritNetPresenter mEnableNetPresenter;
  @Inject
  GetAntiFritQuestionPresenter mGetQuestionPresenter;
  @Inject
  SetAntiNetQuestionPresenter mSetQuestionPresenter;
  private Boolean isQuestionDefinded = false;//自定义
  private InputWatcher mInputWatcher = new InputWatcher();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_question_setting);
    initializeInjector();
    initPresenter();

    initView();

    //控制表情输入
    mBinding.etVirifyQuestion.setFilters(InputUtil.emojiFilters);
    mBinding.etVirifyAnswer.setFilters(InputUtil.emojiFilters);

    if ("0".equals(getFlag())) {
      //0 首次配置
      mBinding.etVirifyQuestion.setText("我的手机号码后六位？");
      mBinding.etVirifyQuestion.setEnabled(false);

      mBinding.etVirifyAnswer.requestFocus();

      mBinding.tvQuestion.setText("默认");

    } else {
      // 1 问题设置
      getQuestionRequest();
    }

  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mEnableNetPresenter.setView(this);
    mGetQuestionPresenter.setView(this);
    mSetQuestionPresenter.setView(this);
  }

  private void getQuestionRequest() {
    mGetQuestionPresenter.getAntiFritQuestion(GlobleConstant.getgDeviceId());
  }

  private void enbleAntiFritNet() {

    if(TextUtils.isEmpty(getText(mBinding.etVirifyQuestion))){
      Toast.makeText(this, "验证问题不能为空", Toast.LENGTH_SHORT).show();
      return;
    }

    if(TextUtils.isEmpty(getText(mBinding.etVirifyAnswer))){
      Toast.makeText(this, "验证答案不能为空", Toast.LENGTH_SHORT).show();
      return;
    }

    if ("0".equals(getFlag())) {
      //新增
      mEnableNetPresenter.enableAntiFritNet(GlobleConstant.getgDeviceId(), 1, mBinding.etVirifyQuestion.getText().toString().trim(),
          mBinding.etVirifyAnswer.getText().toString().trim());

    } else {

      /*mEnableNetPresenter.enableAntiFritNet(GlobleConstant.getgDeviceId(), getAntiStatus(), mBinding.etVirifyQuestion.getText().toString().trim(),
          mBinding.etVirifyAnswer.getText().toString().trim());*/
      mSetQuestionPresenter.setAntiQuestion(GlobleConstant.getgDeviceId(), mBinding.etVirifyQuestion.getText().toString().trim(), mBinding.etVirifyAnswer.getText().toString().trim());
    }
  }

  private void initView() {
    initTitleBar("防蹭网");
    mBinding.rlSetQuestion.setOnClickListener(this);
    mBinding.btnConfirm.setOnClickListener(this);
  }

  /**
   * 判断是否是设置设置
   */
  private String getFlag() {
    return getIntent().getStringExtra("FLAG");
  }

  private int getAntiStatus() {
    return getIntent().getIntExtra("Anti", 0);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_set_question:
        Intent intent = new Intent(this, SetCustomQuestionActivity.class);
        if (isQuestionDefinded) {
          intent.putExtra("Question", "自定义");
        } else {
          intent.putExtra("Question", mBinding.etVirifyQuestion.getText().toString().trim());
        }

        startActivityForResult(intent, 2);
        break;

      case R.id.btn_confirm:
        enbleAntiFritNet();
        break;

      default:
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (data == null) {
      return;
    }

    if (requestCode == 2) {
      String chooseQuestion = data.getStringExtra("CHOOSEDQUESTION");
      mBinding.etVirifyAnswer.setText("");
      setQuestionLimitRule(chooseQuestion);

      if (!TextUtils.isEmpty(chooseQuestion)) {
        if ("自定义".equals(chooseQuestion)) {
          mBinding.tvQuestion.setText("自定义");
          isQuestionDefinded = true;
          mBinding.etVirifyQuestion.getText().clear();
          mBinding.etVirifyAnswer.getText().clear();
          mBinding.etVirifyQuestion.setEnabled(true);
          mBinding.etVirifyQuestion.setFocusable(true);
          mBinding.etVirifyQuestion.requestFocus();

        } else {
          mBinding.tvQuestion.setText("默认");
          isQuestionDefinded = false;
          mBinding.etVirifyQuestion.setText(chooseQuestion);
          /*setQuestionLimitRule(questionChoosed);*/
          mBinding.etVirifyAnswer.getText().clear();
          mBinding.etVirifyAnswer.setFocusable(true);
          mBinding.etVirifyAnswer.requestFocus();

          mBinding.etVirifyQuestion.setSelection(chooseQuestion.length());//光标放在最后
          mBinding.etVirifyQuestion.setEnabled(false);

        }

      }
    }

  }

  private void setQuestionLimitRule(String question) {
    System.out.println("question = " + question);
    if (TextUtils.isEmpty(question)) return;
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    if (question.contains("手机号码")) {
      condition.setLength(6);
      mBinding.etVirifyAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
      InputUtil.setMaxLengthFilter(mBinding.etVirifyAnswer, 6);
    } else if (question.contains("微信号")) {
      condition.setRange(new InputWatcher.InputRange(6, 20));
      mBinding.etVirifyAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
      InputUtil.setMaxLengthFilter(mBinding.etVirifyAnswer, 20);

      String digits = getString(R.string.filter_wei_xin);
      mBinding.etVirifyAnswer.setKeyListener(DigitsKeyListener.getInstance(digits));
    } else if (question.contains("qq号")) {
      condition.setRange(new InputWatcher.InputRange(5, 12));
      mBinding.etVirifyAnswer.setInputType(InputType.TYPE_CLASS_NUMBER);
      InputUtil.setMaxLengthFilter(mBinding.etVirifyAnswer, 12);
    }else{
      condition = new InputWatcher.WatchCondition();
      InputUtil.setMaxLengthFilter(mBinding.etVirifyAnswer, 64);
      mBinding.etVirifyAnswer.setInputType(InputType.TYPE_MASK_CLASS);
    }
    mInputWatcher.clearEts();
    mInputWatcher.addEt(mBinding.etVirifyAnswer, condition);
    mInputWatcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean isEmpty) {
        mBinding.btnConfirm.setClickable(!isEmpty);
        if (isEmpty) {
          mBinding.btnConfirm.setBackgroundColor(SetQuestionActivity.this.getResources().getColor(R.color.white_bbdefb));
        } else {
          mBinding.btnConfirm.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
        }
      }
    });
  }

  @Override
  public void getAntiFritNetStatus(EnableAntiFritNetResponse response) {
    /*if ("0".equals(getFlag())) {
      startActivity(new Intent(this, AntiFritNetMainActivity.class));
    } else {
      Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
      finish();
    }*/
    startActivity(new Intent(this, AntiFritNetMainActivity.class));

  }

  /**
   * 获取历史问题
   */
  @Override
  public void getAntiFritQuestion(GetAntiFritQuestionResponse response) {

    if("我的手机号码后六位？".equals(response.getQuestion()) ||
        "我的微信号？".equals(response.getQuestion()) ||
        "我的qq号码？".equals(response.getQuestion())){
      mBinding.tvQuestion.setText("默认");
      isQuestionDefinded = false;
    }else {
      mBinding.tvQuestion.setText("自定义");
      isQuestionDefinded = true;
    }

    mBinding.etVirifyAnswer.setText(response.getAnswer());
    mBinding.etVirifyQuestion.setText(response.getQuestion());
    /*moveEditCursorToEnd(mBinding.etVirifyQuestion);*/
    mBinding.etVirifyQuestion.setEnabled(false);

    setQuestionLimitRule(response.getQuestion());
  }

  @Override
  public void setAntiNetQuestion(SetAntiNetQuestionResponse response) {
    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
    finish();
  }
}