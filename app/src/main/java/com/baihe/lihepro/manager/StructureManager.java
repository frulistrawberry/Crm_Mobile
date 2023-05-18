package com.baihe.lihepro.manager;

import com.baihe.lihepro.entity.structure.CompanyEntity;
import com.baihe.lihepro.entity.structure.MemberEntity;
import com.baihe.lihepro.entity.structure.RoleEntity;
import com.baihe.lihepro.entity.structure.StructureBaseEntity;
import com.baihe.lihepro.entity.structure.StructureEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Author：xubo
 * Time：2020-08-06
 * Description：组织架构管理
 */
public class StructureManager {
    private static class StructureManagerHolder {
        private static StructureManager structureManager = new StructureManager();
    }

    private boolean isInit;
    private String companyName;
    //用户池
    private List<MemberEntity> members = new ArrayList<>();
    //公司部门(包括部门和人员)
    private List<StructureBaseEntity> structures = new ArrayList<>();
    //本部门(包括子部门和人员)
    private List<StructureBaseEntity> structureSelf = new ArrayList<>();
    //角色(包括角色和人员)
    private List<StructureBaseEntity> roles = new ArrayList<>();

    //选中观察者
    private StructureObserver structureObserver;


    public static StructureManager newInstance() {
        return StructureManager.StructureManagerHolder.structureManager;
    }

    private StructureManager() {

    }

    public void init(CompanyEntity companyEntity) {
        if (companyEntity == null) {
            return;
        }
        isInit = true;

        companyName = companyEntity.getStructure().getName();
        members.clear();
        structures.clear();
        structureSelf.clear();
        roles.clear();


        structureObserver = new StructureObserver();
        /**-------------------数据关联和处理-------------------*/
        //部门数据关联
        overallStructure(companyEntity.getStructure(), true, this.structures);
        //角色数据关联
        overallRoles(companyEntity.getRoleList());
        //本部门关联
        //先去组件架构寻找是否有同一个部门，没有创建新部门
        overallStructureSelf(companyEntity.getMyStructure());
    }

    public boolean isInit() {
        return isInit;
    }

    private void checkInit() {
        if (!isInit) {
            throw new IllegalStateException("StructureManager初始化数据后才能使用");
        }
    }

    /**
     * 部门结构建立关联关系
     *
     * @param structureEntity
     * @param isTopLevel
     * @param receiveList
     */
    private void overallStructure(StructureEntity structureEntity, boolean isTopLevel, List<StructureBaseEntity> receiveList) {
        if (structureEntity == null) {
            return;
        }
        if (structureEntity.getChildlist() != null) {
            for (StructureEntity childStructureEntity : structureEntity.getChildlist()) {
                if (isTopLevel) {
                    receiveList.add(childStructureEntity);
                } else {
                    childStructureEntity.addParentStructure(structureEntity);
                }
                //注册观察者
                childStructureEntity.addObserver(structureObserver);
                overallStructure(childStructureEntity, false, receiveList);
            }
        }

        //存储真是用户对象，保证池子不重复
        List<MemberEntity> realMemberEntities = new ArrayList<>();
        if (structureEntity.getUserList() != null) {
            for (MemberEntity memberEntity : structureEntity.getUserList()) {
                //从用户池找出用户索引
                int index = this.members.indexOf(memberEntity);
                MemberEntity reaMemberEntity;
                if (index > 0) { //如果找到用户，从用户缓存池取出，防止两份数据
                    reaMemberEntity = this.members.get(index);
                } else {
                    reaMemberEntity = memberEntity;
                    //没有添加到用户池
                    this.members.add(memberEntity);
                    //注册观察者
                    reaMemberEntity.addObserver(structureObserver);
                }
                if (isTopLevel) {
                    receiveList.add(reaMemberEntity);
                } else {
                    //关联父级
                    reaMemberEntity.addParentStructure(structureEntity);
                }
                //保证用户不重复
                realMemberEntities.add(reaMemberEntity);
            }
        }
        structureEntity.setUserList(realMemberEntities);
    }

