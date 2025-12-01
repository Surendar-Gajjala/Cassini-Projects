package com.cassinisys.plm.model.pgc;

import com.cassinisys.plm.model.mfr.PLMManufacturerPart;
import com.cassinisys.plm.model.pgc.dto.BosItemDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GSR on 25-11-2020.
 */
@Entity
@Table(name = "PGC_DECLARATION_PART")
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PGCDeclarationPart implements Serializable {

    @Id
    @SequenceGenerator(name = "DECLARATIONPART_ID_GEN", sequenceName = "DECLARATIONPART_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DECLARATIONPART_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "DECLARATION")
    private Integer declaration;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PART", nullable = false)
    private PLMManufacturerPart part;

    @Column(name = "BOS")
    private Integer bos;

    @Transient
    private Integer substanceCount = 0;
    @Transient
    private List<BosItemDto> substances = new ArrayList<>();

}

