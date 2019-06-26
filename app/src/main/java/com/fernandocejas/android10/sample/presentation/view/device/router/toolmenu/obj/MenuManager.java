package com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj;


import android.content.res.Resources;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.constant.MenuConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.util.ACache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuManager {

  private int[] fixed = new int[]{0, 1};

//  public ArrayList<ApplyMenu> loadMenus(String type) {
//    ACache aCache = ACache.get(AndroidApplication.mApplicationContext);
//    ArrayList<ApplyMenu> menus = (ArrayList<ApplyMenu>) aCache.getAsObject(type);
//    if (menus == null) {
//      menus = loadDefMenus(type);
//      aCache.put(type, menus);
//    }
//    return menus;
//  }

  public ArrayList<ApplyMenu> loadMenus(String type, String key) {
    ACache aCache = ACache.get(AndroidApplication.mApplicationContext);
    ArrayList<ApplyMenu> menus = (ArrayList<ApplyMenu>) aCache.getAsObject(key);
    if (menus == null) {
      menus = loadDefMenus(type);
      aCache.put(key, menus);
    }
    return menus;
  }

  private ArrayList<ApplyMenu> loadDefMenus(String type) {
    switch (type) {
      case MenuConstant.APPLY_MINE:
        return loadMineMenus();
      case MenuConstant.APPLY_MORE:
        return loadAllMenus();
      default:
        return new ArrayList<>();
    }
  }


//  String[] titlesHomePage = {"当前状态", "信号调节", "智能宽带", "特别关注", "定时开关", "安全存储", "一键体检"};
//
//  String[] titlesAllPage = {"当前状态", "信号调节", "智能宽带", "一键体检", "无线中继", "防蹭网", "宽带测速", "特别关注", "定时开关", "访客WiFi", "儿童关怀", "安全存储"};
//
//  Integer[] iconsHomePage = {
//      R.drawable.indexic_doorstatus,
//      R.drawable.indexic_cignal,
//      R.drawable.indexic_intelligent,
//      R.drawable.indexic_special,
//      R.drawable.indexic_timing,
//      R.drawable.indexic_save,
//      R.drawable.indexic_test};
//
//  Integer[] iconsAll = {
//      R.drawable.indexic_doorstatus,
//      R.drawable.indexic_cignal,
//      R.drawable.indexic_intelligent,
//      R.drawable.indexic_test,
//      R.drawable.indexic_wireless,
//      R.drawable.indexic_ban,
//      R.drawable.indexic_speedtest,
//      R.drawable.indexic_special,
//      R.drawable.indexic_timing,
//      R.drawable.indexic_guest,
//      R.drawable.indexic_children,
//      R.drawable.indexic_save
//  };

  private ArrayList<ApplyMenu> loadAllMenus() {
    Resources resources = AndroidApplication.mApplicationContext.getResources();
    List<String> names = Arrays.asList(resources.getStringArray(R.array.all_apply_name));
    List<String> ids = Arrays.asList(resources.getStringArray(R.array.all_apply_id));
    List<Integer> images = new ArrayList<>();
    images.add(R.drawable.indexic_doorstatus);
    images.add(R.drawable.indexic_cignal);
    images.add(R.drawable.indexic_intelligent);
    images.add(R.drawable.indexic_test);
    images.add(R.drawable.indexic_wireless);
    images.add(R.drawable.indexic_ban);
    images.add(R.drawable.indexic_speedtest);
    images.add(R.drawable.indexic_special);
    images.add(R.drawable.indexic_timing);
    images.add(R.drawable.indexic_guest);
    images.add(R.drawable.indexic_children);
    images.add(R.drawable.indexic_save);
    ArrayList<ApplyMenu> menus = new ArrayList<>();
    for (int i = 0; i < names.size(); i++) {
      int state = (i == 0 || i == 6 || i == 11) ? 0 : 1;
      menus.add(new ApplyMenu(
          names.get(i),
          ids.get(i),
          MenuConstant.getType(ids.get(i)),
          "",
          i,
          false,
          images.get(i),
          state));
    }
    return menus;
  }

  private ArrayList<ApplyMenu> loadMineMenus() {
    Resources resources = AndroidApplication.mApplicationContext.getResources();
    List<String> names = Arrays.asList(resources.getStringArray(R.array.default_apply_name_s));
    System.out.println(names);
    List<String> ids = Arrays.asList(resources.getStringArray(R.array.default_apply_id_s));
    List<Integer> images = new ArrayList<>();
    images.add(R.drawable.indexic_doorstatus);
    images.add(R.drawable.indexic_speedtest);
    images.add(R.drawable.indexic_save);
    ArrayList<ApplyMenu> defaultMenus = new ArrayList<>();

    for (int i = 0; i < names.size(); i++) {
      defaultMenus.add(new ApplyMenu(
          names.get(i),
          ids.get(i),
          MenuConstant.getType(ids.get(i)),
          "",
          i,
          false,
          images.get(i),
          0));
    }
    return defaultMenus;
  }

  private boolean isFixed(int i) {
    for (int j = 0; j < fixed.length; j++) {
      return i == fixed[j] ? true : false;
    }
    return false;
  }
}
