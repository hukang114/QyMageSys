package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/9/6.
 * 上传文件的模型
 */

public class FileListEnt {

    public String fileName;
    public String filePath;

    public FileListEnt(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
