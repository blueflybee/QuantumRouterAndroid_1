package com.fernandocejas.android10.sample.presentation.view.selectconfig;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import com.fernandocejas.android10.sample.data.net.CloudRestApi;
import com.fernandocejas.android10.sample.data.net.CloudRestApiImpl;
import com.fernandocejas.android10.sample.data.net.ConnectionCreator;
import com.fernandocejas.android10.sample.data.net.IPostConnection;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.LoginConfig;


public class ServerActivity extends ListActivity {
  private TitleBar mTitleBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_server);

    initTitleBar("可选服务器列表");

    String[] servers = {
        "吴志海：" + CloudRestApi.WU_ZHI_HAI,
        "测试环境：" + CloudRestApi.URL_TEST,
        "生产环境：" + CloudRestApi.URL_GATEWAY_3_CARETEC,
    };

    setListAdapter(new ArrayAdapter<>(this, R.layout.item_server_list, servers));

    getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    getListView().setItemChecked(1, true);

/*
    LoginConfig.OS_IP = urlList.get(4).getIp();//截取云端Ip 安全存储外网访问用
*/

    System.out.println("LoginConfig.OS_IP = " + LoginConfig.OS_IP);

    getListView().setOnItemClickListener((parent, view, position, id) -> {
      String server = servers[position].split("：")[1];
      IPostConnection cloudConnection =
          ConnectionCreator.create(ConnectionCreator.CLOUD_CONNECTION, server);
      CloudRestApiImpl.setApiPostConnection(cloudConnection);

 /*     LoginConfig.OS_IP = urlList.get(position).getIp();//截取云端Ip 安全存储外网访问用
      System.out.println("LoginConfig.OS_IP = " + LoginConfig.OS_IP);*/

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
