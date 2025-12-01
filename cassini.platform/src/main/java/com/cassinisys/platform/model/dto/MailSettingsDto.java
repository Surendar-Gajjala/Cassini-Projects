package com.cassinisys.platform.model.dto;

import lombok.Data;

/**
 * Created by subramanyam on 11-05-2021.
 */
@Data
public class MailSettingsDto {
    private String userName;
    private String password;
    private String host;
    private String port;
    private String sslTrust;
}
