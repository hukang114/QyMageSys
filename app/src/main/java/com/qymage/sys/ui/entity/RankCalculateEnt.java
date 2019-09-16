package com.qymage.sys.ui.entity;

/**
 * 类名：
 * 类描述：考勤计算数据
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/1615:16
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class RankCalculateEnt {


    /*  attendanceDate：String 经度
      dayOfWeek：String 星期几 2(周一),3(周二),4(周三),5(周四),6(周五),7(周六)
      attendancePeriod：String 考勤时段
      clockInTime：String 上班打卡时间
      clockInAddr：String 上班打卡地址
      clockInScope：String 考勤范围 1-在范围、0-不在范围
      clockInLongitude：String 经度（上班打卡）
      clockInLatitude：String 纬度（上班打卡）
      clockInType：String 考勤类型 1-在岗  2-出差  3-请假
      clockInStatus：String 考勤状态：1-正常、2-迟到、3-早退、4-旷工
      clockInResult：String 考勤状态描述：如：迟到时长
      clockOutTime：String 下班打卡时间
      clockOutAddr：String 下班打卡地址
      clockOutScope：String 考勤范围 1-在范围、0-不在范围
      clockOutLongitude：String 经度（下班打卡）
      clockOutLatitude：String 纬度（下班打卡）
      clockOutType：String 考勤类型 1-在岗  2-出差  3-请假
      clockOutStatus：String 考勤状态：1-正常、2-迟到、3-早退、4-旷工
      clockOutResult：String 考勤状态描述：如：早退时长
  */
    public String id;
    public String userId;
    public String userNo;
    public String userPost;
    public String userName;
    public String deptId;
    public String deptName;
    public String attendanceDate;
    public String dayOfWeek;
    public String attendancePeriod;
    public String clockInTime;
    public String clockInAddr;
    public int clockInScope;
    public String clockInLongitude;
    public String clockInLatitude;
    public String clockInType;
    public int clockInStatus;
    public String clockInResult;
    public String clockInRemark;
    public String clockOutTime;
    public String clockOutAddr;
    public int clockOutScope;
    public String clockOutLongitude;
    public String clockOutLatitude;
    public String clockOutType;
    public int clockOutStatus;
    public String clockOutResult;
    public String clockOutRemark;
    public String workHour;
    public String remark;
    public String startTime;
    public String endTime;
    public String attendanceType;
    public String associationApprove;
    public String createBy;
    public String updateBy;
}
