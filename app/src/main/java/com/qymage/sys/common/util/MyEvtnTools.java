package com.qymage.sys.common.util;

/**
 * Created by hukang on 2016/7/1 0001 9:41.
 * Email: 358144010@qq.com
 */
public class MyEvtnTools {

    public  int tempStatus=-1;
    public Object ob=null;
    public MyEvtnTools(int status) {
        this.tempStatus=status;
    }

    public MyEvtnTools(int status, Object ob) {
        this.tempStatus=status;
        this.ob=ob;
    }


}
