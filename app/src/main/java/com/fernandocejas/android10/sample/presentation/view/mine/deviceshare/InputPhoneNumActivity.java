package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Selection;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityInputPhoneNumBinding;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;


/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/30
 *      desc: 分享邀请弹窗  解决EditText在PopupWindow无法粘贴问题
 *      version: 1.0
 * </PRE>
 */

public class InputPhoneNumActivity extends Activity implements View.OnClickListener {
  private ActivityInputPhoneNumBinding mBinding;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_input_phone_num);
    getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

    initView();
    initData();
  }

  private void initView() {
    mBinding.rlCloseInputNum.setOnClickListener(this);
    mBinding.dialogSelectNum.setOnClickListener(this);
    mBinding.dialogPos.setOnClickListener(this);


    //控制button置灰与否
    InputWatcher watcher = new InputWatcher();

    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputRange(11, 11));
    watcher.addEt(mBinding.dialogInputNum,pwdCondition);

    watcher.setInputListener(isEmpty -> {
      mBinding.dialogPos.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.dialogPos.setBackgroundColor(getResources().getColor(R.color.gray));
      } else {
        mBinding.dialogPos.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

  }

  private void initData() {


  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if ((data == null) && (requestCode != 2)) {
      return;
    }

    if (requestCode == 1) {
      //通讯簿返回
      Uri uri = data.getData();
      try {
        String[] contacts = getPhoneContacts(uri);
        // trim只是去掉首位空格
        mBinding.dialogInputNum.setText(contacts[1].toString().replace(" ",""));

      } catch (Exception e) {
        e.printStackTrace();
      }

      //当号码返回时光标放在最后
      Selection.setSelection(mBinding.dialogInputNum.getText(), mBinding.dialogInputNum.getText().length());
    }

  }

  private String[] getPhoneContacts(Uri uri) {
    String[] contact = new String[2];
    //得到ContentResolver对象
    ContentResolver cr = getContentResolver();
    //取得电话本中开始一项的光标
    Cursor cursor = cr.query(uri, null, null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
      //取得联系人姓名
      int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
      contact[0] = cursor.getString(nameFieldColumnIndex);
      //取得电话号码
      String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
      Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
          ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
      if (phone != null) {
        phone.moveToFirst();
        System.out.println("phone = " + phone);
        System.out.println("ContactsContract.CommonDataKinds.Phone.NUMBER = " + ContactsContract.CommonDataKinds.Phone.NUMBER);
        System.out.println("phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) = " + phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        try {
          contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      phone.close();
      cursor.close();
    } else {
      return null;
    }
    return contact;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.dialog_pos:
        if (mBinding.dialogInputNum.getText().toString().trim().length() != 11) {
          Toast.makeText(this, "输入有误，请输入11位手机号码", Toast.LENGTH_SHORT).show();
          mBinding.dialogInputNum.getText().clear();
          return;
        }

        Intent intent1 = new Intent();
        intent1.putExtra("PHONENUM", mBinding.dialogInputNum.getText().toString().trim());
        this.setResult(11, intent1);
        finish();
        break;

      case R.id.rl_close_input_num:
        finish();
        break;

      case R.id.dialog_select_num:
        //进入通讯簿 获取权限
        //  verifyStoragePermissions(this);
        //检测程序是否开启权限
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) !=
            PackageManager.PERMISSION_GRANTED) {
          //没有权限则动态获取
          ActivityCompat.requestPermissions(InputPhoneNumActivity.this,
              new String[]{Manifest.permission.READ_CONTACTS}, 33);
        } else {
          Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
          startActivityForResult(intent, 1);
        }

        break;

      default:
        break;
    }

  }

/*  public static void verifyStoragePermissions(Activity activity) {
    // Check if we have write permission
    int permission = ActivityCompat.checkSelfPermission(activity,
        Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (permission != PackageManager.PERMISSION_GRANTED) {
      // We don't have permission so prompt the user
      ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
          REQUEST_EXTERNAL_STORAGE);
    }
  }

  private static final int REQUEST_EXTERNAL_STORAGE = 1;
  private static String[] PERMISSIONS_STORAGE = {
      Manifest.permission.READ_EXTERNAL_STORAGE,
      Manifest.permission.WRITE_EXTERNAL_STORAGE};*/

  /**
   * 动态获取手机权限：android 6.0以上
   *
   * @param
   * @return
   */
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 33) {
      if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {//判断是否给于权限
        Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
