package com.fernandocejas.android10.sample.presentation.view.login.welcome;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentSecondViewBinding;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentThirdViewBinding;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class ThirdViewFragment extends BaseFragment {

  private FragmentThirdViewBinding mBinding;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public static ThirdViewFragment newInstance() {
    Bundle args = new Bundle();
    ThirdViewFragment fragment = new ThirdViewFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_third_view, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  private void initView() {
    mBinding.btnStartApp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
      }
    });

  }

}
