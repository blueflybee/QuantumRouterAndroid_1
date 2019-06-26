/*
 * Copyright 2018 Google Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.fernandocejas.android10.sample.presentation.view.sambatest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.system.StructStat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.mount.AppConstant;
import com.google.android.sambadocumentsprovider.mount.Const;
import com.google.android.sambadocumentsprovider.mount.FileBean;
import com.google.android.sambadocumentsprovider.mount.PermissionUtils;
import com.google.android.sambadocumentsprovider.mount.TimeUtils;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;
import com.google.android.sambadocumentsprovider.nativefacade.SmbFile;
import com.google.android.sambadocumentsprovider.provider.ByteBufferPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.system.OsConstants.S_ISDIR;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2018/03/06
 *      desc:
 *      version: 1.0
 * </pre>
 */

// TODO: 2018/3/8 上传 下载 外网访问 进入子文件夹
// TODO: 2018/3/9 测试整理

public class RemoteMainActivity extends Activity {
  private List<FileBean> sambaFileList;
  private String remoteCurrentPath;
  private RemoteMainAdapter mAapter;
  private TextView title, path;
  private ListView listView;
  private EditText etNewName;
  private SmbClient mClient;
  private String LOCAL_FILE_PATH = "/samba";
  private static final int BUFFER_CAPACITY = 1024 * 1024;
  public static final int PERMISSION_REQUEST_CODE = 0;
  protected final static int REQUEST_CODE_UPLOAD_FILE = 1230;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_remote_main_smb);

    sambaFileList = new ArrayList<>();
    mClient = AndroidApplication.getSambaClient(this);

    requestPermissions();

    sambaFileList = (List<FileBean>) getIntent().getSerializableExtra("SmbFileList");
    remoteCurrentPath = (String) getIntent().getStringExtra("RemoteCurrentPath");

    title = ((TextView) findViewById(R.id.tv_title));
    path = ((TextView) findViewById(R.id.tv_path));
    listView = ((ListView) findViewById(R.id.lv_smbfile_list));
    etNewName = ((EditText) findViewById(R.id.et_newFolerName));

    mAapter = new RemoteMainAdapter(this, sambaFileList, R.layout.item_smbfile_list);
    listView.setAdapter(mAapter);

    title.setText("文件列表： 总数= " + sambaFileList.size());
    path.setText("当前路径: " + remoteCurrentPath);

    ((Button) findViewById(R.id.btn_newFolder)).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //新建文件夹
        String newName = etNewName.getText().toString();
        Boolean isSuccessed = createSmbFolder(mClient, remoteCurrentPath, newName);
        if (isSuccessed) {
          Toast.makeText(RemoteMainActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(RemoteMainActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
        }
      }
    });

    mAapter.setOnDeleteClickListener(new RemoteMainAdapter.OnDeleteClickListener() {
      @Override
      public void onDeleteClick(int position) {
        //删除
        // TODO: 2018/3/7 删除有文件的文件夹  失败
        new DeleteTask(position).execute();
      }
    });

    mAapter.setOnRenameClickListener(new RemoteMainAdapter.OnRenameClickListener() {
      @Override
      public void onRenameClick(int position) {
        // 重命名
        Boolean isSuccessed = renameSmbFile(mClient, sambaFileList.get(position).getPath(), "重命名" + position, sambaFileList.get(position).getFileType());
        if (isSuccessed) {
          Toast.makeText(RemoteMainActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(RemoteMainActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
        }
      }
    });

    mAapter.setOnOpenClickListener(new RemoteMainAdapter.OnOpenClickListener() {
      @Override
      public void onOpenClick(int position) {
        // 打开

      }
    });

    mAapter.setOnDownloadClickListener(new RemoteMainAdapter.OnDownloadClickListener() {
      @Override
      public void onDownloadClick(int position) {
        // 下载
        new DownloadTaskForPreview(mClient, sambaFileList.get(position).getPath(),sambaFileList.get(position).getName(),RemoteMainActivity.this).execute();
      }
    });


    /**新建文件夹*/
    /*createSmbFolder(mClient,remoteCurrentPath,"新建文件夹_ByXieHao");*/
    /**删除文件或文件夹*/
    /*deleteSmbFile(mClient,sambaFileList.get(2).getPath(),false);
    deleteSmbFile(mClient,sambaFileList.get(16).getPath(),true);*/
    /**重命名*/
    /**下载（普通操作+断点操作）*/
    /**上传*/
    /**打开文件*/
    /**移动文件*/
    /**外网访问*/
  }

  /**********************************Samba 工具类**************************************/

  public void onUploadPicture(View view) {
    pickupImages(this,REQUEST_CODE_UPLOAD_FILE);
  }

  public void onUploadVideo(View view) {

  }

  public void onUploadMusic(View view) {

  }

  /**
   * @param bytes
   * @return
   * @Description long转文件大小M单位方法
   * @author temdy^
   */
  public final static String bytes2kb(long bytes) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (bytes < 1024) {
      fileSizeString = df.format((double) bytes) + "B";
    } else if (bytes < 1048576) {
      fileSizeString = df.format((double) bytes / 1024) + "K";
    } else if (bytes < 1073741824) {
      fileSizeString = df.format((double) bytes / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) bytes / 1073741824) + "G";
    }
    return fileSizeString;
  }

  public void onEnterFolder(View view) {
    //进入新建文件夹_ByXieHao
    Uri uri = Uri.parse(sambaFileList.get(13).getPath());
    System.out.println("sambaFileList 进入子文件夹 = path" + sambaFileList.get(13).getPath());
    sambaFileList.clear();
    DocumentMetadata metadata = null;
    try {
      metadata = DocumentMetadata.fromUri(uri, mClient);

      Map<Uri, DocumentMetadata> children = metadata.getChildren();
      Iterator it = children.keySet().iterator();
      while (it.hasNext()) {
        Uri key;
        key = (Uri) it.next();
        // 名称 日期 大小 路径 类型：文件夹或文件
        try {
          DocumentMetadata document = DocumentMetadata.fromUri(key, mClient);
          StructStat stat = mClient.stat(key.toString());

          if (S_ISDIR(stat.st_mode)) {
            //文件夹
            FileBean bean = new FileBean();
            bean.setName(document.getDisplayName());
            bean.setPath(document.getUri() + "");
            bean.setFileType(true);
            bean.setDate(TimeUtils.millis2String(stat.st_ctime * 1000));
            sambaFileList.add(bean);
          } else {
            //文件
            FileBean bean = new FileBean();
            bean.setName(document.getDisplayName());
            bean.setSize(bytes2kb(document.getSize()));
            bean.setSize(bytes2kb(document.getSize()));
            bean.setPath(document.getUri() + "");
            bean.setDate(TimeUtils.millis2String(stat.st_ctime * 1000));
            bean.setFileType(false);
            sambaFileList.add(bean);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < sambaFileList.size(); i++) {
      System.out.println("sambaFileList 进入子文件夹 = " + sambaFileList.get(i).getPath());
    }
  }

  /**
  * 下载(断点续传)
  *
  * @param
  * @return
  */
  public void onDownload(View view) {
    /*// TODO: 2018/3/8
    String threadcount = "1";
    String lastPath = "";
    try {
      System.out.println("isDownloading = " + "开始下载");
      LOCAL_FILE_PATH = Environment.getExternalStorageDirectory().toString() + "/";
      // TODO: 2017/9/1
      downloaders = ((AndroidApplication) getApplication()).getDownloaders();
          *//*downloaders = (Map<String, Downloader>) SambaUtils.getSambaDownloaders();*//*

      //每次下载之前检查一下数据库里的url是否已经删除
      String urlstr = mRemoteFileBeans.get(groupPosition).getPath();

      //下载之前查询一下数据库是否有记录，有的话返回
     *//*     if(mDBUtil.queryByUrl(UploadUtils.genLocalPath(urlstr, LOCAL_FILE_PATH))){
            System.out.println("samba下载 数据库已存在该记录");
            Toast.makeText(this, "文件已存在，无需重复下载", Toast.LENGTH_SHORT).show();
            //关闭展开
            mBinding.lvRemoteFile.collapseGroup(groupPosition);
            return;
          }*//*

      if (downloaders.get(urlstr) != null) {
        //初始化状态1，正在下载状态2，暂停状态3
        if(downloaders.get(urlstr).getState() != 1){
          Toast.makeText(this, "下载任务已存在", Toast.LENGTH_SHORT).show();
          //关闭展开
          return;
        }else {
          downloaders.get(urlstr).delete(urlstr);
          downloaders.get(urlstr).reset();
          downloaders.remove(urlstr);
          mDBUtil.deleteKey(urlstr);//下载完就删除key
        }

      }

      System.out.println("\"下载:\" = " + "开始下载");

      Toast.makeText(this, "成功添加下载任务", Toast.LENGTH_SHORT).show();

      SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(v, downloaders, RemoteMainActivity.this, auth);
      downloadTask.execute(mRemoteFileBeans.get(groupPosition).getPath(), UploadUtils.genLocalPath(mRemoteFileBeans.get(groupPosition).getPath(), LOCAL_FILE_PATH), threadcount);

    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }

  /**
   * 删除
   *
   * @param
   * @return
   */
  class DeleteTask extends AsyncTask<String, Boolean, Boolean> {
    int position;

    public DeleteTask(int position) {
      this.position = position;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean result = deleteSmbFile(mClient, sambaFileList.get(position).getPath(), sambaFileList.get(position).getFileType());
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        Toast.makeText(RemoteMainActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(RemoteMainActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * 下载(预览)
   *
   * @param
   * @return
   */
  class DownloadTaskForPreview extends AsyncTask<String, Boolean, Boolean> {
    private SmbClient smbClient;
    private Context context;
    private String fileName,remotePath;

    public DownloadTaskForPreview(SmbClient smbClient, String remotePath, String fileName, Context context) {
      this.smbClient = smbClient;
      this.remotePath = remotePath;
      this.fileName = fileName;
      this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean result = downloadSmbFileForPreview(smbClient, remotePath,genLocalPath(fileName),context);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        Toast.makeText(RemoteMainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(RemoteMainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * 下载(断点续传)
   *
   * @param
   * @return
   */
  class DownloadTask extends AsyncTask<String, Boolean, Boolean> {
    private SmbClient smbClient;
    private Context context;
    private String fileName,remotePath;

    public DownloadTask(SmbClient smbClient, String remotePath, String fileName, Context context) {
      this.smbClient = smbClient;
      this.remotePath = remotePath;
      this.fileName = fileName;
      this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean result = downloadSmbFileForPreview(smbClient, remotePath,genLocalPath(fileName),context);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        Toast.makeText(RemoteMainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(RemoteMainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    /*if (requestCode == REQUEST_CODE_UPLOAD_FILE) {
      //上传图片视频音乐
      try {
        Uri selectedImage = data.getData();
        String path = UriUtil.getImagePath(this, selectedImage);

        String threadcount = "1";

        //每次下载之前检查一下数据库里的url是否已经删除

        //下载之前查询一下数据库是否有记录，有的话返回
       *//* if(mDBUtil.queryByUploadUrl(SambaUtils.wrapSmbFileUrl(remoteCurrentPath, new File(path).getName()))){
          Toast.makeText(this, "文件已存在，无需重复上传", Toast.LENGTH_SHORT).show();
          return;
        }*//*

        if (uploaders.get(path) != null) {
          //初始化状态1，正在下载状态2，暂停状态3
          if(uploaders.get(path).getState() != 1){
            Toast.makeText(this, "上传任务已存在", Toast.LENGTH_SHORT).show();
            return;
          }else {
            uploaders.get(path).delete(path);
            uploaders.get(path).reset();
            uploaders.remove(path);
            mDBUtil.deleteKey(path);//下载完就删除key
          }

        }

        Toast.makeText(this, "成功添加上传任务", Toast.LENGTH_SHORT).show();

        SambaUtils.UploadTask uploadTask = new SambaUtils.UploadTask(uploaders,null, RemoteMainActivity.this, auth);
        uploadTask.execute(path, SambaUtils.wrapSmbFileUrl(remoteCurrentPath, new File(path).getName()), threadcount);
        System.out.println("上传:" + " execute参数 = " + "1" + path + "  2=" + SambaUtils.wrapSmbFileUrl(remoteCurrentPath, new File(path).getName()) + " 3=" + threadcount);

        //延时10s刷新一下
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            initSmbFileListInfo(remoteCurrentPath);
          }
        }, 5000);

      } catch (Exception e) {
        e.printStackTrace();
      }
    }*/
  }

  /**
   * 功能: 重命名
   *
   * @param:
   * @return:
   */
  public static boolean renameSmbFile(SmbClient mClient, String path, String newName, Boolean isFolder) {
    // 文件 ==》 smb://192.168.1.1/qtec/IMG_20170402_131002.jpg
    // 文件夹 ==》 smb://192.168.1.1/qtec/%E5%93%88%E5%93%88
    String fileSuffix = "";
    String newPath = "";
    try {
      if (isFolder) {
        //文件夹
        newPath = path.substring(0, path.lastIndexOf("/")) + File.separator + newName;
      } else {
        fileSuffix = path.substring(path.lastIndexOf("."));//获取文件的后缀名,带. 如.png
        newPath = path.substring(0, path.lastIndexOf("/")) + File.separator + newName + fileSuffix;
      }

      mClient.rename(path, newPath);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 新建文件夹
   *
   * @param
   * @return
   */
  public static boolean createSmbFolder(SmbClient mClient, final String path, final String name) {
    try {
      mClient.mkdir(path + File.separator + name);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 功能: 删除文件
   *
   * @param:
   * @return:
   */
  public static boolean deleteSmbFile(SmbClient mClient, final String path, Boolean isFolder) {
    try {
      if (isFolder) {
        mClient.rmdir(path.toString());
      } else {
        mClient.unlink(path.toString());
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 功能: 下载文件
   *
   * @param:
   * @return:
   */
  public static boolean downloadSmbFileForPreview(SmbClient mClient, final String remotePath, final String localPath, final Context context) {
    final File localFile = new File(localPath);
    int length;
    long uploaded = 0;
    FileOutputStream outS = null;
    SmbFile file = null;

    ByteBuffer mBuffer = null;
    try {
      outS = new FileOutputStream(localFile);
      file = mClient.openFile(remotePath, "r");

      long fileSize = file.fstat().st_size;

      mBuffer = new ByteBufferPool().obtainBuffer();

      byte[] buf = new byte[mBuffer.capacity()];

      while ((length = file.read(mBuffer, Integer.MAX_VALUE)) > 0) {
        mBuffer.get(buf, 0, length);
        outS.write(buf, 0, length);
        outS.flush();
        uploaded += length;
        System.out.println("下载 downloaded = " + uploaded);
      }

      //下载完打开文件
      openFile(context, localFile);

      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (outS != null) {
          outS.close();
        }
        if(file != null){
          file.close();
        }
        if(mBuffer != null){
          mBuffer = null;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public static final String genLocalPath(String fileName) {
    //File folder = new File(String.valueOf(Environment.getExternalStorageDirectory()) + File.separator + "samba/");
    File folder = new File(AppConstant.SAMBA_DOWNLOAD_PATH);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    if (fileName == null) {
      fileName = String.valueOf(System.currentTimeMillis());
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(folder.getAbsolutePath()).append("/").append(fileName).toString();
    File f = new File(buffer.toString());
    try {
      f.createNewFile();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return f.getAbsolutePath();
  }



  public static void openFile(Context context, File file) {
/*    if (file.isDirectory()) {
      initFileListInfo(file.getPath());
    } else */
    {
      if (file.getName().lastIndexOf(".") == -1) {// 无后缀文件不可读
        //OtherHelp.ErrorProcess(ctx);
        Toast.makeText(context, "文件不可读", Toast.LENGTH_SHORT).show();
      } else {
        requestPermissions(context);//获取权限

        String type = getMIMEType(file.getName());
        if (type.length() == 0) {
          Toast.makeText(context, "不支持该格式", Toast.LENGTH_SHORT).show();
        } else {
          //适配andorid N > 7.0
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            // 设置当前文件类型
            System.out.println("context 打开 = " + context);
            System.out.println("file 打开 = " + file.getName());
            intent.setDataAndType(FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".fileprovider", file), type);
            context.startActivity(intent);
          } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            // 设置当前文件类型
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
          }
        }
      }
    }
  }

  /**
   * 获得MIME类型的方法
   */
  private static String getMIMEType(String fileName) {
    String type = "";
    // 取出文件后缀名并转成小写
    String fileEnds = fileName.substring(fileName.lastIndexOf(".") + 1,
        fileName.length()).toLowerCase();
    for (int i = 0; i < Const.MIME_MapTable.length; i++) {
      if (fileEnds.equals(Const.MIME_MapTable[i][0]))
        type = Const.MIME_MapTable[i][1];
    }
    return type;
  }

  public static void requestPermissions(Context context) {
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    PermissionUtils.requestPermissions(context, PERMISSION_REQUEST_CODE, perms, new PermissionUtils.OnPermissionListener() {
      @Override
      public void onPermissionGranted() {
        Log.i("12312", "允许权限");
      }

      @Override
      public void onPermissionDenied(String[] deniedPermissions) {
        Log.i("2133232213", "拒绝权限");
      }
    });
  }

  private void requestPermissions() {
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        , Manifest.permission.RECORD_AUDIO};
    PermissionUtils.requestPermissions(this, PERMISSION_REQUEST_CODE, perms, new PermissionUtils.OnPermissionListener() {
      @Override
      public void onPermissionGranted() {
        Log.i("RemoteMainActivity", "允许权限");
      }

      @Override
      public void onPermissionDenied(String[] deniedPermissions) {
        Log.i("RemoteMainActivity", "拒绝权限");
      }
    });
  }

  /**
   * 上传图片
   *
   * @param
   * @return
   */
  public final static boolean pickupImages(Activity activity, int requestCode) {
    if (activity == null) {
      return false;
    }
    Intent intent = new Intent(Intent.ACTION_PICK);
    // Uri uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    // intent.setDataAndType(uri, "image/*");
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    boolean available = isIntentAvailable(activity, intent);
    if (available) {
      activity.startActivityForResult(intent, requestCode);
    }
    return available;
  }

  public final static boolean isIntentAvailable(Context context, Intent intent) {
    if (context == null || intent == null) {
      return false;
    }
    try {
      return listResolves(context, intent) != null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public final static List<ResolveInfo> listResolves(Context context, Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
  }


  /****************************************************************************************/

}