    /**
     * 角色结构建立关联关系
     *
     * @param roleEntities
     */
    private void overallRoles(List<RoleEntity> roleEntities) {
        if (roleEntities == null) {
            return;
        }
        for (RoleEntity roleEntity : roleEntities) {
            //存储真是用户对象，保证池子不重复
            List<MemberEntity> realMemberEntities = new ArrayList<>();
            if (roleEntity.getUserList() != null) {
                for (MemberEntity memberEntity : roleEntity.getUserList()) {
                    //从用户池找出用户索引
                    int index = this.members.indexOf(memberEntity);
                    MemberEntity reaMemberEntity;
                    if (index > 0) { //如果找到用户，从用户缓存池取出，防止两份数据
                        reaMemberEntity = this.members.get(index);
                    } else {
                        reaMemberEntity = memberEntity;
                        //没有添加到用户池
                        this.members.add(memberEntity);
                        //注册观察者
                        reaMemberEntity.addObserver(structureObserver);
                    }
                    //关联父级
                    reaMemberEntity.addParentRole(roleEntity);
                    //保证用户不重复
                    realMemberEntities.add(reaMemberEntity);
                }
            }
            roleEntity.setUserList(realMemberEntities);
            this.roles.add(roleEntity);
            //注册观察者
            roleEntity.addObserver(structureObserver);
        }
    }

    /**
     * 本部门结构建立关联关系
     *
     * @param structure
     */
    private void overallStructureSelf(StructureEntity structure) {
        List<StructureEntity> structureEntities = new ArrayList<>();
        for (StructureBaseEntity structureBaseEntity : this.structures) {
            if (structureBaseEntity.getType() == StructureBaseEntity.Type.STRUCTURE) {
                structureEntities.add((StructureEntity) structureBaseEntity);
            }
        }
        StructureEntity findStructureEntity = findStructureEntity(structure, structureEntities);
        //未寻找到需要重新关联数据
        if (findStructureEntity == null) {
            overallStructure(structure, true, this.structureSelf);
        } else {
            if (findStructureEntity.getChildlist() != null) {
                this.structureSelf.addAll(findStructureEntity.getChildlist());
            }
            if (findStructureEntity.getUserList() != null) {
                this.structureSelf.addAll(findStructureEntity.getUserList());
            }

        }
    }

    /**
     * 寻找部门
     *
     * @param structureSrc
     * @param structureEntitys
     * @return
     */
    private StructureEntity findStructureEntity(StructureEntity structureSrc, List<StructureEntity> structureEntitys) {
        for (StructureEntity structureEntity : structureEntitys) {
            if (structureEntity.getId().equals(structureSrc.getId())) {
                return structureEntity;
            } else if (structureEntity.getChildlist() != null) {
                return findStructureEntity(structureSrc, structureEntity.getChildlist());
            }
        }
        return null;
    }

    public String getCompanyName() {
        checkInit();
        return companyName;
    }

    public List<MemberEntity> getMembers() {
        checkInit();
        return members;
    }

    public List<StructureBaseEntity> getStructures() {
        checkInit();
        return structures;
    }

    public List<StructureBaseEntity> getStructureSelf() {
        checkInit();
        return structureSelf;
    }

    public List<StructureBaseEntity> getRoles() {
        checkInit();
        return roles;
    }

    public List<MemberEntity> getSelectMemebers() {
        checkInit();
        List<MemberEntity> selectMembers = new ArrayList<>();
        for (MemberEntity memberEntity : members) {
            if (memberEntity.isSelect()) {
                selectMembers.add(memberEntity);
            }
        }
        return selectMembers;
    }

