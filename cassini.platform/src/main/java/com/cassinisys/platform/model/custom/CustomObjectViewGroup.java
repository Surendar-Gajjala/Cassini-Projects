package com.cassinisys.platform.model.custom;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "CUSTOM_OBJECT_VIEW_GROUP")
public class CustomObjectViewGroup implements Serializable {

    @Id
    @SequenceGenerator(name = "CUSTOM_ID_GEN", sequenceName = "CUSTOM_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOM_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "PERMISSIONS")
    private String permissions;

}
