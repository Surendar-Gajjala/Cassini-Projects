package com.cassinisys.plm.model.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Print {
    private Integer objectId;
    private String objectType;
    private List<String> options = new LinkedList<>();
    private String host;


}