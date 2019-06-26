/**
 *
 */
package com.fernandocejas.android10.sample.presentation.utils;


import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Dialog 工具类
 */
public class TimeUtil {

  private TimeUtil() {
  }


  @NonNull
  public static String[] getStringTime(int[] times) {
    int hour = times[0];
    int min = times[1];
    String strHour = hour < 10 ? "0" + hour : String.valueOf(hour);
    String strMin = min < 10 ? "0" + min : String.valueOf(min);

    return new String[]{strHour, strMin};
  }

  @NonNull
  public static String[] getStringTimeWithSecond(int[] times) {
    int hour = times[0];
    int min = times[1];
    int second = times[2];
    String strHour = hour < 10 ? "0" + hour : String.valueOf(hour);
    String strMin = min < 10 ? "0" + min : String.valueOf(min);
    String strSecond = second < 10 ? "0" + second : String.valueOf(second);

    return new String[]{strHour, strMin,strSecond};
  }

  public static String getStringTime(int time){
    String result = "";

    if(time < 10){
      result = "0"+time;
    }else {
      result = ""+time;
    }
    return result;
  }
  
  /**
   * 时间转化为显示字符串
   *
   * @param timeStamp 单位为秒
   */
  public static String getTimeStr(long timeStamp) {
    if (timeStamp == 0) return "";
    Calendar inputTime = Calendar.getInstance();
    inputTime.setTimeInMillis(timeStamp * 1000);
    Date currenTimeZone = inputTime.getTime();
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    if (calendar.before(inputTime)) {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      return sdf.format(currenTimeZone);
    }
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    if (calendar.before(inputTime)) {
      return "昨天";
    } else {
      calendar.add(Calendar.DAY_OF_MONTH, -5);
      if (calendar.before(inputTime)) {
        return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
      } else {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        int year = inputTime.get(Calendar.YEAR);
        int month = inputTime.get(Calendar.MONTH);
        int day = inputTime.get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
      }


    }

  }

  /**
   * 群发使用的时间转换
   */
  public static String multiSendTimeToStr(long timeStamp) {

    if (timeStamp == 0) return "";
    Calendar inputTime = Calendar.getInstance();
    String timeStr = timeStamp + "";
    if (timeStr.length() == 10) {
      timeStamp = timeStamp * 1000;
    }
    inputTime.setTimeInMillis(timeStamp);
    Date currenTimeZone = inputTime.getTime();
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    if (calendar.before(inputTime)) {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      return sdf.format(currenTimeZone);
    }
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    if (calendar.before(inputTime)) {
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
      return "昨天";
    } else {
      calendar.add(Calendar.DAY_OF_MONTH, -5);
      if (calendar.before(inputTime)) {
        return getWeekDayStr(inputTime.get(Calendar.DAY_OF_WEEK));
      } else {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        if (calendar.before(inputTime)) {
          SimpleDateFormat sdf = new SimpleDateFormat("M" + "/" + "d" + " ");
          String temp1 = sdf.format(currenTimeZone);
          SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
          String temp2 = sdf1.format(currenTimeZone);
          return temp1 + temp2;
        } else {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "/" + "M" + "/" + "d" + " ");
          String temp1 = sdf.format(currenTimeZone);
          SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
          String temp2 = sdf1.format(currenTimeZone);
          return temp1 + temp2;
        }
      }
    }
  }

  /**
   * 时间转化为聊天界面显示字符串
   *
   * @param timeStamp 单位为秒
   */
  public static String getChatTimeStr(long timeStamp) {
    if (timeStamp == 0) return "";
    Calendar inputTime = Calendar.getInstance();
    String timeStr = timeStamp + "";
    if (timeStr.length() == 10) {
      timeStamp = timeStamp * 1000;
    }
    inputTime.setTimeInMillis(timeStamp);
    Date currenTimeZone = inputTime.getTime();
    Calendar calendar = Calendar.getInstance();
//        if (calendar.before(inputTime)){
//            //当前时间在输入时间之前
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年"+"MM"+"月"+"dd"+"日");
//            return sdf.format(currenTimeZone);
//        }
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    if (calendar.before(inputTime)) {
      SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
      return timeFormatStr(inputTime, sdf.format(currenTimeZone));
    }
    calendar.add(Calendar.DAY_OF_MONTH, -1);
    if (calendar.before(inputTime)) {
      SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
      return "昨天" + " " + timeFormatStr(inputTime, sdf.format(currenTimeZone));
    } else {
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.MONTH, Calendar.JANUARY);
      if (calendar.before(inputTime)) {
        SimpleDateFormat sdf = new SimpleDateFormat("M" + "月" + "d" + "日");
        String temp1 = sdf.format(currenTimeZone);
        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
        String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
        return temp1 + temp2;
      } else {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "年" + "M" + "月" + "d" + "日");
        String temp1 = sdf.format(currenTimeZone);
        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
        String temp2 = timeFormatStr(inputTime, sdf1.format(currenTimeZone));
        return temp1 + temp2;
      }

    }

  }

  /**
   * 24小时制转化成12小时制
   *
   * @param strDay
   */
  public static String timeFormatStr(Calendar calendar,String strDay)
  {
    String tempStr = "";
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    if (hour > 11)
    {
      tempStr = "下午"+" " + strDay;
    }
    else
    {
      tempStr = "上午"+" " + strDay;
    }
    return tempStr;
  }

  /**
   * 24小时制转化成12小时制
   *
   */
  public static String timeFormatStr(Calendar calendar) {
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    if (hour >= 6 && hour <= 11) {
      return "早上";
    } else if (hour >= 12 && hour <= 18) {
      return "下午";
    } else {
      return "晚上";
    }
  }

  /**
   * 时间转化为星期
   *
   * @param indexOfWeek 星期的第几天
   */
  public static String getWeekDayStr(int indexOfWeek) {
    String weekDayStr = "";
    switch (indexOfWeek) {
      case 1:
        weekDayStr = "星期日";
        break;
      case 2:
        weekDayStr = "星期一";
        break;
      case 3:
        weekDayStr = "星期二";
        break;
      case 4:
        weekDayStr = "星期三";
        break;
      case 5:
        weekDayStr = "星期四";
        break;
      case 6:
        weekDayStr = "星期五";
        break;
      case 7:
        weekDayStr = "星期六";
        break;
    }
    return weekDayStr;
  }

  /**
   * 将时间戳格式化，13位的转为10位
   *
   * @param timestamp
   * @return
   */
  public static long timestampFormate(long timestamp) {
    String timestampStr = timestamp + "";
    if (timestampStr.length() == 13) {
      timestamp = timestamp / 1000;
    }
    return timestamp;
  }
}
