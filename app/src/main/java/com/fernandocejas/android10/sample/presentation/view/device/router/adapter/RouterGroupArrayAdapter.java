package com.fernandocejas.android10.sample.presentation.view.device.router.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.qtec.mapp.model.rsp.GetRouterGroupsResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/22
 *     desc   : 网关基本信息
 *     version: 1.0
 * </pre>
 */
public class RouterGroupArrayAdapter extends ArrayAdapter<GetRouterGroupsResponse> {

  public RouterGroupArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<GetRouterGroupsResponse> objects) {
    super(context, resource, objects);
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = super.getView(position, convertView, parent);
    CheckedTextView checkedTv = (CheckedTextView) view.findViewById(android.R.id.text1);
    checkedTv.setText(getItem(position).getGroupName());
    return view;
  }




}
