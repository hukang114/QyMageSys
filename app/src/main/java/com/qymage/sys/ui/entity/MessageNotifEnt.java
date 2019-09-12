package com.qymage.sys.ui.entity;

import com.qymage.sys.ui.Test2;

import java.util.List;

/**
 * Created by admin on 2019/8/25.
 * 消息通知类型的数据模型
 */

public class MessageNotifEnt {


    public String Id;
    public String noticeTitle;
    public String noticeDate;
    public String noticeType;
    public String noticeContent;
    public String photo;

    public List<FileListBean> fileList;

    public static class FileListBean {
        /**
         * fileName : 12
         * filePath : 12
         */
        public String fileName;
        public String filePath;
    }

}
