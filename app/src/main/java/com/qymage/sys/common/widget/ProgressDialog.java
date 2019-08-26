package com.qymage.sys.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qymage.sys.R;


/**
 * 类名：ProgressDialog
 * 类描述：网络加载框
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2018/12/3 15:33
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ProgressDialog {

    private ProgressDialog myProgressDialog;
    private Context mContext;
    private Dialog mDialog;
    private LinearLayout mLinearLayout;

    public interface ProgressOnKeyListener {
        void OnKey(ProgressDialog myProgressDialog);
    }

    public ProgressDialog(Context context) {
        this.mContext = context;
        myProgressDialog = this;
        initView();
    }

    private void initView() {
        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setGravity(Gravity.CENTER);
        mLinearLayout.setBackgroundColor(0);

        mDialog = new Dialog(mContext, R.style.progress_dialog_style);

        mDialog.setCanceledOnTouchOutside(true);//点击其他区域dialog消失
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });

        XFCircleImageView pView = new XFCircleImageView(mContext);
        pView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        pView.setImageResource(R.drawable.item_common_loadding);
//		pView.setBackgroundResource(R.drawable.common_loadding);
        mLinearLayout.addView(pView);

        LinearLayout.LayoutParams pViewParams = (LinearLayout.LayoutParams) pView.getLayoutParams();
        pViewParams.width = 150;
        pViewParams.height = 150;
//		pViewParams.gravity = Gravity.CENTER;
        pViewParams.bottomMargin = 16;
        pView.setLayoutParams(pViewParams);

        TextView pText = new TextView(mContext);
        pText.setText("加载中...");
        pText.setTextColor(Color.rgb(211, 211, 211));
        pText.setTextSize(14);
        mLinearLayout.addView(pText);

        LinearLayout.LayoutParams pTextParams = (LinearLayout.LayoutParams) pText.getLayoutParams();
        pTextParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        pTextParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        pTextParams.gravity = Gravity.CENTER;
        pText.setLayoutParams(pTextParams);

        AnimationDrawable animationDrawable = (AnimationDrawable) pView.getDrawable();
        animationDrawable.start();
    }

    public void show() {
        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
//		mLinearLayout.setLayoutParams(lineLayoutParams);
        mDialog.setContentView(mLinearLayout, lineLayoutParams);
        mDialog.show();
    }

    public void no_cance_show() {
        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
//		mLinearLayout.setLayoutParams(lineLayoutParams);
        mDialog.setContentView(mLinearLayout, lineLayoutParams);
        mDialog.setCancelable(false);
        mDialog.show();
    }


    public void dismiss() {
        if (mDialog == null || !mDialog.isShowing()) {
            return;
        }else{
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

    }

    public void setDialogNull(){
        mDialog=null;
    }

    public static ProgressDialog getInstance(Context context) {
        return new ProgressDialog(context);
    }

    public void setOnKeyListener(final ProgressOnKeyListener progressOnKeyListener) {
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                progressOnKeyListener.OnKey(myProgressDialog);
                return true;
            }
        });
    }
}
