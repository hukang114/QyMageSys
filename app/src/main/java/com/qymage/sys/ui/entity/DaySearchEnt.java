package com.qymage.sys.ui.entity;

/**
 * 类名：8.3按天查询考勤
 * 类描述：
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1519:16
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class DaySearchEnt {
 /*   userId: String;// 用户id
    userName: String;// 用户名称
    userCode: String;//工号
    beginTime：String 上班时间
    endTime String//下班时间
    isCurrentDate：String 是否当天时间 1-是、0-否
    clockInTime: String;//上班打卡时间 09:32
    clockInType: String;//上班考勤类型 1-在岗  2-出差  3-请假
    clockInStatus：String 上班考勤状态  1-正常 2-迟到  3-早退  4-旷工
    clockInResult：String 上班考勤结果  迟到几分中...
    clockInAddr：String 上班考勤地址
    clockInScope：string 上班范围  1-是  0-否  针对打卡类型在岗的时候
    clockInRemark：String 上班打卡备注
    clockOutTime: String;// 下班打卡时间 09:32
    clockOutType: String;// 下班考勤类型 1-在岗  2-出差  3-请假
    clockOutStats：String 下考勤状态  1-正常 2-迟到  3-早退  4-旷工
    clockOutResult：String 下考勤结果描述 早退时长
    clockOutAddr：String 下考勤地址
    clockOutScope：string 下范围  1-是  0-否  针对打卡类型在岗的时候
    clockOutRemark：String 下班打卡备注*/

    public String clockOutAddr;
    public int clockInType;
    public String clockInAddr;
    public int clockOutType;
    public String clockOutScope;
    public String userNo;
    public String clockInRemark;
    public String userName;
    public String userId;
    public String clockInResult;
    public String clockInTime;
    public String clockOutStatus;
    public String clockOutTime;
    public String clockInScope;
    public int clockInStatus;
    public String beginTime;
    public String endTime;
    public int isCurrentDate;
    public String clockOutResult;
    public int clockOutStats;
    public String clockOutRemark;
}
