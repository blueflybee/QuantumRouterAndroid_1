package com.fernandocejas.android10.sample.presentation.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 万能的adapter
 * @author tangxian
 *
 * @param <T> 
 */
public abstract class CommAdapter<T> extends BaseAdapter {

	private Context context;
	private List<T> data;
	private int layoutId;
	private int mPostition = 0;

	public CommAdapter(Context context, List<T> data, int layoutId) {
		super();
		this.context = context;
		this.data = data;
		this.layoutId  = layoutId;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mPostition = position;
		BaseViewHolder
				baseViewHolder = BaseViewHolder.get(context, convertView, parent, layoutId, position);
		convert(baseViewHolder,data.get(position));
		return baseViewHolder.getConvertView();
	}

	public abstract void convert(BaseViewHolder baseViewHolder, T t) ;

	/**
	 *
	 * @return
	 */
	public int getLvAdapterPosition(){
		return  mPostition;
	}
}
