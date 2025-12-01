package com.cassinisys.plm.model.pm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "PROJECT_TEMPLATEMEMBER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectTemplateMember implements Serializable {

    @Id
    @SequenceGenerator(name = "PROJECTTEMPLATEMEMBER_ID_GEN", sequenceName = "PROJECTTEMPLATEMEMBER_ID_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTTEMPLATEMEMBER_ID_GEN")
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column
    private Integer template;

    @Column
    private Integer person;
    @Column
    private String role;

    public ProjectTemplateMember() {
    }

}
