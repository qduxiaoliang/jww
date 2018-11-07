package com.jww.common.core.model.dto;

import com.jww.common.core.base.BaseDO;
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
public class TreeDTO extends BaseDO {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long parentId;
    private String name;
    private boolean spread;
    private boolean leaf;
    private int level;
    private String href;
    private Integer type;
    private List<TreeDTO> children;
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


    private static TreeDTO getRootNote(List<TreeDTO> treeDTOList) {
        TreeDTO rootNode = new TreeDTO();
        rootNode.setParentId(-999L);
        rootNode.setId(0L);
        rootNode.setName("root");
        rootNode.setLevel(0);
        rootNode.setLeaf(false);
        return rootNode;
    }

    private static TreeDTO constructTree(TreeDTO rootNode, List<TreeDTO> treeDTOList, int rootLevel) {
        if (treeDTOList == null || treeDTOList.size() == 0) {
            return null;
        }
        List<TreeDTO> childNodeList = new ArrayList<TreeDTO>();
        for (TreeDTO treeDTO : treeDTOList) {
            if (treeDTO.getParentId().equals(rootNode.getId())) {
                TreeDTO childNode = new TreeDTO();
                childNode.setId(treeDTO.getId());
                childNode.setName(treeDTO.getName());
                childNode.setParentId(treeDTO.getParentId());
                childNode.setLevel(rootLevel + 1);
                childNode.setType(treeDTO.getType());
                childNodeList.add(childNode);
            }
        }
        rootNode.setChildren(childNodeList);
        if (childNodeList.size() == 0) {
            rootNode.setLeaf(true);
        } else {
            rootNode.setLeaf(false);
        }
        for (TreeDTO treeDTO : childNodeList) {
            constructTree(treeDTO, treeDTOList, ++rootLevel);
            --rootLevel;
        }
        return rootNode;
    }

    public static List<TreeDTO> getTree(List<TreeDTO> treeDTOList) {
        TreeDTO rootNode = TreeDTO.getRootNote(treeDTOList);
        rootNode = TreeDTO.constructTree(rootNode, treeDTOList, 0);
        if (rootNode == null) {
            return new ArrayList<TreeDTO>();
        }
        return rootNode.getChildren();
    }
}
