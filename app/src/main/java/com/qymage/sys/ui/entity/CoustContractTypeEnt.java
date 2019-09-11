package com.qymage.sys.ui.entity;

import java.util.List;

/**
 * 类名：
 * 类描述：组装合同的类型成二级
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2019/9/119:55
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class CoustContractTypeEnt {

    public String title;
    public List<ContractTypeEnt> typeEnts;

    public CoustContractTypeEnt(String title, List<ContractTypeEnt> typeEnts) {
        this.title = title;
        this.typeEnts = typeEnts;
    }
}
