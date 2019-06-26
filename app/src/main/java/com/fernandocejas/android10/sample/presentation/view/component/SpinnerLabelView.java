package com.fernandocejas.android10.sample.presentation.view.component;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.data.KVPair;

/**
 * 单行输入条，包含一个标签和一个输入框组件
 *
 * @param <T> 所选条目类型
 * @author shaojun
 */
public class SpinnerLabelView<T extends KVPair> extends LinearLayout {

  private Context mContext;

  private TextView mLabel;
  private Spinner mSpinner;
  private ArrayAdapter<T> mAdapter;

  public SpinnerLabelView(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.mContext = context;
  }

  public SpinnerLabelView(Context context) {
    super(context);
    this.mContext = context;
  }

  /**
   * 根据key, 设置spinner默认选中:
   */
  public void setSpinnerItemSelectedByKey(String key) {
    if (TextUtils.isEmpty(key)) return;
    int count = mAdapter.getCount();
    for (int i = 0; i < count; i++) {
      if (key.equals(mAdapter.getItem(i).getKey())) {
        mSpinner.setSelection(i, true);
        break;
      }
    }
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    mLabel = (TextView) findViewById(R.id.name_tv);
    mSpinner = (Spinner) findViewById(R.id.spinner);
  }

  public void setLabel(String label) {
    mLabel.setText(label);
  }

  public void setLabel(int resid) {
    mLabel.setText(resid);
  }

  public void setAdapter(ArrayAdapter<T> adapter) {
    mSpinner.setAdapter(adapter);
    this.mAdapter = adapter;
  }

  public void setSelection(int position, boolean animate) {
    mSpinner.setSelection(position, animate);
  }

  public void setOnSpinnerItemSelectedListener(final OnSpinnerItemSelectedListener<T> listener) {
    mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter != null) {
          listener.onItemSelected(mAdapter.getItem(position));
        }
      }

      @Override public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  public T getSelectedItem() {
    return (T) mSpinner.getSelectedItem();
  }

  public void setSpinnerEnabled(boolean enabled) {
    mSpinner.setEnabled(enabled);
  }

  public interface OnSpinnerItemSelectedListener<T> {

    void onItemSelected(T item);
  }
}
