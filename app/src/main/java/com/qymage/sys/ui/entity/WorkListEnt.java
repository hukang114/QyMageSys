package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * Created by admin on 2019/8/24.
 * 工作的数据类
 */

public class WorkListEnt {


    public String tilte;

    public List<DataBaen> baenList;


    public WorkListEnt(String tilte, List<DataBaen> baenList) {
        this.tilte = tilte;
        this.baenList = baenList;
    }

    public static class DataBaen {
        public String keyname;
        public String child_name;
        public int child_ioc;

        public DataBaen(String keyname, String child_name, int child_ioc) {
            this.keyname = keyname;
            this.child_name = child_name;
            this.child_ioc = child_ioc;
        }
    }


}
