package com.cassinisys.pdm.service.onshape.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onshape.api.Onshape;
import com.onshape.api.exceptions.OnshapeException;
import com.onshape.api.responses.MetadataUpdatePartMetadataResponse;
import com.onshape.api.types.OnshapeDocument;
import com.onshape.api.types.WV;

import javax.validation.constraints.NotNull;

public class CustomMetadataUpdatePartMetadataRequest {
    @JsonProperty("items")
    @NotNull
    CustomMetadataUpdatePartMetadataRequestItems[] items;
    private Onshape onshape;

    public CustomMetadataUpdatePartMetadataRequest(Onshape onshape) {
        this.onshape = onshape;
    }

    public void items(CustomMetadataUpdatePartMetadataRequestItems[] items) {
        this.items = items;
    }


    public final MetadataUpdatePartMetadataResponse call(String pid, String did, WV wvType, String wv, String eid) throws OnshapeException {
        return (MetadataUpdatePartMetadataResponse)this.onshape.call("post", "/metadata/d/:did/[wv]/:wv/e/:eid/p/:pid", this, this.onshape.buildMap(new Object[]{"pid", pid, "did", did, "wvType", wvType, "wv", wv, "eid", eid}), this.onshape.buildMap(new Object[0]), MetadataUpdatePartMetadataResponse.class);
    }

    public final MetadataUpdatePartMetadataResponse call(OnshapeDocument document, String pid) throws OnshapeException {
        return (MetadataUpdatePartMetadataResponse)this.onshape.call("post", "/metadata/d/:did/[wv]/:wv/e/:eid/p/:pid", this, this.onshape.buildMap(new Object[]{"pid", pid, "did", document.getDocumentId(), "wvType", document.getWV(), "wv", document.getWVId(), "eid", document.getElementId()}), this.onshape.buildMap(new Object[0]), MetadataUpdatePartMetadataResponse.class);
    }
}
