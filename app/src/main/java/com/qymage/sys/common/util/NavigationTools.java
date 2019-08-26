package com.qymage.sys.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.qymage.sys.common.tools.Tools;

import java.net.URISyntaxException;

/**
 * Created by HK on 2018/6/20.
 */

public class NavigationTools {

    private Context mContext;
    private double lat = 0.0;
    private double lng = 0.0;
    private String address;

    public NavigationTools(Context context, double lat, double lng, String address) {
        mContext = context;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
    }

    public void judgeNavigation() {
        if (lat > 0 && lng > 0) {
            if (Tools.isAvilible(mContext, "com.baidu.BaiduMap")) {
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("baidumap://map/marker?location=" + lat + "," + lng + "&title=商家位置&content=" + address + "&traffic=on"));
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Log.e("intent", e.getMessage());
                }
            } else if (Tools.isAvilible(mContext, "com.autonavi.minimap")) {
                try {
                    Intent intent = Intent.getIntent("androidamap://route?sourceApplication=慧医&dname=商家位置&dlat=" + lat + "&dlon=" + lng + "&dev=0" + "&m=0&t=0&showType=0");
                    mContext.startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext,"请先安装百度地图或者高德地图！",Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(mContext,"还未添加店铺地址",Toast.LENGTH_SHORT).show();
        }
    }


}
