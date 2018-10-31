package com.jww.base.am.model.entity;

import com.jww.common.core.base.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 树模型
 *
 * @author Ricky Wang
 * @date 17/12/11 21:50:42
 */
@Data
public class SysTreeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long parentId;
    private String name;
    private boolean spread;
    private boolean leaf;
    private int level;
    private String href;
    private Integer type;
    private List<SysTreeEntity> children;
    /**
     * 是否选中
     */
    private Boolean checked;
    /**
     * 是否不可用
     */
    private Boolean disabled;
    /**
     * 节点图标
     */
    private String icon;
    /**
     * 权限
     */
    private String permission;


    private static SysTreeEntity getRootNote(List<SysTreeEntity> sysTreeEntityList) {
        SysTreeEntity rootNode = new SysTreeEntity();
        rootNode.setParentId(-999L);
        rootNode.setId(0L);
        rootNode.setName("root");
        rootNode.setLevel(0);
        rootNode.setLeaf(false);
        return rootNode;
    }

    private static SysTreeEntity constructTree(SysTreeEntity rootNode, List<SysTreeEntity> sysTreeEntityList, int rootLevel) {
        if (sysTreeEntityList == null || sysTreeEntityList.size() == 0) {
            return null;
        }
        List<SysTreeEntity> childNodeList = new ArrayList<SysTreeEntity>();
        for (SysTreeEntity sysTreeEntity : sysTreeEntityList) {
            if (sysTreeEntity.getParentId().equals(rootNode.getId())) {
                SysTreeEntity childNode = new SysTreeEntity();
                childNode.setId(sysTreeEntity.getId());
                childNode.setName(sysTreeEntity.getName());
                childNode.setParentId(sysTreeEntity.getParentId());
                childNode.setLevel(rootLevel + 1);
                childNode.setType(sysTreeEntity.getType());
                childNodeList.add(childNode);
            }
        }
        rootNode.setChildren(childNodeList);
        if (childNodeList.size() == 0) {
            rootNode.setLeaf(true);
        } else {
            rootNode.setLeaf(false);
        }
        for (SysTreeEntity sysTreeEntity : childNodeList) {
            constructTree(sysTreeEntity, sysTreeEntityList, ++rootLevel);
            --rootLevel;
        }
        return rootNode;
    }

    public static List<SysTreeEntity> getTree(List<SysTreeEntity> sysTreeEntityList) {
        SysTreeEntity rootNode = SysTreeEntity.getRootNote(sysTreeEntityList);
        rootNode = SysTreeEntity.constructTree(rootNode, sysTreeEntityList, 0);
        if (rootNode == null) {
            return new ArrayList<SysTreeEntity>();
        }
        return rootNode.getChildren();
    }
}
