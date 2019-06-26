package com.fernandocejas.android10.sample.presentation.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 对map进行按key或value排序
 * @author shaojun
 * @name MapUtil
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-10-30
 */
public class MapUtil {
  /**
   * 使用 Map按key进行排序
   * 1、按键排序
   * jdk内置的java.util包下的TreeMap<K,V>既可满足此类需求，向其构造方法 TreeMap(Comparator<? super K> comparator)
   * 传入我们自定义的比较器即可实现按键排序。
   * @param map
   */
  public static Map<String, Object> sortMapByKey(Map<String, Object> map, Comparator comparator) {
    if (map == null || map.isEmpty()) {
      return null;
    }
    Map<String, Object> sortMap = new TreeMap<String, Object>(comparator);
    sortMap.putAll(map);
    return sortMap;
  }

  /**
   * 使用 Map按key进行排序
   * 1、按键排序
   * jdk内置的java.util包下的TreeMap<K,V>既可满足此类需求，向其构造方法 TreeMap(Comparator<? super K> comparator)
   * 传入我们自定义的比较器即可实现按键排序。
   * @param map
   */
  public static Map<String, String > sortStringMapByKey(Map<String, String > map, Comparator comparator) {
    if (map == null || map.isEmpty()) {
      return null;
    }
    Map<String, String> sortMap = new TreeMap<String, String >(comparator);
    sortMap.putAll(map);
    return sortMap;
  }

  /**
   * 使用 Map按value进行排序
   */
  public static Map<String, String> sortMapByValue(Map<String, String> map) {
    if (map == null || map.isEmpty()) {
      return null;
    }
    Map<String, String> sortedMap = new LinkedHashMap<String, String>();
    List<Map.Entry<String, String>> entryList =
        new ArrayList<Map.Entry<String, String>>(map.entrySet());
    Collections.sort(entryList, new Comparator<Map.Entry<String, String>>() {
      @Override public int compare(Map.Entry<String, String> me1, Map.Entry<String, String> me2) {
        return me1.getValue().compareTo(me2.getValue());
      }
    });
    Iterator<Map.Entry<String, String>> iter = entryList.iterator();
    Map.Entry<String, String> tmpEntry = null;
    while (iter.hasNext()) {
      tmpEntry = iter.next();
      sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
    }
    return sortedMap;
  }

  public static void main(String[] args) {

    //Map<String, Object> map = new TreeMap<String, Object>();
    //map.put("b", "kfc");
    //map.put("c", "cba");
    //map.put("x", "cba");
    //map.put("e", "nba");
    //map.put("k", "cba");
    //map.put("w", "cba");
    //map.put("h", "cba");
    //map.put("i", "cba");
    //map.put("j", "cba");
    //map.put("其他", "cba");
    //map.put("z", "cba");
    //map.put("q", "cba");
    //map.put("l", "cba");
    //map.put("m", "cba");
    //map.put("o", "cba");
    //map.put("g", "cba");
    //map.put("p", "cba");
    //map.put("r", "cba");
    //map.put("s", "cba");
    //map.put("a", "cba");
    //map.put("f", "cba");
    //map.put("n", "cba");
    //map.put("d", "wnba");
    //map.put("t", "cba");
    //map.put("u", "cba");
    //map.put("v", "cba");
    //map.put("y", "cba");
    //Map<String, Object> resultMap = sortMapByKey(map);    //按Key进行排序
    //for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
    //  System.out.println(entry.getKey() + " " + entry.getValue());
    //}
    Comparator<String> comparator = new Comparator<String>() {
      @Override public int compare(String str1, String str2) {
        return str2.compareTo(str1);
      }
    };
    Map<String, Object> map = new TreeMap<String, Object>();
    map.put("1989-12-12", "d");
    map.put("1989-07-28", "c");
    map.put("1991-09-23", "f");
    map.put("1987-07-28", "a");
    map.put("1987-10-28", "b");
    map.put("1992-10-23", "g");
    map.put("2000-02-10", "h");
    map.put("1990-09-23", "e");
    Map<String, Object> resultMap = sortMapByKey(map, comparator);    //按Key进行排序
    for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
      System.out.println(entry.getKey() + " " + entry.getValue());
    }

  }
}
