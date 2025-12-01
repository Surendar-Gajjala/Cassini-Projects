package com.cassinisys.plm.model.pm;

import com.cassinisys.plm.model.rm.PLMGlossary;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by CassiniSystems on 10-09-2018.
 */
@Entity
@Data
@Table(name = "PLM_GLOSSARYDELIVERABLE")
public class PLMGlossaryDeliverable implements Serializable {

    @Id
    @SequenceGenerator(name = "GLOSSARYDELIVERABLE_ID_GEN", sequenceName = "GLOSSARYDELIVERABLE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GLOSSARYDELIVERABLE_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "GLOSSARY")
    private PLMGlossary glossary;


}
