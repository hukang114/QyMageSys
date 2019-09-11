package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2019/9/8.
 * <p>
 * 立项记录详情
 */

public class ProjectApprovaLoglDetEnt {


    public DataInfo dataInfo;

    public static class DataInfo implements Serializable {

        public String id;
        public String projectType;
        public String projectNo;
        public String projectName;
        public String persion;
        public String amount;
        public String date;
        public String introduction;
        public int actStatus;
        public String number;
        public String processInstanceId;

    }


}
