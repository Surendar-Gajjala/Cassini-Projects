package com.cassinisys.platform.model.core;

import com.cassinisys.platform.model.common.MobileDevice;
import lombok.Data;

@Data
public class LoginDTO {
    private Login login;
    private Session session;
    private String loginName;
    private Integer otp;
    private String passcode;
    private String newPassword;
    private String oldPassword;
    private MobileDevice mobileDevice;
    private Boolean hasTwoFactorAuthentication = Boolean.FALSE;
    private Boolean twoFactorChecked = Boolean.FALSE;
}