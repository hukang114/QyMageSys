package com.qymage.sys.ui;

import java.util.List;

/**
 * Created by admin on 2019/8/23.
 */

public class Test {


    /**
     * success : true
     * code : 200
     * message : 请求成功
     * data : [{"nextWeek  ":"6","weekDay":"5","date ":"2019-08-14 18:05:22.0","userCode":"1","userName ":"2","lastWeek ":"4"},{"nextWeek  ":"1下周工作计划","weekDay":"1本周工作总结","date ":"2019-09-07 16:51:09.0","userCode":"1099305400000000001","userName ":"吴贵钰","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 15:25:22.0","userCode":"1099305400000000002","userName ":"冯秋","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 15:35:10.0","userCode":"1099305400000000003","userName ":"冯秋","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 15:35:48.0","userCode":"1099305400000000004","userName ":"冯秋","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 15:49:31.0","userCode":"1099305400000000005","userName ":"冯秋","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 16:06:46.0","userCode":"1099305400000000006","userName ":"冯秋","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 16:28:07.0","userCode":"1099305400000000008","userName ":"冯秋","lastWeek ":null},{"nextWeek  ":"测试-下周工作计划","weekDay":"测试-本周工作总结","date ":"2019-09-09 16:34:27.0","userCode":"1099305400000000009","userName ":"冯秋","lastWeek ":null}]
     */

    public boolean success;
    public int code;
    public String message;
    public List<DataBean> data;

    public static class DataBean {
        /**
         * nextWeek   : 6
         * weekDay : 5
         * date  : 2019-08-14 18:05:22.0
         * userCode : 1
         * userName  : 2
         * lastWeek  : 4
         */

        public String nextWeek;
        public String weekDay;
        public String date;
        public String userCode;
        public String userName;
        public String lastWeek;
    }
}
