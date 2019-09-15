package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * Created by admin on 2019/9/16.
 * 考情排行
 */

public class RankingKaoQing {


    /**
     * avgHours : 20
     * month : 201-08
     * rankList : [{"hours":"工时","cindex":"指数","ranking":"备注","userName":"姓名","userId":""}]
     */
    public String avgHours;
    public String month;
    public List<RankListEntity> rankList;

    public class RankListEntity {
        /**
         * hours : 工时
         * cindex : 指数
         * ranking : 备注
         * userName : 姓名
         * userId :
         */
        public String hours;
        public String cindex;
        public String ranking;
        public String userName;
        public String userId;
    }
}
