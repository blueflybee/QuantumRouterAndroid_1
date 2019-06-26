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

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fernandocejas.android10.sample.presentation.R;
import com.google.android.sambadocumentsprovider.mount.BaseViewHolder;
import com.google.android.sambadocumentsprovider.mount.CommAdapter1;
import com.google.android.sambadocumentsprovider.mount.FileBean;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class RemoteMainAdapter extends CommAdapter1<FileBean> {

    OnDeleteClickListener mOnDelete;
    OnRenameClickListener mOnRename;
    OnOpenClickListener mOnOpen;
    OnDownloadClickListener mOnDownload;


    public RemoteMainAdapter(Context context, List<FileBean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(BaseViewHolder baseViewHolder, FileBean response, final int position) {
        TextView name = baseViewHolder.getView(R.id.tv_name);
        TextView date = baseViewHolder.getView(R.id.tv_date);
        TextView size = baseViewHolder.getView(R.id.tv_size);

        Button delete = baseViewHolder.getView(R.id.btn_delete);
        Button rename = baseViewHolder.getView(R.id.btn_rename);
        Button open = baseViewHolder.getView(R.id.btn_open);
        Button download = baseViewHolder.getView(R.id.btn_download);

        if(response.getFileType()){
            //文件夹
            name.setText(response.getName()+" (文件夹)");
        }else {
            name.setText(response.getName()+" (文件)");
        }

        date.setText(response.getDate());
        size.setText(response.getSize());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDelete != null){
                    mOnDelete.onDeleteClick(position);
                }
            }
        });

        rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRename != null){
                    mOnRename.onRenameClick(position);
                }
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnOpen != null){
                    mOnOpen.onOpenClick(position);
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDownload != null){
                    mOnDownload.onDownloadClick(position);
                }
            }
        });

    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
    public interface OnRenameClickListener {
        void onRenameClick(int position);
    }
    public interface OnOpenClickListener {
        void onOpenClick(int position);
    }
    public interface OnDownloadClickListener {
        void onDownloadClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDelete) {
        this.mOnDelete = mOnDelete;
    }
    public void setOnRenameClickListener(OnRenameClickListener mOnRename) {
        this.mOnRename = mOnRename;
    }
    public void setOnOpenClickListener(OnOpenClickListener mOnOpen) {
        this.mOnOpen = mOnOpen;
    }
    public void setOnDownloadClickListener(OnDownloadClickListener mOnDownload) {
        this.mOnDownload = mOnDownload;
    }
}
