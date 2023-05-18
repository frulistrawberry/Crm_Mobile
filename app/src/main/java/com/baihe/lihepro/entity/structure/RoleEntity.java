package com.baihe.lihepro.entity.structure;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：角色信息
 */
public class RoleEntity extends StructureBaseEntity {
    private String id;
    private String name;
    private List<MemberEntity> userList;

    public RoleEntity() {
        super();
        setType(Type.ROLE);
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
        } else if (obj != null && obj instanceof RoleEntity) {
            RoleEntity other = (RoleEntity) obj;
            if (other.getId().equals(this.id)) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
