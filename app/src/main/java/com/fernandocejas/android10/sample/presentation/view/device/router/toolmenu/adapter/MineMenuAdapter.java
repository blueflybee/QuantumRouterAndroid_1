package com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.callback.MenuDragCallback;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.constant.MenuConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.ApplyMenu;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.util.ACache;

import java.util.ArrayList;
import java.util.List;

/**
 * 头部应用适配器
 */
public class MineMenuAdapter extends RecyclerView.Adapter<MineMenuAdapter.ViewHolder> implements MenuDragCallback.OnItemMoveListener {
  private final Context mContext;
  private final LayoutInflater mInflater;
  private List<ApplyMenu> mMenus = new ArrayList<>();
  private boolean mEditable = false;//编辑
  private MenuDragCallback mMenuDragCallback;
  private OnItemClickListenerEdit mOnItemClickListenerEdit;
  private OnItemClickListener mOnItemClickListener;

  public MineMenuAdapter(Context context, List<ApplyMenu> menus) {
    this.mContext = context;
    this.mInflater = LayoutInflater.from(context);
    if (menus != null) {
      mMenus.clear();
      this.mMenus.addAll(menus);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(mInflater.inflate(R.layout.item_router_tool_menu, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final ApplyMenu menu = mMenus.get(position);
    holder.mTextView.setText(menu.getName());
    holder.mImageView.setImageResource(menu.getImgRes());

    if (menu.getState() == 0) {
      holder.mIvAdd.setImageResource(R.drawable.icon_router_tool_del);
      handleOnClick(holder, menu);//设置点击监听
    }
    handleLongPress(holder);
    holder.mIvAdd.setVisibility(mEditable ? View.VISIBLE : View.GONE);
    holder.mLayout.setOnClickListener(mEditable ? null : new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mOnItemClickListener.onItemClick(view, menu);
      }
    });
  }

  //处理点击事件
  private void handleOnClick(final ViewHolder holder, final ApplyMenu menu) {
    if (mOnItemClickListener != null) {
      holder.mIvAdd.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          //点击是否可以进行增删
          //对项目点击后增删操作的监听
          mOnItemClickListenerEdit.onItemClick(view, holder.getLayoutPosition());
        }
      });
    }
  }

  //处理长按事件 开启ItemDragHelperCallBack拖拽
  private void handleLongPress(ViewHolder holder) {
    if (mMenuDragCallback == null) return;
    View.OnLongClickListener longClickListener = mEditable ? new View.OnLongClickListener() {
      @Override
      public boolean onLongClick(View v) {
        mMenuDragCallback.setLongPressEnabled(true);
        return false;
      }
    } : null;
    holder.mLayout.setOnLongClickListener(longClickListener);
  }

  @Override
  public int getItemCount() {
    return mMenus == null ? 0 : mMenus.size();
  }

  @Override
  public boolean onItemMove(int fromPosition, int toPosition) {
//    if (isChannelFixed(fromPosition, toPosition)) {
//      return false;
//    }
    //在我的频道子频道的移动
//    System.out.println("onItemMove fromPosition = [" + fromPosition + "], toPosition = [" + toPosition + "]");
    if (fromPosition == toPosition) return true;
    ApplyMenu menuFrom = mMenus.remove(fromPosition);
    mMenus.add(toPosition, menuFrom);
//    System.out.println("mMenus = " + mMenus);
//    Collections.swap(getAdapterData(), fromPosition, toPosition);
//    notifyItemMoved(fromPosition, toPosition);
    //通知顺序变换，存储，设置频道顺序，以及显示的顺序
//    ACache.get(mContext.getApplicationContext()).put(getMineMenuKey(), ((ArrayList<ApplyMenu>) mMenus));
    return true;
  }

  @NonNull
  private String getMineMenuKey() {
    return MenuConstant.APPLY_MINE + "_" + GlobleConstant.getgDeviceId() + "_" + PrefConstant.getUserUniqueKey("0");
  }

  //不能移动头条
  private boolean isChannelFixed(int fromPosition, int toPosition) {
    return fromPosition == 0 || toPosition == 0;
  }

  public List<ApplyMenu> getAdapterData() {
    return mMenus;
  }

  /**
   * 添加数据
   *
   * @param menus 需要添加的数据
   */
  public void addData(List<ApplyMenu> menus) {
    if (menus == null || menus.isEmpty()) return;
    mMenus.clear();
    mMenus.addAll(menus);
    notifyDataSetChanged();
  }

  public void setEditable(boolean editable) {
    this.mEditable = editable;
    this.notifyDataSetChanged();
  }

  public boolean getEditable() {
    return mEditable;
  }

  public void setMenuDragCallback(MenuDragCallback menuDragCallback) {
    mMenuDragCallback = menuDragCallback;
  }

  /**
   * 设置状态编辑的点击事件
   *
   * @param onItemClickListener
   */
  public void setOnItemClickListenerEdit(OnItemClickListenerEdit onItemClickListener) {
    mOnItemClickListenerEdit = onItemClickListener;
  }

  /**
   * 设置状态编辑的点击事件
   *
   * @param onItemClickListener
   */
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView mTextView;//文字描述
    private ImageView mImageView, mIvAdd;//图片,添加删除按钮
    private RelativeLayout mLayout;//整个item布局

    ViewHolder(View itemView) {
      super(itemView);
      mImageView = itemView.findViewById(R.id.iv_icon);
      mIvAdd = itemView.findViewById(R.id.iv_add);
      mTextView = itemView.findViewById(R.id.tv_name);
      mLayout = itemView.findViewById(R.id.rv_dev_frag_router);
    }
  }

  //状态编辑Item点击事件的监听接口
  public interface OnItemClickListenerEdit {
    void onItemClick(View view, int position);
  }

  //Item点击事件的监听接口
  public interface OnItemClickListener {
    void onItemClick(View view, ApplyMenu menu);
  }
}
