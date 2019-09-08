package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/8.
 *
 * 额度查询
 *
 */

public class QuotaQuery implements Serializable {


    public String TotalAmount;//  总额度
    public String ceaseAmount;//  已借款
    public String leftAmount;//    剩余额度
    public String orgId;//所属部门Id


}
