package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：获取月报提交页面的数据展示
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1011:40
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class YesQueryEny implements Serializable {


    /**
     * weekPalyList : [{"weekPalyList":"周工作计划"}]
     * name : 张三
     * usercode : app
     * weekTatalList : [{"weekDay":"周工作总结"}]
     */

    public String name;
    public String usercode;
    public String monthWork;//  本月工作计划
    public List<WeekPalyListBean> weekPalyList;
    public List<WeekTatalListBean> weekTatalList;

    public static class WeekPalyListBean implements Serializable {
        /**
         * weekPalyList : 周工作计划
         */
        public String nextWeek;
        public String createTime;
    }

    public static class WeekTatalListBean implements Serializable {
        /**
         * weekDay : 周工作总结
         */
        public String weekDay;
        public String createTime;
    }
}
