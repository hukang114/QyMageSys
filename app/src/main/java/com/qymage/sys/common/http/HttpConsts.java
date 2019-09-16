package com.qymage.sys.common.http;

/**
 *
 */

public class HttpConsts {

    public static final String LANGUAGE = "language";
    // 登录
    public static final String LOGIN = "user/login";
    // 修改密码
    public static final String UPDATEPWD = "user/updatePwd";
    // 退出登录
    public static final String LOGOUT = "user/logout";
    // 获取枚举下拉
    public static final String GETENUM = "getEnum";
    //树形结构部门、人员数据获取
    public static final String GETTREE = "getTree";
    //合同申请提交接口
    public static final String CONTRACT_SUBMIT = "contract/submit";
    //6.1合同编号获取接口 根据合同类型
    public static final String getContractNo = "contract/getContractNo";
    //4.1获取项目编号
    public static final String getProjectNo = "project/getProjectNo";
    //2.7按项目名称/项目编号查询
    public static final String getProject = "getProject";
    //11.1收款/付款/开票/收票申请接口
    public static final String INMONEYADD = "money/inmoneyAdd";
    //投标/履约保证金-支/收提交接口
    public static final String BID_SUBMIT = "bid/submit";
    //立项申请提交接口
    public static final String PROJECT_SUBMIT = "project/submit";
    //4.3查询个人立项记录列表
    public static final String PROJECT_FINDBYUSER = "project/findByUser";
    //4.4查询立项详情
    public static final String PROJECT_FINDBYID = "project/findById";
    //10.1审核接口
    public static final String audit_auditAdd = "audit/auditAdd";
    //12.1额度查询
    public static final String LOAN_QUOTAQUERY = "loan/quotaQuery";
    //12.2借款申请接口
    public static final String LOAN_QUOTAADD = "loan/quotaAdd";
    //12.4还款申请接口
    public static final String LOAN_PAYADD = "loan/payAdd";
    //借款查询接口 我的借款列表
    public static final String LOAN_LOANQUERY = "loan/listLoanQuery";
    //请假申请提交接口
    public static final String LEAVE_SUBMIT = "leave/submit";
    //日报申请接口
    public static final String LOG_LOGADD = "log/logAdd";
    //2.6根据合同编号/合同名称查询
    public static final String GETCONTRACT = "getContract";
    //9.2日志列表查询
    public static final String LOG_LISTLOGQUERY = "log/listLogQuery";
    //9.4周报查询
    public static final String LOG_WEEKLYQUERY = "log/weeklyQuery";
    //9.8获取月报工作计划接口
    public static final String LOG_YESQUERY = "log/yesQuery";
    //9.7月报申请提交接口
    public static final String LOG_MONADD = "log/monAdd";
    //按条件查询人员你列表
    public static final String GETUSERINFO_LIST = "getUserInfo";
    //6.3获取我的合同记录
    public static final String CONTRACT_FINDBYUSER = "contract/findByUser";
    //6.4获取合同记录详情
    public static final String EXPENSE_FINDBYID = "contract/findById";
    //3.5按条件查询所有公司
    public static final String BID_FINDALLCOMPANY = "bid/findAllCompany";

    //3.2投标/履约保证金-收查询对应支数据的接口
    public static final String BID_FINDBYPROJECT = "bid/findByProject";
    //2.8按条件模糊查询单位信息
    public static final String GETCOMPANYINFO = "getCompanyInfo";

    //3.3投标/履约保证金 个人记录查询接口
    public static final String BID_FINDBYUSER = "bid/findByUser";
    //3.4投标/履约保证金详情接口
    public static final String BID_FINDBYID = "bid/findById";
    //2.9消息接口 消息列表
    public static final String MESSAGE_USER_MSG = "message/user/msg";
    //2.12公告列表接口及详情
    public static final String NOTICE_LISTNOTICE = "notice/listNotice";
    //消息更新接口
    public static final String NOTICE_MSGUDATE = "message/consume";
    //11.4按合同编号获取收付款/开票收票信息
    public static final String MONEY_GETRECEIVED = "money/getReceived";
    //11.2我的收款/付款/收票/开票查询列表接口
    public static final String MONEY_LISTMONEYQUERY = "money/listMoneyQuery";
    //11.3我的收款/付款/收票/开票查询详情接口
    public static final String MONEY_MONEYQUERY = "money/moneyQuery";
    //9.5月报查询列表接口
    public static final String LOG_LISTMONQUERY = "log/listMonQuery";
    //12.3借还款查询列表接口
    public static final String LOG_LOANQUERY_DET = "loan/loanQuery";
    //8.1查询考勤配置
    public static final String ATTENDANCE_SETTING_GETSETTINGINFO = "attendance/setting/getSettingInfo";
    //8.2打卡
    public static final String ATTENDANCE_CLOCK = "attendance/clock";
    //8.3按天查询考勤
    public static final String ATTENDANCE_DAYSEARCH = "attendance/daySearch";
    //8.4我的考勤查询
    public static final String ATTENDANCE_CLOCKQUERY = "attendance/clockQuery";
    //8.5排行查询接口
    public static final String ATTENDANCE_RANKQUERY = "attendance/rankQuery";
    //8.2考勤计算接口
    public static final String ATTENDANCE_CALCULATE = "attendance/calculate";
    //7.2查询我的请假接口列表
    public static final String LEAVE_LISTSERCH = "leave/listSerch";
    //7.3请假查询详情接口
    public static final String LEAVE_DETALSERC = "leave/detalSerch";

    //9.3日志接口查询详情
    public static final String LOG_LOGQUERY = "log/logQuery";

    //9.6月报查询详情接口
    public static final String LOG_MONQUERY = "log/monQuery";


}
