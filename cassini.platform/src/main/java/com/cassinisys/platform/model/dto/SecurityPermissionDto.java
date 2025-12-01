package com.cassinisys.platform.model.dto;

import com.cassinisys.platform.model.security.SecurityPermission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SecurityPermissionDto implements Serializable {
    private List<SecurityPermission> securityPermissions;
    private List<String> objectTypes;
    private List<String> subTypes;

}
