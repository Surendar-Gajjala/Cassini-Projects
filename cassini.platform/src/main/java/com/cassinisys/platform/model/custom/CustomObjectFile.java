package com.cassinisys.platform.model.custom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "CUSTOM_OBJECT_FILE")
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomObjectFile extends CustomFile {
    @Column(name = "OBJECT")
    private Integer object;

    @Transient
    private String createdPerson;
}
