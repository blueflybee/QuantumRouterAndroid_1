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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 万能的ViewHolder
 * 
 * @author tangxian
 * 
 */
public class BaseViewHolder {
	/** 用来缓存view */
	private SparseArray<View> views;

	/** 复用的布局 */
	private View convertView;

	private BaseViewHolder(Context context, ViewGroup parent, int resId,
                         int position) {
		this.views = new SparseArray<View>();
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(resId, null);
		convertView.setTag(this);
	}

	/**
	 * 获取BaseViewHolder对象
	 * 
	 * @param context
	 *            上下文
	 * @param convertView
	 *            复用的view
	 * @param parent
	 * @param resId
	 *            item的资源id
	 * @param position
	 *            当前item位置
	 * @return
	 */
	public static BaseViewHolder get(Context context, View convertView,
                                   ViewGroup parent, int resId, int position) {
		if (convertView == null) {
			return new BaseViewHolder(context, parent, resId, position);
		}
		return (BaseViewHolder) convertView.getTag();
	}

	/**
	 * 通过id获取控件，如果没有从布局中找，然后存到views中
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T)view;
	}

	public View getConvertView() {
		return convertView;
	}
}
