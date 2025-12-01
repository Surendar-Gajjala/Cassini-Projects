package com.cassinisys.pdm.service.onshape.requests;

import com.cassinisys.pdm.service.onshape.responses.FoldersCreateFolderResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onshape.api.Onshape;
import com.onshape.api.exceptions.OnshapeException;

import javax.validation.constraints.NotNull;

public final class FoldersCreateFolderRequest {
    @JsonProperty("name")
    @NotNull
    String name;

    @JsonProperty("ownerId")
    @NotNull
    String ownerId;

    @JsonProperty("ownerType")
    @NotNull
    String ownerType;

    @JsonProperty("parentId")
    @NotNull
    String parentId = null;

    private Onshape onshape;

    public FoldersCreateFolderRequest(Onshape onshape) {
        this.onshape = onshape;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public FoldersCreateFolderResponse call() throws OnshapeException {
        return (FoldersCreateFolderResponse)this.onshape.call("post", "/folders", this, this.onshape.buildMap(new Object[0]),
                this.onshape.buildMap(new Object[0]), FoldersCreateFolderResponse.class);
    }
}
