package com.cassinisys.plm.model.pm;
/**
 * Model for ISBid
 */


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PLM_LINKS")
public class PLMLinks {
    @Id
    @SequenceGenerator(name = "LINK_ID_GEN", sequenceName = "LINK_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINK_ID_GEN")
    @Column(name = "LINK_ID")
    private Integer id;

    @Column(name = "DEPENDENCY", nullable = false)
    private String dependency;


    @Column(name = "PROJECT", nullable = false)
    private Integer project;


}
