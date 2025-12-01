package com.cassinisys.pdm.service.onshape.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomMetadataUpdatePartMetadataRequestItemsProperties {
    @JsonProperty("propertyId")
    public String propertyId;

    @JsonProperty("value")
    public String value;

}
