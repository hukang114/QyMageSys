package com.qymage.sys.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.google.gson.Gson;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.qymage.sys.BuildConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.tools.FileSizeUtil;
import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.databinding.ActivityReimbursementBinding;
import com.qymage.sys.ui.adapter.ReimbntAdapter;
import com.qymage.sys.ui.entity.ContractTypeEnt;
import com.qymage.sys.ui.entity.DayLogListEnt;
import com.qymage.sys.ui.entity.ReimburEntCoust;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 报销
 */
public class ReimbursementActivity extends BBActivity<ActivityReimbursementBinding> implements BaseQuickAdapter.OnItemChildClickListener, View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {


    List<DayLogListEnt.SubMoneyListEntity> coustList;
    ReimbntAdapter reimbntAdapter;
    CommonAdapter<String> imgadapter;//报销图片集合
    private ArrayList<TImage> logoList;
    private List<String> planting_imgList = new ArrayList<>();// 最后需要上传的图片集合
    List<String> dataResult = new ArrayList<>();// 类型
    Gson gson = new Gson();
    List<String> stringList;
    private int ClickPosition = 0;
    private Intent mIntent;
    private int select_Position;// 传递过来的合同明细位置
    Bundle bundle;
    List<ContractTypeEnt> typeEnts = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reimbursement;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mIntent = getIntent();
        coustList = (List<DayLogListEnt.SubMoneyListEntity>) mIntent.getSerializableExtra("data");
        select_Position = mIntent.getIntExtra("select_Position", 0);
        if (coustList == null) {
            return;
        }
        LogUtils.e("list===" + coustList.size());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mBinding.recycPic.setLayoutManager(linearLayoutManager);
        reimbntAdapter = new ReimbntAdapter(R.layout.item_list_reimbursement, coustList);
        mBinding.recyclerview.setAdapter(reimbntAdapter);
        reimbntAdapter.setOnItemChildClickListener(this);
        mBinding.addNewBtn.setOnClickListener(this);
        mBinding.saveBtn.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        super.initData();
        // 默认添加一条数据
        if (coustList.size() == 1 && coustList.get(0).stringList.size() == 0) {
//            stringList = new ArrayList<>();
//            stringList.add("add");
//            coustList.add(new DayLogListEnt.SubMoneyListEntity("", "", "", "", stringList));
            coustList.get(0).stringList.add("add");
            reimbntAdapter.notifyDataSetChanged();
        }
        getExpenseType();

    }

    /**
     * 获取报销类型
     */
    private void getExpenseType() {
        showLoading();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "ExpenseType");
        HttpUtil.getEnum(hashMap).execute(new JsonCallback<Result<List<ContractTypeEnt>>>() {
            @Override
            public void onSuccess(Result<List<ContractTypeEnt>> result, Call call, Response response) {
                closeLoading();
                typeEnts.clear();
                dataResult.clear();
                if (result.data != null && result.data.size() > 0) {
                    typeEnts.addAll(result.data);
                    for (int i = 0; i < typeEnts.size(); i++) {
                        dataResult.add(typeEnts.get(i).label);
                    }
                } else {
                    showToast(result.message);
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


    /**
     * @param adapter
     * @param view
     * @param position
     */
    @SingleClick(2000)
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.delete_btn: // 移除
                coustList.remove(position);
                reimbntAdapter.notifyDataSetChanged();
                break;
            case R.id.baoxiao_cate_txt:// 选择报销类型
                setMemberLevel(position);
                break;
        }
    }

    @SingleClick(2000)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_new_btn:// 新增一条
                stringList = new ArrayList<>();
                stringList.add("add");
                coustList.add(new DayLogListEnt.SubMoneyListEntity("", "", "", "", stringList, ""));
                reimbntAdapter.notifyDataSetChanged();
                break;
            case R.id.save_btn:
                if (coustList.get(0).amount.equals("")) {
                    showToast("请填写报销明细内容");
                } else {
                    bundle = new Bundle();
                    bundle.putSerializable("data", (Serializable) coustList);
                    bundle.putInt("select_Position", select_Position);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(800, intent);
                    finish();
                }
                break;
        }

    }


    private void setMemberLevel(int position) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            coustList.get(position).type = typeEnts.get(options1).value;
            coustList.get(position).typeName = dataResult.get(options1);
            reimbntAdapter.notifyDataSetChanged();
        })
                .setTitleText("请选择报销类型")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(18)
                .setOutSideCancelable(false)//点击外部dismiss default true
                .setContentTextSize(16)//滚轮文字大小
                .setSubCalSize(16)//确定和取消文字大小
                .setLineSpacingMultiplier(2.4f)
                .build();
        // 三级选择器
        pvOptions.setPicker(dataResult, null, null);
        pvOptions.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);

    }


    /**
     * 报销适配器
     */
    class ReimbntAdapter extends BaseQuickAdapter<DayLogListEnt.SubMoneyListEntity, BaseViewHolder> {

        List<DayLogListEnt.SubMoneyListEntity> data;


        public ReimbntAdapter(int layoutResId, @Nullable List<DayLogListEnt.SubMoneyListEntity> data) {
            super(layoutResId, data);
            this.data = data;
        }

        @Override
        protected void convert(BaseViewHolder helper, DayLogListEnt.SubMoneyListEntity item) {

            TextView delete_btn = helper.getView(R.id.delete_btn);

            if (data.size() > 1) {
                delete_btn.setVisibility(View.VISIBLE);
            } else {
                delete_btn.setVisibility(View.GONE);
            }
            helper.setText(R.id.baoxiao_money_edt, item.amount)
                    .setText(R.id.baoxiao_cate_txt, item.typeName)
                    .setText(R.id.baoxiao_content, item.detailed)
                    .setText(R.id.baoxiao_id_tv, "报销明细(" + (helper.getAdapterPosition() + 1) + ")");
            helper.addOnClickListener(R.id.delete_btn).addOnClickListener(R.id.baoxiao_cate_txt);

            EditText baoxiao_money_edt = helper.getView(R.id.baoxiao_money_edt);
            EditText baoxiao_content = helper.getView(R.id.baoxiao_content);

            baoxiao_money_edt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString() != null && !s.equals("")) {
                        coustList.get(helper.getAdapterPosition()).amount = s.toString();
                    } else {
                        coustList.get(helper.getAdapterPosition()).amount = "";
                    }
                }
            });

            baoxiao_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString() != null && !s.equals("")) {
                        coustList.get(helper.getAdapterPosition()).detailed = s.toString();
                    } else {
                        coustList.get(helper.getAdapterPosition()).detailed = "";
                    }
                }
            });
            RecyclerView recyc_pic = helper.getView(R.id.recyc_pic);
            recyc_pic.setTag(R.id.recyc_pic, "1");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyc_pic.setLayoutManager(linearLayoutManager);
            recyc_pic.setAdapter(imgadapter = new CommonAdapter<String>(mContext, R.layout.item_evorder_imglist, item.stringList) {
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
                        item.stringList.remove(position);
                        reimbntAdapter.notifyDataSetChanged();
                    });

                    holder.setOnClickListener(R.id.title_right_img, view -> {
                        if (path.equals("add")) {
                            if (item.stringList.size() >= 6) {
                                showToast("最多能上传5张图");
                            } else {
                                ClickPosition = helper.getAdapterPosition();
                                showPhoneDialog();
                            }
                        }
                    });
                }
            });

        }

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
            logoList = result.getImages();
            if (null != logoList && logoList.size() > 0) {

                planting_imgList.clear();
                List<String> strings = new ArrayList<>();
                strings.addAll(coustList.get(ClickPosition).stringList);
                strings.remove(strings.size() - 1);
                coustList.get(ClickPosition).stringList.clear();
                for (int j = 0; j < strings.size(); j++) {
                    coustList.get(ClickPosition).stringList.add(strings.get(j));
                }
                for (int i = 0; i < logoList.size(); i++) {
                    if (coustList.get(ClickPosition).stringList.size() <= 5) {
                        coustList.get(ClickPosition).stringList.add(logoList.get(i).getPath());
                    } else {
                        break;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < coustList.get(ClickPosition).stringList.size(); i++) {
                    sb.append(Tools.imageToBase64(coustList.get(ClickPosition).stringList.get(i)));
//                    sb.append("data:image/" + coustList.get(ClickPosition).stringList.get(i).substring(coustList.get(ClickPosition).stringList.get(i).lastIndexOf(".") + 1) + ";base64," + Tools.imageToBase64(coustList.get(ClickPosition).stringList.get(i)));
                    sb.append("|");
                }
                if (sb.toString().length() > 0) {
                    sb.deleteCharAt(sb.toString().length() - 1);
                }
                // 将转换好的base64图片格式赋值给对应的报销明细photo字段
                coustList.get(ClickPosition).photo = sb.toString();
                planting_imgList.addAll(coustList.get(ClickPosition).stringList);
                coustList.get(ClickPosition).stringList.add("add");
                reimbntAdapter.notifyDataSetChanged();
            }
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


}

