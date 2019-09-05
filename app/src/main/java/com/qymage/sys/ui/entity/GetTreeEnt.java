package com.qymage.sys.ui.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：
 * 类描述：树形结构部门、人员数据获取
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/516:43
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class GetTreeEnt implements Serializable {

    /**
     * name : 角色根节点
     * id : 1099100700000000001
     * parentId : 1099100400000000002
     */

    public String name;
    public String id;
    public String parentId;
    public String userPost;
    public String portrait;
    public boolean isCheck = false;

}
