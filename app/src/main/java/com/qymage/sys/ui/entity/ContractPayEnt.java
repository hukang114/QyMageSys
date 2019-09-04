package com.qymage.sys.ui.entity;

import java.io.Serializable;

/**
 * Created by admin on 2019/9/5.
 * 添加付款比列
 */

public class ContractPayEnt implements Serializable{

    public int id;
    public String date;
    public int bili;
    public String money;

    public ContractPayEnt(int id, String date, int bili, String money) {
        this.id = id;
        this.date = date;
        this.bili = bili;
        this.money = money;
    }
}
