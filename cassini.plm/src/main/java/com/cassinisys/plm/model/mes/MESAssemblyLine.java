package com.cassinisys.plm.model.mes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.LinkedList;
import java.util.List;
import com.cassinisys.plm.model.plm.dto.FileDto;

import javax.persistence.*;

/**
 * Created by GSR on 09-02-2021.
 */
@Entity
@Table(name = "MES_ASSEMBLYLINE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class MESAssemblyLine extends MESObject {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE")
    private MESAssemblyLineType type;

    @Column(name = "PLANT")
    private Integer plant;

    @Transient
    private String plantName;

    @Transient
    private String createPerson;
    @Transient
    private String modifiedPerson;

    public MESAssemblyLine() {
        super.setObjectType(MESEnumObject.ASSEMBLYLINE);
    }

    @Transient
    private List<FileDto> assemblyLineFiles = new LinkedList<>();

}
