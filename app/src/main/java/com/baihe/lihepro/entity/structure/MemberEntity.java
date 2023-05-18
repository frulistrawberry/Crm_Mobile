package com.baihe.lihepro.entity.structure;

import androidx.annotation.Nullable;

import com.baihe.lihepro.entity.KeyValueEntity;

import java.util.List;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：职员信息
 */
public class MemberEntity extends StructureBaseEntity {
    public String id;
    public String name;
    public String position_id;
    public String structure_id;
    public String[] role_id;
    public String structure_name;
    public List<KeyValueEntity> other;

    public MemberEntity() {
        super();
        setType(Type.USER);
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

    public String getPosition_id() {
        return position_id;
    }

    public void setPosition_id(String position_id) {
        this.position_id = position_id;
    }

    public String getStructure_id() {
        return structure_id;
    }

    public void setStructure_id(String structure_id) {
        this.structure_id = structure_id;
    }

    public String[] getRole_id() {
        return role_id;
    }

    public void setRole_id(String[] role_id) {
        this.role_id = role_id;
    }

    public String getStructure_name() {
        return structure_name;
    }

    public void setStructure_name(String structure_name) {
        this.structure_name = structure_name;
    }

    public List<KeyValueEntity> getOther() {
        return other;
    }

    public void setOther(List<KeyValueEntity> other) {
        this.other = other;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj instanceof MemberEntity) {
            MemberEntity other = (MemberEntity) obj;
            if (other.id.equals(this.id)) {
                return true;
            }
        }
        return super.equals(obj);
    }
}
