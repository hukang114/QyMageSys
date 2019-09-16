package com.qymage.sys.common.http;

import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;
import com.qymage.sys.common.config.Constants;

import java.util.Map;

/**
 * Created by admin on 2019/8/23.
 */

public class HttpUtil {

    private static final String SALT = "76576076c1f5f657b634e966c8836a06";
    private static final String DEVICE = "android";
    private static final String VIDEO_SALT = "#2hgfk85cm23mk58vncsark";


    /**
     * 初始化
     */
    public static void init() {
        HttpClient.getInstance().init();
    }

    /**
     * 取消网络请求
     */
    public static void cancel(String tag) {
        HttpClient.getInstance().cancel(tag);
    }


    public static PostRequest getAdv(String name, Map<String, Object> params) {
        return HttpClient.getInstance().postData(name, params);
    }


    /**
     * 用户登录
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest UserLogin(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }


    /**
     * 用户修改密码---接口不通
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest updatePwd(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 用户退出登录---接口不通
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest logout(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 获取各个下拉的枚举类型 --- 接口不通
     * {
     * type:String    ProjectType-项目类型   ExpenseType-报销类型  ContractOutType-合同类型支出类    ContractComeType—合同类型收入类   leaveType-请假类型
     * taxrateType-税率
     * //枚举类型名称
     * }
     *
     * @param params
     * @return
     */
    public static PostRequest getEnum(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.GETENUM, params);
    }

    /**
     * 树形结构部门、人员数据获取
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest getTree(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 6.2合同申请提交接口
     *
     * @param params
     * @return
     */
    public static PostRequest contract_submit(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.CONTRACT_SUBMIT, params);
    }

    /**
     * 6.1合同编号获取接口 根据合同类型
     *
     * @param params
     * @return
     */
    public static PostRequest getContractNo(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.getContractNo, params);
    }

    /**
     * 2.7按项目名称/项目编号查询
     *
     * @param params
     * @return
     */
    public static PostRequest getProjectNo(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.getProject, params);
    }


    /**
     * 2.7按项目名称/项目编号查询
     * {
     * projectNo:string //项目编号
     * projectName:string //项目名称
     * }
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest getProject(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 11.1收款/付款/开票/收票申请接口
     * openType：string  1-收款  2-付款  3-开票  4-收票
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest inmoneyAdd(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 3.1投标/履约保证金-支/收提交接口
     * bidType: String //保证金类型（投标 支-01，投标 收-02，履约支-03，履约 收04）
     *
     * @param params
     * @return
     */
    public static PostRequest bid_submit(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.BID_SUBMIT, params);
    }

    /**
     * 立项申请提交接口
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest project_Submit(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 4.3查询个人立项记录列表
     *
     * @param params
     * @return
     */
    public static PostRequest project_findByUser(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.PROJECT_FINDBYUSER, params);
    }

    /**
     * 4.4查询立项详情
     *
     * @param params
     * @return
     */
    public static PostRequest project_findById(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.PROJECT_FINDBYID, params);
    }

    /**
     * 10.1审核接口
     * <p>
     * userCode:String 工号
     * type：类型  1—通过   2-拒绝 3-撤回
     * remarks String  备注
     * taskId String 任务ID
     *
     * @param params
     * @return
     */
    public static PostRequest audit_auditAdd(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.audit_auditAdd, params);
    }

    /**
     * 12.1额度查询
     * {
     * userCode String 工号
     * }
     *
     * @param params
     * @return
     */
    public static PostRequest loan_quotaQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOAN_QUOTAQUERY, params);
    }

    /**
     * 12.2借款申请接口
     *
     * @param params
     * @return
     */
    public static PostRequest loan_quotaAdd(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOAN_QUOTAADD, params);
    }

    /**
     * 还款申请接口
     *
     * @param params
     * @return
     */
    public static PostRequest loan_payAdd(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOAN_PAYADD, params);
    }

    /**
     * 12.3借款列表查询接口
     *
     * @param params
     * @return
     */
    public static PostRequest loan_loanQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOAN_LOANQUERY, params);
    }

    /**
     * 7.1请假申请提交接口
     *
     * @param params
     * @return
     */
    public static PostRequest leave_Submit(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LEAVE_SUBMIT, params);
    }

    /**
     * 日报申请接口
     *
     * @param params
     * @return
     */
    public static PostRequest log_logAdd(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_LOGADD, params);
    }

    /**
     * 2.6根据合同编号/合同名称查询
     *
     * @param params
     * @return
     */
    public static PostRequest getContract(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.GETCONTRACT, params);
    }

    /**
     * 9.2日志列表查询
     *
     * @param params
     * @return
     */
    public static PostRequest listLogQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_LISTLOGQUERY, params);
    }

    /**
     * 9.4周报查询
     *
     * @param params
     * @return
     */
    public static PostRequest weeklyQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_WEEKLYQUERY, params);
    }

    /**
     * 9.8获取月报工作计划接口
     *
     * @param params
     * @return
     */
    public static PostRequest yesQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_YESQUERY, params);
    }

    /**
     * 9.7月报申请提交接口
     *
     * @param params
     * @return
     */
    public static PostRequest monAdd(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_MONADD, params);
    }

    /**
     * 模糊输入，下拉选择查询所有项目
     * 1.1.3 请求参数---模糊查询
     * name:string //名字
     *
     * @param params
     * @return
     */
    public static PostRequest getUserInfo(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.GETUSERINFO_LIST, params);
    }


    /**
     * 6.3获取我的合同记录
     *
     * @param params
     * @return
     */
    public static PostRequest contract_findByUser(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.CONTRACT_FINDBYUSER, params);
    }

    /**
     * 6.3获取我的合详情
     *
     * @param params
     * @return
     */
    public static PostRequest expense_findById(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.EXPENSE_FINDBYID, params);
    }

    /**
     * 根据项目类型4.1获取项目编号
     *
     * @param params
     * @return
     */
    public static PostRequest project_getProjectNo(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.getProjectNo, params);
    }

    /**
     * 3.5按条件查询所有公司
     *
     * @param params
     * @return
     */
    public static PostRequest bid_findAllCompany(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.BID_FINDALLCOMPANY, params);
    }

    /**
     * 3.2投标/履约保证金-收查询对应支数据的接口
     *
     * @param params
     * @return
     */
    public static PostRequest bid_findByProject(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.BID_FINDBYPROJECT, params);
    }

    /**
     * 2.8按条件模糊查询单位信息
     *
     * @param params
     * @return
     */
    public static PostRequest getCompanyInfo(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.GETCOMPANYINFO, params);
    }

    /**
     * 3.3投标/履约保证金 个人记录查询接口
     *
     * @param params
     * @return
     */
    public static PostRequest bid_findByUser(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.BID_FINDBYUSER, params);
    }

    /**
     * 3.4投标/履约保证金详情接口
     *
     * @param params
     * @return
     */
    public static PostRequest bid_findById(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.BID_FINDBYID, params);
    }

    /**
     * 2.9消息接口
     *
     * @param params
     * @return
     */
    public static PostRequest message_User_Msg(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.MESSAGE_USER_MSG, params);
    }

    /**
     * 2.12公告列表接口及详情
     *
     * @param params
     * @return
     */
    public static PostRequest notice_listNotice(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.NOTICE_LISTNOTICE, params);
    }

    /**
     * 2.11消息更新接口
     *
     * @param params
     * @return
     */
    public static PostRequest msgUdate(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.NOTICE_MSGUDATE, params);
    }

    /**
     * 11.4按合同编号获取收付款/开票收票信息
     *
     * @param params
     * @return
     */
    public static PostRequest money_getReceived(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.MONEY_GETRECEIVED, params);
    }

    /**
     * 11.2我的收款/付款/收票/开票查询列表接口
     *
     * @param params
     * @return
     */
    public static PostRequest money_listMoneyQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.MONEY_LISTMONEYQUERY, params);
    }

    /**
     * 11.3我的收款/付款/收票/开票查询详情接口
     *
     * @param params
     * @return
     */
    public static PostRequest money_moneyQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.MONEY_MONEYQUERY, params);
    }

    /**
     * 9.5月报查询列表接口
     *
     * @param params
     * @return
     */
    public static PostRequest log_ListMonQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_LISTMONQUERY, params);
    }

    /**
     * 12.3借还款查询列表接口
     *
     * @param params
     * @return
     */
    public static PostRequest loan_loanQueryDet(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_LOANQUERY_DET, params);
    }

    /**
     * 8.1查询考勤配置
     *
     * @param params
     * @return
     */
    public static PostRequest getSettingInfo(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.ATTENDANCE_SETTING_GETSETTINGINFO, params);
    }


    /**
     * 8.2打卡
     *
     * @param params
     * @return
     */
    public static PostRequest attendance_Clock(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.ATTENDANCE_CLOCK, params);
    }

    /**
     * 8.3按天查询考勤
     *
     * @param params
     * @return
     */
    public static PostRequest attendance_DaySearch(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.ATTENDANCE_DAYSEARCH, params);
    }

    /**
     * 8.4我的考勤查询
     *
     * @param params
     * @return
     */
    public static PostRequest attendance_ClockQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.ATTENDANCE_CLOCKQUERY, params);
    }

    /**
     * 8.5排行查询接口
     *
     * @param params
     * @return
     */
    public static PostRequest attendance_rankQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.ATTENDANCE_RANKQUERY, params);
    }

    /**
     * 8.2考勤计算接口
     *
     * @param params
     * @return
     */
    public static PostRequest attendance_Calculate(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.ATTENDANCE_CALCULATE, params);
    }

    /**
     * 7.2查询我的请假接口列表
     *
     * @param params
     * @return
     */
    public static PostRequest leave_listSerch(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LEAVE_LISTSERCH, params);
    }

    /**
     * 77.3请假查询详情接口
     *
     * @param params
     * @return
     */
    public static PostRequest leave_detalSerc(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LEAVE_DETALSERC, params);
    }

    /**
     * 9.3日志接口查询详情
     *
     * @param params
     * @return
     */
    public static PostRequest log_logQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_LOGQUERY, params);
    }

    /**
     * 9.6月报查询详情接口
     *
     * @param params
     * @return
     */
    public static PostRequest log_monQuery(Map<String, Object> params) {
        return HttpClient.getInstance().postData(HttpConsts.LOG_MONQUERY, params);
    }


}


