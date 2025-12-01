package com.cassinisys.pdm.model.onshape;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Assembly implements Serializable {
    private int level;
    private String name;
    private String path;
    private int cassiniId;
    private List<Component> children = new ArrayList<>();
}
