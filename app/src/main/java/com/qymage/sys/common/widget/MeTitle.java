package com.qymage.sys.common.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qymage.sys.R;


/**
 * Created by ee on 2016/9/7.
 */
public class MeTitle extends FrameLayout {

    private final Context mContext;
    ImageView mTitleLeftImg, mTitleRightImg, mImageView2;
    TextView mTitleLeftTxt;
    public TextView mTitleRightTxt;
    RelativeLayout rl;
    TextView ctitle;
    View pub_div_view;


    public MeTitle(Context context) {
        this(context, null);
    }

    public MeTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.me_title, this, false);
        mTitleLeftImg = (ImageView) rootView.findViewById(R.id.title_left_img);
        mTitleLeftTxt = (TextView) rootView.findViewById(R.id.title_left_txt);
        mTitleRightTxt = (TextView) rootView.findViewById(R.id.title_right_txt);
        mTitleRightImg = (ImageView) rootView.findViewById(R.id.title_right_img);
        mImageView2 = (ImageView) rootView.findViewById(R.id.imageview2);
        rl = (RelativeLayout) rootView.findViewById(R.id.tool_bar);
        ctitle = (TextView) rootView.findViewById(R.id.ctitle);
        pub_div_view = rootView.findViewById(R.id.pub_div_view);
        addView(rootView);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MeTitle);
        setlImg(typedArray.getDrawable(R.styleable.MeTitle_lImg));
        setlTxt(typedArray.getString(R.styleable.MeTitle_lTxt));
        setrTxt(typedArray.getString(R.styleable.MeTitle_rTxt));
        setcTxt(typedArray.getString(R.styleable.MeTitle_cTxt));
        setrImg(typedArray.getDrawable(R.styleable.MeTitle_rImg));
        showView(typedArray.getBoolean(R.styleable.MeTitle_showView, true));
//        setBg(typedArray.getColor(R.styleable.MeTitle_titleBg, ContextCompat.getColor(mContext,R.color.colorPrimary)));
//        setBg(typedArray.getResourceId(R.styleable.MeTitle_titleBg, -1));
        setcTxtColor(typedArray.getColor(R.styleable.MeTitle_cTxtcolor, ContextCompat.getColor(mContext, R.color.black)));
        setRTxtColor(typedArray.getColor(R.styleable.MeTitle_setRTxtColor, ContextCompat.getColor(mContext, R.color.black)));

    }

    public void setBg(int resourceId) {
        rl.setBackgroundColor(ContextCompat.getColor(mContext, resourceId));

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setBgdr(Drawable drawable) {
        rl.setBackground(drawable);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setlImg(Drawable res) {
        mTitleLeftImg.setBackground(res);
        mTitleLeftImg.setVisibility(VISIBLE);
//        mTitleLeftImg.setOnClickListener(view -> ((Activity) mContext).finish());
    }


    public void setlTxt(String leftTxt) {
        mTitleLeftTxt.setText(leftTxt);
        mTitleLeftTxt.setVisibility(VISIBLE);
    }

    public void setrTxt(String rTxt) {
        mTitleRightTxt.setText(rTxt);
        mTitleRightTxt.setVisibility(VISIBLE);
    }

    public void setcTxt(String rTxt) {
        ctitle.setText(rTxt);
    }

    public void setcTxtColor(int color) {
        ctitle.setTextColor(color);
    }

    public void setRTxtColor(int color) {
        mTitleRightTxt.setTextColor(color);
    }

    public void setrImg(Drawable res) {
        mTitleRightImg.setImageDrawable(res);
    }

    public void showView(boolean showView) {
        if (showView) {
            pub_div_view.setVisibility(VISIBLE);
        } else {
            pub_div_view.setVisibility(GONE);
        }
    }

    public void setlImgClick(OnClickListener v) {
        mTitleLeftImg.setOnClickListener(v);
    }

    public void setlTxtClick(OnClickListener v) {
        mTitleLeftImg.setOnClickListener(v);
    }

    public void setrImgClick(OnClickListener v) {
        mTitleRightImg.setOnClickListener(v);
    }

    public void setrTxtClick(OnClickListener v) {
        mTitleRightTxt.setOnClickListener(v);
    }


}
