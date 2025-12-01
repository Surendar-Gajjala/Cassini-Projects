package com.cassinisys.plm.model.pdm.dto;

import com.cassinisys.plm.model.pdm.PDMAssembly;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName(value = "AssemblyDTO")
public class AssemblyDTO extends ComponentDTO {
    private List<ComponentDTO> children = new ArrayList<>();
    private PDMAssembly assembly;

    public AssemblyDTO() {

    }
}
