package com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.callback.MenuDragCallback;
import com.fernandocejas.android10.sample.presentation.view.device.router.toolmenu.obj.ApplyMenu;

import java.util.Collections;
import java.util.List;

/**
 * 全部应用适配器
 */
public class ApplyAdapter extends RecyclerView.Adapter<ApplyAdapter.ViewHolder> implements MenuDragCallback.OnItemMoveListener {
  private final Context mContext;
  private final LayoutInflater mInflater;
  private List<ApplyMenu> mMenus;
  private boolean mEditable = false;//编辑
  private MenuDragCallback mMenuDragCallback;
  private OnItemClickListenerEdit mOnItemClickListenerEeit;
  private OnItemClickListener mOnItemClickListener;

  public ApplyAdapter(Context context, List<ApplyMenu> menus) {
    this.mContext = context;
    this.mMenus = menus;
    this.mInflater = LayoutInflater.from(context);
  }

  /**
   * 设置编辑状态
   *
   * @param editable
   */
  public void setEditable(boolean editable) {
    this.mEditable = editable;
    this.notifyDataSetChanged();
  }

  /**
   * 获取当前的编辑状态
   *
   * @return 当前的编辑状态
   */
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
    mOnItemClickListenerEeit = onItemClickListener;
  }

  /**
   * 设置状态编辑的点击事件
   *
   * @param onItemClickListener
   */
  public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
    mOnItemClickListener = onItemClickListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(mInflater.inflate(R.layout.item_router_tool_menu, parent, false));
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    ApplyMenu menu = mMenus.get(position);
    holder.mTextView.setText(menu.getName());
    holder.mImageView.setImageResource(menu.getImgRes());
    handleOnClick(holder, menu);//设置点击监听
//    handleLongPress(holder, menu);//设置长按监听
  }

  //处理点击事件
  private void handleOnClick(final ViewHolder holder, final ApplyMenu menu) {
    if (mOnItemClickListenerEeit != null) {
      if (menu.getState() == 0) {//如果在头部，就取消点击事件
        holder.mIvAdd.setImageResource(R.drawable.icon_router_tool_del);
        holder.mIvAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            //对项目点击后增删操作的监听
            mOnItemClickListenerEeit.onItemDel(view, holder.getLayoutPosition());
          }
        });
      } else if (menu.getState() == 1) {
        holder.mIvAdd.setImageResource(R.drawable.icon_router_tool_add);
        holder.mIvAdd.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            //对项目点击后增删操作的监听
            mOnItemClickListenerEeit.onItemAdd(view, holder.getLayoutPosition());
          }
        });
      }
    }
    holder.mIvAdd.setVisibility(mEditable ? View.VISIBLE : View.GONE);
    holder.mImageView.setOnClickListener(mEditable ? null : new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnItemClickListener != null) {
          mOnItemClickListener.onItemClick(v, menu);
        }

      }
    });
  }

  //处理长按事件 开启ItemDragHelperCallBack拖拽
  private void handleLongPress(ViewHolder holder, final ApplyMenu table) {
    if (mMenuDragCallback != null) {
      holder.mLayout.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          mMenuDragCallback.setLongPressEnabled(table.getIndex() == 0 ? false : true);
          return false;
        }
      });
    }
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

  @Override
  public int getItemCount() {
    return mMenus == null ? 0 : mMenus.size();
  }

  @Override
  public boolean onItemMove(int fromPosition, int toPosition) {
    if (isChannelFixed(fromPosition, toPosition)) {
      return false;
    }
    //在我的频道子频道的移动
    Collections.swap(getAdapterData(), fromPosition, toPosition);
    notifyItemMoved(fromPosition, toPosition);
    //通知顺序变换，存储，设置频道顺序，以及显示的顺序
    System.out.println("发送移动的消息");
//        EventBus.getDefault().post(new ChannelBean(getAdapterData()));
    return true;
  }

  //不能移动头条
  private boolean isChannelFixed(int fromPosition, int toPosition) {
    return fromPosition == 0 || toPosition == 0;
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

  public List<ApplyMenu> getAdapterData() {
    return mMenus;
  }

  //状态编辑Item点击事件的监听接口
  public interface OnItemClickListenerEdit {
    void onItemAdd(View view, int position);
    void onItemDel(View view, int position);
  }

  //Item点击事件的监听接口
  public interface OnItemClickListener {
    void onItemClick(View view, ApplyMenu name);
  }
}
