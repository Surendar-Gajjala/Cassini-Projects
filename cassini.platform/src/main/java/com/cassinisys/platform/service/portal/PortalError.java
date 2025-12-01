package com.cassinisys.platform.service.portal;

import lombok.Data;

import java.io.Serializable;

@Data
public class PortalError implements Serializable {
    private String type;
    private String title;
    private String status;
}
