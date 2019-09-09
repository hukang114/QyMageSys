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
    //6.1合同编号获取接口
    public static final String getContractNo = "contract/getContractNo";

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
    public static final String LOAN_LOANQUERY = "loan/loanQuery";
    //请假申请提交接口
    public static final String LEAVE_SUBMIT = "leave/submit";
    //日报申请接口
    public static final String LOG_LOGADD = "log/logAdd";
    //2.6根据合同编号/合同名称查询
    public static final String GETCONTRACT = "getContract";
    //9.2日志列表查询
    public static final String LOG_LISTLOGQUERY= "log/listLogQuery";




}
