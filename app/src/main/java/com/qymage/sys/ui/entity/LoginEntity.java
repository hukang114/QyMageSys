package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test;

import java.io.Serializable;
import java.util.List;

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


       /* processAppBtnVo:[{//权限
            Id   String   id
            btnLable String  模块名称
            btnType String 模块标识
            processDefId String 流程定义ID
            processKey  String 流程key
            processName String 流程名称
            processVersion String 流程版本
            createdDate String  创建日期

        }]*/
        /**
         * 功能模块集合
         */
        public List<ProcessAppBtnVoEntity> processAppBtnVo;

        public class ProcessAppBtnVoEntity {
            /**
             * createdDate : 1568278331000
             * processName : log_approve_process
             * processKey : log_approve_process
             * id : 13
             * updatedDate : 1568278331000
             * btnType : 15
             * processDefId : log_approve_process:1:102585
             * processVersion : 1
             * btnLable : 日报
             */
            public String createdDate;
            public String processName;
            public String processKey;
            public String id;
            public String updatedDate;
            public String btnType;
            public String processDefId;
            public String processVersion;
            public String btnLable;
        }

    }
}
