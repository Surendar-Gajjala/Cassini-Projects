package com.cassinisys.platform.model.dto;

import com.cassinisys.platform.model.security.ModuleType;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SecurityPermissionsDto implements Serializable {
    private List<String> objectTypes = new ArrayList<>();
    private List<String> privileges = new ArrayList<>();
    @Type(type = "com.cassinisys.platform.util.EnumUserType",
            parameters = {@org.hibernate.annotations.Parameter(name = "enumClassName",
                    value = "com.cassinisys.platform.model.security.ModuleType")})
    @Column(name = "TYPE", nullable = false)
    private List<ModuleType> moduleTypes = new ArrayList<>();
}
