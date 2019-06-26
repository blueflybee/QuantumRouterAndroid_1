package com.fernandocejas.android10.sample.presentation.view.component.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fernandocejas.android10.sample.presentation.R;

import java.util.Arrays;
import java.util.List;

public class TimePickerDialog extends Dialog {

  public interface OnTimeSelectedListener {
    void onTimeSelected(int[] times);
  }


  private Params params;

  public TimePickerDialog(Context context, int themeResId) {
    super(context, themeResId);
  }

  private void setParams(TimePickerDialog.Params params) {
    this.params = params;
  }


  private static final class Params {
    private boolean shadow = true;
    private boolean canCancel = true;
    private LoopView loopHour, loopMin,loopSecond;
    private OnTimeSelectedListener callback;
  }

  public static class Builder {
    private final Context context;
    private final TimePickerDialog.Params params;
    private int hour,minute,second;

    public Builder(Context context,int hour,int minute) {
      this.context = context;
      params = new TimePickerDialog.Params();
      this.hour = hour;
      this.minute = minute;
    }

    public Builder(Context context,int hour,int minute,int second) {
      this.context = context;
      params = new TimePickerDialog.Params();
      this.hour = hour;
      this.minute = minute;
      this.second = second;
    }

    /**
     * 获取当前选择的时间
     *
     * @return int[]数组形式返回。例[12,30]
     */
    private final int[] getCurrDateValues() {
      int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
      int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
      return new int[]{currHour, currMin};
    }

    private final int[] getCurrDateValues1() {
      int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
      int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
      int currSecond = Integer.parseInt(params.loopSecond.getCurrentItemValue());
      return new int[]{currHour, currMin, currSecond};
    }

    public TimePickerDialog create() {
      final TimePickerDialog dialog = new TimePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
      View view = LayoutInflater.from(context).inflate(R.layout.dialog_picker_time, null);

      final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);

      //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
      loopHour.setCyclic(false);
      loopHour.setArrayList(d(0, 24));
      loopHour.setCurrentItem(hour);

      final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
      loopMin.setCyclic(false);
      loopMin.setArrayList(d(0, 60));
      loopMin.setCurrentItem(minute);

      view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          dialog.dismiss();
          params.callback.onTimeSelected(getCurrDateValues());
        }
      });

      Window win = dialog.getWindow();
      win.getDecorView().setPadding(0, 0, 0, 0);
      WindowManager.LayoutParams lp = win.getAttributes();
      lp.width = WindowManager.LayoutParams.MATCH_PARENT;
      lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
      win.setAttributes(lp);
      win.setGravity(Gravity.BOTTOM);
      win.setWindowAnimations(R.style.Animation_Bottom_Rising);

      dialog.setContentView(view);
      dialog.setCanceledOnTouchOutside(params.canCancel);
      dialog.setCancelable(params.canCancel);

      params.loopHour = loopHour;
      params.loopMin = loopMin;
      dialog.setParams(params);

      return dialog;
    }

    public TimePickerDialog create(Boolean isSecondVisiable) {
      final TimePickerDialog dialog = new TimePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
      View view = LayoutInflater.from(context).inflate(R.layout.dialog_picker_time, null);

      if(isSecondVisiable){
        view.findViewById(R.id.fl_second).setVisibility(View.VISIBLE);
      }

      final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);

      //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
      loopHour.setCyclic(false);
      loopHour.setArrayList(d(0, 24));
      loopHour.setCurrentItem(hour);

      final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
      loopMin.setCyclic(false);
      loopMin.setArrayList(d(0, 60));
      loopMin.setCurrentItem(minute);

      final LoopView loopSecond = (LoopView) view.findViewById(R.id.loop_second);
      loopSecond.setCyclic(false);
      loopSecond.setArrayList(d(0, 60));
      loopSecond.setCurrentItem(second);

      view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          dialog.dismiss();
          params.callback.onTimeSelected(getCurrDateValues1());
        }
      });

      Window win = dialog.getWindow();
      win.getDecorView().setPadding(0, 0, 0, 0);
      WindowManager.LayoutParams lp = win.getAttributes();
      lp.width = WindowManager.LayoutParams.MATCH_PARENT;
      lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
      win.setAttributes(lp);
      win.setGravity(Gravity.BOTTOM);
      win.setWindowAnimations(R.style.Animation_Bottom_Rising);

      dialog.setContentView(view);
      dialog.setCanceledOnTouchOutside(params.canCancel);
      dialog.setCancelable(params.canCancel);

      params.loopHour = loopHour;
      params.loopMin = loopMin;
      params.loopSecond = loopSecond;
      dialog.setParams(params);

      return dialog;
    }


    public Builder setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
      params.callback = onTimeSelectedListener;
      return this;
    }


    /**
     * 将数字传化为集合，并且补充0
     *
     * @param startNum 数字起点
     * @param count    数字个数
     * @return
     */
    private static List<String> d(int startNum, int count) {
      String[] values = new String[count];
      for (int i = startNum; i < startNum + count; i++) {
        String tempValue = (i < 10 ? "0" : "") + i;
        values[i - startNum] = tempValue;
      }
      return Arrays.asList(values);
    }
  }
}
