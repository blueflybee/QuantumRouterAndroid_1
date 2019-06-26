package com.fernandocejas.android10.sample.presentation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shaojun
 * @name CharUtil
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-10-30
 */
public class CharUtil {

  public static String distinguish(String src) {
    String result = "";

    Pattern p;
    Matcher m;

    p = Pattern.compile("[\u4e00-\u9fa5]");
    m = p.matcher(src);
    if (m.find()) {
      result = result + "有汉字  ";
    }

    p = Pattern.compile("[a-zA-Z]");
    m = p.matcher(src);
    if (m.find()) {
      result = result + "有字母  ";
    }

    p = Pattern.compile("[0-9]");
    m = p.matcher(src);
    if (m.find()) {
      result = result + "有数字  ";
    }

    p = Pattern.compile("\\p{Punct}");
    m = p.matcher(src);
    if (m.find()) {
      result = result + "有标点符号  ";
    }

    return result;
  }

  public static void main(String[] args) {
    String s = "s";

    System.out.println(isChinese("陈"));
    System.out.println(isLetter("c"));
  }

  /**
   * 判断输入的一个字符是否是汉字
   */
  public static boolean isChinese(String str) {
    return match(str, "[\u4e00-\u9fa5]");
  }

  /**
   * 判断输入的一个字符是否是字母
   */
  public static boolean isLetter(String str) {
    return match(str, "[a-zA-Z]");
  }

  private static boolean match(String str, String pattern) {
    if (str == null) {
      return false;
    }
    Pattern p = Pattern.compile(pattern);
    Matcher matcher = p.matcher(str);
    return matcher.find();
  }
}


