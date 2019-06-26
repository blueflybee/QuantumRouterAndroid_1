package com.fernandocejas.android10.sample.presentation.view.selectconfig;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;


public class SelectLockEncryptionActivity extends ListActivity {
  private TitleBar mTitleBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_server);

    initTitleBar("可选服务器列表");

    String[] encryptions = {
        BleMapper.ENCRYPT_SM4,
        BleMapper.ENCRYPT_AES
    };

    setListAdapter(new ArrayAdapter<>(this, R.layout.item_server_list, encryptions));

    getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    getListView().setItemChecked(1, true);
    BleMapper.setEncryptionType(BleMapper.ENCRYPT_AES);

    getListView().setOnItemClickListener((parent, view, position, id) -> {
      BleMapper.setEncryptionType(encryptions[position]);
    });

  }

  private void initTitleBar(String title) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) findViewById(R.id.title_bar);
    }
    mTitleBar.setTitleCenter(title);
    mTitleBar.setLeftAsBackButton();
  }

}
