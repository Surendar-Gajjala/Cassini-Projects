package com.cassinisys.is.model.pm;
/**
 * Model for ISBid
 */

import org.jsondoc.core.annotation.ApiObjectField;

import javax.persistence.*;

@Entity
@Table(name = "IS_LINKS")
public class ISLinks {
    @Id
    @SequenceGenerator(name = "LINK_ID_GEN", sequenceName = "LINK_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LINK_ID_GEN")
    @Column(name = "LINK_ID")
    private Integer id;

    @Column(name = "DEPENDENCY", nullable = false)
    @ApiObjectField(required = true)
    private String dependency;


    @Column(name = "PROJECT", nullable = false)
    @ApiObjectField(required = true)
    private Integer project;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }
}
