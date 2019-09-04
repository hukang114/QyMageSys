package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/4.
 * 添加合同明细的实体类
 */

public class ContractDetAddEnt implements Serializable {

    public String money;

    public int shuilv;

    public String shuijin;

    public ContractDetAddEnt(String money, int shuilv, String shuijin) {
        this.money = money;
        this.shuilv = shuilv;
        this.shuijin = shuijin;
    }
}
