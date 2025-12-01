package com.cassinisys.platform.model.core;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by subramanyam on 18-05-2020.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@Table(name = "MEASUREMENT")
public class Measurement implements Serializable {

    @Column(name = "NAME", nullable = false)
    String name;
    @Id
    @SequenceGenerator(name = "MEASUREMENT_ID_GEN",
            sequenceName = "MEASUREMENT_ID_SEQ",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MEASUREMENT_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Transient
    private List<MeasurementUnit> measurementUnits = new LinkedList<>();


}
