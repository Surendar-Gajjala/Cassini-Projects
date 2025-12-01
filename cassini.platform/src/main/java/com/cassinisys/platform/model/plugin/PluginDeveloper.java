package com.cassinisys.platform.model.plugin;

import lombok.Data;

import java.io.Serializable;

@Data
public class PluginDeveloper implements Serializable {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String url;
}
