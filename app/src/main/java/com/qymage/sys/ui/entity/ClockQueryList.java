package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/170:56
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class ClockQueryList implements Serializable {
    /**
     * userId : 1099100700000000036
     * userName : 孔俊智
     * userNo : app
     * totalHours : 137.0
     * avgHours : 8.06
     * attDays : 17
     * attDaysList : [{"date":"2019-08-01","week":"5"},{"date":"2019-08-02","week":"6"},{"date":"2019-08-05","week":"2"},{"date":"2019-08-06","week":"3"},{"date":"2019-08-07","week":"4"},{"date":"2019-08-08","week":"5"},{"date":"2019-08-09","week":"6"},{"date":"2019-08-12","week":"2"},{"date":"2019-08-13","week":"3"},{"date":"2019-08-14","week":"4"},{"date":"2019-08-15","week":"5"},{"date":"2019-08-16","week":"6"},{"date":"2019-08-19","week":"2"},{"date":"2019-08-20","week":"3"},{"date":"2019-08-21","week":"4"},{"date":"2019-08-22","week":"5"},{"date":"2019-08-23","week":"6"}]
     * restDays : 9
     * restDaysList : [{"date":"2019-08-03","week":"7"},{"date":"2019-08-04","week":"1"},{"date":"2019-08-10","week":"7"},{"date":"2019-08-11","week":"1"},{"date":"2019-08-17","week":"7"},{"date":"2019-08-18","week":"1"},{"date":"2019-08-24","week":"7"},{"date":"2019-08-25","week":"1"},{"date":"2019-08-31","week":"7"}]
     * late : 4
     * lateList : [{"date":"2019-08-02","result":"15","week":"6","remarks":"","clockTime":"09:20:00"},{"date":"2019-08-08","result":"10","week":"5","remarks":"","clockTime":"09:15:00"},{"date":"2019-08-16","result":"15","week":"6","remarks":"","clockTime":"09:20:00"},{"date":"2019-08-19","result":"5","week":"2","remarks":"","clockTime":"09:10:00"}]
     * leaveEarly : 1
     * leaveEarlyList : [{"date":"2019-08-06","result":"0","week":"3","remarks":"","clockTime":"17:00:00"}]
     * absent : 5
     * absentList : [{"date":"2019-08-26","week":"2"},{"date":"2019-08-27","week":"3"},{"date":"2019-08-28","week":"4"},{"date":"2019-08-29","week":"5"},{"date":"2019-08-30","week":"6"}]
     * abnormal : 2
     * abnormalList : [{"date":"2019-08-06","clockMode":"1","week":"3","address":"高科一号","remarks":"不在范围内","clockTime":"08:57:00"},{"date":"2019-08-22","clockMode":"1","week":"5","address":"高科一号","remarks":"范围外打卡","clockTime":"07:02:00"}]
     * leave : null
     * leaveList : null
     * cindex : 129
     * ranking : 1
     */

    public String userId;
    public String userName;
    public String userNo;
    public String totalHours;
    public String avgHours;
    public String attDays;
    public String restDays;
    public String late;
    public String leaveEarly;
    public String absent;
    public String abnormal;
    public String leave;
    public String leaveList;
    public String cindex;
    public String ranking;
    public String business;
    public List<AttDaysListBean> attDaysList;
    public List<RestDaysListBean> restDaysList;
    public List<LateListBean> lateList;
    public List<LeaveEarlyListBean> leaveEarlyList;
    public List<AbsentListBean> absentList;
    public List<AbnormalListBean> abnormalList;

    public static class AttDaysListBean implements Serializable {
        /**
         * date : 2019-08-01
         * week : 5
         */

        public String date;
        public String week;
    }

    public static class RestDaysListBean implements Serializable {
        /**
         * date : 2019-08-03
         * week : 7
         */

        public String date;
        public String week;
    }

    public static class LateListBean implements Serializable {
        /**
         * date : 2019-08-02
         * result : 15
         * week : 6
         * remarks :
         * clockTime : 09:20:00
         */

        public String date;
        public String result;
        public String week;
        public String remarks;
        public String clockTime;
    }

    public static class LeaveEarlyListBean implements Serializable {
        /**
         * date : 2019-08-06
         * result : 0
         * week : 3
         * remarks :
         * clockTime : 17:00:00
         */

        public String date;
        public String result;
        public String week;
        public String remarks;
        public String clockTime;
    }

    public static class AbsentListBean implements Serializable {
        /**
         * date : 2019-08-26
         * week : 2
         */

        public String date;
        public String week;
    }

    public static class AbnormalListBean implements Serializable {
        /**
         * date : 2019-08-06
         * clockMode : 1
         * week : 3
         * address : 高科一号
         * remarks : 不在范围内
         * clockTime : 08:57:00
         */

        public String date;
        public String clockMode;
        public String week;
        public String address;
        public String remarks;
        public String clockTime;
    }
}
