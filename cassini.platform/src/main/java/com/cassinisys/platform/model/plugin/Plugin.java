package com.cassinisys.platform.model.plugin;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Plugin implements Serializable {
    private String id;
    private String name;
    private String description;
    private PluginDeveloper developer;
    private String directory;
    private String pluginClass;
    private Boolean enabled = Boolean.FALSE;
    private Boolean accessPrivate = Boolean.FALSE;
    private List<String> accessTenants = new ArrayList<>();
    private String dataModel;
}
