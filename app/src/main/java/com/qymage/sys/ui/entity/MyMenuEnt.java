package com.qymage.sys.ui.entity;

/**
 * Created by admin on 2019/8/25.
 * 个人中心我的菜单
 */

public class MyMenuEnt {


    public String keyname;
    public String menu_name;
    public int menu_ioc;

    public MyMenuEnt(String keyname, String menu_name, int menu_ioc) {
        this.keyname = keyname;
        this.menu_name = menu_name;
        this.menu_ioc = menu_ioc;
    }
}
