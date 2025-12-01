package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subra on 12-08-2019.
 */
public class BomItemDto {

    private Integer id;

    private String name;

    private BomItemType bomItemType;

    private List<BomItemDto> children = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BomItemType getBomItemType() {
        return bomItemType;
    }

    public void setBomItemType(BomItemType bomItemType) {
        this.bomItemType = bomItemType;
    }

    public List<BomItemDto> getChildren() {
        return children;
    }

    public void setChildren(List<BomItemDto> children) {
        this.children = children;
    }
}
