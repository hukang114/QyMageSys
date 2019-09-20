package com.qymage.sys.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qymage.sys.R;
import com.qymage.sys.ui.entity.FileListEnt;

import java.util.List;

/**
 * 类名：
 * 类描述：文件列表适配器
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/2013:59
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class FileListAdapter extends BaseQuickAdapter<FileListEnt, BaseViewHolder> {


    List<FileListEnt> list;

    public FileListAdapter(int layoutResId, @Nullable List<FileListEnt> data) {
        super(layoutResId, data);
        list = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, FileListEnt item) {
        helper.setText(R.id.file_name, item.fileName);
        helper.setText(R.id.file_url, item.filePath);
        helper.addOnClickListener(R.id.file_del_img);
        if (helper.getAdapterPosition() == list.size() - 1) {
            helper.setVisible(R.id.div_file_view, false);
        } else {
            helper.setVisible(R.id.div_file_view, true);
        }
    }


}
