package com.baihe.lihepro.entity.structure;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：组织架构基类
 */
public class StructureBaseEntity extends Observable {
    public enum SelectEvent {
        SELF,
        TO_CHILD,
        TO_PARENT,
    }

    public enum Type {
        USER,
        ROLE,
        STRUCTURE,
    }

    //是否选中
    private boolean isSelect;
    //类型
    private Type type;
    //关联父级部门
    private List<StructureEntity> parentStructures;
    //关联父级角色
    private List<RoleEntity> parentRoles;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        //选中变更后通知关联项变更
        if (this.isSelect != select) {
            this.isSelect = select;
            setChanged();
            notifyObservers(SelectEvent.SELF);
        }
    }

    public void setSelect(boolean select, SelectEvent event) {
        //选中变更后通知关联项变更
        if (this.isSelect != select) {
            this.isSelect = select;
            setChanged();
            notifyObservers(event);
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<StructureEntity> getParentStructures() {
        return parentStructures;
    }

    public List<RoleEntity> getParentRoles() {
        return parentRoles;
    }

    public void addParentStructure(StructureEntity structureEntity) {
        if (parentStructures == null) {
            parentStructures = new ArrayList<>();
        }
        if (!parentStructures.contains(structureEntity)) {
            parentStructures.add(structureEntity);
        }
    }

    public void addParentRole(RoleEntity roleEntity) {
        if (parentRoles == null) {
            parentRoles = new ArrayList<>();
        }
        if (!parentRoles.contains(roleEntity)) {
            parentRoles.add(roleEntity);
        }
    }
}
