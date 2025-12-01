package com.cassinisys.platform.model.core;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by subramanyam on 18-05-2020.
 */
@Entity
@Data
@Table(name = "MEASUREMENT_UNIT")
public class MeasurementUnit implements Serializable {

    @Id
    @SequenceGenerator(name = "MEASUREMENT_ID_GEN",
            sequenceName = "MEASUREMENT_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MEASUREMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "MEASUREMENT", nullable = false)
    private Integer measurement;

    @Column(name = "BASE_UNIT", nullable = false)
    private Boolean baseUnit = Boolean.FALSE;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SYMBOL", nullable = false)
    private String symbol;

    @Column(name = "CONVERSION_FACTOR")
    private Double conversionFactor;


}
