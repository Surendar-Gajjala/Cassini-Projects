package com.cassinisys.drdo.model.partTracking;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Nageshreddy on 08-10-2018.
 */
@Entity
@Table(name = "PARTTRACKING")
public class PartTracking implements Serializable{

    @Id
    @Column(name = "LIST_ID")
    @SequenceGenerator(name = "LIST_ID_GEN", sequenceName = "LIST_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIST_ID_GEN")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Transient
    private List<PartTrackingSteps> trackingSteps;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PartTrackingSteps> getTrackingSteps() {
        return trackingSteps;
    }

    public void setTrackingSteps(List<PartTrackingSteps> trackingSteps) {
        this.trackingSteps = trackingSteps;
    }
}
