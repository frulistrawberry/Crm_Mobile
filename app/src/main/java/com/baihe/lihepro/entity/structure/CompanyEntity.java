package com.baihe.lihepro.entity.structure;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：公司信息
 */
public class CompanyEntity {
    /**公司组织架构*/
    private StructureEntity structure;
    /**角色*/
    private List<RoleEntity> roleList;
    /**所属部门*/
    private StructureEntity myStructure;

    public StructureEntity getStructure() {
        return structure;
    }

    public void setStructure(StructureEntity structure) {
        this.structure = structure;
    }

    public List<RoleEntity> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleEntity> roleList) {
        this.roleList = roleList;
    }

    public StructureEntity getMyStructure() {
        return myStructure;
    }

    public void setMyStructure(StructureEntity myStructure) {
        this.myStructure = myStructure;
    }
}
