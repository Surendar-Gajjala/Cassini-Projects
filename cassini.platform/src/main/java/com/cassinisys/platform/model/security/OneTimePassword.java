package com.cassinisys.platform.model.security;

import com.cassinisys.platform.model.core.CassiniObject;
import com.cassinisys.platform.model.core.ObjectType;
import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by reddy on 6/17/17.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "ONETIMEPASSWORD")
@PrimaryKeyJoinColumn(name = "ID")
public class OneTimePassword extends CassiniObject {

    @Column(name = "LOGIN")
    private Integer login;

    @Column(name = "OTP")
    private String otp;

    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRES", nullable = false)
    private Date expires;

    @Column(name = "VERIFIED")
    private Boolean verified = Boolean.FALSE;

    @Column(name = "APPLIED")
    private Boolean applied = Boolean.FALSE;

    public OneTimePassword() {
        super(ObjectType.OTP);
    }

}
