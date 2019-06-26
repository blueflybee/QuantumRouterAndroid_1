package com.fernandocejas.android10.sample.presentation.utils;

import java.util.List;
import java.util.Map;

/**
 * @author shaojun
 * @name DataMapUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-10-27
 */
public class DataMapUtil {

  public static final String TITLE = "title";
  public static final String CONTENT = "content";
  private static List<Map<String, String>> mPurchaseAccounts;


  //public static List<Map<String, String>> getCheckDetailMaps(DailyCheck.Content content) {
  //  List<Map<String, String>> list = new ArrayList<Map<String, String>>();
  //  Map<String, String> map;
  //  if (content != null) {
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "检查内容：");
  //    map.put(CONTENT, content.getItemName());
  //    list.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "检查方式：");
  //    map.put(CONTENT, content.getManner());
  //    list.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "合理性缺项：");
  //    map.put(CONTENT, content.getIsLacuna());
  //    list.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "重要性：");
  //    map.put(CONTENT, content.getSignificance());
  //    list.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "分值：");
  //    map.put(CONTENT, content.getScore());
  //    list.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "检查结果：");
  //    map.put(CONTENT, content.getResult());
  //    list.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put(TITLE, "说明：");
  //    map.put(CONTENT, content.getRemark());
  //    list.add(map);
  //  }
  //
  //  return list;
  //}

  //public static List<Map<String, String>> getPurchaseAccountMaps() {
  //  if (mPurchaseAccounts == null) {
  //    mPurchaseAccounts = new ArrayList<Map<String, String>>();
  //    Map<String, String> map = new HashMap<String, String>();
  //    map.put("产品名称：", "");
  //    mPurchaseAccounts.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put("数量：", "");
  //    mPurchaseAccounts.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put("保质期：", "");
  //    mPurchaseAccounts.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put("供货商：", "");
  //    mPurchaseAccounts.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put("生产厂家：", "");
  //    mPurchaseAccounts.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put("生产批号：", "");
  //    mPurchaseAccounts.add(map);
  //
  //    map = new HashMap<String, String>();
  //    map.put("采购日期：", "");
  //    mPurchaseAccounts.add(map);
  //
  //
  //  }
  //  return mPurchaseAccounts;
  //}
}
