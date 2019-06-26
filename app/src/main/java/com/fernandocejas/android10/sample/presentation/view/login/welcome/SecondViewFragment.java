package com.fernandocejas.android10.sample.presentation.view.login.welcome;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentFirstViewBinding;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentSecondViewBinding;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class SecondViewFragment extends BaseFragment{

  private FragmentSecondViewBinding mBinding;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  public static SecondViewFragment newInstance() {
    Bundle args = new Bundle();
    SecondViewFragment fragment = new SecondViewFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_view, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initView();
  }

  private void initView() {

  }

}
