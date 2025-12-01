package com.cassinisys.drdo.model.failureList;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Nageshreddy on 02-01-2019.
 */

@Entity
@Table(name = "FAILURELIST")
public class FailureList implements Serializable {

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
    private List<FailureSteps> failureSteps;

    public List<FailureSteps> getFailureSteps() {
        return failureSteps;
    }

    public void setFailureSteps(List<FailureSteps> failureSteps) {
        this.failureSteps = failureSteps;
    }

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
}

