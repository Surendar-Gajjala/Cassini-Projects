package com.cassinisys.plm.model.pdm.dto;

import com.cassinisys.plm.model.pdm.PDMPart;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "PartDTO")
public class PartDTO extends ComponentDTO {
    private PDMPart part;
    public PartDTO() {

    }
}
