package com.qymage.sys.common.http;

import com.lzy.okgo.request.PostRequest;
import com.qymage.sys.common.callback.JsonCallback;
import com.qymage.sys.common.callback.Result;

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
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest getEnum(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
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
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest contract_submit(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 6.1合同编号获取接口
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest getContractNo(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
    }

    /**
     * 获取项目编号
     *
     * @param urlname
     * @param params
     * @return
     */
    public static PostRequest getProjectNo(String urlname, Map<String, Object> params) {
        return HttpClient.getInstance().postData(urlname, params);
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


}


