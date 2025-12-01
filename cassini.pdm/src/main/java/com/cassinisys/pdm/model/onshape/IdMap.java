package com.cassinisys.pdm.model.onshape;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdMap implements Serializable {
    private Integer fileId;
    private OnshapeIds onshapeIds;
}
