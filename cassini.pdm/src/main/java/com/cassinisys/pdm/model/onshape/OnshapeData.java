package com.cassinisys.pdm.model.onshape;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OnshapeData implements Serializable {
    private Assembly rootAssembly;
    private List<Assembly> subAssemblies = new ArrayList<>();
}
