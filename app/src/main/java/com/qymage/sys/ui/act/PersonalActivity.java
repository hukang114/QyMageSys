package com.qymage.sys.ui.act;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.flyco.dialog.widget.ActionSheetDialog;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.qymage.sys.BuildConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.tools.FileSizeUtil;
import com.qymage.sys.common.util.PicasooUtil;
import com.qymage.sys.databinding.ActivityPersonalBinding;
import com.qymage.sys.ui.entity.LoginEntity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.leo.click.SingleClick;

/**
 * 个人中心
 */
public class PersonalActivity extends BBActivity<ActivityPersonalBinding> implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {


    private String[] sexArry = new String[]{"保密", "女", "男"};// 性别选择
    private int checkedItem = 0;

    LoginEntity.InfoBean infoBean;
    private Intent mIntent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mBinding.sexEt.setOnClickListener(this);
        mBinding.birthdayEt.setOnClickListener(this);
        mBinding.avatar.setOnClickListener(this);
        mIntent = getIntent();
        infoBean = (LoginEntity.InfoBean) mIntent.getSerializableExtra("data");
        if (infoBean == null) {
            return;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mBinding.phoneEt.setText(infoBean.moblie);
        mBinding.jonNumEt.setText(infoBean.userAccount);
        mBinding.usernameEt.setText(infoBean.userName);
        if (infoBean.sex == 1) {
            mBinding.sexEt.setText("男");
        } else {
            mBinding.sexEt.setText("女");
        }
        mBinding.stationEt.setText(infoBean.userPost);
        mBinding.birthdayEt.setText(infoBean.birthday);
        PicasooUtil.setImageResource(infoBean.portrait, mBinding.avatar, 360);

    }


    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sex_et:// 性别
//                showSexChooseDialog();
                break;
            case R.id.birthday_et:// 生日
//                showDateDialog();
                break;
            case R.id.avatar:// 头像选择
//                showPhoneDialog();
                break;

        }

    }

    /**
     * 选择生日
     */
    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String desc = String.format("%d-%d-%d", year, month + 1, dayOfMonth);
            mBinding.birthdayEt.setText(desc);
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**
     * 选择性别
     */
    private void showSexChooseDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);//实例化builder
        //builder.setIcon(R.mipmap.ic_launcher);//设置图标
        builder.setTitle("请选择您的性别");//设置标题
        //设置单选列表
        builder.setSingleChoiceItems(sexArry, checkedItem, (dialog, which) -> {
            checkedItem = which;
        });
        //创建对话框
        AlertDialog dialog = builder.create();
        //设置确定按钮
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", (dialog1, which) -> {
            dialog1.dismiss();
            mBinding.sexEt.setText(sexArry[checkedItem]);
        });
        dialog.show();//显示对话框

    }


    //================上传用户头像相关=============================================

    private void showPhoneDialog() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        final String avatar = formatter.format(curDate);

        final String[] paytitle = {"点击拍照", "相册获取"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this,
                paytitle, null);
        dialog.title("请选择照片")//
                .titleTextSize_SP(18.5f)//
                .show();
        dialog.setOnOperItemClickL((parent, view, position, id) -> {
            File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + avatar + ".jpg");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);// 获取相册中的照片
            if (paytitle[position].contains("拍照")) {
                getTakePhoto().onPickFromCaptureWithCrop(imageUri, new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
            } else { // 相册获取
                getTakePhoto().onPickFromGalleryWithCrop(imageUri, new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create());
            }
            dialog.dismiss();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {// 刷新信息
            initData();
        }
    }

    /**
     * 返回获取到的照片
     *
     * @param result
     */
    @Override
    public void takeSuccess(final TResult result) {

        result.getImages().get(0).getPath();

        if (BuildConfig.DEBUG)
            Log.d("PersonalCenterFragment", "文件大小：" + FileSizeUtil.getAutoFileOrFilesSize(result.getImage().getPath()));
        // 上传用户头像到服务器
        if (BuildConfig.DEBUG) Log.d("PersonalCenterFragment", result.getImage().getPath());
        PicasooUtil.displayFromSDCard(result.getImage().getPath(), mBinding.avatar, 360);
        // upUserData(2, result.getImage().getPath());

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


}
