package com.fernandocejas.android10.sample.presentation.utils;

import com.fernandocejas.android10.sample.presentation.data.KVPair;
import com.fernandocejas.android10.sample.presentation.data.SpinnerItem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 保存和操作字典数据
 * 和spinner结合使用
 */
public class DictUtil {

  private static final String KEY_TAI_ZHANG = "LEDGER_TYPE";

  private static final Map<String, String> accountTypes = new HashMap<>();

  private static Map<String, HashMap<String, String>> dics = new HashMap<>();

  static {
    accountTypes.put("0", "全部");
    accountTypes.put("1", "采购台帐");
    accountTypes.put("2", "添加剂使用台帐");
    accountTypes.put("3", "废弃物回收台帐");
    accountTypes.put("4", "消毒记录台帐");
    accountTypes.put("5", "接待旅游台帐");
    accountTypes.put("6", "集体用餐台帐");
  }

  public static Map<String, HashMap<String, String>> getDics() {
    return dics;
  }

  public static void setDics(Map<String, HashMap<String, String>> dics) {
    if (dics == null) {
      return;
    }
    DictUtil.dics = dics;
  }

  //public static List<PostType> getPostList() {
  //  Map<String, String> postMap = getPostMap();
  //  if (postMap == null) {
  //    return new ArrayList<>();
  //  }
  //  List<PostType> postTypes = new ArrayList<>();
  //  Set<Map.Entry<String, String>> entries = postMap.entrySet();
  //  for (Map.Entry<String, String> entry : entries) {
  //    PostType postType = new PostType();
  //    postType.setId(entry.getKey());
  //    postType.setPost(entry.getValue());
  //    postTypes.add(postType);
  //  }
  //  return postTypes;
  //}

  public static Map<String, String> getPlatAccountMap() {
    if (dics == null || dics.isEmpty()) return accountTypes;
    return dics.get(KEY_TAI_ZHANG);
  }

  public static List<KVPair> getAccountTypeList() {
    Map<String, String> platAccountMap = getPlatAccountMap();
    if (platAccountMap == null) {
      return new ArrayList<>();
    }
    List<KVPair> accountTypes = new ArrayList<>();
    Map<String, String> sortMap =
        MapUtil.sortStringMapByKey(platAccountMap, new Comparator<String>() {
          @Override public int compare(String str1, String str2) {
            return str1.compareTo(str2);
          }
        });
    Set<Map.Entry<String, String>> entries = sortMap.entrySet();
    for (Map.Entry<String, String> entry : entries) {
      SpinnerItem accountType = new SpinnerItem();
      accountType.setKey(entry.getKey());
      accountType.setValue(entry.getValue());
      accountTypes.add(accountType);
    }
    return accountTypes;
  }

  public static String getAccountTypeByKey(String key) {
    return getPlatAccountMap().get(key);
  }

  public static List<SpinnerItem> getYesOrNos() {
    List<SpinnerItem> yesOrNos = new ArrayList<>();

    SpinnerItem yesOrNo = new SpinnerItem();
    yesOrNo.setKey("1");
    yesOrNo.setValue("是");
    yesOrNos.add(yesOrNo);

    yesOrNo = new SpinnerItem();
    yesOrNo.setKey("0");
    yesOrNo.setValue("否");
    yesOrNos.add(yesOrNo);

    return yesOrNos;
  }
}
