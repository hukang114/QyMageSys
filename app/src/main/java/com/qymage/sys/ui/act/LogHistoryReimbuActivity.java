package com.qymage.sys.ui.act;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
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
import com.qymage.sys.BuildConfig;
import com.qymage.sys.R;
import com.qymage.sys.common.base.BBActivity;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.http.HttpUtil;
import com.qymage.sys.common.http.LogUtils;
import com.qymage.sys.common.tools.FileSizeUtil;
import com.qymage.sys.common.tools.Tools;
import com.qymage.sys.databinding.ActivityLoghistoryreimbuBinding;
import com.qymage.sys.databinding.ActivityReimbursementBinding;
import com.qymage.sys.ui.entity.ContractTypeEnt;
import com.qymage.sys.ui.entity.DayLogListEnt;
import com.qymage.sys.ui.entity.DayWeekMonthDet;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.leo.click.SingleClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 日志里面的报销明细
 */
public class LogHistoryReimbuActivity extends BBActivity<ActivityLoghistoryreimbuBinding> {


    List<DayWeekMonthDet.LogListBean.SubMoneyListBean> coustList;
    ReimbntAdapter reimbntAdapter;
    CommonAdapter<String> imgadapter;//报销图片集合
    Gson gson = new Gson();
    private Intent mIntent;
    private int select_Position;// 传递过来的合同明细位置
    Bundle bundle;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_loghistoryreimbu;
    }

    @Override
    protected void initView() {
        super.initView();
        mBinding.metitle.setlImgClick(v -> finish());
        mIntent = getIntent();
        coustList = (List<DayWeekMonthDet.LogListBean.SubMoneyListBean>) mIntent.getSerializableExtra("data");
        if (coustList == null) {
            return;
        }
        LogUtils.e("list===" + coustList.size());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mBinding.recycPic.setLayoutManager(linearLayoutManager);
        reimbntAdapter = new ReimbntAdapter(R.layout.item_loghistor_list_reimbursement, coustList);
        mBinding.recyclerview.setAdapter(reimbntAdapter);

    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 报销适配器
     */
    class ReimbntAdapter extends BaseQuickAdapter<DayWeekMonthDet.LogListBean.SubMoneyListBean, BaseViewHolder> {

        List<DayWeekMonthDet.LogListBean.SubMoneyListBean> data;

        public ReimbntAdapter(int layoutResId, @Nullable List<DayWeekMonthDet.LogListBean.SubMoneyListBean> data) {
            super(layoutResId, data);
            this.data = data;
        }

        @Override
        protected void convert(BaseViewHolder helper, DayWeekMonthDet.LogListBean.SubMoneyListBean item) {

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

            RecyclerView recyc_pic = helper.getView(R.id.recyc_pic);
            recyc_pic.setTag(R.id.recyc_pic, "1");
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyc_pic.setLayoutManager(linearLayoutManager);

            List<String> stringList = new ArrayList<>();

            if (item.photo != null && item.photo.contains("|")) { // 多图
                List<String> result = Arrays.asList(item.photo.split("\\|"));
                stringList.clear();
                stringList.addAll(result);
            } else {// 单图
                stringList.clear();
                stringList.add(item.photo);
            }
            recyc_pic.setAdapter(imgadapter = new CommonAdapter<String>(mContext, R.layout.item_evorder_imglist, stringList) {
                @Override
                protected void convert(ViewHolder holder, String path, int position) {
                    holder.setVisible(R.id.cancel_img, false);
                  /*  if (path.equals("add")) {
                        Glide.with(mActivity).load(R.mipmap.add).into((ImageView) holder.getView(R.id.title_right_img));
                    } else {
                    }*/
                    Glide.with(mActivity).load(path).into((ImageView) holder.getView(R.id.title_right_img));
                }
            });

        }

    }


}

