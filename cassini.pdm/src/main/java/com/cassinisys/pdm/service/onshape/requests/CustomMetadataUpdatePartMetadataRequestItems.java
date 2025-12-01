package com.cassinisys.pdm.service.onshape.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomMetadataUpdatePartMetadataRequestItems {
    @JsonProperty("href")
    public String href;
    @JsonProperty("properties")
    public CustomMetadataUpdatePartMetadataRequestItemsProperties[] properties;
}
