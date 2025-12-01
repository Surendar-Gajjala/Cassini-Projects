package com.cassinisys.plm.model.mes;

import com.cassinisys.plm.model.plm.dto.FileDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Created by Lenovo on 26-10-2020.
 */
@Entity
@Table(name = "MES_OPERATION")
@Data
@EqualsAndHashCode(callSuper = false)
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MESOperation extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESOperationType type;
    @Transient
    private String createPerson;
    @Transient
    private String typeName;
    @Transient
    private String modifiedPerson;
    @Transient
    private Integer workCenterId;
    @Transient
    private String workCenterName;

    public MESOperation() {
        super.setObjectType(MESEnumObject.OPERATION);
    }

    @Transient
    private List<FileDto> operationFiles = new LinkedList<>();
}
