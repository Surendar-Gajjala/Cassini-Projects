package com.cassinisys.plm.model.pdm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "PDM_COMMIT")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDMCommit extends PDMObject {
    @Column
    private Integer vault;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "MESSAGE")
    private String message;

    public PDMCommit() {
        super(PDMObjectType.PDM_COMMIT);
    }
}
