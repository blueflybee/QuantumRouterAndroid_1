package com.fernandocejas.android10.sample.presentation.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/07/28
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ListViewUtil {

  public ListViewUtil() {

  }

  public static void setListViewHeightBasedOnChildren(ListView listView) {
    // 获取ListView对应的Adapter
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight = 0;
    for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0); // 计算子项View 的宽高
      totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight
        + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    // listView.getDividerHeight()获取子项间分隔符占用的高度
    // params.height最后得到整个ListView完整显示需要的高度
    listView.setLayoutParams(params);
  }

  public static void setExpandableListViewHeightBasedOnChildren(ExpandableListView expandableListView) {
    // 获取ListView对应的Adapter
    BaseExpandableListAdapter expandableListAdapter = (BaseExpandableListAdapter) expandableListView.getExpandableListAdapter();
    if (expandableListAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight = 0;
    int num = 0;
    int groupCount = expandableListAdapter.getGroupCount(), count;
    num = groupCount;
    for (int i = 0; i < groupCount; i++) {
      count = expandableListAdapter.getChildrenCount(i);
      View groupListItem = expandableListAdapter.getGroupView(i, false, null, expandableListView);
      groupListItem.measure(0, 0); // 计算子项View 的宽高
      totalHeight += groupListItem.getMeasuredHeight(); // 统计所有子项的总高度
      num = num + count;
      for (int j = 0; j < count; j++) { // listAdapter.getCount()返回数据项的数目

        View listItem = expandableListAdapter.getChildView(i, j, false, null, expandableListView);

        listItem.measure(0, 0); // 计算子项View 的宽高
        totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        //          Log.i("test", ""+totalHeight);
      }
    }
    //  Log.i("test", "num:"+num);
    ViewGroup.LayoutParams params = expandableListView.getLayoutParams();
    params.height = totalHeight
        + (expandableListView.getDividerHeight() * (num - 1));
    //  Log.i("test", "height:"+params.height);
    // listView.getDividerHeight()获取子项间分隔符占用的高度
    // params.height最后得到整个ListView完整显示需要的高度
    expandableListView.setLayoutParams(params);
  }
}
