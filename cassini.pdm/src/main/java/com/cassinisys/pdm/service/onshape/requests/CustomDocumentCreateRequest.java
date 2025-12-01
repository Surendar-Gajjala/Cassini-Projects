package com.cassinisys.pdm.service.onshape.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onshape.api.Onshape;
import com.onshape.api.exceptions.OnshapeException;
import com.onshape.api.responses.DocumentsCreateDocumentResponse;

import javax.validation.constraints.NotNull;

public class CustomDocumentCreateRequest {

    @JsonProperty("name")
    @NotNull
    String name;
    @JsonProperty("ownerId")
    String ownerId;
    @JsonProperty("ownerType")
    @NotNull
    Number ownerType;
    @JsonProperty("betaCapabilityIds")
    String[] betaCapabilityIds;
    @JsonProperty("isPublic")
    @NotNull
    Boolean isPublic = Boolean.FALSE;
    @JsonProperty("isGenerateUnknownMessages")
    Boolean isGenerateUnknownMessages;
    @JsonProperty("tags")
    String[] tags;
    @JsonProperty("parentId")
    String parentId = null;

    Onshape onshape;

    public CustomDocumentCreateRequest(Onshape onshape) {
        this.onshape = onshape;
    }

    public CustomDocumentCreateRequest(Onshape onshape, String name, String ownerId, Number ownerType, String[] betaCapabilityIds, Boolean isPublic,
                                Boolean isGenerateUnknownMessages, String[] tags, String parentId) {
        this.name = name;
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.betaCapabilityIds = betaCapabilityIds;
        this.isPublic = isPublic;
        this.isGenerateUnknownMessages = isGenerateUnknownMessages;
        this.tags = tags;
        this.parentId = parentId;
    }

    public final String getName() {
        return this.name;
    }

    public final String getOwnerId() {
        return this.ownerId;
    }

    public final Number getOwnerType() {
        return this.ownerType;
    }

    public final String[] getBetaCapabilityIds() {
        return this.betaCapabilityIds;
    }

    public final Boolean getIsPublic() {
        return this.isPublic;
    }

    public final Boolean getIsGenerateUnknownMessages() {
        return this.isGenerateUnknownMessages;
    }

    public final String[] getTags() {
        return this.tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwnerType(Number ownerType) {
        this.ownerType = ownerType;
    }

    public void setBetaCapabilityIds(String[] betaCapabilityIds) {
        this.betaCapabilityIds = betaCapabilityIds;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public void setGenerateUnknownMessages(Boolean generateUnknownMessages) {
        isGenerateUnknownMessages = generateUnknownMessages;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String toString() {
        return Onshape.toString(this);
    }

    public final DocumentsCreateDocumentResponse call() throws OnshapeException {
        return (DocumentsCreateDocumentResponse)this.onshape.call("post", "/documents", this, this.onshape.buildMap(new Object[0]),
                this.onshape.buildMap(new Object[0]), DocumentsCreateDocumentResponse.class);
    }
}
