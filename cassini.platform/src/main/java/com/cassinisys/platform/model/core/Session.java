package com.cassinisys.platform.model.core;

import com.cassinisys.platform.model.common.MobileDevice;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author reddy
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "SESSION")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "SESSION_ID_GEN",
            sequenceName = "SESSION_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "SESSION_ID_GEN")
    @Column(name = "SESSION_ID", nullable = false)
    private Integer sessionId;

    @ManyToOne
    @JoinColumn(name = "LOGIN_ID", nullable = false)
    private Login login;

    @Column(name = "IP_ADDRESS", nullable = false)
    private String ipAddress;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGIN_TIME", nullable = false)
    private Date loginTime;

    @JsonSerialize(using = CustomDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGOUT_TIME", nullable = false)
    private Date logoutTime;

    @OneToOne
    @JoinColumn(name = "MOBILE_DEVICE", nullable = false)
    private MobileDevice mobileDevice;

    @Column(name = "USER_AGENT", nullable = false)
    private String userAgentString;

    @Transient
    private String apiKey;

    @Transient
    private String tenantId;
    @Transient
    private String preferredDateFormat;
    @Transient
    private String preferredShortDateFormat;
    @Transient
    private String accessToken;
    @Transient
    private String refreshToken;
    @Transient
    private Boolean hasTwoFactorAuthentication = Boolean.FALSE;
    @Transient
    private Boolean twoFactorChecked = Boolean.FALSE;
    @Transient
    private String passcode;

    public Session() {
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sessionId == null) ? 0 : sessionId.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Session other = (Session) obj;
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
            return false;
        return true;
    }

}
