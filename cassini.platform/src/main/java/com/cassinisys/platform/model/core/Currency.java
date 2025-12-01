package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "CURRENCY")
public class Currency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "CURRENCY_ID_GEN", sequenceName = "CURRENCY_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CURRENCY_ID_GEN")
    @Column(name = "CURRENCY_ID", nullable = false)
    private Integer id;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "SYMBOL", nullable = false)
    private String symbol;


}
