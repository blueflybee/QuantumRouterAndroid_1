package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.system.StructStat;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFileSearchBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.google.android.sambadocumentsprovider.TaskManager;
import com.google.android.sambadocumentsprovider.base.OnTaskFinishedCallback;
import com.google.android.sambadocumentsprovider.cache.DocumentCache;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.document.LoadChildrenTask;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.system.OsConstants.S_ISDIR;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class FileSearchActivity extends BaseActivity implements ExpandableListView.OnGroupExpandListener {
  private ActivityFileSearchBinding mBinding;
  private List<SearchFileHistoryBean> mHistoryFileList;
  private List<FileBean> fileBeansTemp;//保存暂时搜索的文件列表
  private SambaPopupWindow mSambaPopWin;
  protected final static int REQUEST_MOVE_FILE = 1236;
  protected final static int REQUEST_SEARCH_FILE = 1237;
  private static boolean isExit_Back = true; // true=退出,false=后退
  private String remoteCurrentPath = "";
  private String remoteCurrentSharePath = "";
  private String remoteCurrentSharePath_For_Search = "";//缓存搜索前的路径
  private SambaPopupWindow mDeleteHisPop;
  private String searchRootIp = "";//根目录
  private RemoteMainAdapter adapter;
  private DatabaseUtil mDBUtil;
  private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
  private int moveFileIndex = 0;//统计缓存次数 记录移动的文件下标
  private SmbClient mClient;
  private DocumentCache mCache;
  private TaskManager mTaskManager;
  private MyProgressDialog mDialog;
  private List<String> mDeleteFolderList;//记录删除文件夹时里面的文件夹的路径
  private String deleteFolderPath; //记录删除的当前的文件夹及位置
  private Boolean isDeleteFolder = false;
  private Boolean isPauseSearch = false; //点击文件夹进入子项时不需要进行搜索
  private String connect_ip = "";
  private Boolean isSearchingFinished = false;//标记搜索是否完成

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_file_search);
    initView();
    initData();
  }

  private void initEvent() {
    if (adapter != null) {
      //单击进入文件夹
      adapter.setOnFolderListener(new RemoteMainAdapter.OnFolderClickListener() {
        @Override
        public void onFolderClick(View v, int position, String path, Uri key, Boolean isFolder) {
          //进入文件夹

          FileBean bean = adapter.getGroup(position);

          try {
            // 如果该文件是可读的，我们进去查看文件
            if (isFolder) {
              // 如果是文件夹，则直接进入该文件夹，查看文件目录
              isPauseSearch = true;
              remoteCurrentSharePath = remoteCurrentSharePath + "/" + bean.getName();
              fetchAtPath();
            } else {
              /**
               * 文件预览
               * 原理：下载到本地指定的某个目录，然后定期清除缓存,无需支持断点续载
               */
              //损坏的文件提示不可下载
              if (("0K".equals(bean.getSize()))) {
                Toast.makeText(FileSearchActivity.this, "文件已损坏", Toast.LENGTH_SHORT).show();
                return;
              }
              // 判断文件是否预览过 是的话直接从本地取 否的话下载
              try {
                File f = new File(UploadUtils.genLocalPath(path, AppConstant.SAMBA_DOWNLOAD_CACHE_PATH));
                if (f.exists() && f.length() > 0 && f.length() == bean.getLongSize()) {
                  SambaUtils.openFile(FileSearchActivity.this, f);
                  return;
                } else {
                  new SambaUtils.DownloadTaskForPreview(mClient, path, FileSearchActivity.this).execute();
                }
              } catch (Exception e) {
                e.printStackTrace();
              }

              //先判断缓存路径是否文件操作30个，是的话进行删除
              if (SambaUtils.getCacheCount() >= 30) {
                //太大会出现删除时间过长
                UploadUtils.deleteLocalFile(AppConstant.SAMBA_DOWNLOAD_CACHE_PATH);
              }

            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });

      //单击childView的Item选项
      adapter.setOnDetailListener(new RemoteMainAdapter.OnDetailClickListener() {
        @Override
        public void onDetailClick(View view, int groupPosition, int childPosition, Boolean isFolder) {
          itemViewClickEvent(view, groupPosition, childPosition, isFolder);
        }
      });
    }
  }

  private void initView() {
    mHistoryFileList = new ArrayList<>();
    mDBUtil = new DatabaseUtil(this);
    mDeleteFolderList = new ArrayList<>();
    fileBeansTemp = new ArrayList<>();
    downloaders = AndroidApplication.getDownloaders();

    mCache = AndroidApplication.getDocumentCache(this);
    mTaskManager = AndroidApplication.getTaskManager(this);

    mBinding.lvSearchFile.setOnGroupExpandListener(this);

    mClient = AndroidApplication.getSambaClient(this);

    if (GlobleConstant.isSambaExtranetAccess) {
      //外网
      searchRootIp = LoginConfig.OS_REMOTE_ROOT_IP;
      connect_ip = LoginConfig.OS_IP + ":" + LoginConfig.OS_PORT;
    } else {
      searchRootIp = LoginConfig.LAN_REMOTE_ROOT_IP;
      connect_ip = LoginConfig.LAN_IP;
    }

    remoteCurrentSharePath = LoginConfig.LAN_REMOTE_SHARE_IP;

    remoteCurrentPath = searchRootIp;

    System.out.println("文件搜索 searchRootIp = " + searchRootIp);
    System.out.println("文件搜索 remoteCurrentSharePath = " + remoteCurrentSharePath);

    WatchSearchText();
  }

  /**
   * 监听edittext
   */
  private void WatchSearchText() {
    mBinding.etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        //只要输入框有变化就隐藏listView
        mBinding.lvSearchFile.setVisibility(View.GONE);
        mBinding.llHistory.setVisibility(View.VISIBLE);
        mBinding.tvEmpty.setVisibility(View.GONE);

        if (TextUtils.isEmpty(s)) {
          mBinding.rlClear.setVisibility(View.GONE);
        } else {
          mBinding.rlClear.setVisibility(View.VISIBLE);

          mBinding.rlClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mBinding.etSearch.getText().clear();
            }
          });
        }
      }

      @Override
      public void afterTextChanged(Editable s) {
        int index = mBinding.etSearch.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(s.charAt(index))) {
            Editable edit = mBinding.etSearch.getText();
            if (index == 1) {
              edit.clear();
            } else {
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }

  /**
   * 判断是否是表情
   *
   * @param
   * @return
   */
  private static boolean isEmojiCharacter(char codePoint) {
    return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
  }

  private void initData() {

    Intent intent = new Intent();
    intent.putExtra("SHAREPATH", getRemoteCurrentPath());
    FileSearchActivity.this.setResult(REQUEST_SEARCH_FILE,intent);

    mHistoryFileList.clear();
    //查询数据库
    if (mDBUtil.queryAllSearchHistory() != null && mDBUtil.queryAllSearchHistory().size() != 0) {
      mHistoryFileList = mDBUtil.queryAllSearchHistory();
    }

    /**
     * 空视图设置
     */
    if (mHistoryFileList.size() == 0) {
      mBinding.llHistory.setVisibility(View.GONE);
    } else {
      mBinding.llHistory.setVisibility(View.VISIBLE);
      addAllTextView();
    }

    /**
     * 点击软键盘的搜索按钮
     */
    mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          if (TextUtils.isEmpty(getText(mBinding.etSearch))) {
            Toast.makeText(FileSearchActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
          } else {
            new SearchFileTask().execute();
          }
        }
        return true;
      }
    });

    mBinding.btnSearchCancle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

  /**
   * 获取文件列表
   *
   * @param
   * @return
   */
  private synchronized void fetchAtPath() {
    System.out.println("文件搜索 fetchAtPath remoteCurrentSharePath = " + remoteCurrentSharePath);

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        adapter = new RemoteMainAdapter(FileSearchActivity.this);
        mBinding.lvSearchFile.setAdapter(adapter);
      }
    });

    DocumentMetadata metadata = DocumentMetadata.createShare(connect_ip, remoteCurrentSharePath);

    final LoadChildrenTask task =
        new LoadChildrenTask(metadata, mClient, mCache, new OnTaskFinishedCallback<DocumentMetadata>() {
          @Override
          public void onTaskFinished(int status, @Nullable DocumentMetadata metadata, @Nullable Exception exception) {
            initSmbFileList(metadata);
          }
        });

    mTaskManager.runTask(metadata.getUri(), task);
  }

  /**
  * 递归搜索文件夹
  *
  * @param
  * @return
  */
  private void searchMatchFolder(String sharePath){
    isSearchingFinished = false;
    DocumentMetadata metadata = DocumentMetadata.createShare(connect_ip, sharePath);

    final LoadChildrenTask task =
        new LoadChildrenTask(metadata, mClient, mCache, new OnTaskFinishedCallback<DocumentMetadata>() {
          @Override
          public void onTaskFinished(int status, @Nullable DocumentMetadata metadata, @Nullable Exception exception) {
            searchFile(metadata, getText(mBinding.etSearch));
          }
        });

    mTaskManager.runTask(metadata.getUri(), task);
  }

  /**
   * 非递归搜索 只搜索当前路径
   *
   * @param
   * @return
   */
  private void searchFile(DocumentMetadata metadata, String name) {
    try {

      Map<Uri, DocumentMetadata> children = metadata.getChildren();
      Iterator it = children.keySet().iterator();

      while (it.hasNext()) {
        Uri key;
        key = (Uri) it.next();
        // 名称 日期 大小 路径 类型：文件夹或文件
        if(isPauseSearch){
          //点击子项不进行搜索
          try {
            DocumentMetadata document = DocumentMetadata.fromUri(key, mClient);
            StructStat stat = mClient.stat(key.toString());

            isSearchingFinished = true;

            //含字符串的文件名
            if (!(document.getDisplayName()).startsWith(".")) {
              //过滤以.开头的文件
              FileBean file = new FileBean();
              file.setName(document.getDisplayName());
              file.setPath(document.getUri().toString());

              file.setDate(TimeUtils.millis2String(stat.st_ctime * 1000));
              if (S_ISDIR(stat.st_mode)) {
                file.setFileType(true);
                file.setSize(SambaUtils.bytes2kb(document.getSize()));

                file.setLastModifyTime(TimeUtils.millis2String(stat.st_ctime * 1000));
                // 隐藏lost+found文件夹
                if (!"lost+found".equals(document.getDisplayName())) {
                  adapter.add(file, 0);
                }
              } else {
                file.setFileType(false);
                if (document.getSize() == 0) {
                  //已损害的文件大小为0
                  file.setSize("0K");
                } else {
                  file.setSize(SambaUtils.bytes2kb(document.getSize()));
                }
                file.setLastModifyTime(TimeUtils.millis2String(stat.st_ctime * 1000));

                if (name.indexOf(".upload") != -1) {
                  //包含.upload 表示正在上传的文件
                } else {
                  adapter.add(file, 0);
                }
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }else {
          System.out.println("搜索中 开始搜索 ");
          achieveMatchData(key, name);
        }
      }

      if(isSearchingFinished){

        isSearchingFinished = false;

        if (mDialog != null) {
          mDialog.dismiss();
        }

        if (adapter.getGroupCount() != 0) {

          isPauseSearch = false;

          mBinding.tvEmpty.setVisibility(View.GONE);
          mBinding.llHistory.setVisibility(View.GONE);
          findViewById(R.id.lv_searchFile).setVisibility(View.VISIBLE);
          hideSoftKeyBoard();

          initEvent();
        } else {
          // 显示历史记录

          if(isPauseSearch){
            mBinding.tvEmpty.setVisibility(View.VISIBLE);
            mBinding.llHistory.setVisibility(View.GONE);
            mBinding.tvEmpty.setText("该文件夹暂无文件哦~");
          }else {
            mBinding.tvEmpty.setVisibility(View.VISIBLE);
            mBinding.llHistory.setVisibility(View.GONE);
            mBinding.tvEmpty.setText("未找到结果");
          }

          isPauseSearch = false;
          findViewById(R.id.lv_searchFile).setVisibility(View.GONE);
          hideSoftKeyBoard();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取匹配的文件夹或文件
   *
   * @param
   * @return
   */
  private void achieveMatchData(Uri key, String name) throws Exception {

    try {
      isSearchingFinished = false;

      DocumentMetadata document = DocumentMetadata.fromUri(key, mClient);
      StructStat stat = mClient.stat(key.toString());

      FileBean file = new FileBean();
      file.setName(document.getDisplayName());
      file.setPath(document.getUri().toString());

      file.setDate(TimeUtils.millis2String(stat.st_ctime * 1000));

      file.setLongDate(stat.st_ctime * 1000);
      file.setLongSize(stat.st_size);

      if (S_ISDIR(stat.st_mode)) {
        file.setFileType(true);
        file.setSize(SambaUtils.bytes2kb(document.getSize()));

        file.setLastModifyTime(TimeUtils.millis2String(stat.st_ctime * 1000));

        isSearchingFinished = true;

        if (document.getDisplayName().indexOf(name) != -1) {
          //含字符串的文件名
          if (!(document.getDisplayName()).startsWith(".")) {
            // 隐藏lost+found文件夹
            if (!"lost+found".equals(document.getDisplayName())) {
              adapter.add1(file, 2,fileBeansTemp);
              System.out.println("搜索中 文件夹 file.getName() = " + file.getName());
            }
          }
          //递归搜索
          String sharePath = remoteCurrentSharePath + "/" + document.getDisplayName();
          searchMatchFolder(sharePath);
        }else {
          //不包含
          //递归搜索
          String sharePath = remoteCurrentSharePath + "/" + document.getDisplayName();
          searchMatchFolder(sharePath);
        }
      } else {
        isSearchingFinished = true;
        if (document.getDisplayName().indexOf(name) != -1) {
          //含字符串的文件名
          if (!(document.getDisplayName()).startsWith(".")) {
            //过滤以.开头的文件
            file.setFileType(false);
            if (document.getSize() == 0) {
              //已损害的文件大小为0
              file.setSize("0K");
            } else {
              file.setSize(SambaUtils.bytes2kb(document.getSize()));
            }
            file.setLastModifyTime(TimeUtils.millis2String(stat.st_ctime * 1000));

            if (name.indexOf(".upload") != -1) {
              //包含.upload 表示正在上传的文件
            } else {
              adapter.add1(file, 2,fileBeansTemp);
              System.out.println("搜索中 文件 file.getName() = " + file.getName());
            }
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 删除历史记录
   *
   * @param
   * @return
   */
  private void deleteHistoryFileClick() {

    mDeleteHisPop = new SambaPopupWindow(FileSearchActivity.this, 3, "");
    mDeleteHisPop.showAtLocation(mDeleteHisPop.getOuterLayout(), Gravity.CENTER, 0, 0);

    mDeleteHisPop.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick() {
        //清空数据库
        for (int i = 0; i < mHistoryFileList.size(); i++) {
          mDBUtil.DeleteHistoryData(mHistoryFileList.get(i).getName());
        }
        mHistoryFileList.clear();
        mBinding.flSearchhisList.removeAllViews();
        mBinding.llHistory.setVisibility(View.GONE);
        System.out.println("\"清空数据：\" = " + "历史记录数据库清空！");
      }
    });
  }

  /**
   * 添加流式布局的Textview
   *
   * @param
   * @return
   */
  private void addAllTextView() {
    ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.leftMargin = 5;
    lp.rightMargin = 5;
    lp.topMargin = 5;
    lp.bottomMargin = 5;
    if (mBinding.flSearchhisList.getFlowViewLineCount() >= 4) {
      //超过3行的话自动队列式的删除记录
      //移除最早的View
      mBinding.flSearchhisList.removeView(mHistoryFileList.get(0).getTextView());
      mHistoryFileList.remove(0);
      mDBUtil.DeleteHistoryData(mHistoryFileList.get(0).getName());
    }

    for (int i = 0; i < mHistoryFileList.size(); i++) {
      TextView view = new TextView(getContext());
      view.setText(mHistoryFileList.get(i).getName());
      view.setTextColor(getResources().getColor(R.color.black_424242));
      view.setTextSize(12);
      view.setGravity(Gravity.CENTER);
      view.setSingleLine(true);
      view.setEllipsize(TextUtils.TruncateAt.END);
      view.setBackgroundDrawable(getResources().getDrawable(R.drawable.nas_searchhis));
      //view.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_background));
      //mFlowLayout.addView(view, lp);
      mHistoryFileList.get(i).setTextView(view);
      mBinding.flSearchhisList.addView(view, lp);
    }

    flowViewClick();//点击事件

    /**
     * 删除搜索历史
     */
    mBinding.rlDeleteHis.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteHistoryFileClick();
      }
    });

  }

  protected void hideSoftKeyBoard() {
    new Handler().postDelayed(
        () -> KeyboardUtils.hideSoftInput(this), 200);
  }

  @Override
  public void onGroupExpand(int groupPosition) {
    for (int i = 0, count = mBinding.lvSearchFile.getExpandableListAdapter().getGroupCount(); i < count; i++) {
      if (groupPosition != i) {
        // 关闭其他分组
        mBinding.lvSearchFile.collapseGroup(i);
      }
    }
  }

  /**
   * 判断是否有历史数据
   *
   * @param
   * @return
   */
  private Boolean isHasHistoryData(String str) {
    Boolean isHasData = false;
    if (mHistoryFileList == null) {
      return false;
    }

    for (int i = 0; i < mHistoryFileList.size(); i++) {
      if (str.equals(mHistoryFileList.get(i).getName())) {
        isHasData = true;
        break;
      }
    }

    return isHasData;
  }

  /**
   * 搜索文件
   *
   * @param
   * @return
   */
  class SearchFileTask extends AsyncTask<String, Integer, Integer> {


    public SearchFileTask() {
      //不添加重复数据
      if (!isHasHistoryData(getText(mBinding.etSearch))) {
        //匹配搜索的字符
        SearchFileHistoryBean bean = new SearchFileHistoryBean();
        bean.setName(getText(mBinding.etSearch));
        //写入数据库，只要点击了搜索按钮就添加到搜索历史
        //判断历史记录有没有 有的话不保存
        mDBUtil.insertSearchHistory(bean);
        mHistoryFileList.add(bean);

        mBinding.flSearchhisList.removeAllViews();
        addAllTextView();//此时如果点击clearImg回去应该重新加载FlowView
      }

    }

    @Override
    protected void onPreExecute() {
      mDialog = new MyProgressDialog(FileSearchActivity.this);
      mDialog.setMessage("正在查找中……");
      mDialog.show();

      fileBeansTemp.clear();

      remoteCurrentSharePath_For_Search = remoteCurrentSharePath;//缓存
    }

    @Override
    protected Integer doInBackground(String... params) {
      fetchAtPath();
      return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
    }
  }

  /**
   * 删除所有FlowView
   *
   * @param
   * @return
   */
  private void deleteFlowView() {
    for (int i = 0; i < mHistoryFileList.size(); i++) {
      mBinding.flSearchhisList.removeView(mHistoryFileList.get(i).getTextView());
    }
  }

  /**
   * 历史View的点击事件
   *
   * @param
   * @return
   */
  private void flowViewClick() {
    for (int i = 0; i < mHistoryFileList.size(); i++) {
      int finalI = i;
      mHistoryFileList.get(i).getTextView().setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mBinding.etSearch.setText(mHistoryFileList.get(finalI).getName());
          //光标放在最后
          Selection.setSelection(mBinding.etSearch.getText(), mBinding.etSearch.getText().length());
          //自动开始搜索
          new SearchFileTask().execute();
        }
      });
    }
  }

  private void itemViewClickEvent(View v, int groupPosition, int childPosition, Boolean isFolder) {
    //只有进行了文件操作才可能出现文件变化

    FileBean bean  = adapter.getGroup(groupPosition);

    switch (childPosition) {
      case 0:
        //下载

        //损坏的文件提示不可下载
        if (("0K".equals(bean.getSize()))) {
          Toast.makeText(FileSearchActivity.this, "文件已损坏，暂不支持下载", Toast.LENGTH_SHORT).show();
          //关闭展开
          mBinding.lvSearchFile.collapseGroup(groupPosition);
          return;
        }
        try {
          System.out.println("isDownloading = " + "开始下载");
          //每次下载之前检查一下数据库里的url是否已经删除
          String urlstr = bean.getPath();

          if (downloaders.get(urlstr) != null) {
            //初始化状态1，正在下载状态2，暂停状态3
            if (downloaders.get(urlstr).getState() != 1) {
              Toast.makeText(this, "下载任务已存在", Toast.LENGTH_SHORT).show();
              //关闭展开
              mBinding.lvSearchFile.collapseGroup(groupPosition);
              return;
            } else {
              downloaders.get(urlstr).delete(urlstr, this);
              downloaders.get(urlstr).reset();
              downloaders.remove(urlstr);
/*
              mDBUtil.deleteKey(urlstr);//下载完就删除key
*/
            }
          }

          //看下是否已经下载过
          File f = new File(UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH));
          if (f.exists() && f.length() > 0 && f.length() == bean.getLongSize()) {
            //弹窗
            System.out.println("下载 弹窗 文件已存在");
            mSambaPopWin = new SambaPopupWindow(FileSearchActivity.this, 5, "");
            mSambaPopWin.setFocusable(true);
            mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

            mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
              @Override
              public void onPositiveClick() {

                try {
                  //判断该文件是否存在
                  DocumentMetadata.fromUri(Uri.parse(urlstr), mClient).getSize();
                } catch (IOException e) {
                  //文件不存在
                  e.printStackTrace();
                  mBinding.lvSearchFile.collapseGroup(groupPosition);
                  Toast.makeText(FileSearchActivity.this, "下载失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
                  return;
                }

                //覆盖原文件
                System.out.println("下载 弹窗 继续下载");
                System.out.println("\"下载:\" = " + "开始下载");
                Toast.makeText(FileSearchActivity.this, "成功添加下载任务", Toast.LENGTH_SHORT).show();
                SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, FileSearchActivity.this, mClient, DownloadFragment.mHandlerDownload);
                downloadTask.execute(bean.getPath(), UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH), "1");
              }
            });

            mSambaPopWin.setOnNegativeClickListener(new SambaPopupWindow.OnNegativeClickListener() {
              @Override
              public void onNegtiveClick() {
                //取消
                //关闭展开
                System.out.println("下载 弹窗 取消下载");
                mBinding.lvSearchFile.collapseGroup(groupPosition);
                return;
              }
            });

          } else {

            try {
              //判断该文件是否存在
              DocumentMetadata.fromUri(Uri.parse(urlstr), mClient).getSize();
            } catch (IOException e) {
              //文件不存在
              e.printStackTrace();
              mBinding.lvSearchFile.collapseGroup(groupPosition);
              Toast.makeText(FileSearchActivity.this, "下载失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
              return;
            }

            System.out.println("\"下载:\" = " + "开始下载");
            Toast.makeText(this, "成功添加下载任务", Toast.LENGTH_SHORT).show();
            SambaUtils.DownloadTask downloadTask = new SambaUtils.DownloadTask(downloaders, FileSearchActivity.this, mClient, DownloadFragment.mHandlerDownload);
            downloadTask.execute(bean.getPath(), UploadUtils.genLocalPath(bean.getName(), AppConstant.SAMBA_DOWNLOAD_PATH), "1");

          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;

      case 1:
        //重命名
        //损坏的文件提示不可下载
        if (("0K".equals(bean.getSize()))) {
          Toast.makeText(FileSearchActivity.this, "文件已损坏，暂不支持重命名", Toast.LENGTH_SHORT).show();
          //关闭展开
          mBinding.lvSearchFile.collapseGroup(groupPosition);
          return;
        }

        String oldName = "";

        if (bean.getFileType()) {
          //文件夹的旧文件名截取最后的/
          oldName = bean.getName().substring(0, bean.getName().length() - 1);
        } else {
          //文件 截取掉最后一个.之后的
          oldName = bean.getName();
          oldName = oldName.substring(0, oldName.lastIndexOf("."));
        }

        System.out.println("groupPosition+\"  \"+childPosition = " + groupPosition + "  " + childPosition);
        //Toast.makeText(RemoteMainActivity.this, "重命名", Toast.LENGTH_SHORT).show();
        mSambaPopWin = new SambaPopupWindow(FileSearchActivity.this, 2, oldName);
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {
            String newName = getText(mSambaPopWin.getNewNameEdit());
            //不能包含的字符
            if (newName.contains("\\") ||
                newName.contains("/") || newName.contains(":") ||
                newName.contains("*") || newName.contains("?") ||
                newName.contains("\"") || newName.contains("<") ||
                newName.contains(">") || newName.contains("|")
                ) {
              Toast.makeText(FileSearchActivity.this, "文件名不能包含下列任何字符:\n" + " \\  / : * ? \" < > | ", Toast.LENGTH_SHORT).show();
              return;
            }

            new RenameTask(groupPosition, newName, isFolder).execute();
          }
        });
        break;

      case 2:
        //移动
        String[] movePathList = new String[1];
        moveFileIndex = groupPosition;
        movePathList[0] = bean.getPath();
        Bundle bundle = new Bundle();
        bundle.putStringArray("MOVE_FILE_PATH_LIST", movePathList);
        Intent intent = new Intent(FileSearchActivity.this, MoveFileActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_MOVE_FILE);
        break;

      case 3:
        //删除
        System.out.println("groupPosition+\"  \"+childPosition = " + groupPosition + "  " + childPosition);
        mSambaPopWin = new SambaPopupWindow(FileSearchActivity.this, 4, "");
        mSambaPopWin.setFocusable(true);
        mSambaPopWin.showAtLocation(mSambaPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

        mSambaPopWin.setOnPositiveClickListener(new SambaPopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick() {
            //正在上传 下载的文件不能被删除
            String urlstr = bean.getPath();

            MyProgressDialog mDialog = SambaUtils.showDialog(FileSearchActivity.this, "正在删除中…");

            if (bean.getFileType()) {
              //文件夹
              mDeleteFolderList.clear();
              mDeleteFolderList.add(urlstr);

              deleteFolderPath = urlstr;

              isDeleteFolder = true;

              deleteFolder(urlstr,mDialog);
            } else {
              //文件
              isDeleteFolder = false;

              new DeleteTask(urlstr, bean.getFileType(), groupPosition,mDialog).execute();
            }
          }
        });
        break;

      default:
        break;
    }

    //关闭展开
    mBinding.lvSearchFile.collapseGroup(groupPosition);

  }

  /**
   * 删除文件夹(递归删除)
   *
   * @param
   * @return
   */
  private void deleteFolder(String path,MyProgressDialog mDialog) {
    //   smb://192.168.1.1/    qtec/新建文件夹xh0312      qtec/新建文件夹xh0312/文件夹xx        share=qtec/新建文件夹xh0312;
    String sharePath = path.substring(getCharacterPosition(path) + 1);

    DocumentMetadata metadata = DocumentMetadata.createShare(connect_ip, sharePath);
    System.out.println("删除文件夹 sharePath = " + sharePath);

    final LoadChildrenTask task =
        new LoadChildrenTask(metadata, mClient, mCache, new OnTaskFinishedCallback<DocumentMetadata>() {
          @Override
          public void onTaskFinished(int status, @Nullable DocumentMetadata metadata, @Nullable Exception exception) {
            try {
              if(exception != null){
                if(exception.getClass().getName().equals("java.io.FileNotFoundException")){
                  System.out.println("deleteFolder LoadChildrenTask 文件夹不存在");
                  Toast.makeText(FileSearchActivity.this, "文件夹已被删除", Toast.LENGTH_SHORT).show();
                  mDialog.dismiss();
                  return;
                }
              }

              Map<Uri, DocumentMetadata> children = metadata.getChildren();
              Iterator it = children.keySet().iterator();

              while (it.hasNext()) {
                Uri key;
                key = (Uri) it.next();
                // 名称 日期 大小 路径 类型：文件夹或文件
                /*mUriList.add(key);*/

                DocumentMetadata document = DocumentMetadata.fromUri(key, mClient);
                StructStat stat = mClient.stat(key.toString());
                if (S_ISDIR(stat.st_mode)) { //路径
                  // 隐藏lost+found文件夹
                  if (!"lost+found".equals(document.getDisplayName())) {
                    //文件夹
                    System.out.println("删除文件夹 遍历删除文件夹 = " + document.getUri());
                    mDeleteFolderList.add(document.getUri() + "");
                    deleteFolder(document.getUri() + "",mDialog);
                  }
                } else {
                  //文件
                  System.out.println("删除文件夹 删除文件 = " + document.getUri());
                  // 此时的groupPosition 不起作用 由deleteGroupPosition 代替  0为占位符
                  new DeleteTask(document.getUri() + "", false, 0,mDialog).execute();
                }
              }

              //删除完成之后再删除该空文件夹(倒序)
              for (int i = mDeleteFolderList.size() - 1; i >= 0; i--) {
                // 此时的groupPosition 不起作用 由deleteGroupPosition 代替
                new DeleteTask(mDeleteFolderList.get(i), true, 0,mDialog).execute();
              }

            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        });

    mTaskManager.runTask(metadata.getUri(), task);
  }

  /**
   * 获取第N次出现的字符
   *
   * @param
   * @return
   */
  public static int getCharacterPosition(String string) {
    //这里是获取"/"符号的位置
    Matcher slashMatcher = Pattern.compile("/").matcher(string);
    int mIdx = 0;
    while (slashMatcher.find()) {
      mIdx++;
      //当"/"符号第三次出现的位置
      if (mIdx == 3) {
        break;
      }
    }
    return slashMatcher.start();
  }

  /**
   * 重命名
   *
   * @param
   * @return
   */
  class RenameTask extends AsyncTask<String, Boolean, Boolean> {
    private MyProgressDialog mDialog;
    private int groupPosition = 0;
    private String newName = "";
    private Boolean isFolder = false;


    public RenameTask(int groupPosition, String newName, Boolean isFolder) {
      this.groupPosition = groupPosition;
      this.newName = newName;
      this.isFolder = isFolder;
    }

    @Override
    protected void onPreExecute() {
      mDialog = new MyProgressDialog(FileSearchActivity.this);
      mDialog.setMessage("正在重命名……");
      mDialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean isRename = false;
      try {
        isRename = SambaUtils.renameSmbFiles(mClient,adapter.getGroup(groupPosition).getPath(), newName, isFolder);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return isRename;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if(mDialog != null){
        mDialog.dismiss();
      }

      if (result) {
        Toast.makeText(FileSearchActivity.this, "重命名成功", Toast.LENGTH_SHORT).show();
        searchFileInit();
      } else {
        Toast.makeText(FileSearchActivity.this, "重命名失败", Toast.LENGTH_SHORT).show();
      }
    }
  }

  private void initSmbFileList(DocumentMetadata metadata) {
    // remoteRootIp: smb://192.168.1.1/qtec/
    remoteCurrentPath = metadata.getUri().toString() + "/";

    System.out.println("文件搜索 当前路径：remoteCurrentPath = " + remoteCurrentPath);
    System.out.println("文件搜索 当前路径：remoteCurrentSharePath = " + remoteCurrentSharePath);

    if (remoteCurrentPath.equals(searchRootIp))
      isExit_Back = true;
    else
      isExit_Back = false;

    System.out.println("文件搜索 当前路径：isExit_Back = " + isExit_Back);

    searchFile(metadata, getText(mBinding.etSearch));

  }

  /**
   * 单个文件操作完成之后重新搜索文件
   *
   * @param
   * @return
   */
  private void searchFileInit() {
    System.out.println("\"搜索：\" = " + "搜索文件ing");
    //匹配搜索的字符
    // addAllTextView();
    SearchFileHistoryBean bean = new SearchFileHistoryBean();
    bean.setName(getText(mBinding.etSearch));
    //写入数据库，只要点击了搜索按钮就添加到搜索历史
    mDBUtil.insertSearchHistory(bean);
    mHistoryFileList.add(bean);
    mBinding.flSearchhisList.removeAllViews();
    // deleteFlowView();
    addAllTextView();//此时如果点击clearImg回去应该重新加载FlowView
    new SearchFileTask().execute();
  }

  class DeleteTask extends AsyncTask<String, Boolean, Boolean> {
    String path = "";
    private MyProgressDialog mDialog;
    int groupPosition = 0;
    Boolean fileType = false;

    public DeleteTask(String path, Boolean fileType, int groupPosition,MyProgressDialog mDialog) {
      this.path = path;
      this.groupPosition = groupPosition;
      this.fileType = fileType;
      this.mDialog = mDialog;
      System.out.println("delete = " + groupPosition);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... params) {

      Boolean result = SambaUtils.deleteSmbFiles(mClient, path, fileType);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {

      if (isDeleteFolder) {
        //当删除根文件夹时才开始提示，其他情况都不提示
        if (deleteFolderPath.equals(path)) {

          if (result) {

            isDeleteFolder = false;
            mDialog.dismiss();

            Toast.makeText(FileSearchActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

            initEvent();
          } else {
            Toast.makeText(FileSearchActivity.this, SambaUtils.getSambaErrorMsg(), Toast.LENGTH_SHORT).show();
          }
        }

      } else {
        mDialog.dismiss();
        if (result) {
          Toast.makeText(FileSearchActivity.this, "删除成功", Toast.LENGTH_SHORT).show();

          adapter.remove(groupPosition);

          initEvent();
        } else {
          Toast.makeText(FileSearchActivity.this, SambaUtils.getSambaErrorMsg(), Toast.LENGTH_SHORT).show();
          if("文件已被删除".equals(SambaUtils.getSambaErrorMsg())){
            adapter.remove(groupPosition);
          }
        }
      }

    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    System.out.println("文件搜索 onKeyDown remoteCurrentSharePath = " + remoteCurrentSharePath + " isExitBack = " + isExit_Back+"  isPauseSearch = "+isPauseSearch);

    if (keyCode == KeyEvent.KEYCODE_BACK) {
      //如果是根目录的话则退出
      if (isExit_Back) {
        //最好提示是否退出的提示框
        finish();
      } else {

        if (remoteCurrentSharePath.contains("/")) {
          remoteCurrentSharePath = remoteCurrentSharePath.substring(0, remoteCurrentSharePath.lastIndexOf("/"));
        }

        if(remoteCurrentSharePath_For_Search.equals(remoteCurrentSharePath)){
          //展示之前的搜索记录

          isExit_Back = true;

          adapter = new RemoteMainAdapter(FileSearchActivity.this);
          mBinding.lvSearchFile.setAdapter(adapter);

          for (int i = 0; i < fileBeansTemp.size(); i++) {
            adapter.add(fileBeansTemp.get(i),2);
          }
          initEvent();

        }else {
          isPauseSearch = true;
          fetchAtPath();
        }
      }
      return true;
    } else {
      return super.onKeyDown(keyCode, event);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_MOVE_FILE) {
      adapter.remove(moveFileIndex);
      initEvent();
    }
  }

  private String getRemoteCurrentPath() {
    return getIntent().getStringExtra("CurrentRemoteFilePath");
  }
}
