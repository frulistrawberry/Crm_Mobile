package com.baihe.lihepro.entity.structure;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：部门信息
 */
public class StructureEntity extends StructureBaseEntity {
    private String id;
    private String name;
    private List<StructureEntity> childlist;
    private List<MemberEntity> userList;

    public StructureEntity() {
        super();
        setType(Type.STRUCTURE);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StructureEntity> getChildlist() {
        return childlist;
    }

    public void setChildlist(List<StructureEntity> childlist) {
        this.childlist = childlist;
    }

    public List<MemberEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<MemberEntity> userList) {
        this.userList = userList;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj instanceof StructureEntity) {
            StructureEntity other = (StructureEntity) obj;
            if (other.getId().equals(this.id)) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
