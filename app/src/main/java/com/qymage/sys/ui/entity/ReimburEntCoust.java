package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * Created by admin on 2019/8/29.
 * 报销的列表内容
 */

public class ReimburEntCoust {


    public int id_num;
    public String money;
    public String cate_title;
    public String content;
    public List<String> stringList;


    public ReimburEntCoust(int id_num, String money, String cate_title, String content, List<String> stringList) {
        this.id_num = id_num;
        this.money = money;
        this.cate_title = cate_title;
        this.content = content;
        this.stringList = stringList;
    }
}