    public static class StructureObserver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            if (o instanceof StructureBaseEntity && arg instanceof StructureBaseEntity.SelectEvent) {
                StructureBaseEntity structureBaseEntity = (StructureBaseEntity) o;
                StructureBaseEntity.SelectEvent selectEvent = (StructureBaseEntity.SelectEvent) arg;
                StructureBaseEntity.Type type = structureBaseEntity.getType();
                //用户角色接收事件全部需要网上更新父级
                //角色或者组织架构传递事件到最底层的用户时，用户顶层的另一面（角色的另一面组织架构或组织架构的另一面无法接收到更新操作）
                if (type == StructureBaseEntity.Type.USER) {
                    //更新父级选中
                    updateSelectForParent(structureBaseEntity);
                } else {
                    switch (selectEvent) {
                        case SELF:
                            //更新父级选中
                            updateSelectForParent(structureBaseEntity);
                            //更新子级选中
                            updateSelectForChild(structureBaseEntity);
                            break;
                        case TO_CHILD:
                            //更新子级选中
                            updateSelectForChild(structureBaseEntity);
                            break;
                        case TO_PARENT:
                            //更新父级选中
                            updateSelectForParent(structureBaseEntity);
                            break;
                    }
                }
            }
        }

        /**
         * 更新父级选中
         *
         * @param structureBaseEntity
         */
        private void updateSelectForParent(StructureBaseEntity structureBaseEntity) {
            List<StructureEntity> parentStructures = structureBaseEntity.getParentStructures();
            List<RoleEntity> parentRoles = structureBaseEntity.getParentRoles();
            if (parentStructures != null) {
                for (StructureEntity structureEntity : parentStructures) {
                    structureEntity.setSelect(isSelectForStructure(structureEntity), StructureBaseEntity.SelectEvent.TO_PARENT);
                }
            }
            if (parentRoles != null) {
                for (RoleEntity roleEntity : parentRoles) {
                    roleEntity.setSelect(isSelectForRole(roleEntity), StructureBaseEntity.SelectEvent.TO_PARENT);
                }
            }
        }

        /**
         * 更新子级选中
         *
         * @param structureBaseEntity
         */
        private void updateSelectForChild(StructureBaseEntity structureBaseEntity) {
            if (structureBaseEntity.getType() == StructureBaseEntity.Type.STRUCTURE) {
                StructureEntity structureEntity = (StructureEntity) structureBaseEntity;
                List<StructureEntity> childStructures = structureEntity.getChildlist();
                if (childStructures != null) {
                    for (StructureEntity structureEntit : childStructures) {
                        structureEntit.setSelect(structureBaseEntity.isSelect(), StructureBaseEntity.SelectEvent.TO_CHILD);
                    }
                }
                List<MemberEntity> childMembers = structureEntity.getUserList();
                if (childMembers != null) {
                    for (MemberEntity memberEntity : childMembers) {
                        memberEntity.setSelect(structureBaseEntity.isSelect(), StructureBaseEntity.SelectEvent.TO_CHILD);
                    }
                }
            } else if (structureBaseEntity.getType() == StructureBaseEntity.Type.ROLE) {
                RoleEntity roleEntity = (RoleEntity) structureBaseEntity;
                List<MemberEntity> childMembers = roleEntity.getUserList();
                if (childMembers != null) {
                    for (MemberEntity memberEntity : childMembers) {
                        memberEntity.setSelect(structureBaseEntity.isSelect(), StructureBaseEntity.SelectEvent.TO_CHILD);
                    }
                }
            }
        }

        private boolean isSelectForStructure(StructureEntity structureEntity) {
            List<StructureEntity> childStructures = structureEntity.getChildlist();
            if (childStructures != null) {
                for (StructureEntity structureEntit : childStructures) {
                    if (structureEntit.isSelect()) {
                        return true;
                    }
                }
            }
            List<MemberEntity> childMembers = structureEntity.getUserList();
            if (childMembers != null) {
                for (MemberEntity memberEntity : childMembers) {
                    if (memberEntity.isSelect()) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean isSelectForRole(RoleEntity roleEntity) {
            List<MemberEntity> childMembers = roleEntity.getUserList();
            if (childMembers != null) {
                for (MemberEntity memberEntity : childMembers) {
                    if (memberEntity.isSelect()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
