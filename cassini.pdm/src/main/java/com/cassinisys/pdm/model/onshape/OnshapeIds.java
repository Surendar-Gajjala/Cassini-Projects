package com.cassinisys.pdm.model.onshape;

import lombok.Data;

import java.io.Serializable;

@Data
public class OnshapeIds implements Serializable {
    private String documentId;
    private String workspaceId;
    private String elementId;
    private String versionId;
}
