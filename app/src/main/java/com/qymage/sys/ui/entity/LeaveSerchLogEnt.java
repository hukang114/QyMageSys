package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * Created by admin on 2019/9/9.
 * 请假记录列表
 */

public class LeaveSerchLogEnt {


    /**
     * sendList : [{"isCheck":true,"name":"运营中心","id":"1099100700000000024","parentId":"1099100700000000020"},{"isCheck":true,"name":"财务中心","id":"1099100700000000025","parentId":"1099100700000000020"}]
     * processList : [{"isCheck":true,"name":"运营中心","id":"1099100700000000024","parentId":"1099100700000000020"},{"isCheck":true,"name":"财务中心","id":"1099100700000000025","parentId":"1099100700000000020"}]
     * leaveType : 1
     * endDate : 2019-12-09 00:59
     * cause : 有事吗
     * ofTime : 21
     * department : 技术部
     * userId : app
     * startDate : 2019-09-09 00:59
     * fileList : [{"fileName":"123","filePath":"www.baidu.com"}]
     */
    public List<SendListEntity> sendList;
    public List<ProcessListEntity> processList;
    public String leaveType;
    public String endDate;
    public String cause;
    public String ofTime;
    public String department;
    public String userId;
    public String startDate;
    public List<FileListEntity> fileList;

    public class SendListEntity {
        /**
         * isCheck : true
         * name : 运营中心
         * id : 1099100700000000024
         * parentId : 1099100700000000020
         */
        public boolean isCheck;
        public String name;
        public String id;
        public String parentId;
    }

    public class ProcessListEntity {
        /**
         * isCheck : true
         * name : 运营中心
         * id : 1099100700000000024
         * parentId : 1099100700000000020
         */
        public boolean isCheck;
        public String name;
        public String id;
        public String parentId;
    }

    public class FileListEntity {
        /**
         * fileName : 123
         * filePath : www.baidu.com
         */
        public String fileName;
        public String filePath;
    }
}
