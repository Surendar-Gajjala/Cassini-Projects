package com.cassinisys.platform.model.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by GSR on 21-07-2021.
 */
@Data
@Entity
@Table(name = "CUSTOM_OBJECT_RELATED")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectRelated implements Serializable {

    @Id
    @SequenceGenerator(name = "CUSTOM_OBJECT_RELATED_ID_GEN", sequenceName = "CUSTOM_OBJECT_RELATED_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOM_OBJECT_RELATED_ID_GEN")
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SEQUENCE")
    private Integer sequence;

    @Column(name = "PARENT")
    private Integer parent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RELATED")
    private CustomObject related;

    @Column(name = "NOTES")
    private String notes;
}
