package com.qymage.sys.ui.act;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.qymage.sys.BuildConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.datepicker.CustomDatePicker;
import com.qymage.sys.common.datepicker.DateFormatUtils;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.tools.FileSizeUtil;
import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.databinding.ActivityAskForLeaveBinding;
import com.qymage.sys.databinding.ActivityChangePasswordBinding;
import com.qymage.sys.ui.adapter.AuditorListAdapter;
import com.qymage.sys.ui.adapter.CopierListAdapter;
import com.qymage.sys.ui.entity.FileListEnt;
import com.qymage.sys.ui.entity.GetTreeEnt;
import com.qymage.sys.ui.entity.LeaveType;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 请假申请
 */

public class AskForLeaveActivity extends BBActivity<ActivityAskForLeaveBinding> implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {


    List<GetTreeEnt> auditorList = new ArrayList<>();// 审核人
    List<GetTreeEnt> copierList = new ArrayList<>();// 抄送人
    AuditorListAdapter auditorListAdapter;// 审批人适配器
    CopierListAdapter copierListAdapter;// 抄送人适配器
    List<FileListEnt> fileList = new ArrayList<>();// 上传附件

    CommonAdapter<String> imgadapter;//报销图片集合
    private List<String> planting_imgList = new ArrayList<>();// 最后需要上传的图片集合


