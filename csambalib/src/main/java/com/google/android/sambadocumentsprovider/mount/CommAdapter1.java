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

package com.google.android.sambadocumentsprovider.mount;

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
public abstract class CommAdapter1<T> extends BaseAdapter {

	private Context context;
	private List<T> data;
	private int layoutId;

	public CommAdapter1(Context context, List<T> data, int layoutId) {
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
		BaseViewHolder
				baseViewHolder = BaseViewHolder.get(context, convertView, parent, layoutId, position);
		convert(baseViewHolder,data.get(position),position);
		/*System.out.println("内容 comAdapter= " + position+"  "+data.get(position));*/
		return baseViewHolder.getConvertView();
	}

	public abstract void convert(BaseViewHolder baseViewHolder, T t ,int mPostition) ;

}
