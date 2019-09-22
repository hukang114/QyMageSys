package com.qymage.sys.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.databinding.ActivityListAttachmentsBinding;
import com.qymage.sys.ui.adapter.FileAttachmenteAdapter;
import com.qymage.sys.ui.adapter.FileListAdapter;
import com.qymage.sys.ui.entity.FileListEnt;

import java.util.List;

/**
 * 附件列表
 */
public class ListAttachmentsActivity extends BBActivity<ActivityListAttachmentsBinding> {

    List<FileListEnt> fileList;
    private Intent mIntent;
    FileAttachmenteAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_attachments;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlTxtClick(v -> finish());
        mIntent = getIntent();
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        fileList = (List<FileListEnt>) mIntent.getSerializableExtra("data");
        if (fileList == null) {
            return;
        }
        // 文件适配器
        adapter = new FileAttachmenteAdapter(R.layout.item_file_list, fileList);
        mBinding.recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(fileList.get(position).filePath);
            intent.setData(content_url);
            startActivity(intent);
        });
    }

    @Override
    protected void initData() {
        super.initData();

    }
}
