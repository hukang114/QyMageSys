package com.qymage.sys.common.base;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


import com.qymage.sys.AppConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.common.util.AppManager;
import com.qymage.sys.ui.act.MyLoanActivity;
import com.qymage.sys.ui.adapter.LeaveTypeAdapter;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.LeaveType;
import com.qymage.sys.ui.entity.ProjectAppLogEnt;
import com.qymage.sys.ui.fragment.NewsFragment;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;


public abstract class BBActivity<VB extends ViewDataBinding> extends BaseActivity {
    protected VB mBinding;

    protected int getHight;
    protected int getWidth;

    protected DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    protected DateFormat DEFAULT_NYR = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    protected DecimalFormat df = new DecimalFormat("0.00");


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AppManager.getInstance().addActivity(this); //添加到栈中
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), this.getLayoutId(), null, false);
        super.setContentView(mBinding.getRoot());
        setStatusBar();
        initView();
        initData();
    }

    protected void setStatusBar() {

    }

    protected void initView() {
        getWidth = Tools.getScreenWidth(this);
        getHight = Tools.getScreenHeight(this);
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected void initData() {
    }


    //===========================审批的处理开始=====================================================
    // 打分的数据集合
    List<LeaveType> leaveTypes = new ArrayList<>();
    // 打分的适配器
    LeaveTypeAdapter typeAdapter;

    /**
     * 月报打分页面需要提前调用改方法，的时候需要先获取领导打分数据
     */
    protected void getleadType() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "leadType");
        HttpUtil.getEnum(hashMap).execute(new JsonCallback<Result<List<LeaveType>>>() {
            @Override
            public void onSuccess(Result<List<LeaveType>> result, Call call, Response response) {
                if (result.data != null && result.data.size() > 0) {
                    leaveTypes.clear();
                    leaveTypes.addAll(result.data);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });

    }


    private void showCommentsDialog(String type, int modeType, ProjectAppLogEnt item) {
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.dialog_remarks, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText editText = dialog.findViewById(R.id.notes_edt);
        EditText comments_edt = dialog.findViewById(R.id.comments_edt);
        TextView df_tv = dialog.findViewById(R.id.df_tv);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerview);
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        recyclerView.setLayoutManager(layouta);
        //// 月报提交需要打分和提交评语 同意 的时候执行
        if (modeType == AppConfig.status.value14 && type.equals("1")) { // 月报提交需要打分和提交评语 同意 的时候执行
            comments_edt.setVisibility(View.VISIBLE);
            df_tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (leaveTypes != null && leaveTypes.size() > 0) {
                typeAdapter = new LeaveTypeAdapter(R.layout.item_list_leavetype, leaveTypes);
                recyclerView.setAdapter(typeAdapter);
                typeAdapter.setOnItemChildClickListener((adapter, view12, position) -> {
                    switch (view12.getId()) {
                        case R.id.frg_selc_all:
                            for (int i = 0; i < leaveTypes.size(); i++) {
                                if (i == position) {
                                    if (leaveTypes.get(position).isCheck) {
                                        leaveTypes.get(position).isCheck = true;
                                    } else {
                                        leaveTypes.get(position).isCheck = true;
                                    }
                                } else {
                                    leaveTypes.get(i).isCheck = false;
                                }
                            }
                            typeAdapter.notifyDataSetChanged();
                            break;
                    }
                });
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            comments_edt.setVisibility(View.GONE);
            df_tv.setVisibility(View.GONE);
        }
        TextView submit_btn = dialog.findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(v -> {
            if (type.equals("2")) { // 拒绝审批
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    showToast("请填写拒绝的原因");
                } else {
                    dialog.dismiss();
                    subMitAudit(type,
                            modeType,
                            item,
                            editText.getText().toString(),
                            comments_edt.getText().toString(),
                            getSelectVal(),
                            MyLoanActivity.assignees);
                }
            } else {
                dialog.dismiss();
                subMitAudit(type,
                        modeType,
                        item,
                        editText.getText().toString(),
                        comments_edt.getText().toString(),
                        getSelectVal(),
                        MyLoanActivity.assignees);
            }


        });

    }

    /**
     * 获取选择的打分值
     *
     * @return
     */
    private String getSelectVal() {
        String val = "";
        for (int i = 0; i < leaveTypes.size(); i++) {
            if (leaveTypes.get(i).isCheck) {
                val = leaveTypes.get(i).value;
            }
        }
        return val;
    }


    // 审批的同意处理

    /**
     * @param type     1 同意 2  拒绝 3  撤销
     * @param modeType 审批的模块的类型
     * @param item     审批需的参数对象
     */
    protected void auditAdd(String type, int modeType, ProjectAppLogEnt item) {
        if (type.equals("3")) {
            msgDialogBuilder("确认撤销？", (dialog, which) -> {
                dialog.dismiss();
                showCommentsDialog(type, modeType, item);
            }).create().show();
        } else if (type.equals("2")) {
            msgDialogBuilder("拒绝审批？", (dialog, which) -> {
                dialog.dismiss();
                showCommentsDialog(type, modeType, item);
            }).create().show();
        } else if (type.equals("1")) {
            msgDialogBuilder("同意审批？", (dialog, which) -> {
                dialog.dismiss();
                showCommentsDialog(type, modeType, item);
            }).create().show();
        }
    }


    /**
     * @param type     1 同意 2  拒绝 3  审批
     * @param modeType 审批的模块的类型
     * @param item     审批需的参数对象
     *                 id  String  id
     *                 examType：类型  1—通过   2-拒绝 3-撤回
     *                 remarks String  备注
     *                 processInstanceId String 流程实例ID
     *                 modeType String  模块id
     *                 assignees String  借款接口(传借款人id)
     *                 leadComment String 领导评语（月报）
     *                 leadGrade string  领导打分（月报）
     */
    private void subMitAudit(String type, int modeType, ProjectAppLogEnt item, String remarks, String leadComment, String leadGrade, String assignees) {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", item.id);
        hashMap.put("remarks", "");
        hashMap.put("type", type);
        hashMap.put("processInstanceId", item.processInstId);
        hashMap.put("modeType", modeType);
        hashMap.put("assignees", assignees);
        hashMap.put("remarks", remarks);
        hashMap.put("leadComment", leadComment);
        hashMap.put("leadGrade", leadGrade);
        HttpUtil.audit_auditAdd(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder(result.message, (dialog, which) -> {
                    dialog.dismiss();
                    successTreatment();
                }).setCancelable(false).create().show();
                successTreatment();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });
    }

    /**
     * 审批功的处理
     */
    protected void successTreatment() {

    }

    //-=========================审批处理结束=======================================

    /**
     * 更新消息
     *
     * @param msgId
     */
    protected void msgUdate(String msgId, int position) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("msgId", msgId);
        HttpUtil.msgUdate(hashMap).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                msgUpdateSuccess(position);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    /**
     * 更新消息成的处理
     */
    protected void msgUpdateSuccess(int position) {
        if (NewsFragment.mHander != null) {
            Message message = NewsFragment.mHander.obtainMessage();
            message.what = 100;
            NewsFragment.mHander.sendMessage(message);
        }

    }


    /**
     * 获取默认的审批人
     * processDefId String  流程定义ID
     *
     * @param processDefId
     */
    protected void getAuditQuery(String processDefId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("processDefId", processDefId);
        HttpUtil.wf_auditQuery(hashMap).execute(new JsonCallback<Result<List<GetTreeEnt>>>() {
            @Override
            public void onSuccess(Result<List<GetTreeEnt>> result, Call call, Response response) {
                if (result.data != null && result.data.size() > 0) {
                    getAuditQuerySuccess(result.data);
                } else {
                    LogUtils.e("获取审批人：" + result.message);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                LogUtils.e("获取审批人：" + e.getMessage());
            }
        });
    }

    /**
     * 获取默认审批人成功
     *
     * @param listdata
     */
    protected void getAuditQuerySuccess(List<GetTreeEnt> listdata) {

    }


    //===============文件选择处理=======================


    /**
     * 文件上传
     *
     * @param path
     */
    private void uploadFile(String path) {
        showLoading();
        File file = new File(path);
        HttpUtil.file_Upload(file).execute(new JsonCallback<Result<List<FileListEnt>>>() {
            @Override
            public void onSuccess(Result<List<FileListEnt>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    updateFileSuccess(result.data);
                } else {
                    showToast(result.message);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });

    }

    /**
     * 返回上传成功的文件数据
     *
     * @param fileListEnts
     * @return
     */
    protected void updateFileSuccess(List<FileListEnt> fileListEnts) {

    }

    protected void openFileChoose() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 0);
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {// 选择文件回调
            switch (requestCode) {
                case 0:
                    Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
                    if ("file".equalsIgnoreCase(result.getScheme())) {//使用第三方应用打开
                        path = result.getPath();
                        uploadFile(path);
                        return;
                    }
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                        try {
                            path = getPath(this, result);
                            if (path != null) {
                                uploadFile(path);
                            } else {
                                showToast("选择文件为空");
                            }

                        } catch (Exception e) {
                            showToast("文件选择获取失败,请选择其他类型的文件");
                        }
                    } else {//4.4以下下系统调用方法
                        try {
                            path = getRealPathFromURI(result);
                            if (path != null) {
                                uploadFile(path);
                            } else {
                                showToast("选择的文件为空");
                            }
                        } catch (Exception e) {
                            showToast("文件选择获取失败,请选择其他类型的文件");
                        }

                    }
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }


    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //===============文件选择处理=======================

}
