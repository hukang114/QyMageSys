package com.qymage.sys.ui.entity;

import java.io.Serializable;

import cn.leo.click.SingleClick;

/**
 * Created by admin on 2019/9/2.
 * 登录返回数据
 */

public class LoginEntity implements Serializable {


    public String token;
    public InfoBean info;

    public static class InfoBean implements Serializable {
        /**
         * userId : 1099101000000000003
         * userAccount : app
         * userName : 冯秋
         * partId : 1099100700000000033
         * userPost : null
         * deptId : null
         * deptName : null
         */

        public String userId;
        public String userAccount;
        public String userName;
        public String partId;
        public String userPost;
        public String deptId;
        public String deptName;
        public String userCode;
        public int sex;//性别 1男  0女
        public String portrait;// 头像
        public String birthday;
        public String moblie;

    }
}