    private Bundle bundle;
    private List<String> protypelist = new ArrayList<>();
    private String leaveType;// /请假类型
    private String department = "技术部";
    private CustomDatePicker mDatePicker, mTimerPicker;
    private long begintime = 0;
    List<LeaveType> leaveTypes = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ask_for_leave;
    }


    @Override
    protected void initView() {
        // 关闭当前的页面
        mBinding.metitle.setlTxtClick(v -> finish());
        mBinding.metitle.setrTxtClick(v -> {
            openActivity(AskForLeaveRecordlActivity.class);
        });
        LinearLayoutManager layouta = new LinearLayoutManager(this);
        layouta.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列
        LinearLayoutManager layoutb = new LinearLayoutManager(this);
        layoutb.setOrientation(LinearLayoutManager.HORIZONTAL);//设置为横向排列

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.qingjiaTupianRecy.setLayoutManager(linearLayoutManager);

        mBinding.sprImg.setOnClickListener(this);
        mBinding.csrImg.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);
        mBinding.xuanzeShijianEnd.setOnClickListener(this);
        mBinding.xuanzeShijianStart.setOnClickListener(this);
        mBinding.xuanzeQingjialeixing.setOnClickListener(this);
        mBinding.sprRecyclerview.setLayoutManager(layouta);
        mBinding.csrRecyclerview.setLayoutManager(layoutb);
        mBinding.qingjiaTupian.setOnClickListener(this);
        // 审批人
        auditorListAdapter = new AuditorListAdapter(R.layout.item_list_auditor, auditorList);
        mBinding.sprRecyclerview.setAdapter(auditorListAdapter);
        auditorListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.caccel_ioc:
                    auditorList.remove(position);
                    auditorListAdapter.notifyDataSetChanged();
                    break;
            }
        });
        // 抄送人
        copierListAdapter = new CopierListAdapter(R.layout.item_list_auditor, copierList);
        mBinding.csrRecyclerview.setAdapter(copierListAdapter);
        copierListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.caccel_ioc:
                    copierList.remove(position);
                    copierListAdapter.notifyDataSetChanged();
                    break;
            }
        });

        setImgAdapter();

    }

    private void setImgAdapter() {
        mBinding.qingjiaTupianRecy.setAdapter(imgadapter = new CommonAdapter<String>(this, R.layout.item_evorder_imglist, planting_imgList) {
            @Override
            protected void convert(ViewHolder holder, String path, int position) {
                if (path.equals("add")) {
                    holder.setVisible(R.id.cancel_img, false);
                    Glide.with(mActivity).load(R.mipmap.add).into((ImageView) holder.getView(R.id.title_right_img));
                } else {
                    holder.setVisible(R.id.cancel_img, true);
                    Glide.with(mActivity).load(path).into((ImageView) holder.getView(R.id.title_right_img));
                }
                holder.setOnClickListener(R.id.cancel_img, view -> {
                    planting_imgList.remove(position);
                    imgadapter.notifyDataSetChanged();
                });

                holder.setOnClickListener(R.id.title_right_img, view -> {
                    if (path.equals("add")) {
                        if (planting_imgList.size() >= 6) {
                            showToast("最多能上传5张图");
                        } else {
                            showPhoneDialog();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getQingJia();

    }

    private void getQingJia() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "LeaveType");
        showLoading();
        HttpUtil.getEnum(hashMap).execute(new JsonCallback<Result<List<LeaveType>>>() {
            @Override
            public void onSuccess(Result<List<LeaveType>> result, Call call, Response response) {
                closeLoading();
                if (result.data != null && result.data.size() > 0) {
                    leaveTypes.clear();
                    leaveTypes.addAll(result.data);
                    protypelist.clear();
                    for (int i = 0; i < leaveTypes.size(); i++) {
                        protypelist.add(leaveTypes.get(i).label);
                    }

                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                showToast(e.getMessage());
                closeLoading();
            }
        });

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xuanze_shijian_start:
                initTimerPicker(1);
                mTimerPicker.show("yyyy-MM-dd HH:mm");
                break;
            case R.id.xuanze_shijian_end:
                initTimerPicker(2);
                mTimerPicker.show("yyyy-MM-dd HH:mm");
                break;
            case R.id.spr_img:// 添加审批人
                bundle = new Bundle();
                bundle.putString("type", "1");
                openActivity(SelectionDepartmentActivity.class, bundle);
                break;

            case R.id.csr_img:// 添加抄送人
                bundle = new Bundle();
                bundle.putString("type", "2");
                openActivity(SelectionDepartmentActivity.class, bundle);
                break;
            case R.id.save_btn:
                if (isCheck()) {
                    subMitData();
                }
                break;
            case R.id.xuanze_qingjialeixing:// 选择请假类型
                setOnClick();
                break;
            case R.id.qingjia_tupian:// 选择图片
                showPhoneDialog();
                break;
        }
    }


    /**
     * 请假类型
     */
    private void setOnClick() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            mBinding.xuanzeQingjialeixing.setText(protypelist.get(options1));
            leaveType = leaveTypes.get(options1).value;
        })
                .setTitleText("请选择请假类型")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(protypelist, null, null);
        pvOptions.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) { // //收付款明细
        } else if (resultCode == 300) { // 审核人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            auditorList.clear();
            auditorList.addAll(list);
            auditorListAdapter.notifyDataSetChanged();
        } else if (resultCode == 400) { // 抄送人
            List<GetTreeEnt> list = (List<GetTreeEnt>) data.getSerializableExtra("data");
            copierList.clear();
            copierList.addAll(list);
            copierListAdapter.notifyDataSetChanged();
        } else if (resultCode == 500) { // 获取收款方信息
        } else if (resultCode == 600) {// 获取付款方信息
        } else if (resultCode == 700) {//开收票明细

        }

    }


    private HashMap<String, Object> getPer() {
        HashMap<String, Object> hashMap = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < planting_imgList.size(); i++) {
            sb.append(Tools.imageToBase64(planting_imgList.get(i)));
//                    sb.append("data:image/" + coustList.get(ClickPosition).stringList.get(i).substring(coustList.get(ClickPosition).stringList.get(i).lastIndexOf(".") + 1) + ";base64," + Tools.imageToBase64(coustList.get(ClickPosition).stringList.get(i)));
            sb.append("|");
        }
        if (sb.toString().length() > 0) {
            sb.deleteCharAt(sb.toString().length() - 1);
        }

        hashMap.put("userId", getUserId());
//        hashMap.put("department", department);
        hashMap.put("leaveType", leaveType);
        hashMap.put("startDate", mBinding.xuanzeShijianStart.getText().toString());
        hashMap.put("endDate", mBinding.xuanzeShijianEnd.getText().toString());
        hashMap.put("ofTime", mBinding.shuruShichang.getText().toString());
        hashMap.put("cause", mBinding.qingjiaShiyou.getText().toString());
//        hashMap.put("fileList", fileList);
        hashMap.put("auditor", auditorList);
        hashMap.put("copier", copierList);
        hashMap.put("photo", sb.toString());
        return hashMap;
    }


    private void subMitData() {
        showLoading();
        HttpUtil.leave_Submit(getPer()).execute(new JsonCallback<Result<String>>() {
            @Override
            public void onSuccess(Result<String> result, Call call, Response response) {
                closeLoading();
                msgDialogBuilder("请假申请提交成功", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                }).setCancelable(false).create().show();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                closeLoading();
                showToast(e.getMessage());
            }
        });

    }


    private boolean isCheck() {
        if (TextUtils.isEmpty(mBinding.xuanzeQingjialeixing.getText().toString())) {
            showToast("请选择请假类型");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xuanzeShijianStart.getText().toString())) {
            showToast("请选择开始时间");
            return false;
        } else if (TextUtils.isEmpty(mBinding.xuanzeShijianEnd.getText().toString())) {
            showToast("请选择结束时间");
            return false;
        } else if (TextUtils.isEmpty(mBinding.shuruShichang.getText().toString())) {
            showToast("请输入时长");
            return false;
        } else if (TextUtils.isEmpty(mBinding.qingjiaShiyou.getText().toString())) {
            showToast("请输请假事由");
            return false;
        } else if (auditorList.size() == 0) {
            showToast("请选择审批人");
            return false;
        } else if (copierList.size() == 0) {
            showToast("请选择抄送人");
            return false;
        } else {
            return true;
        }

    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mBinding.xuanzeShijianStart.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mBinding.xuanzeShijianStart.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker(int type) {

        String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2030-12-31 00:00";
        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                if (type == 1) {
                    begintime = timestamp;
                    mBinding.xuanzeShijianStart.setText(DateFormatUtils.long2Str(timestamp, true));
                } else {
                    if (timestamp < begintime) {
                        showToast("结束时间不能小于开始时间");
                    } else {
                        mBinding.xuanzeShijianEnd.setText(DateFormatUtils.long2Str(timestamp, true));
                    }
                }
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(false);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    //================获取照片相关=============================================

    private void showPhoneDialog() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        final String avatar = formatter.format(curDate);

        final String[] paytitle = {"点击拍照", "相册获取"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mActivity,
                paytitle, null);
        dialog.title("请选择照片")//
                .titleTextSize_SP(18.5f)//
                .show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                File file = new File(Environment.getExternalStorageDirectory(), "/hmapp/" + avatar + ".jpg");
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                Uri imageUri = Uri.fromFile(file);// 获取相册中的照片
                if (paytitle[position].contains("拍照")) {
                    CompressConfig config = new CompressConfig.Builder()
                            .create();
                    getTakePhoto().onEnableCompress(config, true);
//                    getTakePhoto().onPickFromCaptureWithCrop(imageUri, new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
                    getTakePhoto().onPickFromCapture(imageUri);
                } else { // 相册获取
//                    getTakePhoto().onPickFromGalleryWithCrop(imageUri, new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
                    CompressConfig config = new CompressConfig.Builder()
                            .create();
                    getTakePhoto().onEnableCompress(config, true);
                    getTakePhoto().onPickMultiple(5);
                }
                dialog.dismiss();
            }
        });
    }




    //======获取照片相关==================================
    private InvokeParam invokeParam;
    private TakePhoto takePhoto;

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    /**
     * 返回获取到的照片
     *
     * @param result
     */
    @Override
    public void takeSuccess(final TResult result) {
        if (BuildConfig.DEBUG) {
            Log.d("PersonalCenterFragment", "文件大小：" + FileSizeUtil.getAutoFileOrFilesSize(result.getImage().getPath()));
        }
        if (result.getImages() != null) {
            for (int i = 0; i < result.getImages().size(); i++) {
                planting_imgList.add(result.getImages().get(i).getPath());
            }
            imgadapter.notifyDataSetChanged();

        }

    }

    /**
     * 获取失败
     *
     * @param result
     * @param msg
     */
    @Override
    public void takeFail(TResult result, String msg) {
        showToast(msg);
    }

    /**
     * 取消获取照片
     */
    @Override
    public void takeCancel() {
        showToast("您已取消获取照片");
    }

    // ===========获取图片相关结束===================================


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mDatePicker.onDestroy();
    }

}
