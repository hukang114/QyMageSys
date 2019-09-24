package com.qymage.sys;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.SparseArray;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxf on 2017/8/4.
 */

public class AppConfig {
    //http://192.168.1.173:50050
    //public static final String HOST = "http://livenewtest.yunbaozb.com";//50050 ，，120.79.18.66:8085
    public static final String HOST = "http://192.168.1.173:50050";
    //http://192.51.188.17
    //外部sd卡
    public static final String DCMI_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    // 选择合同类型的回传标识
    public static final int num1000 = 1000;

    // 消息更新模块的id
    public interface status {
        final int value1 = 1;//
        final int value2 = 2;//
        final int value3 = 3;//
        final int value4 = 4;//
        final int value5 = 5;//
        final int value6 = 6;//
        final int value7 = 7;//
        final int value8 = 8;//
        final int value9 = 9;//
        final int value10 = 10;//
        final int value11 = 11;//
        final int value12 = 12;//
        final int value13 = 13;//
        final int value14 = 14;//
        final int value15 = 15;//

      /*  <enum label="立项" value="1"/>
        <enum label="日报" value="2"/>
        <enum label="投标保证金支" value="3"/>
        <enum label="投标保证金收" value="4"/>
        <enum label="履约保证金支" value="5"/>
        <enum label="履约保证金收" value="6"/>
        <enum label="请假" value="7"/>
        <enum label="合同" value="8"/>
        <enum label="付款" value="9"/>
        <enum label="收款" value="10"/>
        <enum label="开票" value="11"/>
        <enum label="收票" value="12"/>
        <enum label="借款" value="13"/>
        <enum label="月报" value="14"/>
        <enum label="通知" value="15"/>*/

    }

    public static String btnType1 = "01";// 上班打卡
    public static String btnType2 = "02";// 下班打开
    public static String btnType3 = "03";// 立项申请
    public static String btnType4 = "04";// 合同申请
    public static String btnType5 = "05";// 付款申请
    public static String btnType6 = "06";// 收款申请
    public static String btnType7 = "07";// 开票申请
    public static String btnType8 = "08";// 收票申请
    public static String btnType9 = "09";// 投标保证金收
    public static String btnType10 = "10";//投标保证金支
    public static String btnType11 = "11";// 履约保证金支
    public static String btnType12 = "12";// 履约保证金收
    public static String btnType13 = "13";// 借款申请
    public static String btnType14 = "14";// 请假申请
    public static String btnType15 = "15";// 日报
    public static String btnType16 = "16";//月报

    // 用户权限按钮
           /*    "processAppBtnVo":[

            {
                "id":"13",
                    "btnLable":"日报",
                    "btnType":"15",
                    "processDefId":"log_approve_process:1:102585",
                    "processKey":"log_approve_process",
                    "processName":"log_approve_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"1",
                    "btnLable":"立项申请",
                    "btnType":"03",
                    "processDefId":"project_zfr:5:200056",
                    "processKey":"project_zfr",
                    "processName":"project_zfr",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"10",
                    "btnLable":"履约保证金收",
                    "btnType":"12",
                    "processDefId":"performance_come_process:2:200080",
                    "processKey":"performance_come_process",
                    "processName":"performance_come_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"11",
                    "btnLable":"借款申请",
                    "btnType":"13",
                    "processDefId":"multi-sign-process:1:77504",
                    "processKey":"multi-sign-process",
                    "processName":"multi-sign-process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"12",
                    "btnLable":"请假申请",
                    "btnType":"14",
                    "processDefId":"leave_process:8:200084",
                    "processKey":"leave_process",
                    "processName":"leave_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"14",
                    "btnLable":"月报",
                    "btnType":"16",
                    "processDefId":"month_log_process:2:200016",
                    "processKey":"month_log_process",
                    "processName":"month_log_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"2",
                    "btnLable":"合同申请",
                    "btnType":"04",
                    "processDefId":"\t\r\nprocess-1:3:200052",
                    "processKey":"process-1",
                    "processName":"process-1",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"3",
                    "btnLable":"收款申请",
                    "btnType":"06",
                    "processDefId":"process_col:1:172514",
                    "processKey":"process_col",
                    "processName":"process_col",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"4",
                    "btnLable":"付款申请",
                    "btnType":"05",
                    "processDefId":"process_pay:1:172510",
                    "processKey":"process_pay",
                    "processName":"process_pay",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"5",
                    "btnLable":"开票申请",
                    "btnType":"07",
                    "processDefId":"process_Invoice:1:175054",
                    "processKey":"process_Invoice",
                    "processName":"process_Invoice",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"6",
                    "btnLable":"收票申请",
                    "btnType":"08",
                    "processDefId":"process_collect:1:175061",
                    "processKey":"process_Invoice",
                    "processName":"process_Invoice",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"7",
                    "btnLable":"投标保证金收",
                    "btnType":"09",
                    "processDefId":"bid_come_process:3:200064",
                    "processKey":"bid_come_process",
                    "processName":"bid_come_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"8",
                    "btnLable":"投标保证金支",
                    "btnType":"10",
                    "processDefId":"bid_out_process:5:200060",
                    "processKey":"bid_out_process",
                    "processName":"bid_out_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            },

            {
                "id":"9",
                    "btnLable":"履约保证金支",
                    "btnType":"11",
                    "processDefId":"performance_out_process:2:200068",
                    "processKey":"performance_out_process",
                    "processName":"performance_out_process",
                    "processVersion":1,
                    "createdDate":1568278331000,
                    "updatedDate":1568278331000
            }
                    ]
        }*/

}
