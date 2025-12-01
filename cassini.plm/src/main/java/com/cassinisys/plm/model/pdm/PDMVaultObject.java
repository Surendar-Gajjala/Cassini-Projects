package com.cassinisys.plm.model.pdm;

import com.cassinisys.platform.util.CustomDateDeserializer;
import com.cassinisys.platform.util.CustomDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "PDM_VAULT_OBJECT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PDMVaultObject extends PDMObject {
    @Column(name = "VAULT")
    private Integer vault;

    @Column(name = "NAMEPATH")
    private String namePath;

    @Column(name = "IDPATH")
    private String idPath;

    @Column(name = "CHECKED_OUT")
    private Boolean checkedOut = Boolean.FALSE;

    @Column(name = "CHECKED_OUT_BY")
    private Integer checkedOutBy;

    @Column(name = "CHECKED_OUT_TIMESTAMP")
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonDeserialize(using = CustomDateDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkedOutTimestamp;

    @Column(name = "CHECKED_OUT_IPADDRESS")
    private String checkedOutIpAddress;


    public PDMVaultObject(Enum type) {
        super(type);
    }
}
